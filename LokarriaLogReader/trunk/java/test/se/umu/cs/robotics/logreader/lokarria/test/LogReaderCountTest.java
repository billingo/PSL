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

import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.lokarria.core.JsonOperations.JsonException;
import se.umu.cs.robotics.lokarria.log.JsonMessage;
import se.umu.cs.robotics.lokarria.laser.LaserArrayList;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.logreader.LogEvent;
import java.io.File;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.logreader.lokarria.filter.LaserFilter;
import se.umu.cs.robotics.logreader.lokarria.LokarriaMessageHandler;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;
import se.umu.cs.robotics.logreader.xml.BufferedLogReader;
import se.umu.cs.robotics.lokarria.statespace.LaserSpace;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LogReaderCountTest {

    public LogReaderCountTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void countLaserEvents() throws SAXException, JsonException {
        final String logFile = "logs/DrivingToTheTVRepeat.log.xml";
        
        LaserSpace space = new LaserSpace(10);
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<LokarriaLogMessage>("http://se.umu.cs.robotics.lokarria", "log", 10);
        reader.addMessageHandler(new LokarriaMessageHandler());
        reader.setFilter(new LaserFilter());

        reader.start(new File(logFile));
        int count = 0;

        for (LogEvent<LokarriaLogMessage> e : reader) {
            LaserArray laser = null;
            for (LokarriaLogMessage m : e.messages()) {
                laser = LaserArrayList.fromJSON(((JsonMessage) m).toJSON());
            }
//            for (LaserEcho echo: laser) {
//                System.out.print(Math.round(echo.getDistance())+";");
//            }
//            System.out.println("");
            SpaceDistribution<Double> pd = space.newDistribution(laser);
            count++;
        }
        System.out.println(logFile+" contains " + count + " laser events.");
    }
}
