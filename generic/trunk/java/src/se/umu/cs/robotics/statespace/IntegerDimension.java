/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright 2007 - 2009 Erik Billing
Department of Computing Science, Umea University, Sweden,
(http://www.cognitionreversed.com).

LICENSE:

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston,
MA 02111-1307, USA.
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
