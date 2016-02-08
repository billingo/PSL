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

import se.umu.cs.robotics.statespace.StateSpace;

/**
 *
 * @author billing
 */
public abstract class AbstractSpaceDistribution<E> implements SpaceDistribution<E> {

    private final StateSpace<E> space;

    public AbstractSpaceDistribution(StateSpace<E> space) {
        this.space = space;
    }

    @Override
    public StateSpace<E> stateSpace() {
        return space;
    }

    @Override
    public double intersection(SpaceDistribution<E> pd) {
        return space.comparator().intersection(this, pd);
    }

    @Override
    public boolean isUniform() {
        for (int d = 0; d < space.size(); d++) {
            final ProbabilityDistribution<E> dimension = getDimension(d);
            if (dimension != null && !dimension.isUniform()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append('(');
        boolean first = true;
        for (int dim = 0; dim < space.size(); dim++) {
            if (first) {
                first = false;
            } else {
                s.append(';');
            }
            final ProbabilityDistribution<E> dist = getDimension(dim);
            if (dist == null || dist.isUniform()) {
                s.append("-");
            } else {
                s.append(dist);
            }
        }
        s.append(')');
        return s.toString();
    }
}
