/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.lokarria.psl.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.lokarria.laser.LaserOperations;
import se.umu.cs.robotics.log.LogConfigurator;
import se.umu.cs.robotics.lokarria.statespace.LaserSpace;
import static org.junit.Assert.*;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class PslLokarriaClientTest {

    LaserOperations laser = new LaserOperations();
    LaserSpace laserSpace = new LaserSpace(10,50);

    public PslLokarriaClientTest() {
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
    public void testSingleLaserScan() {
        LaserArray echoes = laser.getEchoes();
        System.out.println(echoes);
        SpaceDistribution<Double> d = laserSpace.newDistribution(echoes);
        System.out.println(d);
    }
}
