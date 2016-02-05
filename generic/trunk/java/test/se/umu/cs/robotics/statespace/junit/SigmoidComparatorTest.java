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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.statespace.comparator.SigmoidDoubleComparator;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SigmoidComparatorTest {

    SigmoidDoubleComparator comparator = new SigmoidDoubleComparator(1, 1);

    public SigmoidComparatorTest() {
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
    public void canComputeDistance() {
        double distance = comparator.distance(0d, 1d);
        assertEquals(0.5, distance,0.0);
    }

    @Test
    public void canComputeThreshold() {
        double distance = comparator.distance(0d, 1.8d);
        System.out.println(distance);
    }
}
