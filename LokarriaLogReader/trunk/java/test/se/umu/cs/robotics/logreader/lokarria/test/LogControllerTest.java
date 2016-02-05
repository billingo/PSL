
package se.umu.cs.robotics.logreader.lokarria.test;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.logreader.lokarria.LokarriaLogController;
import se.umu.cs.robotics.lokarria.laser.LaserReader;
import se.umu.cs.robotics.log.LogConfigurator;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LogControllerTest {

    public LogControllerTest() {
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
     public void testLogController() throws SAXException, InterruptedException {
//        File sourceFile = new File("logs/DrivingToTheKitchen.log.xml");
        File sourceFile = new File("logs/DrivingToTheTV3.log.xml");
        LokarriaLogController controller = new LokarriaLogController();
        LaserReader laser = LaserReader.getInstance();
        laser.start();
        controller.play(sourceFile);
        controller.await();
        laser.stop();
     }

}