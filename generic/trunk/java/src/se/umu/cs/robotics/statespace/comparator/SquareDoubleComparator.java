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

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SquareDoubleComparator extends LinearDoubleComparator {

    public SquareDoubleComparator(double tolerance, double minTolerance) {
        super(tolerance, minTolerance);
    }

    @Override
    public double distance(Double state1, Double state2) {
        final double distance = Math.abs(state1 - state2) / getTolerance();
        final double match = distance < 1d ? 1d - distance : 0d;
        return 1d - match * match;
    }

    @Override
    public LinearDoubleComparator clone() {
        return new SquareDoubleComparator(getTolerance(), minTolerance());
    }

}
