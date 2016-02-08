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

package se.umu.cs.robotics.lokarria.statespace;

import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.statespace.FloatDimension;
import se.umu.cs.robotics.statespace.comparator.StateComparator;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public abstract class SpeedDimension extends FloatDimension {

    private final StateComparator<Double> comparator;

    public SpeedDimension(double minSpeed, double maxSpeed, int size) {
        super(minSpeed, maxSpeed, 0, size);
        final double tolerance = range() / size();
        comparator = SensoryMotorSpace.newStateComparator(minSpeed, maxSpeed, tolerance);
    }

    public abstract Double getState(DifferentialDriveCommand command);

    @Override
    public StateComparator<Double> comparator() {
        return comparator;
    }
}
