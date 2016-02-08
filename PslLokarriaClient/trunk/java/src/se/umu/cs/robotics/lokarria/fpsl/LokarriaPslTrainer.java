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
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.fpsl.selection.FHypothesisSelector;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.fpsl.log.LearningStatus;
import se.umu.cs.robotics.fpsl.selection.MaxProductSelector;
import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.logreader.lokarria.LokarriaMessageHandler;
import se.umu.cs.robotics.logreader.lokarria.SensoryMotorEventReader;
import se.umu.cs.robotics.logreader.xml.BufferedLogReader;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LokarriaPslTrainer {

    static Logger logger = Logger.getLogger(LokarriaPslTrainer.class);
    private final SensoryMotorSpace space;
    private final FLibrary<Double> library;
    private final FHypothesisSelector<Double> selector;
    private final File sourceFile;
    private final LinkedPositionIterator<SensoryMotorDistribution> data;
    ArrayList<Double> intersections = new ArrayList<>();
    private Thread thread;
    private CountDownLatch finished = new CountDownLatch(1);
    private CountDownLatch pause = null;
    private long trainingDuration;
        
    public LokarriaPslTrainer(FLibrary<Double> library, File sourceFile) throws SAXException {
        this.space = (SensoryMotorSpace) library.stateSpace();
        this.library = library;
        this.selector = new MaxProductSelector<>(library);
        this.sourceFile = sourceFile;
        
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<>("http://www.cs.umu.se/robotics", "log", 100);
        reader.addMessageHandler(new LokarriaMessageHandler());
        SensoryMotorEventReader eventReader = new SensoryMotorEventReader(space, reader, 50);
        data = new LinkedPositionIterator<>(eventReader);

        reader.start(sourceFile);
    }

    public synchronized void cancelTraining() {
        thread = null;
        logger.info(LearningStatus.canceled(library));
    }

    public synchronized void pauseTraining() {
        if (pause == null) {
            pause = new CountDownLatch(1);
        }
    }

    public synchronized void continueTraining() {
        if (pause != null) {
            pause.countDown();
            pause = null;
        }
    }

    public boolean isRunning() {
        return thread != null && pause == null;
    }

    public boolean isFiniashed() {
        return finished.getCount() == 0;
    }

    private void finish() {
        finished.countDown();
        logger.info(LearningStatus.finished(library));
    }

    public synchronized void start() {
        if (thread == null) {
            
            
            thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    int i = 0;
                    final long trainingStartTime = System.currentTimeMillis();

                    logger.info(LearningStatus.started(library,sourceFile));

                    try {
                        data.next();
                        
                        /* HACK - Skip first elements of log file */
                        for (int d=0; d<20; d++) {
                            data.next();
                        }
                        
                        while (thread != null) {
                            if (!data.hasNext()) {
                                break;
                            } else if (pause != null) {
                                logger.info(LearningStatus.paused(library, pause));
                                pause.await();
                                logger.info(LearningStatus.continued(library));
                            } else {
                                IteratorPosition p = null;
                                try {
                                    p = data.next();
                                } catch (NoSuchElementException ex) {
                                    logger.warn("No elements in source file!", ex);
                                    logger.warn(LearningStatus.aborted(library, ex));
                                }
                                if (p != null) {
                                    i++;
                                    FPrediction<Double> prediction = new FPrediction<Double>(selector, p.getPrevious(), true);
                                    logger.info(prediction);
                                    SpaceDistribution<Double> predDistribution = prediction.element();
                                    SpaceDistribution<Double> targetDistribution = (SpaceDistribution<Double>) p.element();
                                    logger.info(targetDistribution);
//                                    SingleDimensionSpaceDistribution<Double> d1 = new SingleDimensionSpaceDistribution<Double>(space, targetDistribution.getDimension(0));
                                    prediction.teach(targetDistribution);
                                    intersections.add(targetDistribution.intersection(predDistribution));
                                }
                            }
                        }
                        finish();
                    } catch (InterruptedException ex) {
                        logger.warn(LearningStatus.aborted(library, ex));
                    } catch (Error ex) {
                        ex.printStackTrace();
                    } finally {
                        thread = null;
                        trainingDuration = System.currentTimeMillis() - trainingStartTime;
                    }
                }
            }, LokarriaPslTrainer.class.getName());
            thread.start();
        }
    }

    public void await() throws InterruptedException {
        finished.await();
    }

    public long getTrainingDuration() {
        return trainingDuration;
    }
}
