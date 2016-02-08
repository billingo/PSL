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

package se.umu.cs.robotics.lokarria.statespace.junit;

import java.util.ArrayList;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.lokarria.laser.ConcreteLaserEcho;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.lokarria.laser.LaserArrayList;
import se.umu.cs.robotics.lokarria.laser.LaserEcho;
import se.umu.cs.robotics.lokarria.statespace.DifferentialDriveSpace;
import se.umu.cs.robotics.lokarria.statespace.LaserSpace;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSequentializer;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;
import static org.junit.Assert.*;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SingleStateDistribution;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SensoryMotorSequentializerTest {

    final DifferentialDriveSpace ddSpace = new DifferentialDriveSpace(30, 10);
    final LaserSpace laserSpace = new LaserSpace(5, 16);
    final SensoryMotorSpace space = new SensoryMotorSpace(ddSpace, laserSpace);
    SensoryMotorSequentializer seq;

    public SensoryMotorSequentializerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        seq = new SensoryMotorSequentializer(space);
        seq.putLaser(newDummyLaserArray(1, 1));
        seq.putLaser(newDummyLaserArray(3, 3));
        seq.putLaser(newDummyLaserArray(5, 3));
        seq.putLaser(newDummyLaserArray(7, 4));
    }

    @After
    public void tearDown() {
    }

    LaserArray newDummyLaserArray(long timeStamp, double distance) {
        ArrayList<LaserEcho> echoes = new ArrayList<LaserEcho>();
        for (double a : LaserArray.DEFAULT_ANGLES) {
            echoes.add(new ConcreteLaserEcho(a, distance));
        }
        return new LaserArrayList(timeStamp, echoes);
    }

    @Test
    public void canInterpolate() {
        SensoryMotorDistribution smDistribution = seq.getSensoryDistribution(2);
        Iterator<? extends ProbabilityDistribution<Double>> dimensions = smDistribution.getSensoryDistribution().dimensions();
        int dCount = 0;
        while (dimensions.hasNext()) {
            ProbabilityDistribution<Double> d = dimensions.next();
            if (d instanceof SingleStateDistribution) {
                assertEquals(2.0, ((SingleStateDistribution) d).getState());
            } else {
                assertEquals(SingleStateDistribution.class, d.getClass());

            }
            dCount++;
        }
        assertEquals(laserSpace.size(), dCount);
    }

    @Test
    public void canComputeIntersection() {
        SensoryMotorDistribution smDist2 = seq.getSensoryDistribution(2);
        SensoryMotorDistribution smDist6 = seq.getSensoryDistribution(6);

        double v = smDist2.intersection(smDist6);
        assertEquals(0.25, v, 0d);
    }
}
