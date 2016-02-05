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
import se.umu.cs.robotics.probabilitydistribution.SingleStateSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.FloatSpace;

/**
 * The state space for the Differential Drive
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class DifferentialDriveSpace extends FloatSpace {

    public DifferentialDriveSpace(int angularDiscretizationLevels, int linearDiscretizationLevels) {
        super(SensoryMotorSpace.newDimensionComparator(),new AngularSpeedDimension(angularDiscretizationLevels), new LinearSpeedDimension(linearDiscretizationLevels));
    }

    public SpaceDistribution<Double> newDistribution(DifferentialDriveCommand command) {
        Double[] values = new Double[2];
        values[0] = command.getAngularSpeed();
        values[1] = command.getLinearSpeed();
        return new SingleStateSpaceDistribution<Double>(this, values);
    }

    public AngularSpeedDimension getAngularDimension() {
        return (AngularSpeedDimension) getDimension(0);
    }

    public LinearSpeedDimension getLinearDimension() {
        return (LinearSpeedDimension) getDimension(1);
    }
}
