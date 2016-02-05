/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.frequency.junit;

import se.umu.cs.robotics.frequency.FractaleFrequency;
import se.umu.cs.robotics.frequency.SpikeFrequency;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.frequency.StepFrequency;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.CharDimension;
import se.umu.cs.robotics.statespace.CharSpace;
import static org.junit.Assert.*;

/**
 *
 * @author billing
 */
public class FrequencyTest {

    CharSpace space = new CharSpace(new CharDimension(), new CharDimension());

    public FrequencyTest() {
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
    public void testSpikingFrequency() {
        SpikeFrequency<Character> f = new SpikeFrequency<Character>(new StepFrequency<Character>(space, 20, 3, 3),space.newDistribution());
        for (int i = 0; i < 20; i++) {
            System.out.println(f.next());
        }
        assertEquals(false, f.hasNext());
    }

    @Test
    public void testStepFrequency3Levels() {
        StepFrequency<Character> f = new StepFrequency<Character>(space, 10, 3, 3);
        SpaceDistribution<Character> next = f.next();
        next = f.next();
        SpaceDistribution<Character> d1 = f.next();
        SpaceDistribution<Character> d2 = f.next();
        for (int i = 0; i < 5; i++) {
            next = f.next();
        }
        SpaceDistribution<Character> d3 = f.next();
        assertEquals(0d, d1.intersection(d3), 0.001);
        assertEquals(1d, d2.intersection(d3), 0.001);
        assertEquals(false, f.hasNext());
    }

    @Test
    public void testStepFrequency2Levels() {
        StepFrequency<Character> f = new StepFrequency<Character>(space, 10, 3, 2);
        SpaceDistribution<Character> next = f.next();
        next = f.next();
        SpaceDistribution<Character> d1 = f.next();
        SpaceDistribution<Character> d2 = f.next();
        for (int i = 0; i < 4; i++) {
            next = f.next();
        }
        SpaceDistribution<Character> d3 = f.next();
        assertEquals(1d, d1.intersection(d3), 0.001);
        assertEquals(0d, d2.intersection(d3), 0.001);
        next = f.next();
        assertEquals(false, f.hasNext());
    }

    @Test
    public void testFractaleFrequency2Levels() {
        FractaleFrequency<Character> f = new FractaleFrequency<Character>(space, 10, 3, 2);
        SpaceDistribution<Character> next = f.next();
        next = f.next();
        SpaceDistribution<Character> d1 = f.next();
        SpaceDistribution<Character> d2 = f.next();
        for (int i = 0; i < 4; i++) {
            next = f.next();
        }
        SpaceDistribution<Character> d3 = f.next();
        assertEquals(1d, d1.intersection(d3), 0.001);
        assertEquals(0d, d2.intersection(d3), 0.001);
        next = f.next();
        assertEquals(false, f.hasNext());
    }

    @Test
    public void testFractaleFrequency3Levels() {
        FractaleFrequency<Character> f = new FractaleFrequency<Character>(space, 26, 2, 3);
        StringBuilder s = new StringBuilder();
        for (SpaceDistribution<Character> e: f) {
            s.append(e.getDimension(1).max().next().element());
        }
        assertEquals("00110011221100110011221100", s.toString());
    }
}
