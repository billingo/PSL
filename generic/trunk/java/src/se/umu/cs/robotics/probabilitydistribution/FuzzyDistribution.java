/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright (C) 2007 - 2015  Erik Billing, <http://www.his.se/erikb>
School of Informatics, University of Skovde, Sweden

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
\*-------------------------------------------------------------------*/

package se.umu.cs.robotics.probabilitydistribution;

import java.util.ArrayList;
import java.util.Map.Entry;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.statespace.StateDimension;
import se.umu.cs.robotics.statespace.comparator.LinearDoubleComparator;
import se.umu.cs.robotics.statespace.comparator.StateComparator;
import se.umu.cs.robotics.utils.MathTools;

/**
 * @author Erik Billing <billing@cs.umu.se>
 */
public class FuzzyDistribution<E> extends SparseDistribution<E> {


    final static boolean FUZZY = true;
    
    private double centerValue;
    private boolean centerValueUpdated = false;
    private final LinearDoubleComparator comparator;

    public FuzzyDistribution(StateDimension<E> dimension) {
        super(dimension);
        StateComparator<E> defaultComparator = dimension.comparator();
        if (defaultComparator instanceof LinearDoubleComparator) {
            comparator = ((LinearDoubleComparator) defaultComparator).clone();
        } else {
            comparator = new LinearDoubleComparator(1d, 1d);
        }
    }

    public FuzzyDistribution(StateDimension<E> dimension, LinearDoubleComparator comparator) {
        super(dimension);
        this.comparator = comparator;
    }

    public FuzzyDistribution(ProbabilityDistribution<E> distribution) {
        this(distribution.getDimension());
        initDimensions(distribution);
    }

    public FuzzyDistribution(ProbabilityDistribution<E> distribution, LinearDoubleComparator comparator) {
        this(distribution.getDimension(), comparator);
        initDimensions(distribution);
    }

    private void initDimensions(ProbabilityDistribution<E> distribution) {
        for (FuzzyItem<E> item : distribution.nonZeroStates()) {
            addValue(item.element(), item.value());
        }
    }

    public LinearDoubleComparator getComparator() {
        return comparator;
    }

    @Override
    public double getProbability(E state) {
        if (state instanceof Double) {
            if (sum > 0) {
                return 1d - comparator.distance(defuzzify(), (Double) state);
            } else {
                return 0d;
            }
        } else {
            return membership(state);
        }
    }

    @Override
    public void addValue(E state, double value) {
        super.addValue(state, value);
        centerValueUpdated = false;
    }

    @Override
    public void setValue(E state, double value) {
        super.setValue(state, value);
        centerValueUpdated = false;
    }

    /**
     * Adds the probability values from pd the absolute values to this
     * @param pd
     * @param value the impact of the added distribution
     */
    @Override
    public void add(ProbabilityDistribution<E> pd, double value) {
        super.add(pd, value);
        centerValueUpdated = false;
    }

    /**
     * @return the center state as the weighted sum of all cones
     */
    public double centerOfSum() {
        if (sum > 0) {
            double stateSum = 0;
            for (Entry<E, Double> e : elements.entrySet()) {
                stateSum += ((Double) e.getKey()) * e.getValue();
            }
            return stateSum / sum;
        } else {
            final E state = getDimension().defaultState();
            return state instanceof Double ? ((Double) state) : 0d;
        }
    }

    /**
     * @return the center state as the weighted sum of all cones
     */
    public double centerOfSumSquare() {
        if (sum > 0) {
            double stateSum = 0;
            double valueSum = 0;
            for (Entry<E, Double> e : elements.entrySet()) {
                final double state = (Double) e.getKey();
                final double v = e.getValue();
                stateSum += state * v * v;
                valueSum += v * v;
            }
            return stateSum / valueSum;
        } else {
            final E state = getDimension().defaultState();
            return state instanceof Double ? ((Double) state) : 0d;
        }
    }

    public double centerOfMaxima() {
        if (sum > 0) {
            ArrayList<E> maximas = new ArrayList<E>();
            for (FuzzyItem<E> item : max()) {
                maximas.add(item.element());
            }
            return MathTools.mean(maximas);
        } else {
            final E state = getDimension().defaultState();
            return state instanceof Double ? ((Double) state) : 0d;
        }
    }

    public double firstMaxima() {
        if (sum > 0) {
            return (Double) max().next().element();
        } else {
            final E state = getDimension().defaultState();
            return state instanceof Double ? ((Double) state) : 0d;
        }
    }

    public double membership(E state) {
        final Entry<E, Double> ceiling = elements.ceilingEntry(state);
        final Entry<E, Double> floor;
        final double value;
        if (ceiling == null) {
            floor = elements.floorEntry(state);
            if (floor == null) {
                value = 0d;
            } else {
                value = fuzzyValue(floor, state);
            }
        } else if (ceiling.getKey().equals(state)) {
            value = ceiling.getValue();
        } else {
            floor = elements.floorEntry(state);
            if (floor == null) {
                value = fuzzyValue(ceiling, state);
            } else {
                // Cutting cones based on the match (fuzzy membership function)
                value = Math.max(fuzzyValue(ceiling, state), fuzzyValue(floor, state));
            }
        }
        return value;

    }

    private double fuzzyValue(final Entry<E, Double> item, E state) {
        StateComparator<E> c = getDimension().comparator();
        return Math.min(1 - c.distance(item.getKey(), state), item.getValue());
    }

    public double defuzzify() {
        if (!centerValueUpdated) {
            if (FUZZY) {
                //        return centerOfSum();
                centerValue = centerOfSumSquare();
//                centerValue = centerOfMaxima();
//                centerValue = firstMaxima();
            } else {
                centerValue = (Double) max().next().element();
            }
            centerValueUpdated = true;
        }
        return centerValue;
    }

    public static boolean isFuzzy() {
        return FUZZY;
    }

    public static FuzzyDistribution<Double> newLinquisticValue(ProbabilityDistribution<Double> pd) {
        StateDimension<Double> dimension = pd.getDimension();
        FuzzyDistribution<Double> dist = new FuzzyDistribution<Double>(dimension);
        LinearDoubleComparator comp = (LinearDoubleComparator) dimension.comparator();
        double min = dimension.firstState();
        double max = dimension.lastState();
        for (FuzzyItem<Double> item: pd.nonZeroStates()) {
            int closestIndex = (int) Math.round((item.element()-min)/comp.getTolerance()*2);
            double closestValue = dimension.getState(closestIndex);
            dist.addValue(closestValue, item.value());
        }
        return dist;
    }
}
