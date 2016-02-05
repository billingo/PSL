package se.umu.cs.robotics.lokarria.reactivedrive;

import se.umu.cs.robotics.lokarria.differentialdrive.ConcreteDifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.laser.LaserArray;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class TargetCommandComponent implements ReactiveComponent {

    private double impact;

    public TargetCommandComponent() {
        this(1);
    }

    public TargetCommandComponent(double impact) {
        this.impact = impact;
    }

    public DifferentialDriveCommand get(LaserArray laser, DifferentialDriveCommand targetCommand) {
        if (impact == 1d) {
            return targetCommand;
        } else {
            double aSpeed = targetCommand.getAngularSpeed() * impact;
            double lSpeed = targetCommand.getLinearSpeed() * impact;
            return new ConcreteDifferentialDriveCommand(aSpeed, lSpeed);
        }
    }
}
