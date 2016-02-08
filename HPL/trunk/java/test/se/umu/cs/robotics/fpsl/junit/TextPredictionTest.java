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

import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import java.util.Iterator;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.statespace.StateSpace;
import se.umu.cs.robotics.statespace.CharSpace;
import se.umu.cs.robotics.statespace.CharDimension;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.fpsl.FHypothesis;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.fpsl.selection.MaxPredictionSelector;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.log.LogConfigurator;
import se.umu.cs.robotics.probabilitydistribution.FuzzyDistribution;
import se.umu.cs.robotics.probabilitydistribution.SingleStateDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.iteration.SingleStateDistributionPositionIterator;
import se.umu.cs.robotics.probabilitydistribution.iteration.SpacePositionIterator;
import se.umu.cs.robotics.utils.StringTools;
import static org.junit.Assert.*;

/**
 * A simple demo of FPSL on text prediction
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class TextPredictionTest {

    String data = "Jag är en festis, men jag gillar saft!";
    CharDimension dimension = new CharDimension();
    StateSpace<Character> space = new CharSpace(dimension);
    FLibrary<Character> library = new FLibrary<Character>(space);
    MaxPredictionSelector<Character> selector = new MaxPredictionSelector<Character>(library);

    public TextPredictionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogConfigurator.configure();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        LogConfigurator.shutdown();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    PositionIterator<SpaceDistribution<Character>> iterData() {
        Iterator<Character> i = StringTools.iterator(data);
        LinkedPositionIterator<Character> iPos = new LinkedPositionIterator<Character>(i);
        SingleStateDistributionPositionIterator<Character> iDim = new SingleStateDistributionPositionIterator<Character>(dimension, iPos);
        return new SpacePositionIterator<Character>(space, iDim);
    }

    void train() {
        PositionIterator<SpaceDistribution<Character>> i = iterData();
        i.next();
        while (i.hasNext()) {
            FPrediction<Character> prediction = new FPrediction<Character>(selector, i.getPosition(), true);
            IteratorPosition<SpaceDistribution<Character>> p = i.next();
            prediction.teach(p.element());
        }
    }

    void predict() {
        System.out.println(data);
        PositionIterator<SpaceDistribution<Character>> i = iterData();
        IteratorPosition<SpaceDistribution<Character>> p = i.next();
        System.out.print(getCharacter(p.element()));
        while (i.hasNext()) {
            FPrediction<Character> prediction = new FPrediction<Character>(selector, i.getPosition(), true);
            p = i.next();
            System.out.print(getCharacter(prediction.element()));
        }
        System.out.print("\n");
    }

    char getCharacter(SpaceDistribution<Character> dist) {
        ProbabilityDistribution<Character> c = dist.getDimension(dimension);
        return c.max().next().element();
    }

    @Test
    public void canPredict() {
        int epochs = 6;
        int maxId = 0;
        System.out.println("Before training:");
        predict();
        for (int e = 1; e <= epochs; e++) {
            System.out.println("Epoch " + e + ":");
            train();
            predict();
            maxId = printLibrary(maxId);
        }
    }

    private int printLibrary(int minId) {
        System.out.println("Library:");
        int maxId = 0;
        for (FuzzyItem<FHypothesis<Character>> i: library) {
            FHypothesis<Character> h = i.element();
            if (h.length()>0 && h.getId()>minId) {
                System.out.println(h);
                maxId = Math.max(maxId, h.getId());
            }
        }
        System.out.println("Total size: "+library.size());
        return maxId == 0 ? minId : maxId;
    }
}
