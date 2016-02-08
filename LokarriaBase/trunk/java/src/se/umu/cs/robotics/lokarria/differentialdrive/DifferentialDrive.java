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

package se.umu.cs.robotics.lokarria.differentialdrive;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class DifferentialDrive {

    private final DifferentialDriveOperations ddOp;
    private double angularSpeed = 0;
    private double linearSpeed = 0;

    public DifferentialDrive() {
        this(new DifferentialDriveOperations());
    }

    public DifferentialDrive(DifferentialDriveOperations differentialDriveOperations) {
        this.ddOp = differentialDriveOperations;
    }

    public DifferentialDrive(double linearSpeed, double angularSpeed) {
        this();
        this.linearSpeed = linearSpeed;
        this.angularSpeed = angularSpeed;
    }

    public double getAngularSpeed() {
        return angularSpeed;
    }

    public void setAngularSpeed(double angularSpeed) {
        this.angularSpeed = angularSpeed;
    }

    public double getLinearSpeed() {
        return linearSpeed;
    }

    public void setLinearSpeed(double linearSpeed) {
        this.linearSpeed = linearSpeed;
    }

    public void changeAngularSpeed(double diff) {
        this.angularSpeed += diff;
    }

    public void changeLinearSpeed(double diff) {
        this.linearSpeed += diff;
    }

    public void turn(double turn) {
        if ((angularSpeed >= 0 && turn > 0) || (angularSpeed <= 0 && turn < 0)) {
            angularSpeed = angularSpeed + turn;
        } else if (turn != 0) {
            angularSpeed = 0;
        }
    }

    public void post() {
        ddOp.postCommand(new ConcreteDifferentialDriveCommand(angularSpeed, linearSpeed));
    }
}
