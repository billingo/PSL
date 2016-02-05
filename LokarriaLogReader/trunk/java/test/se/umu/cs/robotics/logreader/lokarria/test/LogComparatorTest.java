/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.logreader.lokarria.test;

import java.io.File;
import java.util.ArrayList;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.logreader.lokarria.SensoryMotorEventReader;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;
import se.umu.cs.robotics.lokarria.statespace.LaserSpace;
import se.umu.cs.robotics.lokarria.statespace.DifferentialDriveSpace;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;
import se.umu.cs.robotics.logreader.xml.BufferedLogReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.logreader.lokarria.LokarriaMessageHandler;
import se.umu.cs.robotics.log.LogConfigurator;
import static org.junit.Assert.*;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.utils.MathTools;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LogComparatorTest {

    public LogComparatorTest() {
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
    public void compareLogs() throws SAXException {
        SensoryMotorSpace space = new SensoryMotorSpace(new DifferentialDriveSpace(15, 10), new LaserSpace(10));
        BufferedLogReader<LokarriaLogMessage> reader1 = new BufferedLogReader<LokarriaLogMessage>("http://se.umu.cs.robotics.lokarria", "log", 100);
        BufferedLogReader<LokarriaLogMessage> reader2 = new BufferedLogReader<LokarriaLogMessage>("http://se.umu.cs.robotics.lokarria", "log", 100);
        reader1.addMessageHandler(new LokarriaMessageHandler());
        reader2.addMessageHandler(new LokarriaMessageHandler());
        SensoryMotorEventReader eventReader1 = new SensoryMotorEventReader(space, reader1, 50);
        SensoryMotorEventReader eventReader2 = new SensoryMotorEventReader(space, reader2, 50);

        reader1.start(new File("logs/DrivingToTheTVRepeat.log.xml"));
        reader2.start(new File("logs/DrivingToTheTV.log.xml"));

        ArrayList<Double> intersections = new ArrayList<Double>();

        while (eventReader1.hasNext() && eventReader2.hasNext()) {
            SensoryMotorDistribution e1 = eventReader1.next();
            SensoryMotorDistribution e2 = eventReader2.next();
            if (true || (e1.isSensoryDistribution() && e2.isSensoryDistribution())) {
                double intersection = e1.intersection(e2);
                System.out.println(intersection);
                intersections.add(intersection);
            }
        }

        final double meanIntersection = MathTools.mean(intersections);
        System.out.println(String.format("Average intersection %.3f", meanIntersection));
        assertTrue(0.8 < meanIntersection);
    }
}
