/*
 *  Copyright (C) 2011 Erik Billing <billing@cs.umu.se>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.umu.cs.robotics.probabilitydistribution;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import se.umu.cs.robotics.collections.fuzzy.ConcreteFuzzyItem;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.iteration.AbstractIterator;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.statespace.StateDimension;
import se.umu.cs.robotics.statespace.comparator.StateComparator;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class DualStateDistribution<E> extends AbstractProbabilityDistribution<E> {

    static NumberFormat format = NumberFormat.getInstance(Locale.US);
    private E state1;
    private E state2;
    private int state1Index;
    private int state2Index;
    private final double weight1;
    private final double weight2;

    public DualStateDistribution(StateDimension<E> dimension, E state1, double weight1, E state2, double weight2) {
        super(dimension);
        this.state1 = state1;
        this.state2 = state2;
        this.state1Index = dimension.getIndex(state1);
        this.state2Index = dimension.getIndex(state2);
        this.weight1 = weight1;
        this.weight2 = weight2;
    }

    public double getProbability(E state) {
        StateComparator<E> comparator = getDimension().comparator();
        double d1 = 1 - comparator.distance(state1, state);
        double d2 = 1 - comparator.distance(state2, state);
        return Math.max(d1 * weight1, d2 * weight2) / (weight1 + weight2);
    }

    public double getProbability(int index) {
        return getProbability(getDimension().getState(index));
    }

    public E getFirst() {
        return state1;
    }

    public int getFirstIndex() {
        return state1Index;
    }

    public E getLast() {
        return state2;
    }

    public int getLastIndex() {
        return state2Index;
    }

    public double getFirstProbability() {
        return weight1;
    }

    public double getLastProbability() {
        return weight2;
    }

    public IterableIterator<FuzzyItem<E>> min() {
        return new IterableIterator<FuzzyItem<E>>() {

            private Iterator<E> dims = getDimension().iterator();
            private E next;

            public Iterator<FuzzyItem<E>> iterator() {
                return this;
            }

            public boolean hasNext() {
                if (!dims.hasNext()) {
                    return false;
                } else if (next == null || (next.equals(state1) && weight1 > 0) || (next.equals(state2) && weight2 > 0)) {
                    next = dims.next();
                    return hasNext();
                } else {
                    return true;
                }
            }

            public FuzzyItem<E> next() {
                if (next == null && !hasNext()) {
                    throw new NoSuchElementException();
                } else {
                    ConcreteFuzzyItem<E> item = new ConcreteFuzzyItem<E>(next, 0);
                    next = null;
                    return item;
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    public IterableIterator<FuzzyItem<E>> max() {
        final double sum = weight1 + weight2;
        return new IterableIterator<FuzzyItem<E>>() {

            private int hasNext = weight1 == weight2 ? 2 : 1;
            private Iterator<E> i = weight1 + weight2 > 0 ? null : getDimension().iterator();

            public Iterator<FuzzyItem<E>> iterator() {
                return this;
            }

            public boolean hasNext() {
                if (i == null) {
                    return hasNext > 0;
                } else {
                    return i.hasNext();
                }
            }

            public FuzzyItem<E> next() {
                if (hasNext()) {
                    hasNext--;
                    if (i == null) {
                        if (hasNext == 1) {
                            return new ConcreteFuzzyItem<E>(state1, weight1);
                        } else {
                            if (weight1 <= weight2) {
                                return new ConcreteFuzzyItem<E>(state2, weight2);
                            } else {
                                return new ConcreteFuzzyItem<E>(state1, weight1);
                            }
                        }
                    } else {
                        return new ConcreteFuzzyItem<E>(i.next(), 0d);
                    }
                } else {
                    throw new NoSuchElementException();
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    @Override
    public String toString() {
        if (weight2 > weight1) {
            return String.format("{%s:%s,%s:%s}", state2.toString(), format.format(weight2), state1.toString(), format.format(weight1));
        } else {
            return String.format("{%s:%s,%s:%s}", state1.toString(), format.format(weight1), state2.toString(), format.format(weight2));
        }

    }

    public boolean isUniform() {
        return false;
    }

    public IterableIterator<FuzzyItem<E>> nonZeroStates() {
        return new AbstractIterator<FuzzyItem<E>>() {

            private byte i = 0;

            public boolean hasNext() {
                while ((i == 0 && weight1 == 0) || (i == 1 && weight2 == 0)) {
                    i++;
                }
                return i < 2;
            }

            public FuzzyItem<E> next() {
                if (hasNext()) {
                    i++;
                    final double sum = weight1 + weight2;
                    if (i == 1) {
                        return new ConcreteFuzzyItem<E>(state1, weight1 / sum);
                    } else {
                        return new ConcreteFuzzyItem<E>(state2, weight2 / sum);
                    }
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }
}
