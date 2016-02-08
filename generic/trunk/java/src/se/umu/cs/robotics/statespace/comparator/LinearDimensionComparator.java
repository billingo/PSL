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

import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.StateSpace;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LinearDimensionComparator implements DimensionComparator {

    private final double zeroBase;

    public LinearDimensionComparator() {
        this(0);
    }

    public LinearDimensionComparator(double zeroBase) {
        this.zeroBase = zeroBase;
    }

    public double intersection(SpaceDistribution pd1, SpaceDistribution pd2) {
        double overlap = 0;
        StateSpace space = pd1.stateSpace();
        double dimensions = 0;
        for (int d = 0; d < space.size(); d++) {
            final ProbabilityDistribution dim1 = pd1.getDimension(d);
            final ProbabilityDistribution dim2 = pd2.getDimension(d);
            if (dim1 != null && dim2 != null) {
                dimensions++;
                overlap += dim1.intersection(dim2);
            }
        }
        final double avgOverlap = dimensions > 0 ? overlap / dimensions : 0d;
        return avgOverlap > zeroBase ? (avgOverlap - zeroBase) / (1d - zeroBase) : 0d;
    }
}
