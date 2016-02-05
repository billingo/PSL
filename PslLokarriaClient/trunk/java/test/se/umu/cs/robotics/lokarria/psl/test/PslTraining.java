package se.umu.cs.robotics.lokarria.psl.test;

import java.io.File;
import java.util.ArrayList;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.logreader.lokarria.SensoryMotorEventReader;
import se.umu.cs.robotics.logreader.lokarria.LokarriaMessageHandler;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;
import se.umu.cs.robotics.logreader.xml.BufferedLogReader;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;
import se.umu.cs.robotics.lokarria.statespace.LaserSpace;
import se.umu.cs.robotics.lokarria.statespace.DifferentialDriveSpace;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.fpsl.selection.MaxPredictionSelector;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.log.LogConfigurator;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.comparator.MaxProbabilityComparator;
import se.umu.cs.robotics.utils.MathTools;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class PslTraining {

    static final SensoryMotorSpace space = new SensoryMotorSpace(new DifferentialDriveSpace(15, 10), new LaserSpace(10));
    static final FLibrary<Double> library = new FLibrary<Double>(space);

    public PslTraining() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogConfigurator.configure();
        trainPsl(new File("logs/DrivingToTheTV.log.xml"));
        trainPsl(new File("logs/DrivingToTheTV2.log.xml"));
        trainPsl(new File("logs/DrivingToTheTV.log.xml"));
        trainPsl(new File("logs/DrivingToTheTV2.log.xml"));
        trainPsl(new File("logs/DrivingToTheTV.log.xml"));
        trainPsl(new File("logs/DrivingToTheTV2.log.xml"));
        trainPsl(new File("logs/DrivingToTheTV.log.xml"));
        trainPsl(new File("logs/DrivingToTheTV2.log.xml"));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        LogConfigurator.shutdown();
    }

    @Before
    public void setUp() throws SAXException {
    }

    @After
    public void tearDown() {
    }

    static void trainPsl(File file) throws SAXException {
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<LokarriaLogMessage>("http://se.umu.cs.robotics.lokarria", "log", 100);
        reader.addMessageHandler(new LokarriaMessageHandler());
        SensoryMotorEventReader eventReader = new SensoryMotorEventReader(space, reader, 50);
        LinkedPositionIterator<SensoryMotorDistribution> events = new LinkedPositionIterator<SensoryMotorDistribution>(eventReader);
        MaxPredictionSelector<Double> selector = new MaxPredictionSelector<Double>(library);

        reader.start(file);
        events.next();

        ArrayList<Double> trainingIntersections = new ArrayList<Double>();

        for (IteratorPosition p : events) {
            FPrediction<Double> prediction = new FPrediction<Double>(selector, p.getPrevious(), true);
            SpaceDistribution<Double> predDistribution = prediction.element();
            SpaceDistribution<Double> realDistribution = (SpaceDistribution<Double>) p.element();
            prediction.teach(realDistribution);
            trainingIntersections.add(realDistribution.intersection(predDistribution));
        }

        System.out.println(String.format("Average performance on %s during training: %.3f", file.getName(), MathTools.mean(trainingIntersections)));
        System.out.println("Library size: " + library.size());
    }

    @Test
    public void testPerformance1() throws SAXException {
        testPerformance(new File("logs/DrivingToTheTV.log.xml"));
    }

    @Test
    public void testPerformance2() throws SAXException {
        testPerformance(new File("logs/DrivingToTheTV3.log.xml"));
    }

    void testPerformance(File file) throws SAXException {
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<LokarriaLogMessage>("http://se.umu.cs.robotics.lokarria", "log", 100);
        reader.addMessageHandler(new LokarriaMessageHandler());
        SensoryMotorEventReader eventReader = new SensoryMotorEventReader(space, reader, 50);
        LinkedPositionIterator<SensoryMotorDistribution> events = new LinkedPositionIterator<SensoryMotorDistribution>(eventReader);
        MaxPredictionSelector<Double> selector = new MaxPredictionSelector<Double>(library);
        MaxProbabilityComparator<Double> comparator = new MaxProbabilityComparator<Double>();

        reader.start(file);
        events.next();

        ArrayList<Double> intersections = new ArrayList<Double>();
        ArrayList<Double> maxEquals = new ArrayList<Double>();

        for (IteratorPosition p : events) {
            FPrediction<Double> prediction = new FPrediction<Double>(selector, p.getPrevious(), true);
            SpaceDistribution<Double> predDistribution = prediction.element();
            SpaceDistribution<Double> realDistribution = (SpaceDistribution<Double>) p.element();
//            System.out.println("Real: "+realDistribution);
//            System.out.println("Predicted: "+predDistribution.toString());
            final double intersection = realDistribution.intersection(predDistribution);
            final double maxEq = comparator.compare(realDistribution, predDistribution);
//            System.out.println("Intersection: "+intersection);
            intersections.add(intersection);
            maxEquals.add(maxEq);
        }
        System.out.println(String.format("Average performance on %s after training: %.3f", file.getName(), MathTools.mean(intersections)));
        System.out.println(String.format("Average max performance on %s after training: %.3f", file.getName(), MathTools.mean(maxEquals)));
    }
}
