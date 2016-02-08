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

import se.umu.cs.robotics.probabilitydistribution.SparseDistribution;
import se.umu.cs.robotics.statespace.IntegerDimension;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.probabilitydistribution.ArrayDistribution;
import se.umu.cs.robotics.probabilitydistribution.DualStateDistribution;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ProbabilityDistributionTest {

    IntegerDimension dim = new IntegerDimension(-5, 5);
    ArrayDistribution<Integer> arrayDist = new ArrayDistribution<Integer>(dim);
    DualStateDistribution<Integer> dualDist = new DualStateDistribution<Integer>(dim, 1, 3, 4, 6);
    SparseDistribution<Integer> sparseDist = new SparseDistribution<Integer>(dim);

    public ProbabilityDistributionTest() {
        arrayDist.addValue(new Integer(1), 3);
        arrayDist.addValue(new Integer(4), 6);
        sparseDist.addValue(1, 3);
        sparseDist.addValue(4, 6);
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
    public void arrayDistCanReturnNonZeroStates() {
        IterableIterator<FuzzyItem<Integer>> i = arrayDist.nonZeroStates();
        FuzzyItem<Integer> i1 = i.next();
        FuzzyItem<Integer> i2 = i.next();

        assertEquals(new Integer(1), i1.element());
        assertEquals(new Integer(4), i2.element());
        assertEquals(0.333, i1.value(), 0.003);
        assertEquals(0.666, i2.value(), 0.003);
        assertFalse(i.hasNext());
    }

    @Test
    public void dualDistCanReturnNonZeroStates() {
        IterableIterator<FuzzyItem<Integer>> i = dualDist.nonZeroStates();
        FuzzyItem<Integer> i1 = i.next();
        FuzzyItem<Integer> i2 = i.next();

        assertEquals(new Integer(1), i1.element());
        assertEquals(new Integer(4), i2.element());
        assertEquals(0.333, i1.value(), 0.003);
        assertEquals(0.666, i2.value(), 0.003);
    }

    @Test
    public void sparseDistCanReturnNonZeroStates() {
        IterableIterator<FuzzyItem<Integer>> i = sparseDist.nonZeroStates();
        FuzzyItem<Integer> i1 = i.next();
        FuzzyItem<Integer> i2 = i.next();

        assertEquals(new Integer(1), i1.element());
        assertEquals(new Integer(4), i2.element());
        assertEquals(0.333, i1.value(), 0.003);
        assertEquals(0.666, i2.value(), 0.003);
        assertFalse(i.hasNext());
    }

    @Test
    public void canComputeIntersections() {
        assertEquals(1d, arrayDist.intersection(dualDist),0d);
        assertEquals(1d, arrayDist.intersection(sparseDist),0d);
        assertEquals(1d, dualDist.intersection(arrayDist),0d);
        assertEquals(1d, dualDist.intersection(sparseDist),0d);
        assertEquals(1d, sparseDist.intersection(arrayDist),0d);
        assertEquals(1d, sparseDist.intersection(dualDist),0d);
    }
}