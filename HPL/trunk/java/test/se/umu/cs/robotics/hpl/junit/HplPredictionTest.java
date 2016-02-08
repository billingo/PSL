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

package se.umu.cs.robotics.hpl.junit;

import se.umu.cs.robotics.probabilitydistribution.iteration.DimensionIterator;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.psl.Predictor;
import se.umu.cs.robotics.hpl.Context.ContextMatch;
import se.umu.cs.robotics.hpl.Step;
import se.umu.cs.robotics.hpl.StepIterator;
import se.umu.cs.robotics.hpl.Layer;
import se.umu.cs.robotics.psl.Psl.TrainingReference;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.psl.listener.EmptyLearningListener;
import se.umu.cs.robotics.psl.Hypothesis;
import se.umu.cs.robotics.psl.Psl;
import se.umu.cs.robotics.psl.listener.SimplePerformanceListener;
import se.umu.cs.robotics.psl.Library;
import se.umu.cs.robotics.frequency.StepFrequency;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.iteration.DimensionPositionIterator;
import se.umu.cs.robotics.probabilitydistribution.iteration.MaximumProbabilityIterator;
import se.umu.cs.robotics.probabilitydistribution.iteration.MaximumProbabilityPositionIterator;
import se.umu.cs.robotics.statespace.IntegerDimension;
import se.umu.cs.robotics.statespace.IntegerSpace;
import static org.junit.Assert.*;

/**
 *
 * @author billing
 */
public class HplPredictionTest {

    private final IntegerSpace space = new IntegerSpace(new IntegerDimension(0, 10),new IntegerDimension(0, 10));

    public HplPredictionTest() {
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
    public void testFrequencyPrediction() {
        final StepFrequency<Integer> f1 = new StepFrequency<Integer>(space,100, 4, 2);
        final StepFrequency<Integer> f2 = new StepFrequency<Integer>(space,100, 4, 2);

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
        PositionIterator<Integer> i = new MaximumProbabilityPositionIterator<Integer>(new DimensionPositionIterator<Integer>(new LinkedPositionIterator<SpaceDistribution<Integer>>(f1),0));
        TrainingReference<Integer> trainingReference = learner.train(i);
        trainingReference.await();

        Layer<String, Integer> layer = new Layer<String, Integer>("A", library, 2, 4);
        StepIterator<String, Integer> steps = new StepIterator<String, Integer>(new Predictor<Integer>(library), new MaximumProbabilityIterator<Integer>(new DimensionIterator<Integer>(f2,0)), "A");
        while (steps.hasNext()) {
            Step<String, Integer> step = steps.next();
            ContextMatch<String, Integer> match = layer.matchContext(step);
            layer.updateResponsibilities(match);
            layer.step(step);
            layer.updatePriors(match);
            layer.updateWeights(step);
        }

        // assertEquals(5, library.size());
        // assertEquals(6, performanceListener.getHits());
        // assertEquals(6, performanceListener.getMisses());
    }
}
