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

package se.umu.cs.robotics.lokarria.statespace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.apache.log4j.Logger;
import se.umu.cs.robotics.lokarria.differentialdrive.ConcreteDifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.lokarria.laser.LaserArrayList;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SensoryMotorSequentializer {

    /**
     * the number of ms before/after current time that are considered during interpolation
     */
    public static final long INTERPOLATION_WINDOW = 500;
    static Logger logger = Logger.getLogger(SensoryMotorSequentializer.class);
    private SensoryMotorSpace space;
    private LinkedList<DifferentialDriveCommand> commands = new LinkedList<DifferentialDriveCommand>();
    private LinkedList<LaserArray> laserArrays = new LinkedList<LaserArray>();

    public SensoryMotorSequentializer(SensoryMotorSpace space) {
        this.space = space;
    }

    public void putCommand(DifferentialDriveCommand command) {
        if (commands.isEmpty() || !commands.getLast().equals(command)) {
            commands.add(command);
//            System.out.println("Added command: " + command.timeStamp() + "  queue: " + commands.size());
        } else {
//            System.out.println("Ignored command: " + command.timeStamp() + "  queue: " + commands.size());
        }
    }

    public void putLaser(LaserArray laser) {
        if (laserArrays.isEmpty() || !laserArrays.getLast().equals(laser)) {
            laserArrays.add(laser);
//            System.out.println("Added laser: " + laser.timeStamp() + "  queue: " + laserArrays.size());
        } else {
//            System.out.println("Ignored laser: " + laser.timeStamp() + "  queue: " + laserArrays.size());
        }
    }

    public SensoryMotorSpace getSpace() {
        return space;
    }

    private DifferentialDriveCommand getInterpolatedCommand(long time) {
        if (commands.isEmpty()) {
            throw new NoSuchElementException();
        }
        double angularSpeed = 0;
        double linearSpeed = 0;
        double count = 0;
        Iterator<DifferentialDriveCommand> i = commands.iterator();
        while (i.hasNext()) {
            DifferentialDriveCommand c = i.next();
            if (c.timeStamp() < time - INTERPOLATION_WINDOW) {
                logger.warn("Command lost during sequentialization: command too old");
            } else {
                angularSpeed += c.getAngularSpeed();
                linearSpeed += c.getLinearSpeed();
                count++;
                i.remove();
                if (c.timeStamp() >= time) {
                    break;
                }
            }
        }
        return new ConcreteDifferentialDriveCommand(angularSpeed / count, linearSpeed / count);
    }

    private LaserArray getInterpolatedLaserArray(long time) {
        if (laserArrays.isEmpty()) {
            throw new NoSuchElementException();
        }
        Iterator<LaserArray> i = laserArrays.iterator();
        ArrayList<LaserArray> arrays = new ArrayList<LaserArray>();
        while (i.hasNext()) {
            LaserArray c = i.next();
            if (c.timeStamp() < time - INTERPOLATION_WINDOW) {
                logger.warn("Command lost during sequentialization: command too old");
            } else {
                arrays.add(c);
                i.remove();
                if (c.timeStamp() >= time) {
                    break;
                }
            }
        }
        return LaserArrayList.newAverageArray(arrays);
    }

    public SensoryMotorDistribution getMotorDistribution(long time) {
        DifferentialDriveCommand cmd = getInterpolatedCommand(time);
        return space.newDistribution(cmd);
    }

    public SensoryMotorDistribution getSensoryDistribution(long time) {
        LaserArray laser = getInterpolatedLaserArray(time);
        return space.newDistribution(laser);
    }

    public SensoryMotorDistribution getSensoryMotorDistribution(long time) {
        DifferentialDriveCommand cmd = getInterpolatedCommand(time);
        LaserArray laser = getInterpolatedLaserArray(time);
        return space.newDistribution(cmd,laser);
    }

    public boolean isOutdated(long time, boolean commands, boolean lasers) {
        boolean outCommand = commands && (this.commands.isEmpty() || this.commands.getLast().timeStamp() < time);
        boolean outLaser = lasers && (this.laserArrays.isEmpty() || this.laserArrays.getLast().timeStamp() < time);
        return outCommand || outLaser;
    }

    private void checkCommandBuffer() {
        if (commands.size() > 2) {
            logger.warn("Future commands not used during sequentialization");
        }
    }

    private void checkLaserBuffer() {
        if (laserArrays.size() > 2) {
            logger.warn("Future laser echoes not used during sequentialization");
        }
    }
}
