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

import java.util.Comparator;

/**
 * An extention of the standard Comparator interface adding a distance method.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public interface StateComparator<T> extends Comparator<T> {

    /**
     * @param state1
     * @param state2
     * @return the normalized distance between state1 and state2.
     */
    double distance(T state1, T state2);

}
