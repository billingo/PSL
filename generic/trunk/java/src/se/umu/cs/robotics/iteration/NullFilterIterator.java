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
package se.umu.cs.robotics.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Returns only non-null elements from the source iterator.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class NullFilterIterator<E> implements IterableIterator<E> {

    private final Iterator<E> source;
    private E next;

    public NullFilterIterator(Iterator<E> source) {
        this.source = source;
    }

    public Iterator<E> iterator() {
        return this;
    }

    private void feed() {
        while (next == null && source.hasNext()) {
            next = source.next();
        }
    }

    public boolean hasNext() {
        feed();
        return next != null;
    }

    public E next() {
        if (hasNext()) {
            E e = next;
            next = null;
            return e;
        } else {
            throw new NoSuchElementException();
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }
}
