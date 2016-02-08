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


package se.umu.cs.robotics.lokarria.reactivedrive;

import se.umu.cs.robotics.lokarria.differentialdrive.ConcreteDifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.laser.LaserArray;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class DecayingSpeedComponent implements ReactiveComponent {

    private double angularTarget = 0;
    private double linearTarget = 0;
    private double angularSpeed = 0;
    private double linearSpeed = 0;
    private double angularDecay;
    private double linearDecay;

    public DecayingSpeedComponent(double angularDecay, double linearDecay) {
        this.angularDecay = angularDecay;
        this.linearDecay = linearDecay;
    }

    public DifferentialDriveCommand get(LaserArray laser, DifferentialDriveCommand targetCommand) {
        if (angularTarget==targetCommand.getAngularSpeed()) {
            angularSpeed = angularSpeed * (1d-angularDecay);
        } else {
            angularTarget = targetCommand.getAngularSpeed();
            angularSpeed = angularTarget;
        }
        if (linearTarget==targetCommand.getLinearSpeed()) {
            linearSpeed = linearSpeed * (1d-linearDecay);
        } else {
            linearTarget = targetCommand.getLinearSpeed();
            linearSpeed = linearTarget;
        }
        return new ConcreteDifferentialDriveCommand(angularSpeed, linearSpeed);
    }

    public double getAngularDecay() {
        return angularDecay;
    }

    public void setAngularDecay(double angularDecay) {
        this.angularDecay = angularDecay;
    }

    public double getLinearDecay() {
        return linearDecay;
    }

    public void setLinearDecay(double linearDecay) {
        this.linearDecay = linearDecay;
    }

}
