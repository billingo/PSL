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

package se.umu.cs.robotics.statespace;

import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.comparator.DimensionComparator;

/**
 * A state space defining a set of {@link StateDimension}s. The dimensions are ordered such that they can be refered by their unique integer index.
 *
 * @author Erik Billing <billing@cs.umu.se>
 * @param <E> the dimension element type (generic type appying to all dimensions)
 */
public interface StateSpace<E> extends Iterable<StateDimension<E>> {

    /**
     * @return the number of dimensions
     */
    int size();

    /**
     * @param dimension
     * @return the index of specified dimension
     */
    int getDimensionIndex(StateDimension<E> dimension);

    /**
     * @param index of desired dimension
     * @return the dimension with specified index
     */
    StateDimension<E> getDimension(int index);

    /**
     * @return a new SpaceDistribution with default states.
     */
    SpaceDistribution<E> newDistribution();

    /**
     * @param stateIndex one index for each dimension in the state space
     * @return a new single state distribution with specified state indexes
     */
    SpaceDistribution<E> newDistribution(int... stateIndex);

    /**
     * @param dimensions
     * @return a new space distribution over all specified dimensions
     */
    SpaceDistribution<E> newDistribution(ProbabilityDistribution<E>... dimensions);

    DimensionComparator comparator();
}
