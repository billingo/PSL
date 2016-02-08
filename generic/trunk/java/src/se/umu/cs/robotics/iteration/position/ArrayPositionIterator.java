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
 * A PositionIterator over an array.
 * 
 * @author billing
 * @param <E>
 */
public class ArrayPositionIterator<E> implements PositionIterator<E> {

	private ArrayPosition<E> pos;
	private final int beginning;
	private final int end;

	public ArrayPositionIterator(final ArrayPosition<E> pos) {
		this(pos, 0, pos.getArray().length);
	}

	public ArrayPositionIterator(final ArrayPosition<E> pos, final int beginning, final int end) {
		this.pos = pos;
		this.beginning = beginning;
		this.end = end;
	}

	public ArrayPositionIterator(final E[] array) {
		this(array, 0);
	}

	public ArrayPositionIterator(final E[] array, final int start) {
		this(array, start, 0, array.length);
	}

	public ArrayPositionIterator(final E[] array, final int start, final int beginning, final int end) {
		this(new ArrayPrePosition<E>(array, start), beginning, end);
	}

	@Override
	public IteratorPosition<E> getPosition() {
		return pos;
	}

	public int getBeginning() {
		return beginning;
	}

	public int getEnd() {
		return end;
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
			return pos;
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public Iterator<IteratorPosition<E>> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		if (pos.getIndex() < end - 1) {
			return pos.hasNext();
		} else {
			return false;
		}
	}

	@Override
	public IteratorPosition<E> next() {
		if (hasNext()) {
			pos = pos.getNext();
			return pos;
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ArrayPositionIterator<E> clone() {
		return new ArrayPositionIterator<E>(pos, beginning, end);
	}

}
