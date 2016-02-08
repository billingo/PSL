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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.lokarria.statespace.AngularSpeedDimension;
import se.umu.cs.robotics.lokarria.statespace.DifferentialDriveSpace;
import se.umu.cs.robotics.lokarria.statespace.LinearSpeedDimension;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SpeedDimensionTest {

    final DifferentialDriveSpace space = new DifferentialDriveSpace(30, 20);
    final AngularSpeedDimension angularDim = space.getAngularDimension();
    final LinearSpeedDimension linearDim = space.getLinearDimension();

    public SpeedDimensionTest() {
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
    public void canReturnIndex() {
        assertEquals(0, angularDim.getIndex(-1.5));
        assertEquals(15, angularDim.getIndex(0d));
        assertEquals(29,angularDim.getIndex(1.5));

        assertEquals(0, linearDim.getIndex(-1d));
        assertEquals(10, linearDim.getIndex(0d));
        assertEquals(19,linearDim.getIndex(1d));
    }
}
