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
import se.umu.cs.robotics.collections.fuzzy.ConcreteFuzzyItem;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.iteration.AbstractIterator;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.statespace.StateDimension;

/**
 * A Standard (Gaussian) Distribution over a StateDimension.
 * 
 * @author billing
 */
public class GaussianDistribution<E> extends AbstractProbabilityDistribution<E> {

    private final static NumberFormat format = NumberFormat.getInstance(Locale.US);
    private final double mean;
    private final double std;

    /*
     * A potential problem! If the distribution goes outside the dimension range
     * the integral of the distribution will not sum to 1. The fraction should
     * probably be calculated with respect to this.
     */
    private final double fraction;

    public GaussianDistribution(StateDimension<E> dimension, double mean, double std) {
        super(dimension);
        this.mean = mean;
        this.std = std;
        this.fraction = 1d / (std * Math.sqrt(2 * Math.PI));
    }

    public double getProbability(E state) {
        return getProbability(getDimension().getIndex(state));
    }

    public double getProbability(int pos) {
        return getProbability((double) pos);
    }

    public double getProbability(double pos) {
        double diff = (pos - mean);
        return fraction * Math.exp(-diff * diff / (2 * std * std));
    }

    @Override
    public String toString() {
        return "G" + format.format(mean) + "s" + format.format(std);
    }

    public IterableIterator<FuzzyItem<E>> min() {

        return new IterableIterator<FuzzyItem<E>>() {

            private int hasNext = 2;
            double p0 = getProbability(0);
            double p1 = getProbability(getDimension().size() - 1);

            public Iterator<FuzzyItem<E>> iterator() {
                return this;
            }

            public boolean hasNext() {
                return hasNext > 0;
            }

            public FuzzyItem<E> next() {
                if (hasNext > 0) {
                    double p;
                    E e;
                    if (p0 == p1 && hasNext > 1) {
                        p0 += hasNext == 2 ? 1d : 0d;
                        p1 += hasNext == 1 ? 1d : 0d;
                        hasNext--;
                    } else {
                        hasNext = 0;
                    }
                    if (p0 > p1) {
                        p = p1;
                        e = getDimension().getState(getDimension().size() - 1);
                    } else {
                        p = p0;
                        e = getDimension().getState(0);
                    }
                    return new ConcreteFuzzyItem<E>(e, p);
                } else {
                    throw new NoSuchElementException();
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    public IterableIterator<FuzzyItem<E>> max() {

        return new IterableIterator<FuzzyItem<E>>() {

            private Iterator<FuzzyItem<E>> source = getMaximumStates().iterator();

            public Iterator<FuzzyItem<E>> iterator() {
                return this;
            }

            public boolean hasNext() {
                return source.hasNext();
            }

            public FuzzyItem<E> next() {
                return source.next();
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    private ArrayList<FuzzyItem<E>> getMaximumStates() {
        ArrayList<FuzzyItem<E>> els = new ArrayList<FuzzyItem<E>>(2);
        if (mean <= 0) {
            els.add(new ConcreteFuzzyItem<E>(getDimension().getState(0),getProbability(0)));
        } else
        if (mean >= getDimension().size()) {
            int i = getDimension().size()-1;
            els.add(new ConcreteFuzzyItem<E>(getDimension().getState(i),getProbability(i)));
        } else {
            int ilow = (int) Math.floor(mean);
            int ihigh = (int) Math.ceil(mean);
            double plow = getProbability(ilow);
            double phigh = getProbability(ihigh);
            if (ilow!=ihigh && plow >= phigh) {
                els.add(new ConcreteFuzzyItem<E>(getDimension().getState(ilow), plow));
            }
            if (plow <= phigh){
                els.add(new ConcreteFuzzyItem<E>(getDimension().getState(ihigh), phigh));
            }
        }
        return els;
    }

    public boolean isUniform() {
        return false;
    }

    public IterableIterator<FuzzyItem<E>> nonZeroStates() {
        return new AbstractIterator<FuzzyItem<E>>() {

            private Iterator<E> states = getDimension().iterator();

            public boolean hasNext() {
                return states.hasNext();
            }

            public FuzzyItem<E> next() {
                return new FuzzyItem<E>() {

                    private E e = states.next();

                    public E element() {
                        return e;
                    }

                    public double value() {
                        return getProbability(e);
                    }
                };
            }

        };
    }
}
