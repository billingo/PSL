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

package se.umu.cs.robotics.lokarria.reactivedrive.obstacleavoidance;

import se.umu.cs.robotics.lokarria.differentialdrive.ConcreteDifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.lokarria.laser.LaserEcho;
import se.umu.cs.robotics.lokarria.reactivedrive.ReactiveComponent;

/**
 * A simple wall following control component
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ReactiveWallFollowing implements ReactiveComponent {

    private static final double TURN_POWER = 0.15;
    private static final double SPEED_POWER = 0.02;
    private double minFront = 0.010;
    private final boolean leftWall;
    private double baseSpeed;

    public ReactiveWallFollowing(boolean leftWall) {
        this(leftWall, 1d);
    }

    private ReactiveWallFollowing(boolean leftWall, double baseSpeed) {
        this.leftWall = leftWall;
        this.baseSpeed = baseSpeed;
    }

    public static ReactiveWallFollowing followLeftWall() {
        return new ReactiveWallFollowing(true);
    }

    public static ReactiveWallFollowing followRightWall() {
        return new ReactiveWallFollowing(false);
    }

    public static ReactiveWallFollowing followLeftWall(double baseSpeed) {
        return new ReactiveWallFollowing(true, baseSpeed);
    }

    public static ReactiveWallFollowing followRightWall(double baseSpeed) {
        return new ReactiveWallFollowing(false, baseSpeed);
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

                double lo = (0.2 + Math.abs(leftTurnAngle)) * d * d;
                double ro = (0.2 + Math.abs(rightTurnAngle)) * d * d;

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
        if (leftWall) {
            aSpeed = 2 - TURN_POWER / rightObstacle;
        } else {
            aSpeed = TURN_POWER / leftObstacle - 2;
        }

        lSpeed = 1 - SPEED_POWER / Math.min(leftObstacle, rightObstacle);
        if (lSpeed < 0.01) {
            if (leftWall) {
                aSpeed = aSpeed - 0.5;
            } else {
                aSpeed = aSpeed + 0.5;
            }
        }
//        System.out.println(aSpeed + "    " + lSpeed);
        return new ConcreteDifferentialDriveCommand(aSpeed, lSpeed);
    }
}
