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

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class AngularSpeedDimension extends SpeedDimension {
    public static final double MAX_TURN = 1.5;

    public AngularSpeedDimension(int discretizationLevels) {
        super(-MAX_TURN, MAX_TURN, discretizationLevels);
    }

    @Override
    public Double getState(DifferentialDriveCommand command) {
        return command.getAngularSpeed();
    }

}
