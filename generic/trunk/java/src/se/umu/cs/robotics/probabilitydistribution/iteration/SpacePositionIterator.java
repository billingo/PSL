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
import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.probabilitydistribution.GenericSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.StateSpace;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SpacePositionIterator<E> implements PositionIterator<SpaceDistribution<E>> {

    private StateSpace<E> space;
    private SpaceIteratorPosition pos;

    public SpacePositionIterator(StateSpace<E> space, PositionIterator<ProbabilityDistribution<E>>... sources) {
        this.space = space;
        this.pos = new SpaceIteratorPosition(sources);
    }

    private SpacePositionIterator(StateSpace<E> space, SpaceIteratorPosition pos) {
        this.space = space;
        this.pos = pos;
    }

    public boolean hasPrevious() {
        return pos.hasPrevious();
    }

    public IteratorPosition<SpaceDistribution<E>> previous() {
        pos = pos.getPrevious();
        return pos;
    }

    public IteratorPosition<SpaceDistribution<E>> getPosition() {
        return pos;
    }

    @Override
    public PositionIterator<SpaceDistribution<E>> clone() {
        return new SpacePositionIterator<E>(space,pos);
    }

    public Iterator<IteratorPosition<SpaceDistribution<E>>> iterator() {
        return this;
    }

    public boolean hasNext() {
        return pos.hasNext();
    }

    public IteratorPosition<SpaceDistribution<E>> next() {
        pos = pos.getNext();
        return pos;
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }

    private class SpaceIteratorPosition implements IteratorPosition<SpaceDistribution<E>> {

        private final IteratorPosition<ProbabilityDistribution<E>>[] positions;

        public SpaceIteratorPosition(IteratorPosition<ProbabilityDistribution<E>>... positions) {
            this.positions = positions;
        }

        public SpaceIteratorPosition(PositionIterator<ProbabilityDistribution<E>>... sources) {
            this.positions = new IteratorPosition[sources.length];
            for (int i = 0; i < sources.length; i++) {
                positions[i] = sources[i].getPosition();
            }
        }

        public SpaceIteratorPosition getPrevious() {
            IteratorPosition[] prev = new IteratorPosition[positions.length];
            for (int i = 0; i < prev.length; i++) {
                prev[i] = positions[i].getPrevious();
            }
            return new SpaceIteratorPosition(prev);
        }

        public SpaceIteratorPosition getNext() {
            IteratorPosition[] next = new IteratorPosition[positions.length];
            for (int i = 0; i < next.length; i++) {
                next[i] = positions[i].getNext();
            }
            return new SpaceIteratorPosition(next);
        }

        public SpaceDistribution<E> element() {
            ProbabilityDistribution[] elements = new ProbabilityDistribution[positions.length];
            for (int i = 0; i < elements.length; i++) {
                elements[i] = positions[i].element();
            }
            return new GenericSpaceDistribution<E>(space, elements);
        }

        public boolean hasNext() {
            for (IteratorPosition<ProbabilityDistribution<E>> pos : positions) {
                if (!pos.hasNext()) {
                    return false;
                }
            }
            return true;
        }

        public boolean hasPrevious() {
            for (IteratorPosition<ProbabilityDistribution<E>> pos : positions) {
                if (!pos.hasPrevious()) {
                    return false;
                }
            }
            return true;
        }
    }
}
