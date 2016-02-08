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

/**
 * A general purpose reversed position iterator. May be used togeather with most implementations of the IteratorPosition interface.
 *
 * The reversed iterator iterates over the position elements in reversed order. 
 *
 * @author billing
 */
public class GenericReverseIterator<E> extends GenericPositionIterator<E> {

    public GenericReverseIterator(final IteratorPosition<E> position) {
        super(position);
    }

    public GenericReverseIterator(final IteratorPosition<E> position, boolean inPreposition) {
        super(position, inPreposition);
    }

    @Override
    public boolean hasNext() {
        return super.hasPrevious();
    }

    @Override
    public boolean hasPrevious() {
        return super.hasNext();
    }

    @Override
    public IteratorPosition<E> next() {
        return super.previous();
    }

    @Override
    public IteratorPosition<E> previous() {
        return super.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GenericReverseIterator<E> clone() {
        return new GenericReverseIterator<E>(position, inPreposition);
    }
}
