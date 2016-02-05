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

package se.umu.cs.robotics.probabilitydistribution;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.ArrayIterator;
import se.umu.cs.robotics.statespace.StateDimension;
import se.umu.cs.robotics.statespace.StateSpace;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class GenericSpaceDistribution<E> extends AbstractSpaceDistribution<E> {

    private final ProbabilityDistribution<E>[] dimensions;

    public GenericSpaceDistribution(StateSpace<E> space, ProbabilityDistribution<E>[] dimensions) {
        super(space);
        this.dimensions = dimensions;
    }

    public ProbabilityDistribution<E> getDimension(int dim) {
        return dimensions[dim];
    }

    public ProbabilityDistribution<E> getDimension(StateDimension<E> dim) {
        return dimensions[stateSpace().getDimensionIndex(dim)];
    }

    public Iterator<? extends ProbabilityDistribution<E>> dimensions() {
        return new ArrayIterator<ProbabilityDistribution<E>>(dimensions);
    }

}
