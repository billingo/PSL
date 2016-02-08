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
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A PositionIterator over an ArrayList.
 *
 * @author billing
 */
public class ArrayListPositionIterator<E> implements PositionIterator<E> {

    private ArrayListPosition<E> pos;
    private final int beginning;
    private final int end;

    public ArrayListPositionIterator(final ArrayListPosition<E> pos) {
        this(pos, 0, -1);
    }

    public ArrayListPositionIterator(final ArrayListPosition<E> pos, final int beginning, final int end) {
        this.pos = pos;
        this.beginning = beginning;
        this.end = end;
    }

    public ArrayListPositionIterator(final ArrayList<E> array) {
        this(array, 0);
    }

    public ArrayListPositionIterator(final ArrayList<E> array, final int start) {
        this(array, start, 0, -1);
    }

    public ArrayListPositionIterator(final ArrayList<E> array, final int start, final int beginning, final int end) {
        this(new ArrayListPrePosition<E>(array, start), beginning, end);
    }

    @Override
    public IteratorPosition<E> getPosition() {
        return pos;
    }

    public int getBeginning() {
        return beginning;
    }

    public int getEnd() {
        return end < 0 ? pos.getArray().size() : end;
    }

    @Override
    public boolean hasPrevious() {
        if (pos.getIndex() > beginning) {
            return pos.hasPrevious();
        } else {
            return false;
        }
    }

    @Override
    public IteratorPosition<E> previous() {
        if (hasPrevious()) {
            pos = pos.getPrevious();
        } else {
            throw new NoSuchElementException();
        }
        return pos;
    }

    @Override
    public Iterator<IteratorPosition<E>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if (end < 0 || pos.getIndex() < end - 1) {
            return pos.hasNext();
        } else {
            return false;
        }
    }

    @Override
    public IteratorPosition<E> next() {
        if (hasNext()) {
            pos = pos.getNext();
        } else {
            throw new NoSuchElementException();
        }
        return pos;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ArrayListPositionIterator<E> clone() {
        return new ArrayListPositionIterator<E>(pos, beginning, end);
    }
}
