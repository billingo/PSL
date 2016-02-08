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
package se.umu.cs.robotics.logreader.lokarria;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.logreader.LogEvent;
import se.umu.cs.robotics.logreader.xml.BufferedLogReader;
import se.umu.cs.robotics.lokarria.differentialdrive.ConcreteDifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveOperations;
import se.umu.cs.robotics.lokarria.laser.LaserArrayList;
import se.umu.cs.robotics.lokarria.laser.LaserOperations;
import se.umu.cs.robotics.lokarria.log.JsonMessage;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSequentializer;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;

/**
 * Reads Sensor events from log Messages returned by a BufferedLogReader.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SensoryMotorEventReader implements IterableIterator<SensoryMotorDistribution> {

    static Logger logger = Logger.getLogger(SensoryMotorEventReader.class);
    private static Logger ddoLogger = Logger.getLogger(DifferentialDriveOperations.class);
    private static Logger laserLogger = Logger.getLogger(LaserOperations.class);
    private final long timeStep;
    private final SensoryMotorSequentializer seq;
    private long currentTime = 0;
    private long lastEventTimeStamp;
    private long lastMrdsTimeStamp;
    private LokarriaMessageIterator messages;
    private final boolean concatenateSensorsAndMotors;
    private byte switchSensoryMotor;
    private final static byte SWITCH_SENSOR = 2;
    private final static byte SWITCH_MOTOR = 1;
    private final static byte SWITCH_BOTH = 0;

    public SensoryMotorEventReader(SensoryMotorSpace space, Iterator<LogEvent<LokarriaLogMessage>> source, long timeStep) {
        this(space, source, timeStep, SensoryMotorSpace.CONCATENATE_SENSORS_AND_MOTORS);
    }

    public SensoryMotorEventReader(SensoryMotorSpace space, Iterator<LogEvent<LokarriaLogMessage>> source, long timeStep, boolean concatenateSensorsAndMotors) {
        this.concatenateSensorsAndMotors = concatenateSensorsAndMotors;
        this.messages = new LokarriaMessageIterator(source);
        this.timeStep = timeStep;
        this.seq = new SensoryMotorSequentializer(space);
    }

    private long estimateTimeStamp() {
        if (lastMrdsTimeStamp > 0) {
            long dTime = messages.currentEvent().timeStamp() - lastEventTimeStamp;
            if (dTime < 0) {
                logger.warn(String.format("Neggative event time differance! Current event: %d. Last event: %d.", messages.currentEvent().timeStamp(), lastEventTimeStamp));
            }
            return lastMrdsTimeStamp + dTime;
        } else {
            return 0;
        }
    }

    private void feed() {
        boolean feedCommands = switchSensoryMotor == SWITCH_BOTH || switchSensoryMotor == SWITCH_MOTOR;
        boolean feedLaser = switchSensoryMotor == SWITCH_BOTH || switchSensoryMotor == SWITCH_SENSOR;
        while (currentTime == 0 || seq.isOutdated(currentTime + timeStep, feedCommands, feedLaser)) {
            final LokarriaLogMessage m = messages.next();
            final LogEvent<LokarriaLogMessage> currentEvent = messages.currentEvent();
            try {
                if (ddoLogger.equals(currentEvent.logger())) {
                    final DifferentialDriveCommand command = ConcreteDifferentialDriveCommand.fromJSON(((JsonMessage) m).toJSON(), estimateTimeStamp());
                    if (command.timeStamp() > 0) {
                        if (currentTime == 0) {
                            currentTime = command.timeStamp();
                            if (!concatenateSensorsAndMotors) {
                                switchSensoryMotor = SWITCH_MOTOR;
                            }
                        }
                        seq.putCommand(command);
                        lastMrdsTimeStamp = command.timeStamp();
                    }
                } else if (laserLogger.equals(currentEvent.logger())) {
                    LaserArrayList laser = LaserArrayList.fromJSON(((JsonMessage) m).toJSON());
                    if (laser!=null) {
                        if (currentTime == 0) {
                            currentTime = laser.timeStamp();
                            if (!concatenateSensorsAndMotors) {
                                switchSensoryMotor = SWITCH_SENSOR;
                            }
                        }
                        seq.putLaser(laser);
                        lastMrdsTimeStamp = laser.timeStamp();
                    }
                }
            } catch (ParseException ex) {
                logger.warn(ex);
            }
            lastEventTimeStamp = currentEvent.timeStamp();
        }
    }

    public boolean hasNext() {
        try {
            feed();
        } catch (NoSuchElementException ex) {
            return false;
        }
        return true;
    }

    public SensoryMotorDistribution next() {
        feed();
        currentTime += timeStep;
        switch (switchSensoryMotor) {
            case SWITCH_MOTOR:
                switchSensoryMotor = SWITCH_SENSOR;
                return seq.getMotorDistribution(currentTime);
            case SWITCH_SENSOR:
                switchSensoryMotor = SWITCH_MOTOR;
                return seq.getSensoryDistribution(currentTime);
            default:
                return seq.getSensoryMotorDistribution(currentTime);
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public Iterator<SensoryMotorDistribution> iterator() {
        return this;
    }
}
