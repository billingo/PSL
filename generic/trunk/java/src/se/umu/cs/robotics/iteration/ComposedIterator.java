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

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Iterates over a collection of objects. If any of the specified objects are
 * iterators, it will be included it in iteration.
 * 
 * @author billing
 * 
 * @param <E>
 */
public class ComposedIterator<E> implements Iterable<E>, Iterator<E> {

	private final LinkedList<Object> parts = new LinkedList<Object>();
	private final Iterator<Object> partsIterator;
	private Iterator<?> partIterator;

	public ComposedIterator(final Object... parts) {
		for (Object part : parts) {
			this.parts.add(part);
		}
		partsIterator = this.parts.iterator();
	}

	@Override
	public Iterator<E> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		if (partIterator == null) {
			return partsIterator.hasNext();
		} else if (partIterator.hasNext()) {
			return true;
		} else {
			partIterator = null;
			return hasNext();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public E next() {
		if (partIterator == null) {
			Object next = partsIterator.next();
			if (next instanceof Iterator) {
				partIterator = (Iterator<?>) next;
				return next();
			} else {
				return (E) next;
			}
		} else {
			return (E) partIterator.next();
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
