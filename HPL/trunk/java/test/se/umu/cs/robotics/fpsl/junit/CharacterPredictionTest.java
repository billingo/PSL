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

import se.umu.cs.robotics.fpsl.selection.FHypothesisSelection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.iteration.position.ArrayListPositionIterator;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.fpsl.FHypothesis;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.probabilitydistribution.GaussianSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.SingleStateSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.fpsl.FPslParameters;
import se.umu.cs.robotics.fpsl.selection.MaxPredictionSelector;
import se.umu.cs.robotics.iteration.position.ArrayListPosition;
import se.umu.cs.robotics.iteration.position.LinkedReverseIterator;
import se.umu.cs.robotics.log.LogConfigurator;
import se.umu.cs.robotics.statespace.CharDimension;
import se.umu.cs.robotics.statespace.CharSpace;
import se.umu.cs.robotics.statespace.StateSpace;
import static org.junit.Assert.*;

/**
 *
 * @author billing
 */
public class CharacterPredictionTest {

    private final static NumberFormat format = NumberFormat.getInstance(Locale.US);
    StateSpace<Character> space = new CharSpace(new CharDimension(), new CharDimension());
    ArrayList<SpaceDistribution<Character>> events = new ArrayList<SpaceDistribution<Character>>();
    FLibrary<Character> library1g;
    FLibrary<Character> library2g;
    FLibrary<Character> library4g;

    public CharacterPredictionTest() {
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
    public void setUp2GradientLibrary() {
        setUpEvents();
        FPslParameters parameters = new FPslParameters();
        parameters.setIntegrationGradient(2);
        library2g = new FLibrary<Character>(space,parameters);

//        FRoot<Character> c = library2g.addRoot(newSingleStateSpace('C', 'X'));
//        FRoot<Character> d = library2g.addRoot(newSingleStateSpace('D', 'Y'));
//
//        FRoot<Character> bGauss = library2g.addRoot(newGaussianSpace('B', 0.5, 'X', 0.5));
//
//        FHypothesis<Character> bc = c.addChild(newSingleStateSpace('B', 'X'));
//        FHypothesis<Character> abc = bc.addChild(newSingleStateSpace('A', 'X'));
//        FHypothesis<Character> bd = d.addChild(newSingleStateSpace('B', 'X'));
//        FHypothesis<Character> cd = d.addChild(newSingleStateSpace('C', 'Y'));
//        FHypothesis<Character> abd = bd.addChild(newSingleStateSpace('A', 'X'));

//        FHypothesis<Character> dabc = abc.addChild(newSingleStateSpace('D', 'X'));
//        FHypothesis<Character> edabc = dabc.addChild(newSingleStateSpace('E', 'X'));

//        FHypothesis<Character> bbGauss = bGauss.addChild(newGaussianSpace('B', 0.5, 'X', 0.5));
//        FHypothesis<Character> abGauss = bGauss.addChild(newGaussianSpace('A', 0.5, 'X', 0.5));
    }

    @Before
    public void setUp1GradientLibrary() {
        setUpEvents();
        library1g = new FLibrary<Character>(space);

//        FRoot<Character> c = library1g.addRoot(newSingleStateSpace('C', 'X'));
//        FRoot<Character> d = library1g.addRoot(newSingleStateSpace('D', 'Y'));
//
//        FHypothesis<Character> bc = c.addChild(newSingleStateSpace('B', 'X'));
//        FHypothesis<Character> abc = bc.addChild(newSingleStateSpace('A', 'X'));
//        FHypothesis<Character> dabc = abc.addChild(newSingleStateSpace('D', 'X'));
    }

    @Before
    public void setUp4GradientLibrary() {
        setUpEvents();
        FPslParameters parameters = new FPslParameters();
        parameters.setIntegrationGradient(4);
        library4g = new FLibrary<Character>(space, parameters);

//        FRoot<Character> c = library4g.addRoot(newSingleStateSpace('C', 'X'));
//        FRoot<Character> d = library4g.addRoot(newSingleStateSpace('D', 'Y'));
//
//        FHypothesis<Character> bc = c.addChild(newSingleStateSpace('B', 'X'));
//        FHypothesis<Character> abc = bc.addChild(newSingleStateSpace('A', 'X'));
//        FHypothesis<Character> dabc = abc.addChild(newSingleStateSpace('D', 'X'));
    }

    public SingleStateSpaceDistribution<Character> newSingleStateSpace(Character d1, Character d2) {
        return new SingleStateSpaceDistribution<Character>(space, d1, d2);
    }

    public GaussianSpaceDistribution<Character> newGaussianSpace(char mean1, double std1, char mean2, double std2) {
        double[] means = {space.getDimension(0).getIndex(mean1), space.getDimension(1).getIndex(mean2)};
        double[] stds = {std1, std2};
        return new GaussianSpaceDistribution<Character>(space, means, stds);
    }

    @After
    public void tearDown() {
    }

    private void setUpEvents() {
        events.clear();
        events.addAll(stringDistribution("ABCDABCDABCDABCDAB", "XXXXXXXXXXXXXXXXXX"));
    }

    private ArrayList<SpaceDistribution<Character>> stringDistribution(String s1, String s2) {
        ArrayList<SpaceDistribution<Character>> list = new ArrayList<SpaceDistribution<Character>>();
        for (int i = 0; i < s1.length(); i++) {
            list.add(new SingleStateSpaceDistribution<Character>(space, new Character(s1.charAt(i)), new Character(s2.charAt(i))));
        }
        return list;
    }

    public void testIntersection() {
    }

    @Test
    public void canMatch() {
//        System.out.println(events);
        ArrayListPositionIterator<SpaceDistribution<Character>> i = new ArrayListPositionIterator<SpaceDistribution<Character>>(events, events.size() - 1);
        MaxPredictionSelector<Character> selector = new MaxPredictionSelector<Character>(library2g);
        FHypothesisSelection<Character> selection = selector.lhsSelect(new LinkedReverseIterator<SpaceDistribution<Character>>(i));
        SpaceDistribution target = selection.getTarget();
        IterableIterator<FuzzyItem<Character>> max = target.getDimension(0).max();
        FuzzyItem<Character> firstMax = max.next();
        FuzzyItem<Character> secondMax = max.next();
        assertEquals('C', firstMax.element().charValue());
        assertEquals(0.5, firstMax.value(), 0);
        assertEquals('D', secondMax.element().charValue());
        assertEquals(0.5, secondMax.value(), 0);
    }

    @Test
    public void testTeaching() {
        ArrayListPositionIterator<SpaceDistribution<Character>> i = new ArrayListPositionIterator<SpaceDistribution<Character>>(new ArrayListPosition<SpaceDistribution<Character>>(events, events.size() - 1));
        MaxPredictionSelector<Character> selector = new MaxPredictionSelector<Character>(library2g);


        for (int t = 1; t <= 3; t++) {
            FPrediction<Character> p1 = new FPrediction<Character>(selector, i.previous(), true);
            SpaceDistribution<Character> e1 = p1.element();
            System.out.println("Prediction " + t + ": " + e1);
            p1.teach(newSingleStateSpace('C', 'X'));
        }
    }

    @Test
    public void testGradients() {
        System.out.println("Temporal status for Library with gradient 1:");
        for (FuzzyItem<FHypothesis<Character>> i : library1g) {
            FHypothesis<Character> h = i.element();
            if (h.length() > 0) {
                System.out.println(h.integrationRange() + "\t" + h);
            }
        }

        System.out.println("Temporal status for Library with gradient 2:");
        for (FuzzyItem<FHypothesis<Character>> i : library2g) {
            FHypothesis<Character> h = i.element();
            if (h.length() > 0) {
                System.out.println(h.integrationRange() + "\t" + h);
            }
        }

        System.out.println("Temporal status for Library with gradient 4:");
        for (FuzzyItem<FHypothesis<Character>> i : library4g) {
            FHypothesis<Character> h = i.element();
            if (h.length() > 0) {
                System.out.println(h.integrationRange() + "\t" + h);
            }
        }
    }
}
