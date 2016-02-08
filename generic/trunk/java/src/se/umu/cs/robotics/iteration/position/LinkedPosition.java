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
import java.util.NoSuchElementException;

/**
 * An element returned by the {@link LinkedPositionIterator}. This class holds
 * an element at a specific position in the underlying sequence. The position is
 * hold together using pre- and post references much like the internal structure
 * of a linked list. The exposure of this class, in contrast to returning the
 * elements directly, allows references to a certain position in the underlying
 * sequence with out the use of an index.
 * 
 * @author billing
 * 
 * @param <E>
 */
public class LinkedPosition<E> implements IteratorPosition<E> {
	private final Iterator<E> source;
	private final LinkedPosition<E> previous;
	private LinkedPosition<E> next = null;
	private final E element;

	/**
	 * Calls next() on the specified source iterator and creates a position.
	 * 
	 * @param source
	 * @param previous
	 */
	public LinkedPosition(final Iterator<E> source, final LinkedPosition<E> previous, final E element) {
		this.source = source;
		this.previous = previous;
		this.element = element;
	}

	/**
	 * Returns the position element previous to this. In case this is the first
	 * element returned by the underlying iterator, a
	 * {@link NoSuchElementException} is thrown.
	 * 
	 * @return the previous position element in sequence
	 */
	public LinkedPosition<E> getPrevious() {
		if (!hasPrevious())
			throw new NoSuchElementException();
		return previous;
	}

	/**
	 * This method will use the underlying {@link LinkedPositionIterator} to
	 * fetch the next element, if not already referenced. As such, it may raise
	 * a {@link NoSuchElementException} in case the iterator does not have more
	 * elements.
	 * 
	 * @return the next position element in sequence
	 */
	public LinkedPosition<E> getNext() {
		/*
		 * A null value implies that this is the most recent element returned by
		 * owner
		 */
		if (next == null)
			next = new LinkedPosition<E>(source, this, source.next());
		return next;
	}

	public E element() {
		return element;
	}

	public boolean hasNext() {
		if (next == null) {
			return source.hasNext();
		} else {
			return true;
		}
	}

	protected boolean hasNextPointer() {
		return (next != null);
	}

	public boolean hasPrevious() {
		return !(previous == null || previous instanceof PreIterationPosition);
	}

	public LinkedPosition<E> getFirst() {
		if (hasPrevious())
			return getPrevious().getFirst();
		else
			return this;
	}

	public LinkedPosition<E> getLast() {
		if (hasNext())
			return getNext().getLast();
		else
			return this;
	}

	@Override
	public String toString() {
		return element == null ? "null" : element.toString();
	}

	public static class PreIterationPosition<E> extends LinkedPosition<E> {

		protected PreIterationPosition(final Iterator<E> sourceIterator) {
			super(sourceIterator, null, null);
		}

		@Override
		public E element() {
			throw new NoSuchElementException();
		}
	}
}
