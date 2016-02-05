package se.umu.cs.robotics.lokarria.reactivedrive.obstacleavoidance;

import se.umu.cs.robotics.lokarria.differentialdrive.ConcreteDifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.lokarria.laser.LaserEcho;
import se.umu.cs.robotics.lokarria.reactivedrive.ReactiveComponent;

/**
 * A simple reactive obstacle avoidance
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ReactiveObstacleAvoidance implements ReactiveComponent {

    private static final double TURN_POWER = 0.08;
    private static final double SPEED_POWER = 0.03;
    private static final double MAX_SPEED = 0.5;
    private double minFront = 0.010;

    public ReactiveObstacleAvoidance() {
    }

    public DifferentialDriveCommand get(LaserArray laser, DifferentialDriveCommand target) {
        double aSpeed = 0;
        double lSpeed = 0;

        double leftObstacle = Double.MAX_VALUE;
        double rightObstacle = Double.MAX_VALUE;

        for (LaserEcho echo : laser) {
            double a = echo.getAngle();
            double d = echo.getDistance();
            double x = echo.getX();
            double y = echo.getY();

            if (y > minFront) {
                double leftTurnAngle = Math.PI / 10 + a;
                double rightTurnAngle = Math.PI / 10 - a;

                double lo = (0.1 + Math.abs(leftTurnAngle)) * d * d;
                double ro = (0.1 + Math.abs(rightTurnAngle)) * d * d;

                if (lo < leftObstacle) {
                    leftObstacle = lo;
                }
                if (ro < rightObstacle) {
                    rightObstacle = ro;
                }
            }
        }
//        System.out.println(leftObstacle + "    " + frontObstacle + "    " + rightObstacle);
//        System.out.println(leftEcho.toString() + "    " + rightEcho.toString());
        if (target.getLinearSpeed() > 0) {
            aSpeed = TURN_POWER / leftObstacle - TURN_POWER / rightObstacle;
        }

//        lSpeed = -SPEED_POWER / frontObstacle * Math.abs(target.getLinearSpeed());
        lSpeed = -SPEED_POWER / Math.min(leftObstacle,rightObstacle) * Math.abs(target.getLinearSpeed());
        lSpeed = Math.abs(lSpeed) > MAX_SPEED ? MAX_SPEED * Math.signum(lSpeed) : lSpeed;
        
        return new ConcreteDifferentialDriveCommand(aSpeed, lSpeed);
    }
}
