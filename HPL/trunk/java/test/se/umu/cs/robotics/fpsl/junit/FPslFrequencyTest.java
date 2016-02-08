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

import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.frequency.FractaleFrequency;
import se.umu.cs.robotics.fpsl.FHypothesis;
import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.fpsl.selection.MaxPredictionSelector;
import se.umu.cs.robotics.statespace.CharSpace;
import se.umu.cs.robotics.statespace.CharDimension;
import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.fpsl.FPslParameters;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class FPslFrequencyTest {

    CharDimension dim = new CharDimension();
    CharSpace space = new CharSpace(dim);

    public FPslFrequencyTest() {
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
    public void testSmpleFrequency1() {
        final int sequenceLength = 200;
        final int stepLength = 5;
        final int levels = 2;
        LinkedPositionIterator i = new LinkedPositionIterator(new FractaleFrequency(space, sequenceLength, stepLength, levels));
        FLibrary<Character> library = createTrainedLibrary(i, 1);
        assertEquals(14, library.size());

    }

    @Test
    public void testSmpleFrequency3() {
        final int sequenceLength = 2000;
        final int stepLength = 5;
        final int levels = 2;
        LinkedPositionIterator i = new LinkedPositionIterator(new FractaleFrequency(space, sequenceLength, stepLength, levels));
        FLibrary<Character> library = createTrainedLibrary(i, 3);
        assertEquals(10, library.size());
    }

    @Test
    public void testSmpleFrequency2() {
        final int sequenceLength = 2000;
        final int stepLength = 10;
        final int levels = 2;
        LinkedPositionIterator i = new LinkedPositionIterator(new FractaleFrequency(space, sequenceLength, stepLength, levels));
        FLibrary<Character> library = createTrainedLibrary(i, 2);
        assertEquals(18, library.size());
    }

    @Test
    public void testBigFrequency() {
        long tStart = System.currentTimeMillis();
        final int sequenceLength = 2000;
        final int stepLength = 10;
        final int levels = 4;
        LinkedPositionIterator i = new LinkedPositionIterator(new FractaleFrequency(space, sequenceLength, stepLength, levels));
        FLibrary<Character> library = createTrainedLibrary(i, 2);
//        assertEquals(135, library.size());
        long tEnd = System.currentTimeMillis();
        System.out.println(String.format("Runtime for profiling test: %d", tEnd - tStart));
    }

    private FLibrary<Character> createTrainedLibrary(PositionIterator<SpaceDistribution<Character>> i, double integrationGradient) {
        FPslParameters parameters = new FPslParameters();
        parameters.setIntegrationGradient(integrationGradient);
        IteratorPosition<SpaceDistribution<Character>> p = i.next();
        FLibrary library = new FLibrary(space, parameters);
        MaxPredictionSelector selector = new MaxPredictionSelector(library);

        while (i.hasNext()) {
//            System.out.println("\nStep: "+i.getPosition().getNext().element());
            FPrediction prediction = new FPrediction(selector, i.getPosition(), true);
            p = i.next();
            prediction.teach(p.element());
        }
        System.out.println(String.format("Library created with %d hypotheses.", library.size()));
        return library;
    }
}
