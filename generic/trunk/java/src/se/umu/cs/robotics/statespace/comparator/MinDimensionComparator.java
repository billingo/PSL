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
package se.umu.cs.robotics.statespace.comparator;

import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.StateSpace;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class MinDimensionComparator implements DimensionComparator {

    @Override
    public double intersection(SpaceDistribution pd1, SpaceDistribution pd2) {
        StateSpace space = pd1.stateSpace();
        double overlap = 1;
        int dimensionMatches = 0;
        for (int d = 0; d < space.size(); d++) {
            final ProbabilityDistribution dim1 = pd1.getDimension(d);
            final ProbabilityDistribution dim2 = pd2.getDimension(d);
            if (dim1 != null && dim2 != null) {
                dimensionMatches++;
                overlap = Math.min(overlap, dim1.intersection(dim2));
            }
        }
        return dimensionMatches > 0 ? overlap : 0;
    }
}
