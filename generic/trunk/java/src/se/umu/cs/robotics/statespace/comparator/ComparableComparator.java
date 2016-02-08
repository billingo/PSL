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

package se.umu.cs.robotics.statespace.comparator;

/**
 * A dummy comparator comparing objects using the compareTo method of the comparable interface. Any non-equal objects have distance 1.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ComparableComparator<T extends Comparable> implements StateComparator<T> {

    public double distance(T state1, T state2) {
//        return ((double)Math.abs(state1-state2))/((double)size()-1);
        if (state1 == null || state2 == null) {
            return 1d;
        } else {
            return state1.equals(state2) ? 0d : 1d;
        }
    }

    public int compare(T o1, T o2) {
        if (o1 == o2) {
            return 0;
        } else if (o1 == null) {
            return 1;
        } else if (o2 == null) {
            return -1;
        } else {
            return o1.compareTo(o2);
        }
    }
}
