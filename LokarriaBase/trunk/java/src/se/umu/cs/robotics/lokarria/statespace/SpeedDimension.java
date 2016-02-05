/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright 2011 Benjamin Fonooni and Erik Billing
 fonooni@cs.umu.se, billing@cs.umu.se
Department of Computing Science, Umea University, Sweden,
(http://www.cognitionreversed.com).

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
