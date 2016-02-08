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