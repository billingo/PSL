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

package se.umu.cs.robotics.lokarria.fpsl;

import java.io.File;
import java.util.TimerTask;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.collections.LinkedBuffer;
import se.umu.cs.robotics.collections.ObjectBuffer;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.fpsl.responsibility.ContextPredictionErrors;
import se.umu.cs.robotics.fpsl.selection.FHypothesisSelection;
import se.umu.cs.robotics.logreader.lokarria.LokarriaMessageHandler;
import se.umu.cs.robotics.logreader.lokarria.SensoryMotorEventReader;
import se.umu.cs.robotics.logreader.xml.BufferedLogReader;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import static se.umu.cs.robotics.lokarria.fpsl.PslController.logger;
import se.umu.cs.robotics.lokarria.fpsl.listeners.PslControllerListener;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 * @author Erik Billing
 */
public class PslSimulator extends PslController {
    private SensoryMotorDistribution simulationStart;

    public PslSimulator(SensoryMotorSpace space, FLibrary<Double> library) {
        super(space, library);
        readSimulationStart();
    }
        
    private void readSimulationStart() {
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<>("http://www.cs.umu.se/robotics", "log", 100);
        reader.addMessageHandler(new LokarriaMessageHandler());
        SensoryMotorEventReader eventReader = new SensoryMotorEventReader(space, reader, 50);
        try {
            reader.start(new File("logs/ToKitchen-1.xml"));
            setSimulationStart(eventReader.next());
        } catch (SAXException ex) {
            System.out.println(ex);
        }
    }
    
    public void setSimulationStart(SensoryMotorDistribution simulationStart) {
//        this.simulationStart = simulationStart;
    }
    
    @Override
    public synchronized void start() {
        if (responsibilities == null || responsibilities.getContextCount() != library.getContextCount() || responsibilities.isNan()) {
            resetResponsibilitySignals();
        }
        if (task == null) {
            if (simulationStart == null) {
                laser.start();
                laser.getEchoes(); // Flush cache
            }
            task = new TimerTask() {

                ObjectBuffer<SpaceDistribution> sensoryMotorBuffer = new LinkedBuffer<>(ObjectBuffer.BufferDirection.BACKWARD, 500);
                SpaceDistribution dist = simulationStart;
                DifferentialDriveCommand command = null;
                int step = 0;
                
                @Override
                public void run() {
                    
//                    updateResponsiblitySignals(dist);
                    if (dist == null) {
                        final LaserArray echoes = laser.getEchoes();
                        if (echoes == null) {
                            return;
                        }
                        dist = space.newDistribution(echoes);
                    }
                    sensoryMotorBuffer.add(dist);
                    

                    FPrediction prediction = predict(sensoryMotorBuffer);
                    if (postCommands) {
                        command = control(prediction);
                    }
                    FHypothesisSelection selection = prediction.getSelection();
                    dist = selection.getTarget();
                                        
                    ContextPredictionErrors contextPredictionErrors = new ContextPredictionErrors<>(library,selection,dist);
                    logger.info(contextPredictionErrors);
                    lastPrediction = prediction;
                    step++;
                    if (stepLimit > 0 && step >= stepLimit) {
                        stop();
                    }
                }
            };
            timer.scheduleAtFixedRate(task, 0, timerPeriod);
            for (PslControllerListener l : listeners) {
                l.controllerStarted(this);
            }
        } else {
            logger.warn("PSL Simulator allready running!");
        }
    }
}
