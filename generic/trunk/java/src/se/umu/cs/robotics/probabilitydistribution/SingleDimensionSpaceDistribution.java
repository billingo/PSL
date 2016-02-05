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

import java.util.Iterator;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.StateDimension;
import se.umu.cs.robotics.statespace.StateSpace;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SingleDimensionSpaceDistribution<E> extends AbstractSpaceDistribution<E> {

    private final ProbabilityDistribution<E> dist;

    public SingleDimensionSpaceDistribution(StateSpace<E> space, ProbabilityDistribution<E> dist) {
        super(space);
        this.dist = dist;
    }

    public ProbabilityDistribution<E> getDimension() {
        return dist;
    }

    public ProbabilityDistribution<E> getDimension(int dim) {
        StateDimension dimension = stateSpace().getDimension(dim);
        return getDimension(dimension);
    }

    public ProbabilityDistribution<E> getDimension(StateDimension<E> dim) {
        return dim.equals(dist.getDimension()) ? dist : null;
    }

    public Iterator<? extends ProbabilityDistribution<E>> dimensions() {

        return new Iterator<ProbabilityDistribution<E>>() {

            private Iterator<StateDimension<E>> i = stateSpace().iterator();

            public boolean hasNext() {
                return i.hasNext();
            }

            public ProbabilityDistribution<E> next() {
                StateDimension<E> d = i.next();
                return d.equals(dist.getDimension()) ? dist : null;
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }

        };
    }

}
