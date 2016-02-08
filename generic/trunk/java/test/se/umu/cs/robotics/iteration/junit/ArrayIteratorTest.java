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

package se.umu.cs.robotics.iteration.junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.iteration.ArrayIterator;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ArrayIteratorTest {

    public ArrayIteratorTest() {
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
    public void canIterateTwoArrays() {
        Double[] d1 = {1.5, 2.5, 3.5, 4.5, 5.5};
        Double[] d2 = {2d, 3d, 4d, 5d};
        ArrayIterator<Double> i = new ArrayIterator<Double>(d1, d2);
        assertEquals(1.5, i.next(), 0);
        for (int e = 0; e < 5; e++) {
            i.next();
            assertTrue(i.hasNext());
        }
        assertEquals(3d, i.next(),0);
        assertEquals(4d, i.next(),0);
        assertEquals(5d, i.next(),0);
        assertFalse(i.hasNext());
    }

}
