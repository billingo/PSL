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
 * Computes the distance between the Double states as a the proportion of it's tolerance.
 *
 * For example, two states 1.5 and 3.5 will have the distance 0.5 if the tolerance is 4, while their distance is 1 if the tolerance is less or equal to 2.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LinearDoubleComparator extends ComparableComparator<Double> {

    public static double TOLERANCE_MULTIPLIER = 1d;
    private double tolerance;
    private final double minTolerance;

    public LinearDoubleComparator(double tolerance, double minTolerance) {
        this.minTolerance = minTolerance;
        setTolerance(tolerance);
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance > minTolerance ? tolerance : minTolerance;
    }

    public double minTolerance() {
        return minTolerance;
    }

    @Override
    public double distance(Double state1, Double state2) {
        final double distance = Math.abs(state1 - state2) / (tolerance * TOLERANCE_MULTIPLIER);
        return distance < 1d ? distance : 1d;
    }

    @Override
    public LinearDoubleComparator clone() {
        return new LinearDoubleComparator(tolerance, minTolerance);
    }
}
