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
