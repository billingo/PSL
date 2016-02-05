/*-------------------------------------------------------------------*\
 THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

 Copyright 2007 - 2010 Erik Billing
 Department of Computing Science, Umea University, Sweden,
 (www.cs.umu.se/~billing/).

 LICENSE:

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 MA 02111-1307, USA.
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
