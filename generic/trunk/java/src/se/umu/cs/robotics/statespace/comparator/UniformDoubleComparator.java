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

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class UniformDoubleComparator extends LinearDoubleComparator {

    private final double min;
    private final double max;

    /**
     * Hack to allow modification of selection threshold for discrete data. 
     */

    public UniformDoubleComparator(double min, double max, double tolerance, double minTolerance) {
        super(tolerance, minTolerance);
        this.min = min;
        this.max = max;
    }

    public int index(double state) {
        double i = (state - min) / (getTolerance() * TOLERANCE_MULTIPLIER);
        return (int) i;
    }

    @Override
    public double distance(Double state1, Double state2) {
        if (index(state1) == index(state2)) {
            return 0d;
        } else {
            return 1d;
        }
    }

    @Override
    public UniformDoubleComparator clone() {
        return new UniformDoubleComparator(min, max, getTolerance(), minTolerance());
    }
}
