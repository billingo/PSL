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

package se.umu.cs.robotics.lokarria.fpsl.main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.collections.IntegerRange;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.fpsl.FPslParameters;
import se.umu.cs.robotics.hpl.HplPropertyLoader;
import se.umu.cs.robotics.log.LogConfigurator;
import se.umu.cs.robotics.lokarria.MrdsLauncher;
import se.umu.cs.robotics.lokarria.core.LokarriaPropertyLoader;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.fpsl.LokarriaPslTrainer;
import se.umu.cs.robotics.lokarria.fpsl.PslController;
import se.umu.cs.robotics.lokarria.fpsl.PslSimulator;
import se.umu.cs.robotics.lokarria.fpsl.listeners.PslControllerListener;
import se.umu.cs.robotics.lokarria.statespace.DifferentialDriveSpace;
import se.umu.cs.robotics.lokarria.statespace.LaserSpace;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class PslControllerApp {

    final static int repeatTraining = 8;
    final static boolean useInternalSimulation = true;
    final static String[] behaviors = {"ToKitchen", "ToTheTV", "GoOut"};
    final static String[] startingPositions = {"Storage", "Bed", "Bathroom"};
//    final static String[] behaviors = {"ToKitchen"};
    final static String[][] behaviorData = {
        {"ToKitchen-1", "ToKitchen-2", "ToKitchen-3", "ToKitchen-4"},
        {"ToTV-1", "ToTV-2", "ToTV-3", "ToTV-4"},
        {"GoOut-1", "GoOut-2", "GoOut-3", "GoOut-4"}
    };

    File logFolder = new File("logs");
    final static String dataPath = HplPropertyLoader.loadProperty("log.reader.path");
    final DifferentialDriveSpace motorSpace = new DifferentialDriveSpace(20, 10);
    final LaserSpace sensorSpace = new LaserSpace(20, 10);
    final SensoryMotorSpace space = new SensoryMotorSpace(motorSpace, sensorSpace);

    final FLibrary<Double> library = new FLibrary<>(space, new FPslParameters(), behaviors);
    PslController controller;

    public PslControllerApp() {
        System.out.println("PSL Controller Application");
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        LogConfigurator.configure();
        PslControllerApp app = new PslControllerApp();

        app.trainPsl();
//        app.runGoalDirected(useInternalSimulation,20);
        app.runAllActive(useInternalSimulation, 20);
        app.runBehaviorRecognition(useInternalSimulation, 20);

//        app.consoleControl();
        LogConfigurator.shutdown();
    }

    private void consoleControl() {
        System.out.println("PSL Controller running.");
        System.out.println(String.format("There are %d contexts available:", behaviors.length));
        printContexts();
        System.out.println("Press the corresponding number to activate that context.");
        System.out.println("Use S and P to start and pause the controller, respectively, or press Q to quit:");
        while (true) {
            try {
                int c = Character.toUpperCase(System.in.read());
                if (c == 'Q') {
                    shutdown();
                    break;
                } else if (c == 'P') {
                    controller.stop(true);
                    System.out.println("Paused.");
                } else if (c == 'S') {
                    controller.start();
                    System.out.println("Started.");
                } else {
                    try {
                        int contextId = new Integer(new Character((char) c).toString());
                        for (int b = 0; b < behaviors.length; b++) {
                            library.getContext(b).setResponsibility(b == contextId ? 1d : 0d);
                        }
                        printContexts();
                    } catch (NumberFormatException ex) {
                    } catch (IndexOutOfBoundsException ex) {
                        System.out.println("Selected index does not exist.");
                    }
                }
            } catch (IOException ex) {
            }
        }
    }

    private void printContexts() {
        for (int b = 0; b < behaviors.length; b++) {
            System.out.println(String.format("%d: %s (responsibility: %.2f)", b, behaviors[b], library.getContext(b).getResponsibility()));
        }
    }

    private void trainPsl() {
        System.out.println("Trining PSL:");

        library.getContext(0).setResponsibility(0d);
        IntegerRange trainingOrder = new IntegerRange(behaviors.length);

        for (int epoch = 0; epoch < repeatTraining; epoch++) {
            System.out.println(String.format("Epoch %d out of %d.", epoch + 1, repeatTraining));
            int index = 0;
            int trained;
            do {
                trained = 0;
                Collections.shuffle(trainingOrder);
                for (int b : trainingOrder) {
                    library.activateOnly(b);
                    String[] data = behaviorData[b];
                    if (data.length > index) {
                        File file = new File(dataPath, data[index] + ".xml");
                        System.out.print(String.format("Training on file %s...", file.toString()));
                        try {
                            LokarriaPslTrainer trainer = new LokarriaPslTrainer(library, file);
                            trainer.start();
                            trainer.await();
                        } catch (SAXException ex) {
                            System.err.println("WARNING! Error occured when training on log file: " + file.toString());
                            ex.printStackTrace();
                        } catch (InterruptedException ex) {
                            System.err.println("WARNING! Interrupted while waiting for training to finish!");
                        }
                        trained++;
                        System.out.println(" done. Library size: " + library.size());
                    }
                }
                index++;
            } while (trained > 0);
        }
        System.out.println("Training finished.\n");
    }

    private void run(String configName, int configId, boolean useBehaviorRecognition, boolean internalSimulation) {
        MrdsLauncher mrds = new MrdsLauncher(configName, configId);
        initController(mrds, useBehaviorRecognition, internalSimulation);

        try {
            Process mrdsInstance = mrds.start();
            Thread.sleep(new Long(LokarriaPropertyLoader.getValue("mrds.startdelay")));
            controller.start();
            mrdsInstance.waitFor();
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex);
        }
        controller.shutdown();
    }

    private void runGoalDirected(boolean internalSimulation, int repetitions) {
        /*
         These sessions are executed with a single context highly active, providing goal directed bias. 
         */
        for (int i = 1; i <= repetitions; i++) {
            for (int config = 0; config < startingPositions.length; config++) {
                for (int c = 0; c < behaviors.length; c++) {
                    final String sessionName = String.format("All3-%s-%s-%s-%d", internalSimulation ? "sim" : "control", startingPositions[config], behaviors[c], i);
                    if (!sessionLogExists(sessionName)) {
                        setActiveBehavior(c);
                        System.out.println(String.format("Running %s from %s, session: %d", behaviors[c], startingPositions[config], i));
                        run(startingPositions[config], i, false, internalSimulation);
                        saveSessionLog(sessionName);
                    }
                }
            }
        }
    }
    
    private void runAllActive(boolean internalSimulation, int repetitions) {
        for (int i = 1; i<= repetitions; i++) {
            for (int config = 0; config < startingPositions.length; config++) {
                final String sessionName = String.format("All3-%s-%s-%d", internalSimulation ? "sim" : "control", startingPositions[config], i);
                if (!sessionLogExists(sessionName)) {
                    setAllActive();
                    System.out.println(String.format("Running all active from %s, session: %d", startingPositions[config], i));
                    run(startingPositions[config], i, false, internalSimulation);
                    saveSessionLog(sessionName);
                }
            }
        }
    }
    
    private void runBehaviorRecognition(boolean internalSimulation, int repetitions) {
        for (int i = 1; i<= repetitions; i++) {
            for (int config = 0; config < startingPositions.length; config++) {
                final String sessionName = String.format("All3-%s-%s-recog-%d", internalSimulation ? "sim" : "control", startingPositions[config], i);
                if (!sessionLogExists(sessionName)) {
                    setAllActive();
                    System.out.println(String.format("Running all active from %s, session: %d", startingPositions[config], i));
                    run(startingPositions[config], i, true, internalSimulation);
                    saveSessionLog(sessionName);
                }
            }
        }
    }

    private void setAllActive() {
        for (int c = 0; c < behaviors.length; c++) {
            library.getContext(c).setResponsibility(1d);
        }
    }
    
    private void setActiveBehavior(int behaviorId) {
        for (int c = 0; c < behaviors.length; c++) {
            library.getContext(c).setResponsibility(c == behaviorId ? 1d : 0.1d);
        }
    }

    private boolean sessionLogExists(String sessionName) {
        return new File(logFolder, sessionName + ".xml").exists();
    }

    private void saveSessionLog(String sessionName) {
        /*
         Note! Hack! Assumes ./logs/log.xml to be active log file. 
         */
        String date = new SimpleDateFormat("-yyyy-MM-dd_HH_mm_ss").format(new Date());
        LogConfigurator.shutdown();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
        File logFile = new File(logFolder, "log.xml");
        if (logFile.renameTo(new File(logFolder, sessionName + ".xml"))) {
            System.out.println("Log saved as: " + sessionName + ".xml");
            LogConfigurator.configure();
        } else if (logFile.renameTo(new File(logFolder, sessionName + date + ".xml"))) {
            System.out.println("Log saved as: " + sessionName + date + ".xml");
            LogConfigurator.configure();
        } else {
            System.err.println("Warning! Unable to save session log! Logging terminated!");
        }
    }

    private void initController(MrdsLauncher mrds, boolean useBehaviorRecognition, boolean internalSimulation) {
        if (internalSimulation) {
            controller = new PslSimulator(space, library);
        } else {
            controller = new PslController(space, library);
        }
        controller.setStepLimit(1000);
        controller.setBehaviorRecognitionEnabled(useBehaviorRecognition);
        controller.addListener(new PslControllerListener() {

            @Override
            public void controllerStarted(PslController controller) {
            }

            @Override
            public void controllerStopped(PslController controller) {
                mrds.stop();
            }

            @Override
            public void command(PslController controller, DifferentialDriveCommand command, FPrediction<Double> prediction) {
            }
        });
    }

    private void shutdown() {
        controller.shutdown();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        LogConfigurator.shutdown();
        System.out.println("Controller stoped.");
    }
}
