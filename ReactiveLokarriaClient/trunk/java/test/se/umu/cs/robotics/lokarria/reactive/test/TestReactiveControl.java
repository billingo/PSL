/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.lokarria.reactive.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.log.LogConfigurator;
import se.umu.cs.robotics.lokarria.reactivedrive.GoForwardComponent;
import se.umu.cs.robotics.lokarria.reactivedrive.ReactiveControl;
import se.umu.cs.robotics.lokarria.reactivedrive.obstacleavoidance.ReactiveObstacleAvoidance;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class TestReactiveControl {

    public TestReactiveControl() {
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
    public void testReactive() throws InterruptedException {
        ReactiveControl control = new ReactiveControl();
        control.addComponent(new ReactiveObstacleAvoidance());
        control.addComponent(new GoForwardComponent(0.4));
        control.start();

        Thread.sleep(20000);
        control.stop(true);
        Thread.sleep(100);
    }
}
