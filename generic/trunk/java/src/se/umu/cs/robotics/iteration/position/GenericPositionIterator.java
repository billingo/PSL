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

import java.util.Iterator;

/**
 * A general purpose position iterator. May be used togeather with most implementations of the IteratorPosition interface.
 *
 * @author billing
 */
public class GenericPositionIterator<E> implements PositionIterator<E> {

    protected IteratorPosition<E> position;
    protected boolean inPreposition;

    public GenericPositionIterator(final IteratorPosition<E> position) {
        this(position, false);
    }

    public GenericPositionIterator(final IteratorPosition<E> position, boolean inPreposition) {
        this.position = position;
        this.inPreposition = inPreposition;
    }

    @Override
    public Iterator<IteratorPosition<E>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return inPreposition || position != null && position.hasNext();
    }

    public boolean hasPrevious() {
        return inPreposition || position != null && position.hasPrevious();
    }

    public IteratorPosition<E> getPosition() {
        return inPreposition ? null : position;
    }

    @Override
    public IteratorPosition<E> previous() {
        if (inPreposition) {
            inPreposition = false;
        } else {
            position = position.getPrevious();
        }
        return position;
    }

    public IteratorPosition<E> next() {
        if (inPreposition) {
            inPreposition = false;
        } else {
            position = position.getNext();
        }
        return position;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GenericPositionIterator<E> clone() {
        return new GenericPositionIterator<E>(this.position, inPreposition);
    }
}
