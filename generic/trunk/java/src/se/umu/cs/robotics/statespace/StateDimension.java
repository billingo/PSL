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

/**
 * Represents a single dimension of a discrete state space. Each point in time,
 * the system is in exactly one state (of each dimension).
 * 
 * @author billing
 * 
 * @param <E>
 */
public interface StateDimension<E> extends Iterable<E> {

    /**
     * @return the number of states in this dimension
     */
    int size();

    /**
     * @param state
     * @return the int index of specified state
     */
    int getIndex(E state);

    /**
     * @param index
     * @return the state with specified index
     */
    E getState(int index);

    /**
     * @return the default state of this dimension
     */
    E defaultState();

    /**
     * @return the state with index 0
     */
    E firstState();

    /**
     * @return the state with the highest index
     */
    E lastState();

    StateComparator<E> comparator();
}
