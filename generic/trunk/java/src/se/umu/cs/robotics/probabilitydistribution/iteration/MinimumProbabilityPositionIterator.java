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

package se.umu.cs.robotics.probabilitydistribution.iteration;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.position.AbstractIteratorPositionDelegate;
import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;

/**
 *
 * @author billing
 */
public class MinimumProbabilityPositionIterator<E> implements PositionIterator<E> {

    private final PositionIterator<ProbabilityDistribution<E>> source;

    public MinimumProbabilityPositionIterator(PositionIterator<ProbabilityDistribution<E>> source) {
        this.source = source;
    }

    public boolean hasPrevious() {
        return source.hasPrevious();
    }

    public IteratorPosition<E> previous() {
        return new MinPosition<E>(source.previous());
    }

    public IteratorPosition<E> getPosition() {
        return new MinPosition<E>(source.getPosition());
    }

    @Override
    public PositionIterator<E> clone() {
        return new MaximumProbabilityPositionIterator<E>(source.clone());
    }

    public Iterator<IteratorPosition<E>> iterator() {
        return this;
    }

    public boolean hasNext() {
        return source.hasNext();
    }

    public IteratorPosition<E> next() {
        return new MinPosition<E>(source.next());
    }

    public void remove() {
        source.remove();
    }

    private static class MinPosition<E> extends AbstractIteratorPositionDelegate<E, ProbabilityDistribution<E>> {

        private final E element;

        public MinPosition(IteratorPosition<ProbabilityDistribution<E>> pos) {
            super(pos);
            this.element = pos.element().min().next().element();
        }

        public IteratorPosition<E> getPrevious() {
            return new MinPosition<E>(sourcePosition().getPrevious());
        }

        public IteratorPosition<E> getNext() {
            return new MinPosition<E>(sourcePosition().getNext());
        }

        public E element() {
            return element;
        }
    }
}
