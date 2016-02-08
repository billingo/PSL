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

import se.umu.cs.robotics.iteration.position.IteratorPosition;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import se.umu.cs.robotics.collections.*;

/**
 * Represents a position within a specific ArrayList.
 * 
 * @author billing
 */
public class ArrayListPosition<E> implements IteratorPosition<E> {

	private final ArrayList<E> array;
	private final int index;

	public ArrayListPosition(final ArrayList<E> list, final int index) {
		this.array = list;
		this.index = index;
	}

	@Override
	public E element() {
		try {
			return array.get(index);
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchElementException(Integer.toString(index));
		}
	}

	@Override
	public ArrayListPosition<E> getNext() {
		if (hasNext()) {
			return new ArrayListPosition<E>(array, index + 1);
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public ArrayListPosition<E> getPrevious() {
		if (hasPrevious()) {
			return new ArrayListPosition<E>(array, index - 1);
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public boolean hasNext() {
		return index + 1 < array.size();
	}

	@Override
	public boolean hasPrevious() {
		return index > 0;
	}

	@Override
	public String toString() {
		try {
			return index + ":" + element();
		} catch (NoSuchElementException e) {
			return index + ":?";
		}
	}

	public ArrayList<E> getArray() {
		return array;
	}

	public int getIndex() {
		return index;
	}
}