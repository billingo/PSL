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
package se.umu.cs.robotics.iteration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author billing
 */
public class ArrayListIterator<E> implements IterableIterator<E> {

    private final ArrayList<E> array;
    private int i;
    private int stop;

    public ArrayListIterator(final ArrayList<E> array) {
        this(array, 0);
    }

    public ArrayListIterator(final ArrayList<E> array, final int startIndex) {
        this(array, startIndex, array.size());
    }

    public ArrayListIterator(final ArrayList<E> array, final int startIndex, final int stopIndex) {
        this.array = array;
        this.i = startIndex;
        this.stop = stopIndex;
    }

    @Override
    public boolean hasNext() {
        return i < stop;
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        } else {
            return array.get(i++);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }
}
