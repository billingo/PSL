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

package se.umu.cs.robotics.fpsl.junit;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author billing
 */
public class HashMapSpeedTest {

    public HashMapSpeedTest() {
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
    public void testAutoboxingSpeed() {
        HashMap<String, Double> map1 = new HashMap<String, Double>();
        HashMap<String, Double> map2 = new HashMap<String, Double>();

        long start = System.currentTimeMillis();

        // Filling the map
        String key = "x";
        Double value = 1d;
        for (int i=0; i<1000000; i++) {
            map1.put(key,value);
        }

        long checkpoint = System.currentTimeMillis();

        for (int i=0; i<1000000; i++) {
            map2.put(key, (double)i);
        }

        long stop = System.currentTimeMillis();

        System.out.println("No boxing: "+(checkpoint-start));
        System.out.println("With boxing: "+(stop-checkpoint));
    }

    @Test
    public void testHasmMapVsList() {
        HashMap<Integer, Double> map = new HashMap<Integer, Double>();
        ArrayList<Double> list = new ArrayList<Double>();

        long start = System.currentTimeMillis();

        for (int i=0; i<100000; i++) {
            map.put(i, (double)i);
        }

        long checkpoint = System.currentTimeMillis();

        for (int i=0; i<100000; i++) {
            list.add((double)i);
        }

        long stop = System.currentTimeMillis();

        System.out.println("HashMap: "+(checkpoint-start));
        System.out.println("List: "+(stop-checkpoint));

        start = System.currentTimeMillis();

        for (int i=0; i<100000; i++) {
            double d = map.get(i);
        }

        checkpoint = System.currentTimeMillis();

        for (int i=0; i<100000; i++) {
            boolean contains = list.contains((double) i);
        }

        stop = System.currentTimeMillis();

        System.out.println("HashMap get: "+(checkpoint-start));
        System.out.println("List contains: "+(stop-checkpoint));
    }
}
