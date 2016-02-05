/*
 *  Copyright (C) 2011 Erik Billing <billing@cs.umu.se>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.umu.cs.robotics.statespace.junit;

import java.util.Iterator;
import java.util.TreeMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.statespace.FloatDimension;
import se.umu.cs.robotics.statespace.comparator.LinearDoubleComparator;
import static org.junit.Assert.*;
import se.umu.cs.robotics.statespace.comparator.StateComparator;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class FloatDimensionTest {

    FloatDimension dimension = new FloatDimension(-3, 5, 0, 16);

    public FloatDimensionTest() {
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
    public void canReturnRange() {
        assertEquals(8, dimension.range(), 0.001);
    }

    @Test
    public void canReturnSize() {
        assertEquals(16, dimension.size());
    }

    @Test
    public void canReturnResolution() {
        assertEquals(0.5, dimension.resolution(), 0.001);
    }

    @Test
    public void canReturnState() {
        assertEquals(-0.5, dimension.getState(5), 0.001);
    }

    @Test
    public void canReturnIndex() {
        assertEquals(7, dimension.getIndex(0.5));
    }

    @Test
    public void canReturnComparator() {
        StateComparator<Double> c = dimension.comparator();
        assertEquals(1d, ((LinearDoubleComparator) c).getTolerance(), 0d);
    }

    @Test
    public void canComputeDistance() {
        final StateComparator<Double> c = dimension.comparator();
        assertEquals(1, c.distance(-3d, -2d), 0d);
        assertEquals(1, c.distance(0d, -4d), 0d);
        assertEquals(0.5, c.distance(0d, 0.5), 0d);
    }

    @Test
    public void compare() {
        Double d1 = new Double(1);
        Double d2 = new Double(1.5);

        System.out.println(d2.compareTo(d1));
        TreeMap<Double, Integer> map = new TreeMap<Double, Integer>();

        map.put(2d, 1);
        map.put(1.5d, 2);
        map.put(3.5d, 3);

        System.out.println(map.ceilingKey(2.1d));
        System.out.println(map.floorKey(2.2d));
        Iterator<Double> i = map.keySet().iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
        }
    }
}
