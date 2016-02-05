
package se.umu.cs.robotics.lokarria.fpsl.test.prediction;

import se.umu.cs.robotics.lokarria.fpsl.LokarriaPslPredictor.Predictions;
import se.umu.cs.robotics.lokarria.fpsl.LokarriaPslPredictor;
import se.umu.cs.robotics.log.LogConfigurator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.lokarria.fpsl.LokarriaPslTrainer;
import se.umu.cs.robotics.lokarria.fpsl.plots.LokarriaPslPredictorPlot;
import se.umu.cs.robotics.lokarria.statespace.DifferentialDriveSpace;
import se.umu.cs.robotics.lokarria.statespace.LaserSpace;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;
import se.umu.cs.robotics.utils.MathTools;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import se.umu.cs.robotics.fpsl.FPslParameters;
import se.umu.cs.robotics.hpl.HplPropertyLoader;
import se.umu.cs.robotics.logreader.lokarria.helpers.LokarriaLogReader;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LokarriaPslPredictionTest {

    public static final int DEFAULT_REPETITIONS = 3;
    DifferentialDriveSpace motorSpace = new DifferentialDriveSpace(20, 10);
    LaserSpace sensorSpace = new LaserSpace(20, 8);
    SensoryMotorSpace space = new SensoryMotorSpace(motorSpace, sensorSpace);
    protected ArrayList<String> sourceFiles = new ArrayList<String>();
    LokarriaPslPredictor predictor;

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogConfigurator.configure();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        LogConfigurator.shutdown();
    }

    @Before
    public void setUpSourceFiles() {
        sourceFiles.add("DrivingToTheTV1");
        sourceFiles.add("DrivingToTheTV2");
        sourceFiles.add("DrivingToTheTV3");
        sourceFiles.add("DrivingToTheTV4");
        sourceFiles.add("DrivingToTheTV5");
        sourceFiles.add("DrivingToTheTV6");
        sourceFiles.add("DrivingToTheTV7");
        sourceFiles.add("DrivingToTheTV8");
        sourceFiles.add("DrivingToTheTV9");
        sourceFiles.add("DrivingToTheTV10");
    }

    @Before
    public void setUpPredictor() {
        setUpPredictor(new FPslParameters());
    }

    public void setUpPredictor(FPslParameters parameters) {
        final boolean PREDICT_SENSORS = true;
        final boolean PREDICT_MOTORS = true;
        predictor = new LokarriaPslPredictor(space, PREDICT_SENSORS, PREDICT_MOTORS, parameters);
    }

    public int getNameIndex(String name) {
        for (int i = 0; i < sourceFiles.size(); i++) {
            if (sourceFiles.get(i).equals(name)) {
                return i;
            }
        }
        throw new IllegalArgumentException(name);
    }

    public ArrayList<TrainingStatus> trainAllFilesExcept(int index) throws InterruptedException, SAXException {
        return trainAllFilesExcept(predictor, index, DEFAULT_REPETITIONS);
    }

    public ArrayList<TrainingStatus> trainAllFilesExcept(int index, int repeat) throws InterruptedException, SAXException {
        return trainAllFilesExcept(predictor, index, repeat);
    }

    public ArrayList<TrainingStatus> trainAllFilesExcept(LokarriaPslPredictor predictor, String logName, int repeat) throws InterruptedException, SAXException {
        return trainAllFilesExcept(predictor, getNameIndex(logName), repeat);
    }

    public ArrayList<TrainingStatus> trainAllFilesExcept(LokarriaPslPredictor predictor, int index, int repeat) throws InterruptedException, SAXException {
        ArrayList<TrainingStatus> trainingHistory = new ArrayList<TrainingStatus>();
        for (int repetition = 0; repetition < repeat; repetition++) {
            for (int i = 0; i < sourceFiles.size(); i++) {
                if (i != index) {
                    trainingHistory.add(train(predictor, sourceFiles.get(i)));
                }
            }
        }
        return trainingHistory;
    }

    protected TrainingStatus train(LokarriaPslPredictor predictor, String logFile) throws InterruptedException, SAXException {
        LokarriaPslTrainer trainer = predictor.train(new File(logPath(logFile)));
        trainer.await();
        System.out.println(String.format("Training of %s finished with %d hypotheses in library.", logFile, predictor.getLibrary().size()));
        return new TrainingStatus(logFile, predictor.getLibrary().size());
    }

    protected String plotPath(String name) {
        return String.format("%s/%s_%s.py", HplPropertyLoader.loadProperty("plot.path"), this.getClass().getSimpleName(), name);
    }

    protected String logPath(String name) {
        return String.format("%s/%s.log.xml", HplPropertyLoader.loadProperty("log.reader.path"), name);
    }

    private void renderResult(Predictions predictions, final String name) throws IOException {
        double avgIntersection = MathTools.mean(predictions.getIntersections());
        double avgDistance = MathTools.mean(predictions.getAverageLaserDistances());
        double angularSpeedErrors = MathTools.mean(predictions.getAngularSpeedErrors());
        double linearSpeedErrors = MathTools.mean(predictions.getLinearSpeedErrors());
        double avgMatch = predictions.getAverageMatch();
        double avgLength = MathTools.mean(predictions.getHypothesesLengths());
        double avgConsiderations = 100d * meanHypothesesConsiderations(predictions.getHypothesisConsiderationCounts());
        final int[] matchCounts = predictions.getHypothesisMatchCounts();
        System.out.println(String.format("Average prediction performance for %s: %.3f", name, avgIntersection));
        System.out.println(String.format("Average laser distance for %s: %.3f", name, avgDistance));
        System.out.println(String.format("Average angular speed error for %s: %.3f", name, angularSpeedErrors));
        System.out.println(String.format("Average linear speed error for %s: %.3f", name, linearSpeedErrors));
        System.out.println(String.format("Average hypothesis match: %.0f%%", avgMatch * 100));
        System.out.println(String.format("Average hypothesis length: %.3f", avgLength));
        System.out.println(String.format("Average hypotheses considerations: %.1f%%", avgConsiderations));
        System.out.println(String.format("Hypothesis match count - Average: %.3f Max: %d", MathTools.mean(matchCounts), MathTools.max(matchCounts)));
        Writer out = new FileWriter(new File(plotPath(name)));
        new LokarriaPslPredictorPlot(predictor, predictions).render(out);
        out.close();
    }

    private double meanHypothesesConsiderations(int[] considerations) {
        double count = 0;
        double size = predictor.getLibrary().size();
        for (int i = 0; i < considerations.length; i++) {
            count += ((double) considerations[i]) / size;
        }
        return count / ((double) considerations.length);
    }

    protected LinkedPositionIterator<SensoryMotorDistribution> readEvents(String name) throws SAXException {
        return LokarriaLogReader.read(space, logPath(name));
    }

    void test(String name, int index) throws SAXException, IOException {
        LinkedPositionIterator<SensoryMotorDistribution> events = readEvents(name);
        Predictions predictions = predictor.predict(events);
        renderResult(predictions, name + "." + index);
    }

    public static class TrainingStatus {

        private final int librarySize;
        private final String logFile;

        TrainingStatus(String logFile, int librarySize) {
            this.logFile = logFile;
            this.librarySize = librarySize;
        }

        @Override
        public String toString() {
            return String.format("{'librarySize':%d, 'logFile':'%s'}", librarySize, logFile);
        }
    }
}
