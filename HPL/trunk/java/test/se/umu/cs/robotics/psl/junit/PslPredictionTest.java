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

package se.umu.cs.robotics.psl.junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.frequency.StepFrequency;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.iteration.DimensionIterator;
import se.umu.cs.robotics.probabilitydistribution.iteration.MaximumProbabilityIterator;
import se.umu.cs.robotics.psl.Hypothesis;
import se.umu.cs.robotics.psl.Library;
import se.umu.cs.robotics.psl.Psl;
import se.umu.cs.robotics.psl.Psl.TrainingReference;
import se.umu.cs.robotics.psl.listener.EmptyLearningListener;
import se.umu.cs.robotics.psl.listener.SimplePerformanceListener;
import se.umu.cs.robotics.statespace.IntegerDimension;
import se.umu.cs.robotics.statespace.IntegerSpace;
import se.umu.cs.robotics.statespace.StateSpace;
import se.umu.cs.robotics.utils.StringTools;

/**
 *
 * @author billing
 */
public class PslPredictionTest {

    private final StateSpace<Integer> intSpace = new IntegerSpace(new IntegerDimension(0, 10));

    public PslPredictionTest() {
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
    public void testStringPrediction() {
        final String testStirng = "ABCCABCCABCC";

        Library<Character> library = new Library<Character>(10);
        Psl<Character> learner = new Psl<Character>(library);
        SimplePerformanceListener<Character> performanceListener = new SimplePerformanceListener<Character>() {

            int count = 0;

            @Override
            public void hit(final Hypothesis<Character> h) {
                super.hit(h);
                System.out.println(testStirng.charAt(count++) + ":" + h.getTarget() + " " + h);
            }

            @Override
            public void miss(final Hypothesis<Character> h) {
                super.miss(h);
                System.out.println(testStirng.charAt(count++) + ":" + (h == null ? "null" : h.getTarget() + " " + h));
            }
        };
        learner.addPerformanceListener(performanceListener);
        learner.addListener(new EmptyLearningListener<Character>() {

            @Override
            public void hypothesisCreated(final TrainingReference<Character> ref, final Hypothesis<Character> h) {
                System.out.println("New Hypothesis: " + h.toString());
            }
        });
        LinkedPositionIterator<Character> i = new LinkedPositionIterator<Character>(StringTools.iterator(testStirng));
        TrainingReference<Character> trainingReference = learner.train(i);
        trainingReference.await();

        System.out.println(library);
        assertEquals(5, library.size());
        assertEquals(6, performanceListener.getHits());
        assertEquals(6, performanceListener.getMisses());
    }

    @Test
    public void testFrequencyPrediction() {
        final StepFrequency<Integer> f = new StepFrequency<Integer>(intSpace,100, 4, 2);


        Library<Integer> library = new Library<Integer>(100);
        Psl<Integer> learner = new Psl<Integer>(library);
        SimplePerformanceListener<Integer> performanceListener = new SimplePerformanceListener<Integer>() {

            int count = 0;

            @Override
            public void hit(final Hypothesis<Integer> h) {
                super.hit(h);
                // System.out.println(testStirng.charAt(count++) + ":" +
                // h.getTarget() + " " + h);
            }

            @Override
            public void miss(final Hypothesis<Integer> h) {
                super.miss(h);
                // System.out.println(testStirng.charAt(count++) + ":" + (h ==
                // null ? "null" : h.getTarget() + " " + h));
            }
        };
        learner.addPerformanceListener(performanceListener);
        learner.addListener(new EmptyLearningListener<Integer>() {

            @Override
            public void hypothesisCreated(final TrainingReference<Integer> ref, final Hypothesis<Integer> h) {
                System.out.println("New Hypothesis: " + h.toString());
            }
        });
        LinkedPositionIterator<Integer> i = new LinkedPositionIterator<Integer>(new MaximumProbabilityIterator<Integer>(new DimensionIterator<Integer>(f,0)));
        TrainingReference<Integer> trainingReference = learner.train(i);
        trainingReference.await();

        System.out.println(library);
        assertEquals(16, library.size());
        // assertEquals(6, performanceListener.getHits());
        // assertEquals(6, performanceListener.getMisses());
    }
}
