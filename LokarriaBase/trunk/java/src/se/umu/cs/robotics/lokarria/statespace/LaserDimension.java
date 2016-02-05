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

import java.util.List;
import se.umu.cs.robotics.lokarria.laser.LaserEcho;
import se.umu.cs.robotics.statespace.FloatDimension;
import se.umu.cs.robotics.statespace.comparator.LinearDoubleComparator;
import se.umu.cs.robotics.statespace.comparator.StateComparator;

/**
 * A single dimension in the Laser Array, representing readings with a specific angle.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LaserDimension extends FloatDimension {

    public static double MAX_DISTANCE = 16; // meters
    private final StateComparator<Double> comparator;

    public LaserDimension(int levels) {
        super(0, MAX_DISTANCE, 0, levels);
        final double tolerance = range() / size();
        comparator = (LinearDoubleComparator) SensoryMotorSpace.newStateComparator(firstState(), lastState(), tolerance);
    }

    /**
     * Returns the integer state representing specified echo distance
     * @param echo
     * @return
     */
    public Double getState(LaserEcho echo) {
        return echo.getDistance();
    }

    /**
     * Returns the integer state representing the average distance over all echoes in specified list.
     *
     * @param echoes
     * @return average state
     */
    public Double getState(List<LaserEcho> echoes) {
        double sum = 0;
        for (LaserEcho echo : echoes) {
            sum += echo.getDistance();
        }
        return sum / echoes.size();
    }

    @Override
    public StateComparator<Double> comparator() {
        return comparator;
    }
}
