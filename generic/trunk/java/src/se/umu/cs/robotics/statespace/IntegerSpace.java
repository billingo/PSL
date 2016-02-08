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

package se.umu.cs.robotics.statespace;

import java.util.HashMap;
import java.util.Iterator;

import se.umu.cs.robotics.iteration.ArrayIterator;

/**
 * A state space consisting of {@link IntegerDimension}s.
 * 
 * @author Erik Billing <billing@cs.umu.se>
 */
public class IntegerSpace extends AbstractStateSpace<Integer> {

    private final StateDimension<Integer>[] dimensions;
    private final HashMap<StateDimension<Integer>, Integer> dimensionMap = new HashMap<StateDimension<Integer>, Integer>();

    public IntegerSpace(StateDimension<Integer>... dimensions) {
        this.dimensions = dimensions;
        initDimensionMap();
    }

    private void initDimensionMap() {
        for (int i = 0; i < dimensions.length; i++) {
            dimensionMap.put(dimensions[i], i);
        }
    }

    @Override
    public Iterator<StateDimension<Integer>> iterator() {
        return new ArrayIterator<StateDimension<Integer>>(dimensions);
    }

    @Override
    public int size() {
        return dimensions.length;
    }

    @Override
    public int getDimensionIndex(StateDimension<Integer> dim) {
        Integer d = dimensionMap.get(dim);
        return d==null ? -1 : d;
    }

    @Override
    public StateDimension<Integer> getDimension(int id) {
        return dimensions[id];
    }
}
