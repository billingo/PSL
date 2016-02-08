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

import com.sun.corba.se.impl.orbutil.GetPropertyAction;
import java.text.NumberFormat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.probabilitydistribution.ArrayDistribution;
import se.umu.cs.robotics.probabilitydistribution.GaussianDistribution;
import se.umu.cs.robotics.statespace.CharDimension;
import se.umu.cs.robotics.statespace.IntegerDimension;
import static org.junit.Assert.*;

/**
 *
 * @author billing
 */
public class ProbabilityDistributionTests {

    NumberFormat format = NumberFormat.getInstance();
    IntegerDimension intDimension = new IntegerDimension(1,10);
    CharDimension charDimension = new CharDimension();

    public ProbabilityDistributionTests() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        format.setMaximumFractionDigits(3);
        format.setMinimumFractionDigits(3);
        format.setMaximumIntegerDigits(2);
        format.setMinimumIntegerDigits(2);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGaussianDistribution() {
        GaussianDistribution<Integer> pd = new GaussianDistribution<Integer>(intDimension, 3, 2);
        for (Integer i: intDimension) System.out.print(format.format(i)+" ");
        System.out.print('\n');
        for (Integer i: intDimension) {
            System.out.print(format.format(pd.getProbability(i.intValue()))+" ");
        }
        System.out.print('\n');

        assertEquals(0.176, pd.getProbability(4),0.01);
        assertEquals(0.121, pd.getProbability(new Integer(4)),0.01);
    }

    @Test
    public void testArrayDistributionMinMax() {
        ArrayDistribution<Character> d = new ArrayDistribution<Character>(charDimension);
        d.addValue(2, 1);
        d.addValue(10, 1);
        d.addValue(3,0.5);
        IterableIterator<FuzzyItem<Character>> iMax = d.max();
        IterableIterator<FuzzyItem<Character>> iMin = d.min();

        FuzzyItem<Character> e;
        e = iMax.next();
        assertEquals('C', e.element().charValue());
        e = iMax.next();
        assertEquals('K', e.element().charValue());
        assertEquals(false, iMax.hasNext());

        e = iMin.next();
        assertEquals('A', e.element().charValue());
        e = iMin.next();
        assertEquals('B', e.element().charValue());
        e = iMin.next();
        assertEquals('E', e.element().charValue());
    }

    @Test
    public void GaussianDistributionMinMax() {
        GaussianDistribution<Character> d = new GaussianDistribution<Character>(charDimension, 3, 1);
        IterableIterator<FuzzyItem<Character>> dMax = d.max();
        FuzzyItem<Character> dItem = dMax.next();
        assertEquals('D', dItem.element().charValue());
        assertEquals(0.399, dItem.value(),0.01);
        assertEquals(false, dMax.hasNext());
        IterableIterator<FuzzyItem<Character>> dMin = d.min();
        FuzzyItem<Character> lastItem = dMin.next();
        System.out.println(lastItem);
        assertEquals(charDimension.lastState(), lastItem.element());
        assertEquals(false, dMin.hasNext());

        GaussianDistribution<Character> bc = new GaussianDistribution<Character>(charDimension, 1.5, 1);
        IterableIterator<FuzzyItem<Character>> bcMax = bc.max();
        FuzzyItem<Character> bItem = bcMax.next();
        FuzzyItem<Character> cItem = bcMax.next();
        assertEquals('B', bItem.element().charValue());
        assertEquals('C', cItem.element().charValue());
        assertEquals(false, bcMax.hasNext());
        IterableIterator<FuzzyItem<Character>> bcMin = d.min();
        lastItem = bcMin.next();
        assertEquals(charDimension.lastState(), lastItem.element());
        assertEquals(false, bcMin.hasNext());
    }

}