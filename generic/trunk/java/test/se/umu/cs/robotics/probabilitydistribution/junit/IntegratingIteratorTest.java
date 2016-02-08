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

package se.umu.cs.robotics.probabilitydistribution.junit;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import se.umu.cs.robotics.iteration.position.ArrayListPosition;
import se.umu.cs.robotics.iteration.position.ArrayListPositionIterator;
import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.iteration.IntegratingIterator;
import se.umu.cs.robotics.probabilitydistribution.iteration.IntegratingPositionIterator;
import se.umu.cs.robotics.statespace.CharDimension;
import se.umu.cs.robotics.statespace.CharSpace;


/**
 *
 * @author billing
 */
public class IntegratingIteratorTest {

    private final CharSpace space = new CharSpace(new CharDimension(), new CharDimension());

    public IntegratingIteratorTest() {
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

    private ArrayList<SpaceDistribution<Character>> newEventHistory() {
        ArrayList<SpaceDistribution<Character>> list = new ArrayList<SpaceDistribution<Character>>();
        list.add(space.newSingleState("AX"));
        list.add(space.newSingleState("BX"));
        list.add(space.newSingleState("CX"));
        list.add(space.newSingleState("DY"));
        list.add(space.newSingleState("EY"));
        list.add(space.newSingleState("FY"));
        list.add(space.newSingleState("GZ"));
        list.add(space.newSingleState("HZ"));
        list.add(space.newSingleState("IZ"));
        list.add(space.newSingleState("JZ"));
        list.add(space.newSingleState("KZ"));
        list.add(space.newSingleState("LZ"));
        list.add(space.newSingleState("MZ"));
        list.add(space.newSingleState("NZ"));
        list.add(space.newSingleState("OZ"));
        list.add(space.newSingleState("PÖ"));
        return list;
    }

    @Test
    public void testIntegratedDistributionIterator() {
        ArrayList<SpaceDistribution<Character>> list = newEventHistory();
        IntegratingIterator<Character> i = new IntegratingIterator<Character>(list.iterator(), 2);
        for (SpaceDistribution<Character> c : i) {
            System.out.println(c);
        }
    }

    @Test
    public void testBackwardIteration() {
        ArrayList<SpaceDistribution<Character>> list = newEventHistory();
        ArrayListPositionIterator<SpaceDistribution<Character>> li = new ArrayListPositionIterator<SpaceDistribution<Character>>(new ArrayListPosition<SpaceDistribution<Character>>(list, 5));
        IntegratingPositionIterator<Character> pi = new IntegratingPositionIterator<Character>(li.getPosition(),1);
        IteratorPosition<SpaceDistribution<Character>> previous = pi.previous();
        previous = pi.previous();
        IteratorPosition<SpaceDistribution<Character>> pos = pi.previous();
        assertEquals(1,list.get(2).intersection(pos.element()),0.01);
    }

    @Test
    public void testBackwardIteration15() {
        ArrayList<SpaceDistribution<Character>> list = newEventHistory();
        ArrayListPositionIterator<SpaceDistribution<Character>> li = new ArrayListPositionIterator<SpaceDistribution<Character>>(new ArrayListPosition<SpaceDistribution<Character>>(list, 8));
        IntegratingPositionIterator<Character> pi = new IntegratingPositionIterator<Character>(li.getPosition(),1.5);
        IteratorPosition<SpaceDistribution<Character>> pos1 = pi.previous();
        System.out.println("First: "+pos1.element());
        IteratorPosition<SpaceDistribution<Character>> pos2 = pi.previous();
        IteratorPosition<SpaceDistribution<Character>> pos3 = pi.previous();
        System.out.println("First: "+pos1.element()+" Second: "+pos2.element()+" Thrid: "+pos3.element());
        assertEquals(0.5,list.get(1).intersection(pos3.element()),0.01);
    }

    @Test
    public void testBackwardIteration2() {
        ArrayList<SpaceDistribution<Character>> list = newEventHistory();
        ArrayListPositionIterator<SpaceDistribution<Character>> li = new ArrayListPositionIterator<SpaceDistribution<Character>>(list,15);
        IntegratingPositionIterator<Character> pi = new IntegratingPositionIterator<Character>(li.getPosition(),2);

        System.out.println(pi.previous());
        System.out.println(pi.previous());
        IteratorPosition<SpaceDistribution<Character>> pos = pi.previous();
        System.out.println(pos);
        assertEquals(0.25,list.get(0).intersection(pos.element()),0.001);
    }

    @Test
    public void testForwardIteration() {
        ArrayList<SpaceDistribution<Character>> list = newEventHistory();
        ArrayListPositionIterator<SpaceDistribution<Character>> li = new ArrayListPositionIterator<SpaceDistribution<Character>>(list);
        IntegratingPositionIterator<Character> pi = new IntegratingPositionIterator<Character>(li.getPosition(),2);
        IteratorPosition<SpaceDistribution<Character>> pos = pi.next();
        pos = pi.next();
        pos = pi.next();
        assertEquals(0.125, list.get(14).getDimension(0).intersection(pos.element().getDimension(0)),0.001);
    }

    @Test
    public void testWithoutIntegration() {
        ArrayList<SpaceDistribution<Character>> list = newEventHistory();
        ArrayListPositionIterator<SpaceDistribution<Character>> li = new ArrayListPositionIterator<SpaceDistribution<Character>>(list);
        IntegratingPositionIterator<Character> pi = new IntegratingPositionIterator<Character>(li.getPosition(),1);

        while (li.hasNext()) {
            SpaceDistribution<Character> e = li.next().element();
            SpaceDistribution<Character> eInt = pi.next().element();
            assertEquals(1, e.intersection(eInt),0.001);
        }
        assertEquals(false, pi.hasNext());
    }

    @Test
    public void testIntegrationStep() {
        assertEquals(1, IntegratingIterator.stepIntegrationTime(0, 1),0.01);
        assertEquals(1, IntegratingIterator.stepIntegrationTime(1, 1),0.01);
        assertEquals(1, IntegratingIterator.stepIntegrationTime(1, 1),0.01);
        assertEquals(1, IntegratingIterator.stepIntegrationTime(1, 1),0.01);

        assertEquals(1, IntegratingIterator.stepIntegrationTime(0, 1.4),0.01);
        assertEquals(1.4, IntegratingIterator.stepIntegrationTime(1, 1.4),0.01);
        assertEquals(1.96, IntegratingIterator.stepIntegrationTime(1.4, 1.4),0.01);
        assertEquals(2.74, IntegratingIterator.stepIntegrationTime(1.96, 1.4),0.01);

        assertEquals(1, IntegratingIterator.stepIntegrationTime(0, 2),0.01);
        assertEquals(2, IntegratingIterator.stepIntegrationTime(1, 2),0.01);
        assertEquals(4, IntegratingIterator.stepIntegrationTime(2, 2),0.01);
        assertEquals(8, IntegratingIterator.stepIntegrationTime(4, 2),0.01);

        double v = 0;
        for (int i=0;i<10;i++) {
            v = IntegratingIterator.stepIntegrationTime(v, 1.2);
            System.out.println(v);
        }
    }
}
