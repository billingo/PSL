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

import java.util.Iterator;
import java.util.NoSuchElementException;
import se.umu.cs.robotics.statespace.comparator.LinearDoubleComparator;
import se.umu.cs.robotics.statespace.comparator.StateComparator;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class FloatDimension implements StateDimension<Double> {

    private final double low;
    private final double high;
    private final int size;
    private final double defaultState;

    private final LinearDoubleComparator comparator;

    public FloatDimension(double low, double high, double defaultState, int size) {
        this.low = low;
        this.high = high;
        this.defaultState = defaultState;
        this.size = size;
        this.comparator = new LinearDoubleComparator(range()/size,range()/size);
    }

    public int size() {
        return size;
    }

    public double range() {
        return high - low;
    }

    public double resolution() {
        return range() / size;
    }

    public int getIndex(Double state) {
        int index = (int) ((state - low) / resolution());
        return index < 0 ? 0 : index >= size ? size-1 : index;
    }

    public Double getState(int index) {
        return low + resolution() * index;
    }

    public Double defaultState() {
        return defaultState;
    }

    public Double firstState() {
        return low;
    }

    public Double lastState() {
        return high;
    }

    public Iterator<Double> iterator() {
        return new Iterator<Double>() {

            private int i = 0;

            public boolean hasNext() {
                return i + 1 < size;
            }

            public Double next() {
                if (hasNext()) {
                    return getState(i++);
                } else {
                    throw new NoSuchElementException();
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    public StateComparator<Double> comparator() {
        return comparator;
    }
}
