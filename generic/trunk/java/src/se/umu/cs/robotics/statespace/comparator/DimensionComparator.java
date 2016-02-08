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


package se.umu.cs.robotics.statespace.comparator;

import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 * The comparator method of StateSpace returns a DimensionComparator that is used to compute distance measures between multidimensional distributions.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public interface DimensionComparator {

    /**
     * @param pd1
     * @param pd2
     * @return the intersection value between two multidimensional distributions.
     */
    double intersection(SpaceDistribution pd1, SpaceDistribution pd2);
}
