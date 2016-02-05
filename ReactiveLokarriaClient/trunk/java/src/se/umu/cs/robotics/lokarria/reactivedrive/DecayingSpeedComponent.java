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
