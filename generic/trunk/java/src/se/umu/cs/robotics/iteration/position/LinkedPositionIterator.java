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

import se.umu.cs.robotics.iteration.IterableIterator;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * An iterator returning {@link LinkedPosition} elements. The
 * {@link LinkedPositionIterator} is created upon any other {@link Iterator} and
 * will dynamically use this iterator to create a structure much like a
 * {@link LinkedList}.
 * 
 * @author billing
 * 
 * @param <E>
 */
public class LinkedPositionIterator<E> implements PositionIterator<E> {

	private LinkedPosition<E> position;

	public LinkedPositionIterator(final Iterator<E> sourceIterator) {
		this.position = new LinkedPosition.PreIterationPosition<E>(sourceIterator);
	}

	public LinkedPositionIterator(final LinkedPosition<E> position) {
		this.position = position;
	}

	public LinkedPositionIterator(final IterableIterator<E> source) {
		this((Iterator<E>) source);
	}

	public LinkedPositionIterator(final Iterable<E> source) {
		this(source.iterator());
	}

	@Override
	public Iterator<IteratorPosition<E>> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return position.hasNext();
	}

	public boolean hasPrevious() {
		return position != null && position.hasPrevious();
	}

	public LinkedPosition<E> getPosition() {
		return position;
	}

	@Override
	public LinkedPosition<E> next() {
		position = position.getNext();
		return position;
	}

	public LinkedPosition<E> previous() {
		position = position.getPrevious();
		return position;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public LinkedPositionIterator<E> clone() {
		return new LinkedPositionIterator<E>(this.position);
	}

}
