/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.logreader.lokarria.test;

import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.lokarria.core.JsonOperations.JsonException;
import se.umu.cs.robotics.lokarria.log.JsonMessage;
import se.umu.cs.robotics.lokarria.laser.LaserArrayList;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.logreader.LogEvent;
import java.io.File;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.logreader.lokarria.filter.LaserFilter;
import se.umu.cs.robotics.logreader.lokarria.LokarriaMessageHandler;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;
import se.umu.cs.robotics.logreader.xml.BufferedLogReader;
import se.umu.cs.robotics.lokarria.statespace.LaserSpace;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LogReaderCountTest {

    public LogReaderCountTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void countLaserEvents() throws SAXException, JsonException {
        final String logFile = "logs/DrivingToTheTVRepeat.log.xml";
        
        LaserSpace space = new LaserSpace(10);
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<LokarriaLogMessage>("http://se.umu.cs.robotics.lokarria", "log", 10);
        reader.addMessageHandler(new LokarriaMessageHandler());
        reader.setFilter(new LaserFilter());

        reader.start(new File(logFile));
        int count = 0;

        for (LogEvent<LokarriaLogMessage> e : reader) {
            LaserArray laser = null;
            for (LokarriaLogMessage m : e.messages()) {
                laser = LaserArrayList.fromJSON(((JsonMessage) m).toJSON());
            }
//            for (LaserEcho echo: laser) {
//                System.out.print(Math.round(echo.getDistance())+";");
//            }
//            System.out.println("");
            SpaceDistribution<Double> pd = space.newDistribution(laser);
            count++;
        }
        System.out.println(logFile+" contains " + count + " laser events.");
    }
}
