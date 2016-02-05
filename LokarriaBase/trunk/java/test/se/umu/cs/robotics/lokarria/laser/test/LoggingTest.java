/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.lokarria.laser.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.varia.NullAppender;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDrive;
import se.umu.cs.robotics.lokarria.differentialdrive.ConcreteDifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveOperations;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.lokarria.laser.LaserReader;
import se.umu.cs.robotics.log.LogConfigurator;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LoggingTest {

    private LaserReader laser;
    private DifferentialDriveOperations ddrive;

    public LoggingTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        LogConfigurator.configure("log.test.config.xml");

        laser = LaserReader.getInstance();
        ddrive = new DifferentialDriveOperations();
    }

    @After
    public void tearDown() {
        LogManager.shutdown();
    }

    @Test
    public void testSynchroniousLaserAndDDLog() throws InterruptedException {
        laser.start();
        laser.await();
        LaserArray echoes1 = laser.getEchoes();
        assertNotNull(echoes1);
        for (int i = 0; i < 20; i++) {
            ddrive.postCommand(new ConcreteDifferentialDriveCommand(0.2, 0.4));
            Thread.sleep(50);
        }
        LaserArray echoes2 = laser.getEchoes();
        Thread.sleep(60);
        LaserArray echoes3 = laser.getEchoes();
        System.out.println(String.format("Update frequency: %.2f", laser.getUpdateFrequency()));
        System.out.println(echoes3.timeStamp() - echoes2.timeStamp());
        ddrive.postCommand(ConcreteDifferentialDriveCommand.stop());
    }

    @Test
    public void testDDGet() throws InterruptedException {
        DifferentialDriveCommand command = ddrive.getCommand();
        System.out.println(command);
    }
}
