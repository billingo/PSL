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
package se.umu.cs.robotics.collections.sort.junit;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.collections.sort.SortedDouble;
import se.umu.cs.robotics.collections.sort.Sorter;
import static org.junit.Assert.*;

/**
 *
 * @author billing
 */
public class SorterTest {

    public static final double[] doubles = {1, 0, 3, 5, 9, 3, 2.5, 2, 4};

    public SorterTest() {
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
    public void testSorter() {
        Sorter<SortedDouble> sorted = Sorter.sort(doubles);
        Iterator<SortedDouble> i = sorted.iterator();

        SortedDouble e = i.next();
        assertEquals(0d,e.value(),0.0001);
        assertEquals(1, e.originalPosition());
        e = i.next();
        assertEquals(1d,e.value(),0.0001);
        assertEquals(0, e.originalPosition());
        e = i.next();
        assertEquals(2d,e.value(),0.0001);
        assertEquals(7, e.originalPosition());
    }
}
