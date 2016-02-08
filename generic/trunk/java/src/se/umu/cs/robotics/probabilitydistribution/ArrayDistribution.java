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
import java.util.NoSuchElementException;
import java.util.Random;
import se.umu.cs.robotics.collections.fuzzy.ConcreteFuzzyItem;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.collections.sort.SortedDouble;
import se.umu.cs.robotics.collections.sort.Sorter;
import se.umu.cs.robotics.collections.sort.SorterOrder;
import se.umu.cs.robotics.iteration.IterableIterator;

import se.umu.cs.robotics.statespace.StateDimension;

public class ArrayDistribution<E> extends AbstractProbabilityDistribution<E> {

    public static final NumberFormat format = NumberFormat.getInstance(Locale.US);
    private static Random random = new Random();
    private final double[] values;
    private double sum = 0;

    public ArrayDistribution(final StateDimension<E> dimension) {
        super(dimension);
        this.values = new double[dimension.size()];
    }

    @Override
    public double getProbability(E state) {
        return getProbability(getDimension().getIndex(state));
    }

    @Override
    public double getProbability(int pos) {
        if (sum == 0) {
            return 1d / values.length;
        } else {
            return values[pos] / sum;
        }
    }

    public double getValue(E state) {
        return getValue(getDimension().getIndex(state));
    }

    public void addValue(E state, double value) {
        int pos = getDimension().getIndex(state);
        addValue(pos, value);
    }

    public void setValue(E state, double value) {
        int pos = getDimension().getIndex(state);
        setValue(pos, value);
    }

    public double getValue(int pos) {
        return values[pos];
    }

    public void addValue(int pos, double value) {
        values[pos] += value;
        sum += value;
    }

    public void setValue(int pos, double value) {
        sum += value - values[pos];
        values[pos] = value;
    }

    /**
     * Adds the probability values from pd the absolute values to this
     * @param pd
     * @param value
     */
    public void add(ProbabilityDistribution<E> pd, double value) {
        if (pd == null) {
            return;
        } else if (getDimension().equals(pd.getDimension())) {
            if (pd instanceof SingleStateDistribution) {
                addValue(((SingleStateDistribution) pd).getStateIndex(), value);
            } else if (pd instanceof DualStateDistribution) {
                final DualStateDistribution dualPd = (DualStateDistribution) pd;
                addValue(dualPd.getFirstIndex(), dualPd.getFirstProbability() * value);
                addValue(dualPd.getLastIndex(), dualPd.getLastProbability() * value);
            } else {
                for (int i = 0; i < values.length; i++) {
                    addValue(i, pd.getProbability(i) * value);
                }
            }
        } else {
            throw new IllegalArgumentException("Specified pd must be from the same dimension.");
        }
    }

    /**
     * Adding each eleemnt in pd to corresponding element in this,
     * pultiplied with the given value. The addidion is proportional, meaning that the a
     * value of 1 will allways produce a 50/50 combination of the two spaces.
     *
     * @param pd
     * @param value
     */
    public void addProportional(ProbabilityDistribution<E> pd, double value) {
        if (getDimension().equals(pd.getDimension())) {
            double csum = this.sum;
            if (pd instanceof SingleStateDistribution) {
                addValue(((SingleStateDistribution) pd).getStateIndex(), value * csum);
            } else {
                for (int i = 0; i < values.length; i++) {
                    addValue(i, pd.getProbability(i) * value * csum);
                }
            }
        } else {
            throw new IllegalArgumentException("Specified pd must be from the same dimension.");
        }
    }

    public static <E> ArrayDistribution<E> randomDistribution(StateDimension<E> dimension) {
        ArrayDistribution<E> pd = new ArrayDistribution<E>(dimension);
        for (int i = 0; i < dimension.size(); i++) {
            pd.setValue(i, random.nextDouble());
        }
        return pd;
    }

    @Override
    public String toString() {
        if (sum > 0) {
            Sorter<SortedDouble> sortedValues = Sorter.sort(values, SorterOrder.REVERSED_NATURAL);
            Iterator<SortedDouble> sorted = sortedValues.iterator();
            StringBuilder s = new StringBuilder();
            boolean continuedDistribution = true;
            s.append('{');
            s.append(toString(sorted.next()));
            for (int i = 0; i < 2 && sorted.hasNext(); i++) {
                SortedDouble x = sorted.next();
                if (x.value() > 0) {
                    s.append(',');
                    s.append(toString(x));
                } else {
                    continuedDistribution = false;
                    break;
                }
            }
            if (continuedDistribution) {
                s.append("...");
            }
            s.append("}");
            return s.toString();
        } else {
            return "{uniform:" + format.format(1d / (double) values.length) + "}";
        }
    }

    private String toString(SortedDouble d) {
        double v = sum > 0 ? d.value() / sum : 1d / (double) values.length;
        return getDimension().getState(d.originalPosition()).toString() + ":" + format.format(v);
    }

    public ArrayList<FuzzyItem<E>> minElements() {
        ArrayList<FuzzyItem<E>> els = new ArrayList<FuzzyItem<E>>();
        double min = 1;
        for (int i = 0; i < getDimension().size(); i++) {
            if (values[i] < min) {
                els.clear();
                min = values[i];
            }
            if (values[i] == min) {
                els.add(new ConcreteFuzzyItem<E>(getDimension().getState(i), min));
            }
        }
        return els;
    }

    public ArrayList<FuzzyItem<E>> maxElements() {
        ArrayList<FuzzyItem<E>> els = new ArrayList<FuzzyItem<E>>();
        double max = 0;
        for (int i = 0; i < getDimension().size(); i++) {
            if (values[i] > max) {
                els.clear();
                max = values[i];
            }
            if (values[i] == max) {
                els.add(new ConcreteFuzzyItem<E>(getDimension().getState(i), max / sum));
            }
        }
        return els;
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
        return new IterableIterator<FuzzyItem<E>>() {

            private Iterator<FuzzyItem<E>> i = maxElements().iterator();

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

    public boolean isUniform() {
        return sum == 0;
    }

    public IterableIterator<FuzzyItem<E>> nonZeroStates() {
        return new IterableIterator<FuzzyItem<E>>() {

            private int i = 0;

            public Iterator<FuzzyItem<E>> iterator() {
                return this;
            }

            public boolean hasNext() {
                while (i < values.length && values[i] == 0) {
                    i++;
                }
                return i < values.length;
            }

            public FuzzyItem<E> next() {
                if (hasNext()) {
                    final E state = getDimension().getState(i);
                    final double value = sum > 0 ? values[i++] / sum : 0;
                    return new ConcreteFuzzyItem<E>(state, value);
                } else {
                    throw new NoSuchElementException();
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }
}
