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

import java.util.NoSuchElementException;

/**
 * An IteratorPosition represents a specific position within a sequence. The underlying sequence does not need to actually exist in memory in any explicit sense.
 *
 * The IteratorPosition is used togeather with implementations of the PositionIterator, allowing iterations over dynamic sequences that may change during the iteration or when iterators in both directions are required.
 *
 * @author billing
 * @param <E>
 */
public interface IteratorPosition<E> {
	/**
	 * Returns the position element previous to this. In case this is the first
	 * element returned by the underlying iterator, a
	 * {@link NoSuchElementException} is thrown.
	 * 
	 * @return the previous position element in sequence
	 */
	public IteratorPosition<E> getPrevious();

	/**
	 * This method will use the underlying {@link LinkedPositionIterator} to
	 * fetch the next element, if not already referenced. As such, it may raise
	 * a {@link NoSuchElementException} in case the iterator does not have more
	 * elements.
	 * 
	 * @return the next position element in sequence
	 */
	public IteratorPosition<E> getNext();

	public E element();

	public boolean hasNext();

	public boolean hasPrevious();

}
