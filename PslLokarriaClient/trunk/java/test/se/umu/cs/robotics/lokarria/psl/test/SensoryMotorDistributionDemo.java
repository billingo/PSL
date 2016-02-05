/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.lokarria.psl.test;

import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.logreader.lokarria.SensoryMotorEventReader;
import java.io.File;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.logreader.lokarria.LokarriaMessageHandler;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;
import se.umu.cs.robotics.logreader.xml.BufferedLogReader;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;
import se.umu.cs.robotics.lokarria.statespace.LaserSpace;
import se.umu.cs.robotics.lokarria.statespace.DifferentialDriveSpace;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.iteration.NullFilterIterator;
import se.umu.cs.robotics.log.LogConfigurator;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.iteration.DimensionIterator;
import se.umu.cs.robotics.probabilitydistribution.iteration.MaximumProbabilityIterator;
import se.umu.cs.robotics.statespace.StateDimension;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SensoryMotorDistributionDemo {

    /*
     * The state space, specifying how the data will be descritized.
     */
    final SensoryMotorSpace space = new SensoryMotorSpace(new DifferentialDriveSpace(15, 10), new LaserSpace(5));

    /*
     * The index of the dimension to iterate over.
     */
    public static final int DIMENSION = 1;

    public SensoryMotorDistributionDemo() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        LogConfigurator.configure();
    }

    @After
    public void tearDown() {
        LogConfigurator.shutdown();

    }

    @Test
    public void smDemo() throws SAXException {
        System.out.println("\nPrinting the most probable states form dimension "+DIMENSION+" by calling max() on the probability distributions.");
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<LokarriaLogMessage>("http://www.cs.umu.se/robotics", "log", 100);
        reader.addMessageHandler(new LokarriaMessageHandler());
        SensoryMotorEventReader eventReader = new SensoryMotorEventReader(space, reader, 50, false);
        reader.start(new File("logs/DrivingOut1.log.xml"));

        StateDimension<Double> dimension = space.getDimension(DIMENSION);

        for (SensoryMotorDistribution dist : eventReader) {
            ProbabilityDistribution<Double> pd = dist.getDimension(dimension);
            /*
             * The event reader data alternates between sensor and motor dists.
             * When no data is available for the specified dimension,
             * getDimension returns null.
             */
            if (pd != null) {
                IterableIterator<FuzzyItem<Double>> max = pd.max();
                FuzzyItem<Double> fuzzyItem = max.next();
                Double element = fuzzyItem.element();
                double probability = fuzzyItem.value();
                System.out.println(element + ": " + probability);
            }
        }
        System.out.println(dimension);
    }

    @Test
    public void smMaxIteratorDemo() throws SAXException {
        System.out.println("\nPrinting the most probable states form dimension "+DIMENSION+" using the "+MaximumProbabilityIterator.class.getSimpleName());
        final SensoryMotorSpace space = new SensoryMotorSpace(new DifferentialDriveSpace(15, 10), new LaserSpace(5));

        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<LokarriaLogMessage>("http://www.cs.umu.se/robotics", "log", 100);
        reader.addMessageHandler(new LokarriaMessageHandler());
        SensoryMotorEventReader eventReader = new SensoryMotorEventReader(space, reader, 50, false);
        reader.start(new File("logs/DrivingOut1.log.xml"));

        DimensionIterator dimensionIterator = new DimensionIterator(eventReader, DIMENSION);
        NullFilterIterator filteredDimensionIterator = new NullFilterIterator(dimensionIterator);
        MaximumProbabilityIterator<Double> maximumProbabilityIterator = new MaximumProbabilityIterator<Double>(filteredDimensionIterator);

        for (Double state : maximumProbabilityIterator) {
            System.out.println(state);
        }
    }
}
