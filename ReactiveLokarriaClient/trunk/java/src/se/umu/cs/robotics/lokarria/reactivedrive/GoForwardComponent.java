/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package se.umu.cs.robotics.lokarria.reactivedrive;

import se.umu.cs.robotics.lokarria.differentialdrive.ConcreteDifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.laser.LaserArray;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class GoForwardComponent implements ReactiveComponent {

    private double linearSpeed;

    public GoForwardComponent(double linearSpeed) {
        this.linearSpeed = linearSpeed;
    }

    public DifferentialDriveCommand get(LaserArray laser, DifferentialDriveCommand target) {
        return new ConcreteDifferentialDriveCommand(0, linearSpeed);
    }

    public double getLinearSpeed() {
        return linearSpeed;
    }

    public void setLinearSpeed(double linearSpeed) {
        this.linearSpeed = linearSpeed;
    }

}
