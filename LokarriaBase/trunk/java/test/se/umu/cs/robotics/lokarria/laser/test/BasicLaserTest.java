
package se.umu.cs.robotics.lokarria.laser.test;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.DOMConfiguration;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.lokarria.laser.LaserReader;
import se.umu.cs.robotics.log.LogConfigurator;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class BasicLaserTest {

    private LaserReader laser = LaserReader.getInstance();

    public BasicLaserTest() {
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
    public void testLaserEcho() throws InterruptedException {
        laser.start();
        laser.await();
        LaserArray echoes1 = laser.getEchoes();
        assertNotNull(echoes1);
        Thread.sleep(10000);
        LaserArray echoes2 = laser.getEchoes();
        Thread.sleep(60);
        LaserArray echoes3 = laser.getEchoes();
        System.out.println(String.format("Update frequency: %.2f", laser.getUpdateFrequency()));
        System.out.println(echoes2);
        System.out.println(echoes3.timeStamp() - echoes2.timeStamp());
        System.out.println("Update: " + laser.getUpdateFrequency());
    }
    
}
