/*
 *  Copyright (C) 2011 Erik Billing <billing@cs.umu.se>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.umu.cs.robotics.statespace;

import java.util.HashMap;
import java.util.Iterator;
import se.umu.cs.robotics.iteration.ArrayIterator;
import se.umu.cs.robotics.statespace.comparator.DimensionComparator;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class FloatSpace extends AbstractStateSpace<Double> {

    private final StateDimension<Double>[] dimensions;
    private final HashMap<StateDimension<Double>, Integer> dimensionMap = new HashMap<StateDimension<Double>, Integer>();

    public FloatSpace(StateDimension<Double>... dimensions) {
        super();
        this.dimensions = dimensions;
        initDimensionMap();
    }

    public FloatSpace(DimensionComparator comparator, StateDimension<Double>... dimensions) {
        super(comparator);
        this.dimensions = dimensions;
        initDimensionMap();
    }

    private void initDimensionMap() {
        for (int i = 0; i < dimensions.length; i++) {
            dimensionMap.put(dimensions[i], i);
        }
    }

    @Override
    public Iterator<StateDimension<Double>> iterator() {
        return new ArrayIterator<StateDimension<Double>>(dimensions);
    }

    @Override
    public int size() {
        return dimensions.length;
    }

    @Override
    public int getDimensionIndex(StateDimension<Double> dim) {
        Integer d = dimensionMap.get(dim);
        return d==null ? -1 : d;
    }

    @Override
    public StateDimension<Double> getDimension(int id) {
        return dimensions[id];
    }

}
