package se.umu.cs.robotics.lokarria.fpsl.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.fpsl.FPslParameters;
import se.umu.cs.robotics.fpsl.responsibility.Responsibility;
import se.umu.cs.robotics.hpl.HplPropertyLoader;
import se.umu.cs.robotics.io.WriterFormat;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.log.LogConfigurator;
import se.umu.cs.robotics.logreader.lokarria.helpers.LokarriaLogReader;
import se.umu.cs.robotics.lokarria.fpsl.LokarriaPslPredictor;
import se.umu.cs.robotics.lokarria.fpsl.LokarriaPslPredictor.Predictions;
import se.umu.cs.robotics.lokarria.fpsl.LokarriaPslTrainer;
import se.umu.cs.robotics.lokarria.statespace.DifferentialDriveSpace;
import se.umu.cs.robotics.lokarria.statespace.LaserSpace;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;
import se.umu.cs.robotics.utils.StringTools;

/**
 * A test of behavior recognition
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class PslRecognitionApp {

    final double[] RESPONSIBILITY_SCALING = {0.5, 1, 2, 3, 4};
    final double NULL_ERROR  = 5;
    
    final static int repeatTraining = 1;
    final static String[] behaviors = {"ToTheTV", "Corridor", "ToTheKitchen"};
    final static String[][] behaviorData = {
        {"ToTheTV.train.1", "ToTheTV.train.2", "ToTheTV.train.3", "ToTheTV.train.4", "ToTheTV.train.5", "ToTheTV.train.6", "ToTheTV.train.7", "ToTheTV.train.8"},
        {"DrivingAroundTheCorridor1", "DrivingAroundTheCorridor2", "DrivingAroundTheCorridor3"}, 
        {"FromBedroomCornerToKitchen1","FromBedroomCornerToKitchen2","FromBedroomCornerToKitchen3","FromBedroomCornerToKitchen4","FromBedroomCornerToKitchen6"}
    };
    final static String[] testData = {
        "ToTheTV.train.9",
        "DrivingAroundTheCorridor4",
        "FromBedToTV1",
        "FromKitchenToBedroomCorner1",
        "FromFridgeToTV1", 
        "FromBedroomCornerToKitchen5"
    };
    final static String dataPath = HplPropertyLoader.loadProperty("log.reader.path");
    final DifferentialDriveSpace motorSpace = new DifferentialDriveSpace(30, 10);
    final LaserSpace sensorSpace = new LaserSpace(20, 20);
    final SensoryMotorSpace space = new SensoryMotorSpace(motorSpace, sensorSpace);
    final LokarriaPslPredictor predictor = new LokarriaPslPredictor(space, true, true, new FPslParameters(), behaviors);
    final WriterFormat out;

    public PslRecognitionApp() throws IOException {
        System.out.println("Psl Recognition Test");
        final String outFile = String.format(Locale.US, "%s/PslRecognition.py", HplPropertyLoader.loadProperty("plot.data.path"));
        out = new WriterFormat(new FileWriter(new File(outFile)));
        System.out.println(String.format("Data is written to %s.", outFile));
    }

    public static void main(String[] args) {
        LogConfigurator.configure();
        PslRecognitionApp app;
        try {
            app = new PslRecognitionApp();
            app.trainPsl();
            app.runRecognition();
            app.close();
        } catch (IOException ex) {
            System.err.println("Unable to open output file:");
            ex.printStackTrace();
        }
        LogConfigurator.shutdown();
    }

    private ArrayList<HashMap<String, ArrayList<Double>>> calculatePredictionErrors(FLibrary<Double> library) {
        System.out.print("Calculating prediction errors... ");
        ArrayList<HashMap<String, ArrayList<Double>>> predictionErrors = new ArrayList<HashMap<String, ArrayList<Double>>>();
        for (int context = 0; context < library.getContexts().size(); context++) {
            library.activateOnly(context);
            HashMap<String, ArrayList<Double>> map = new HashMap<String, ArrayList<Double>>();
            predictionErrors.add(map);
            for (String testFile : testData) {
                try {
                    LinkedPositionIterator<SensoryMotorDistribution> events = LokarriaLogReader.read(space, getLogFile(testFile));
                    Predictions predictions = predictor.predict(events);
                    final ArrayList<Double> averageLaserDistances = predictions.getAverageLaserDistances();
                    map.put(testFile, averageLaserDistances);
                } catch (SAXException ex) {
                    ex.printStackTrace();
                }
            }
        }
        System.out.println(" done.");
        return predictionErrors;
    }

    private HashMap<String, ArrayList<Responsibility<Double>>> calculateResponsibilities(FLibrary<Double> library, ArrayList<HashMap<String, ArrayList<Double>>> predictionErrors, double responsibilityScaling) {
        System.out.print(String.format(Locale.US, "Calculating responsibilities using a scaling factor of %.3f...", responsibilityScaling));
        HashMap<String, ArrayList<Responsibility<Double>>> responsibilities = new HashMap<String, ArrayList<Responsibility<Double>>>();
        for (String testFile : testData) {
            ArrayList<Responsibility<Double>> resps = new ArrayList<Responsibility<Double>>();
            Responsibility<Double> resp = new Responsibility<Double>(library, responsibilityScaling);
            int t = 0;
            while (true) {
                resps.add(resp);
                t++;
                try {
                    double[] errors = new double[predictionErrors.size()];
                    for (int context = 0; context < predictionErrors.size(); context++) {
                        Double contextError = predictionErrors.get(context).get(testFile).get(t);
                        errors[context] = contextError == null ? NULL_ERROR : contextError;
                    }
                    resp = resp.next(errors);
                } catch (IndexOutOfBoundsException ex) {
                    break;
                }
            }
            responsibilities.put(testFile, resps);
        }
        System.out.println(" done.");
        return responsibilities;
    }

    private void trainPsl() {
        System.out.println("Trining PSL:");
        FLibrary<Double> library = predictor.getLibrary();
        library.getContext(0).setResponsibility(0d);

        for (int epoch = 0; epoch < repeatTraining; epoch++) {
            System.out.println(String.format("Epoch %d out of %d.", epoch + 1, repeatTraining));
            int index = 0;
            int trained;
            do {
                trained = 0;
                for (int b = 0; b < behaviors.length; b++) {
                    library.activateOnly(b);
                    String[] data = behaviorData[b];
                    if (data.length > index) {
                        File file = getLogFile(data[index]);
                        System.out.print(String.format("Training on file %s...", file.toString()));
                        try {
                            LokarriaPslTrainer trainer = predictor.train(file);
                            trainer.await();
                        } catch (SAXException ex) {
                            System.err.println("WARNING! Error occured when training on log file: " + file.toString());
                            ex.printStackTrace();
                        } catch (InterruptedException ex) {
                            System.err.println("WARNING! Interrupted while waiting for training to finish!");
                        }
                        trained++;
                        System.out.println(" done.");
                    }
                }
                index++;
            } while (trained > 0);
        }
        System.out.println("Training finished.\n");
    }

    private File getLogFile(String logName) {
        return new File(dataPath, logName + ".log.xml");
    }

    private void runRecognition() {
        FLibrary<Double> library = predictor.getLibrary();

        ArrayList<HashMap<String, ArrayList<Double>>> predictionErrors = calculatePredictionErrors(library);
        try {
            out.writeln("behaviors = ('%s')", StringTools.join(behaviors, "','"));
            out.writeln("responsibilities = {");
            boolean first = true;
            for (double scaling : RESPONSIBILITY_SCALING) {
                out.write("%.5f:", scaling);
                HashMap<String, ArrayList<Responsibility<Double>>> responsibilities = calculateResponsibilities(library, predictionErrors, scaling);
                renderResponsibilities(responsibilities);
                out.writeln(",");
            }
            out.writeln("}");
        } catch (IOException ex) {
            System.err.println("Unable to render results!");
            ex.printStackTrace();
        }


    }

    private void renderResponsibilities(HashMap<String, ArrayList<Responsibility<Double>>> responsibilities) {
        System.out.print("Rendering results...");
        try {
            out.writeln("{");
            for (Entry<String, ArrayList<Responsibility<Double>>> entry : responsibilities.entrySet()) {
                String restFile = entry.getKey();
                out.writeln("'%s':[", restFile);
                for (Responsibility<Double> responsibility : entry.getValue()) {
                    out.writeln("\t(%s),", StringTools.join("%.8f", responsibility.get(), ","));
                }
                out.writeln("],");
            }
            out.writeln("}");
        } catch (IOException ex) {
            System.err.println("Unable to render results!");
            ex.printStackTrace();
        }
        System.out.println(" done.");
    }

    private void close() throws IOException {
        out.close();
    }
}
