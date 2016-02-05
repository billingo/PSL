/*-------------------------------------------------------------------*\
 THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

 Copyright 2011 Erik Billing
 Department of Computing Science, Umea University, Sweden,
 (http://www.cognitionreversed.com).

 LICENSE:

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 MA 02111-1307, USA.
 \*-------------------------------------------------------------------*/
package se.umu.cs.robotics.lokarria.fpsl;

import se.umu.cs.robotics.fpsl.FContext;
import se.umu.cs.robotics.fpsl.selection.FHypothesisMatch;
import se.umu.cs.robotics.lokarria.fpsl.listeners.PslControllerListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;
import org.json.simple.JSONValue;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.collections.LinkedBuffer;
import se.umu.cs.robotics.collections.ObjectBuffer;
import se.umu.cs.robotics.fpsl.selection.FHypothesisSelector;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.fpsl.responsibility.ContextPredictionErrors;
import se.umu.cs.robotics.fpsl.responsibility.Responsibility;
import se.umu.cs.robotics.fpsl.selection.MaxProductSelector;
import se.umu.cs.robotics.hpl.HplPropertyLoader;
import se.umu.cs.robotics.lokarria.fpsl.log.ControllerStatus;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.lokarria.differentialdrive.ConcreteDifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveOperations;
import se.umu.cs.robotics.lokarria.fpsl.goal.CircularGoal;
import se.umu.cs.robotics.lokarria.fpsl.goal.GoalReachedEvent;
import se.umu.cs.robotics.lokarria.fpsl.goal.PslGoal;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.lokarria.laser.LaserReader;
import se.umu.cs.robotics.lokarria.localization.LocalizationOperations;
import se.umu.cs.robotics.lokarria.localization.Pose;
import se.umu.cs.robotics.lokarria.reactivedrive.ReactiveComponent;
import se.umu.cs.robotics.lokarria.reactivedrive.VectorSumCommand;
import se.umu.cs.robotics.lokarria.statespace.FuzzyCommand;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class PslController {

    public static final boolean USE_MOTORS_IN_HISTORY = true;

    static Logger logger = Logger.getLogger(PslController.class);
    static boolean PRINT_TO_STD = false;

    final SensoryMotorSpace space;
    final LaserReader laser = LaserReader.getInstance();
    final DifferentialDriveOperations drive = new DifferentialDriveOperations();
    final LocalizationOperations licalization = new LocalizationOperations();
    final FLibrary<Double> library;
    private final FHypothesisSelector<Double> selector;
    final List<PslControllerListener> listeners = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<ReactiveComponent> fallbacks = new CopyOnWriteArrayList<>();
    private final ArrayList<PslGoal> goals = new ArrayList<>();
    Timer timer = new Timer("Psl Controller");
    TimerTask task = null;
    long timerPeriod = 50;
    long stepLimit = 0;
    long goalCountdown = -1;
    boolean postCommands = true;
    private boolean behaviorRecognitionEnabled = true;
    FPrediction<Double> lastPrediction;
    Responsibility<Double> responsibilities;

    public PslController(SensoryMotorSpace space, FLibrary<Double> library) {
        this.space = space;
        this.library = library;
        this.selector = new MaxProductSelector<>(library);
        loadFallbacksFromPropertyFile();
        loadGoalsFromPropertyFile();
    }

    private void loadFallbacksFromPropertyFile() {
        String fallbackClasses = HplPropertyLoader.loadProperty("lokarria.psl.controller.fallbacks");
        if (fallbackClasses != null) {
            for (String cName : fallbackClasses.split(";")) {
                try {
                    Class<ReactiveComponent> c = (Class<ReactiveComponent>) Class.forName(cName);
                    addFallback(c.newInstance());
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                    logger.warn(ex);
                }
            }
        }
    }

    private void loadGoalsFromPropertyFile() {
        final String controllerGoals = HplPropertyLoader.loadProperty("lokarria.psl.controller.goals");
        if (controllerGoals != null) {
            HashMap<String, HashMap> goals = (HashMap<String, HashMap>) JSONValue.parse(controllerGoals);
            for (String goal : goals.keySet()) {
                this.goals.add(new CircularGoal(goal, goals.get(goal)));
            }
        }
    }

    public synchronized void resetResponsibilitySignals() {
        responsibilities = new Responsibility<>(library);
        logger.info("Resetting responsibility signals");
        logger.info(responsibilities);
        if (PRINT_TO_STD) {
            System.out.println("Resetting responsibilities:");
            System.out.println(responsibilities);
        }
    }

    public LokarriaPslTrainer train(File file) throws SAXException {
        final LokarriaPslTrainer trainer = new LokarriaPslTrainer(library, file);
        trainer.start();
        return trainer;
    }

    public synchronized void start() {
        if (responsibilities == null || responsibilities.getContextCount() != library.getContextCount() || responsibilities.isNan()) {
            resetResponsibilitySignals();
        }
        if (task == null) {
            laser.start();
            laser.getEchoes(); // Fluch cache. 
            task = new TimerTask() {

                ObjectBuffer<SpaceDistribution> sensoryMotorBuffer = new LinkedBuffer<>(ObjectBuffer.BufferDirection.BACKWARD, 500);
                DifferentialDriveCommand command = null;
                long step = 0;

                @Override
                public void run() {
                    final LaserArray echoes = laser.getEchoes();
                    command = null;
                    if (echoes == null || echoes.size() == 0) {
                        return;
                    }
                    final SpaceDistribution dist;
                    if (command != null && USE_MOTORS_IN_HISTORY) {
                        dist = space.newDistribution(command, echoes);
                    } else {
                        dist = space.newDistribution(echoes);
                    }

                    updateResponsibilitySignals(dist);
                    sensoryMotorBuffer.add(dist);

                    FPrediction prediction = predict(sensoryMotorBuffer);
                    if (lastPrediction != null) {
                        ContextPredictionErrors<Double> contextPredictionErrors = new ContextPredictionErrors<>(library, lastPrediction.getSelection(), dist);
                        logger.info(contextPredictionErrors);
                    }

                    if (postCommands) {
                        command = control(prediction);
                    }
                    lastPrediction = prediction;

                    step++;
                    if (stepLimit > 0 && step >= stepLimit) {
                        stop();
                    } else {
                        Pose pose = licalization.getRobotLocalization().getRobotPosition();
                        checkGoals(pose, step);
                    }
                }
            };
            timer.scheduleAtFixedRate(task, 0, timerPeriod);
            for (PslControllerListener l : listeners) {
                l.controllerStarted(this);
            }
        } else {
            logger.warn("PSL Controller allready running!");
        }
    }

    FPrediction predict(ObjectBuffer<SpaceDistribution> buffer) {
        if (buffer.isEmpty()) {
            return null;
        } else {
            LinkedPositionIterator<SpaceDistribution> i = new LinkedPositionIterator<>(buffer);
            FPrediction<Double> prediction = new FPrediction(selector, i.next(), false);
            logger.info(prediction);
            return prediction;
        }
    }

    private synchronized void updateResponsibilitySignals(SpaceDistribution correctDistribution) {

        if (lastPrediction != null) {
            double[] errors = new double[library.getContextCount()];
            for (int c = 0; c < errors.length; c++) {
                FContext context = library.getContext(c);
                SpaceDistribution<Double> predictedDistribution = lastPrediction.element(context);

                double sum = 0;
                int dimensions = 0;
                Iterator<? extends ProbabilityDistribution<Double>> dims = correctDistribution.dimensions();
                while (dims.hasNext()) {
                    ProbabilityDistribution<Double> dist = dims.next();
                    if (dist != null) {
                        ProbabilityDistribution<Double> pDist = predictedDistribution.getDimension(dist.getDimension());
                        if (pDist != null) {
                            double distance = Math.abs(LokarriaPslPredictor.centerOfDist(pDist) - LokarriaPslPredictor.centerOfDist(dist));
                            sum += distance;
                            dimensions++;
                        }
                    }
                }
                errors[c] = sum / dimensions;
            }
            responsibilities = responsibilities.next(errors);
            logger.info(responsibilities);
            if (PRINT_TO_STD) {
                System.out.println(responsibilities);
            }
        }

        if (behaviorRecognitionEnabled) {
            responsibilities.propagateToLibrary();
        }

    }

    DifferentialDriveCommand control(FPrediction<Double> prediction) {
        SpaceDistribution<Double> sensoryMotorDistribution = prediction.element();
        List<FHypothesisMatch<Double>> targetHypotheses = prediction.getSelection().getTargetHypotheses();
        if (PRINT_TO_STD) {
            System.out.println(String.format("Length: %d, count: %d, id: %d",
                    targetHypotheses.size() > 0 ? targetHypotheses.get(0).getHypothesis().length() : 0,
                    targetHypotheses.size(),
                    targetHypotheses.size() > 0 ? targetHypotheses.get(0).getHypothesis().getId() : 0));
        }
        DifferentialDriveCommand command = null;
        SpaceDistribution<Double> motorDistribution = space.getMotorDistribution(sensoryMotorDistribution);
        if (motorDistribution.isUniform()) {
            command = controlFallback();
            if (PRINT_TO_STD) {
                System.out.println("Psl Controller: Fallback");
            }
            logger.info(new ControllerStatus(prediction));
        } else {
            command = new FuzzyCommand(motorDistribution);
            if (PRINT_TO_STD) {
                System.out.println("Psl Controller: Mach");
            }
            logger.info(new ControllerStatus(prediction, command));
        }
        if (command != null) {
            drive.postCommand(command);
        }
        for (PslControllerListener l : listeners) {
            l.command(this, command, prediction);
        }
        return command;
    }

    private DifferentialDriveCommand controlFallback() {
        LaserArray echoes = laser.getEchoes();
        DifferentialDriveCommand command = new ConcreteDifferentialDriveCommand(0, 1);
        if (fallbacks.isEmpty()) {
            if (PRINT_TO_STD) {
                System.out.println("No fallback");
            }
        } else if (echoes != null) {
            Object[] components = fallbacks.toArray();
            DifferentialDriveCommand[] commands = new DifferentialDriveCommand[components.length];
            int i = 0;
            for (Object c : components) {
                commands[i++] = ((ReactiveComponent) c).get(echoes, command);
            }
            return new VectorSumCommand(commands);
        }
        return null;
    }

    private void checkGoals(Pose pose, long step) {
        if (goalCountdown > 0) {
            goalCountdown--;
        } else if (goalCountdown == 0) {
            stop(false);
        } else {
            for (PslGoal goal : goals) {
                if (PRINT_TO_STD) {
                    System.out.println(String.format("Goal distance: %.3f", ((CircularGoal) goal).distance(pose)));
                }
                if (goal.goalReached(pose)) {
                    logger.info(new GoalReachedEvent(goal, step));
                    System.out.println(String.format("Goal reached after %d steps!", step));
                    goalCountdown = 40;
                }
            }
        }

    }

    public void stop() {
        stop(false);
    }

    public synchronized void stop(boolean sendStop) {
        if (task != null) {
            task.cancel();
            task = null;
            laser.stop();
            
            if (sendStop) {
                ConcreteDifferentialDriveCommand.stop();
            }
            for (PslControllerListener l : listeners) {
                l.controllerStopped(this);
            }
        }
    }

    public void shutdown() {
        stop();
        timer.cancel();
//        laser.shutdown();
    }

    public boolean isRunning() {
        return task != null;
    }

    public FLibrary<Double> getLibrary() {
        return library;
    }

    public void addListener(PslControllerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(PslControllerListener listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public SensoryMotorSpace stateSpace() {
        return space;
    }

    public void addFallback(ReactiveComponent component) {
        fallbacks.add(component);
    }

    public void removeFallback(ReactiveComponent component) {
        fallbacks.remove(component);
    }

    public void clearFallbacks() {
        fallbacks.clear();
    }

    public boolean isBehaviorRecognitionEnabled() {
        return behaviorRecognitionEnabled;
    }

    public void setBehaviorRecognitionEnabled(boolean behaviorRecognitionEnabled) {
        this.behaviorRecognitionEnabled = behaviorRecognitionEnabled;
    }

    public boolean isPostCommandsEnabled() {
        return postCommands;
    }

    public void setPostCommandsEnabled(boolean postCommands) {
        this.postCommands = postCommands;
    }

    public void setStepLimit(long count) {
        stepLimit = count;
    }

    public long getStepLimit() {
        return stepLimit;
    }
}
