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
import se.umu.cs.robotics.iteration.ArrayIterator;
import se.umu.cs.robotics.statespace.StateDimension;
import se.umu.cs.robotics.statespace.StateSpace;
import se.umu.cs.robotics.utils.StringTools;

/**
 *
 * @author billing
 */
public class SingleStateSpaceDistribution<E> extends AbstractSpaceDistribution<E> {

    private final E[] dimensions;

    public SingleStateSpaceDistribution(StateSpace<E> space, E... dimensions) {
        super(space);
        this.dimensions = dimensions;
    }

    public ProbabilityDistribution<E> getDimension(int dim) {
        if (dimensions[dim] == null) {
            return null;
        } else {
            return new SingleStateDistribution<E>(stateSpace().getDimension(dim), dimensions[dim]);
        }
    }

    public E getState(int dim) {
        return dimensions[dim];
    }

    public ProbabilityDistribution<E> getDimension(StateDimension<E> dim) {
        return new SingleStateDistribution<E>(dim, dimensions[stateSpace().getDimensionIndex(dim)]);
    }

    @Override
    public String toString() {
        return "(" + StringTools.join(dimensions, ";") + ")";
    }

    public Iterator<ProbabilityDistribution<E>> dimensions() {
        final ArrayIterator<E> i = new ArrayIterator<E>(dimensions);
        final Iterator<StateDimension<E>> dims = stateSpace().iterator();
        return new Iterator<ProbabilityDistribution<E>>() {

            public boolean hasNext() {
                return i.hasNext();
            }

            public ProbabilityDistribution<E> next() {
                return new SingleStateDistribution<E>(dims.next(), i.next());
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }
}
