
package se.umu.cs.robotics.lokarria.reactivedrive;

import se.umu.cs.robotics.lokarria.differentialdrive.AbstractDifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;

/**
 * A single command representing the vector sum of all source commands.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class VectorSumCommand extends AbstractDifferentialDriveCommand {

    private final double angularSpeed;
    private final double linearSpeed;

    public VectorSumCommand(DifferentialDriveCommand ... commands) {
        double aSpeed = 0;
        double lSpeed = 0;
        for (DifferentialDriveCommand c: commands) {
            aSpeed+= c.getAngularSpeed()/commands.length;
            lSpeed+=c.getLinearSpeed()/commands.length;
        }
        angularSpeed = aSpeed;
        linearSpeed = lSpeed;
    }

    public long timeStamp() {
        return 0;
    }

    public double getAngularSpeed() {
        return angularSpeed;
    }

    public double getLinearSpeed() {
        return linearSpeed;
    }

}
