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

/**
 * This class can be used as a wrapper for the LinkedPositionIterator when the
 * position iterator should return the element directly (not the position).
 * 
 * @author billing
 * 
 * @param <E>
 */
public class ElementIterator<E> implements IterableIterator<E> {

	private final PositionIterator<E> pIterator;

	public ElementIterator(final PositionIterator<E> iterator) {
		pIterator = iterator;
	}

	@Override
	public Iterator<E> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return pIterator.hasNext();
	}

	@Override
	public E next() {
		return pIterator.next().element();
	}

	@Override
	public void remove() {
		pIterator.remove();
	}

}
