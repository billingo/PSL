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

package se.umu.cs.robotics.iteration.junit;

import se.umu.cs.robotics.probabilitydistribution.iteration.IntegratingPositionIterator;
import se.umu.cs.robotics.probabilitydistribution.iteration.IntegratingIterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.iteration.position.ArrayPositionIterator;
import se.umu.cs.robotics.iteration.position.GenericReverseIterator;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.iteration.SingleStateDistributionPositionIterator;
import se.umu.cs.robotics.probabilitydistribution.iteration.SpacePositionIterator;
import se.umu.cs.robotics.statespace.FloatDimension;
import se.umu.cs.robotics.statespace.FloatSpace;
import se.umu.cs.robotics.statespace.StateSpace;
import se.umu.cs.robotics.utils.ArrayTools;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class IntegratingIteratorTest {

    StateSpace<Double> space = new FloatSpace(new FloatDimension(0, 5, 0, 5));
    static double[] data = {1, 2, 3, 4, 5};

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

    @Test
    public void canIterateArray() {
        ArrayPositionIterator<Double> i = new ArrayPositionIterator<Double>(ArrayTools.toDoubleArray(data));
        assertEquals(1d, i.next().element(), 0d);
        assertEquals(2d, i.next().element(), 0d);
        assertEquals(3d, i.next().element(), 0d);
        assertEquals(4d, i.next().element(), 0d);
        assertEquals(5d, i.next().element(), 0d);
        assertFalse(i.hasNext());
        assertEquals(4d, i.previous().element(), 0d);
        assertEquals(3d, i.previous().element(), 0d);
        assertEquals(2d, i.previous().element(), 0d);
    }

    @Test
    public void canIterateWithoutIntegration() {
        ArrayPositionIterator<Double> array = new ArrayPositionIterator<Double>(ArrayTools.toDoubleArray(data));
        SingleStateDistributionPositionIterator<Double> iDist = new SingleStateDistributionPositionIterator<Double>(space.getDimension(0), array);
        SpacePositionIterator<Double> iSpace = new SpacePositionIterator<Double>(space, iDist);
        IntegratingPositionIterator<Double> i = new IntegratingPositionIterator<Double>(iSpace.getPosition(), 1);
        assertEquals(1d, i.next().element().getDimension(0).max().next().element(), 0d);
        assertEquals(2d, i.next().element().getDimension(0).max().next().element(), 0d);
        assertEquals(3d, i.next().element().getDimension(0).max().next().element(), 0d);
        assertEquals(4d, i.next().element().getDimension(0).max().next().element(), 0d);
        assertEquals(5d, i.next().element().getDimension(0).max().next().element(), 0d);
        assertFalse(i.hasNext());
        assertEquals(4d, i.previous().element().getDimension(0).max().next().element(), 0d);
        assertEquals(3d, i.previous().element().getDimension(0).max().next().element(), 0d);
        assertEquals(2d, i.previous().element().getDimension(0).max().next().element(), 0d);
    }

    @Test
    public void canIterateWithoutIntegration2() {
        ArrayPositionIterator<Double> array = new ArrayPositionIterator<Double>(ArrayTools.toDoubleArray(data), 3);
        SingleStateDistributionPositionIterator<Double> iDist = new SingleStateDistributionPositionIterator<Double>(space.getDimension(0), array);
        SpacePositionIterator<Double> iSpace = new SpacePositionIterator<Double>(space, iDist);
        IntegratingPositionIterator<Double> i = new IntegratingPositionIterator<Double>(iSpace.getPosition(), 1);
        assertEquals(4d, i.previous().element().getDimension(0).max().next().element(), 0d);
        assertEquals(3d, i.previous().element().getDimension(0).max().next().element(), 0d);
        assertEquals(2d, i.previous().element().getDimension(0).max().next().element(), 0d);
    }

    @Test
    public void canIterateWithoutIntegrationReverse() {
        ArrayPositionIterator<Double> array = new ArrayPositionIterator<Double>(ArrayTools.toDoubleArray(data), 3);
        SingleStateDistributionPositionIterator<Double> iDist = new SingleStateDistributionPositionIterator<Double>(space.getDimension(0), array);
        SpacePositionIterator<Double> iSpace = new SpacePositionIterator<Double>(space, iDist);
        IntegratingPositionIterator<Double> iInt = new IntegratingPositionIterator<Double>(iSpace.getPosition(), 1);
        GenericReverseIterator<SpaceDistribution<Double>> i = new GenericReverseIterator<SpaceDistribution<Double>>(iInt.getPosition());
        assertEquals(4d, i.next().element().getDimension(0).max().next().element(), 0d);
        assertEquals(3d, i.next().element().getDimension(0).max().next().element(), 0d);
        assertEquals(2d, i.next().element().getDimension(0).max().next().element(), 0d);
    }

    @Test
    public void canIteratePrevious() {
        ArrayPositionIterator<Double> array = new ArrayPositionIterator<Double>(ArrayTools.toDoubleArray(data), 3);
        SingleStateDistributionPositionIterator<Double> iDist = new SingleStateDistributionPositionIterator<Double>(space.getDimension(0), array);
        SpacePositionIterator<Double> iSpace = new SpacePositionIterator<Double>(space, iDist);
        IntegratingPositionIterator<Double> i = new IntegratingPositionIterator<Double>(iSpace.getPosition(), 1);
        assertEquals(4d, i.previous().element().getDimension(0).max().next().element(), 0d);
        assertEquals(3d, i.previous().element().getDimension(0).max().next().element(), 0d);
        assertEquals(2d, i.previous().element().getDimension(0).max().next().element(), 0d);
    }
}
