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

package se.umu.cs.robotics.iteration.position;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * The ArrayListPositionIterator initiate using this PrePosition. The iterator 
 * will stay in the preposition until next() or previous() is called the first 
 * time. The PrePosition always retuns the starting element, i.e., next() == previous(). 
 *
 * @author billing
 */
public class ArrayListPrePosition<E> extends ArrayListPosition<E> {

    public ArrayListPrePosition(ArrayList<E> list, int index) {
        super(list, index);
    }

    @Override
    public ArrayListPosition<E> getNext() {
        if (hasNext()) {
            return new ArrayListPosition<E>(getArray(), getIndex());
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public ArrayListPosition<E> getPrevious() {
        return getNext();
    }

    @Override
    public boolean hasNext() {
        return getIndex() >= 0 && getIndex() < getArray().size();
    }

    @Override
    public boolean hasPrevious() {
        return hasNext();
    }
}
