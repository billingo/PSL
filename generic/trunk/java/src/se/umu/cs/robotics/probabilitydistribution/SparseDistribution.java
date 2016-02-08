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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import se.umu.cs.robotics.collections.fuzzy.ConcreteFuzzyItem;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.collections.sort.SortedDouble;
import se.umu.cs.robotics.collections.sort.Sorter;
import se.umu.cs.robotics.iteration.AbstractIterator;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.statespace.StateDimension;
import se.umu.cs.robotics.statespace.comparator.StateComparator;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SparseDistribution<E> extends AbstractProbabilityDistribution<E> {

    protected final TreeMap<E, Double> elements;
    protected double sum;

    public SparseDistribution(StateDimension<E> dimension) {
        super(dimension);
        elements = new TreeMap<E, Double>(getDimension().comparator());
    }

    public void addValue(E state, double value) {
        if (value > 0) {
            Double v = elements.get(state);
            if (v == null) {
                elements.put(state, value);
            } else {
                elements.put(state, v + value);
            }
            sum += value;
        }
    }

    public void setValue(E state, double value) {
        Double v = elements.get(state);
        if (v == null) {
            if (value > 0) {
                elements.put(state, value);
                sum += value;
            }
        } else {
            if (value > 0) {
                elements.put(state, value);
            } else {
                elements.remove(state);
            }
            sum += value - v;
        }
    }

    /**
     * Adds the probability values from pd the absolute values to this
     * @param pd
     * @param weight the impact of the added distribution
     */
    public void add(ProbabilityDistribution<E> pd, double weight) {
        if (pd == null) {
            return;
        } else if (getDimension().equals(pd.getDimension())) {
            for (FuzzyItem<E> item : pd.nonZeroStates()) {
                addValue(item.element(), item.value() * weight);
            }
        } else {
            throw new IllegalArgumentException("Specified pd must be from the same dimension.");
        }
    }

    public double getProbability(E state) {
        if (sum > 0) {
            StateComparator<E> comparator = getDimension().comparator();
            final Entry<E, Double> ceiling = elements.ceilingEntry(state);
            final Entry<E, Double> floor;
            final double value;
            if (ceiling == null) {
                floor = elements.floorEntry(state);
                if (floor == null) {
                    value = 0d;
                } else {
                    value = floor.getValue() * (1 - comparator.distance(floor.getKey(), state));
                }
            } else if (ceiling.getKey().equals(state)) {
                value = ceiling.getValue() * (1 - comparator.distance(ceiling.getKey(), state));
            } else {
                floor = elements.floorEntry(state);
                if (floor == null) {
                    value = ceiling.getValue();
                } else {
                    final double ceilingSimilarity = 1 - comparator.distance(ceiling.getKey(), state);
                    final double floorSimilarity = 1 - comparator.distance(floor.getKey(), state);
                    value = (ceiling.getValue() * ceilingSimilarity) + (floor.getValue() * floorSimilarity);
                }
            }
            return value / sum;
        } else {
            return 0;

        }
    }

    public double getProbability(int index) {
        return getProbability(getDimension().getState(index));
    }

    public IterableIterator<FuzzyItem<E>> min() {
        return new IterableIterator<FuzzyItem<E>>() {

            private Iterator<FuzzyItem<E>> i = minElements().iterator();

            public Iterator<FuzzyItem<E>> iterator() {
                return this;
            }

            public boolean hasNext() {
                return i.hasNext();
            }

            public FuzzyItem<E> next() {
                return i.next();
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    public IterableIterator<FuzzyItem<E>> max() {
        final ArrayList<FuzzyItem<E>> maxElements = maxElements();
        return new IterableIterator<FuzzyItem<E>>() {

            private Iterator<?> i = maxElements.isEmpty() ? getDimension().iterator() : maxElements.iterator();

            public Iterator<FuzzyItem<E>> iterator() {
                return this;
            }

            public boolean hasNext() {
                return i.hasNext();
            }

            public FuzzyItem<E> next() {
                final Object next = i.next();
                if (next instanceof FuzzyItem) {
                    return (FuzzyItem<E>) next;
                } else {
                    return new FuzzyItem<E>() {

                        public E element() {
                            return (E) next;
                        }

                        public double value() {
                            return 1d / getDimension().size();
                        }
                    };
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    public boolean isUniform() {
        return sum == 0;
    }

    public ArrayList<FuzzyItem<E>> minElements() {
        ArrayList<FuzzyItem<E>> els = new ArrayList<FuzzyItem<E>>();
        double min = 1;
        for (Entry<E, Double> e : elements.entrySet()) {
            final double v = e.getValue();
            if (v < min) {
                els.clear();
                min = v;
            }
            if (v == min) {
                els.add(new ConcreteFuzzyItem<E>(e.getKey(), min));
            }
        }
        return els;
    }

    public ArrayList<FuzzyItem<E>> maxElements() {
        ArrayList<FuzzyItem<E>> els = new ArrayList<FuzzyItem<E>>();
        double max = 0;
        for (Entry<E, Double> e : elements.entrySet()) {
            final double v = e.getValue();
            if (v > max) {
                els.clear();
                max = v;
            }
            if (v == max) {
                els.add(new ConcreteFuzzyItem<E>(e.getKey(), max / sum));
            }
        }
        return els;
    }

    public IterableIterator<FuzzyItem<E>> nonZeroStates() {

        return new AbstractIterator<FuzzyItem<E>>() {

            private Iterator<Entry<E, Double>> states = elements.entrySet().iterator();

            public boolean hasNext() {
                return states.hasNext();
            }

            public FuzzyItem<E> next() {
                Entry<E, Double> e = states.next();
                if (sum > 0) {
                    return new ConcreteFuzzyItem<E>(e.getKey(), e.getValue() / sum);
                } else {
                    return new ConcreteFuzzyItem<E>(e.getKey(), 0);
                }

            }
        };
    }

    @Override
    public String toString() {
        if (sum > 0) {
            StringBuilder s = new StringBuilder();
            boolean first = true;
            s.append('{');
            for (Entry<E, Double> e : elements.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    s.append(",");
                }
                s.append(toString(e.getKey()));
                s.append(": ");
                s.append(String.format(Locale.US,"%.2f",e.getValue()));
            }
            s.append("}");
            return s.toString();
        } else {
            return String.format(Locale.US,"{uniform: %.2f}",1d / (double) getDimension().size());
        }
    }

    private String toString(E state) {
        if (state instanceof Double) {
            return String.format(Locale.US,"%.2f",state);
        } else {
            return state.toString();
        }
    }
}
