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
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 * Iterates over one dimension of the source iterator.
 *
 * @author billing
 */
public class DimensionPositionIterator<E> implements PositionIterator<ProbabilityDistribution<E>> {

    private final PositionIterator<SpaceDistribution<E>> source;
    private final int dimension;

    public DimensionPositionIterator(PositionIterator<SpaceDistribution<E>> source, int dimension) {
        this.source = source;
        this.dimension = dimension;
    }

    public boolean hasPrevious() {
        return source.hasPrevious();
    }

    public IteratorPosition<ProbabilityDistribution<E>> previous() {
        return new DimensionIteratorPosition(source.previous());
    }

    public IteratorPosition<ProbabilityDistribution<E>> getPosition() {
        return new DimensionIteratorPosition(source.getPosition());
    }

    @Override
    public PositionIterator<ProbabilityDistribution<E>> clone() {
        return new DimensionPositionIterator<E>(source.clone(),dimension);
    }

    public Iterator<IteratorPosition<ProbabilityDistribution<E>>> iterator() {
        return this;
    }

    public boolean hasNext() {
        return source.hasNext();
    }

    public IteratorPosition<ProbabilityDistribution<E>> next() {
        return new DimensionIteratorPosition(source.next());
    }

    public void remove() {
        source.remove();
    }

    private class DimensionIteratorPosition extends AbstractIteratorPositionDelegate<ProbabilityDistribution<E>, SpaceDistribution<E>> {

        public DimensionIteratorPosition(IteratorPosition<SpaceDistribution<E>> pos) {
            super(pos);
        }

        public IteratorPosition<ProbabilityDistribution<E>> getPrevious() {
            return new DimensionIteratorPosition(sourcePosition().getPrevious());
        }

        public IteratorPosition<ProbabilityDistribution<E>> getNext() {
            return new DimensionIteratorPosition(sourcePosition().getNext());
        }

        public ProbabilityDistribution<E> element() {
            return sourcePosition().element().getDimension(dimension);
        }
    }
}
