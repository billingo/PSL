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

import se.umu.cs.robotics.statespace.comparator.StateComparator;
import java.util.Iterator;

import se.umu.cs.robotics.iteration.IntegerIterator;
import se.umu.cs.robotics.statespace.comparator.ComparableComparator;

/**
 * A state dimension of integers.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class IntegerDimension implements StateDimension<Integer> {

    private final int low;
    private final int high;

    public IntegerDimension(int low, int high) {
        this.low = low;
        this.high = high;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new IntegerIterator(low, high);
    }

    @Override
    public int size() {
        return high - low + 1;
    }

    @Override
    public int getIndex(Integer state) {
        return state == null ? -1 : state - low;
    }

    public Integer defaultState() {
        return low;
    }

    public Integer getState(int position) {
        return low + position;
    }

    public Integer firstState() {
        return low;
    }

    public Integer lastState() {
        return high;
    }

    public StateComparator<Integer> comparator() {
        return new ComparableComparator<Integer>();
    }
}
