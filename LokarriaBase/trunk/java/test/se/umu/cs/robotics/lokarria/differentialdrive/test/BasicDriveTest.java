/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.lokarria.differentialdrive.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import se.umu.cs.robotics.log.LogConfigurator;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDrive;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class BasicDriveTest {
    
    private DifferentialDrive drive;
    
    public BasicDriveTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        LogConfigurator.configure();
        this.drive = new DifferentialDrive();
    }
    
    @After
    public void tearDown() {
        LogConfigurator.shutdown();
    }

     @Test
     public void drive() {
         drive.setAngularSpeed(0.5);
         drive.setLinearSpeed(0.2);
         drive.post();
     }
}
