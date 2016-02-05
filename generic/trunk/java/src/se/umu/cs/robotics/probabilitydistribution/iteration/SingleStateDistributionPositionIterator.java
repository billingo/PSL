/*
 *  Copyright (C) 2010 Erik Billing <billing@cs.umu.se>
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

package se.umu.cs.robotics.probabilitydistribution.iteration;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SingleStateDistribution;
import se.umu.cs.robotics.statespace.StateDimension;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SingleStateDistributionPositionIterator<E> implements PositionIterator<ProbabilityDistribution<E>> {

    private StateDimension<E> dimension;
    private IteratorPosition pos;

    public SingleStateDistributionPositionIterator(StateDimension<E> dimension, PositionIterator<E> source) {
        this(dimension,source.getPosition());
    }

    public SingleStateDistributionPositionIterator(StateDimension<E> dimension, IteratorPosition<E> pos) {
        this.dimension = dimension;
        this.pos = new SingleStateIteratorPosition(pos);
    }

    private SingleStateDistributionPositionIterator(StateDimension<E> dimension, SingleStateIteratorPosition pos) {
        this.dimension = dimension;
        this.pos = pos;
    }

    public boolean hasPrevious() {
        return pos.hasPrevious();
    }

    public IteratorPosition<ProbabilityDistribution<E>> previous() {
        pos = pos.getPrevious();
        return pos;
    }

    public IteratorPosition<ProbabilityDistribution<E>> getPosition() {
        return pos;
    }

    @Override
    public PositionIterator<ProbabilityDistribution<E>> clone() {
        return new SingleStateDistributionPositionIterator<E>(dimension,pos);
    }

    public Iterator<IteratorPosition<ProbabilityDistribution<E>>> iterator() {
        return this;
    }

    public boolean hasNext() {
        return pos.hasNext();
    }

    public IteratorPosition<ProbabilityDistribution<E>> next() {
        pos = pos.getNext();
        return pos;
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }


    private class SingleStateIteratorPosition implements IteratorPosition<SingleStateDistribution<E>> {

        private IteratorPosition<E> pos;

        public SingleStateIteratorPosition(IteratorPosition<E> pos) {
            this.pos = pos;
        }

        public boolean hasPrevious() {
            return pos.hasPrevious();
        }

        public SingleStateIteratorPosition getPrevious() {
            return new SingleStateIteratorPosition(pos.getPrevious());
        }

        public SingleStateIteratorPosition getNext() {
            return new SingleStateIteratorPosition(pos.getNext());
        }

        public SingleStateDistribution<E> element() {
            return new SingleStateDistribution<E>(dimension, pos.element());
        }

        public boolean hasNext() {
            return pos.hasNext();
        }

    }
}
