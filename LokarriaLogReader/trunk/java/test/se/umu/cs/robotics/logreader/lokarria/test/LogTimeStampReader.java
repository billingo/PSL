/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.logreader.lokarria.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.iteration.position.LinkedPosition;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.logreader.lokarria.helpers.LokarriaLogReader;
import se.umu.cs.robotics.logreader.xml.BufferedLogReader;
import se.umu.cs.robotics.lokarria.statespace.DifferentialDriveSpace;
import se.umu.cs.robotics.lokarria.statespace.LaserSpace;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;
import sun.rmi.log.ReliableLog.LogFile;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LogTimeStampReader {

    public static final String LOG_PATH = "../LokarriaFxClient/logs/FromTVTurnarroundAndBack.log.xml";
    public static long lookForTimestamp = 1306496732506L;
    static SensoryMotorSpace space = new SensoryMotorSpace(new DifferentialDriveSpace(10, 10), new LaserSpace(1));

    public LogTimeStampReader() {
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
    public void readTimeStamps() throws SAXException {
        LokarriaLogReader logReader = LokarriaLogReader.newReader(space, LOG_PATH);

        int i = 10;
        boolean found = false;
        while (logReader.hasNext()) {
            LinkedPosition<SensoryMotorDistribution> next = logReader.next();
            if (lookForTimestamp < logReader.getTimeStamp() && found == false) {
                System.out.println(String.format("Index %d: Time Stamp: %d", i, logReader.getTimeStamp()));
                found = true;
            } else if (lookForTimestamp == 0 && i % 10 == 0) {
                System.out.println(String.format("Index %d: Time Stamp: %d", i, logReader.getTimeStamp()));
            }
            i++;
        }
    }
}
