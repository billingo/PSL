/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.lokarria.psl.test;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.fpsl.LokarriaPslTrainer;
import se.umu.cs.robotics.lokarria.fpsl.PslController;
import se.umu.cs.robotics.lokarria.fpsl.listeners.PslControllerListener;
import se.umu.cs.robotics.log.LogConfigurator;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;
import se.umu.cs.robotics.lokarria.statespace.DifferentialDriveSpace;
import se.umu.cs.robotics.lokarria.statespace.LaserSpace;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class PslControllerTest {
    final DifferentialDriveSpace motorSpace = new DifferentialDriveSpace(15, 10);
    final LaserSpace sensorSpace = new LaserSpace(20);
    final SensoryMotorSpace space = new SensoryMotorSpace(motorSpace, sensorSpace);

    public PslControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogConfigurator.configure();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        LogConfigurator.shutdown();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private void trainController(PslController controller, String logFile) throws InterruptedException, SAXException {
        LokarriaPslTrainer trainer = controller.train(new File(logFile));
        trainer.await();
        System.out.println(String.format("Training of %s finished with %d hypotheses in library.", logFile, controller.getLibrary().size()));
    }

    @Test
    public void testController() throws SAXException, InterruptedException {
        final CountDownLatch running = new CountDownLatch(1);
        FLibrary<Double> library = new FLibrary<>(space);
        PslController controller = new PslController(space, library);

        for (int i = 0; i < 3; i++) {
            trainController(controller, "logs/DrivingOut1.log.xml");
            trainController(controller, "logs/DrivingOut2.log.xml");
            trainController(controller, "logs/DrivingOut3.log.xml");
//            trainController(controller, "logs/DrivingToTheTV3.log.xml");
            trainController(controller, "logs/DrivingToTheTV4.log.xml");
            trainController(controller, "logs/DrivingToTheTV5.log.xml");
            trainController(controller, "logs/DrivingToTheTV6.log.xml");
            trainController(controller, "logs/DrivingToTheTV7.log.xml");
            trainController(controller, "logs/DrivingToTheTV8.log.xml");
            trainController(controller, "logs/DrivingToTheTV9.log.xml");
        }

        controller.addListener(new PslControllerListener() {

            public void controllerStarted(PslController controller) {
                System.out.println("PSL Controller started!");
            }

            public void controllerStopped(PslController controller) {
                System.out.println("PSL Controller stoped.");
                running.countDown();
            }

            public void command(PslController controller, DifferentialDriveCommand command, FPrediction<Double> prediction) {
                SpaceDistribution<Double> pd = prediction.element();
                SpaceDistribution<Double> motorDistribution = space.getMotorDistribution(pd);
                SpaceDistribution<Double> sensoryDistribution = space.getSensoryDistribution(pd);
//                if (!motorDistribution.isUniform()) {
//                    System.out.println(motorDistribution);
//                }
//                if (!sensoryDistribution.isUniform()) {
//                    System.out.println(sensoryDistribution);
//                }
            }
        });


        controller.start();
        running.await();
    }
}
