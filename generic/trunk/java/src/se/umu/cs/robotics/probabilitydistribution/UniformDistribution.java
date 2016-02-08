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

import java.util.Iterator;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.iteration.AbstractIterator;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.statespace.StateDimension;

/**
 *
 * @author billing
 */
public class UniformDistribution<E> extends AbstractProbabilityDistribution<E> {

    public UniformDistribution(StateDimension<E> dimension) {
        super(dimension);
    }

    public double getProbability(E state) {
        return getProbability();
    }

    public double getProbability(int pos) {
        return getProbability();
    }

    public double getProbability() {
        return 1d/(double)getDimension().size();
    }

    public IterableIterator<FuzzyItem<E>> min() {
        return new IterableIterator<FuzzyItem<E>>() {

            private Iterator<E> source = getDimension().iterator();

            public Iterator<FuzzyItem<E>> iterator() {
                return this;
            }

            public boolean hasNext() {
                return source.hasNext();
            }

            public FuzzyItem<E> next() {
                return new FuzzyItem<E>() {
                    private E e = source.next();

                    public E element() {
                        return e;
                    }

                    public double value() {
                        return getProbability();
                    }
                };
            }

            public void remove() {
                source.remove();
            }
        };
    }

    public IterableIterator<FuzzyItem<E>> max() {
        return min();
    }

    public boolean isUniform() {
        return true;
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
                        return getProbability();
                    }
                };
            }

        };
    }

}
