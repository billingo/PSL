/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright (C) 2007 - 2015  Erik Billing, <http://www.his.se/erikb>
School of Informatics, University of Skovde, Sweden

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
\*-------------------------------------------------------------------*/

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
