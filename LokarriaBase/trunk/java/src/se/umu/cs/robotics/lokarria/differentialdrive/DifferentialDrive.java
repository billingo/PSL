/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright 2011 Benjamin Fonooni and Erik Billing
 billing@cs.umu.se, fonooni@cs.umu.se
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
