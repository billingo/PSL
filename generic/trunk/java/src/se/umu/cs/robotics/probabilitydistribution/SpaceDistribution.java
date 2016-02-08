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
import se.umu.cs.robotics.statespace.StateDimension;
import se.umu.cs.robotics.statespace.StateSpace;

/**
 * A multi-dimensional probability distribution over all dimensions in a a state space.
 *
 * @see ProbabilityDistribution
 * @see StateSpace
 * @author Erik Billing
 */
public interface SpaceDistribution<E> {

    /**
     * @return the state space over which this probiability distribution is defined
     */
    StateSpace<E> stateSpace();

    /**
     * @param dim the descired dimension
     * @return a single dimension of this probability distribution
     */
    ProbabilityDistribution<E> getDimension(int dim);

    /**
     * @param dim
     * @return a single dimension of this probability distribution
     */
    ProbabilityDistribution<E> getDimension(StateDimension<E> dim);

    /**
     * @return a distribution iterator over all dimensions
     */
    Iterator<? extends ProbabilityDistribution<E>> dimensions();

    /**
     * Returns the intersection between this and the specified probability distribution. Both distributions must be over the same space.
     * @param pd
     * @return the intersection between this and the specified probability distribution
     */
    double intersection(SpaceDistribution<E> pd);

    /**
     * @return true if all dimensions have uniform distributions
     */
    boolean isUniform();
}
