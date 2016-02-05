/*-------------------------------------------------------------------*\
 THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com
 
 Copyright 2007 - 2009 Erik Billing
 Department of Computing Science, Umea University, Sweden,
 (www.cs.umu.se/~billing/).                               

 LICENSE:

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 MA 02111-1307, USA.
\*-------------------------------------------------------------------*/

package se.umu.cs.robotics.collections;

/**
 * Stores a certain amount of objects. When the buffer capacity is exceeded, the
 * oldest element is removed. All ObjectBuffers have a direction specifying how
 * elements is added, removed and retrieved.
 * 
 * @author billing
 * 
 * @param <E>
 */
public interface ObjectBuffer<E> extends Iterable<E> {
	/**
	 * Adds an element to the buffer.
	 * 
	 * @param e the new element
	 * @return true if the buffer content was changed, false otherwise
	 */
	public abstract boolean add(E e);

	/**
	 * @return the first element of the buffer.
	 */
	public abstract E getFirst();

	/**
	 * @return the last element of the buffer.
	 */
	public abstract E getLast();

	/**
	 * @return the capacity of the buffer.
	 */
	public abstract int getCapacity();

	/**
	 * Sets the capacity of the buffer.
	 * 
	 * @param capacity
	 */
	public abstract void setCapacity(int capacity);

	/**
	 * @return the number of elements currently in the buffer.
	 */
	public abstract int size();

        public abstract boolean isEmpty();

	/**
	 * Clears the buffer.
	 */
	public abstract void clear();

	public abstract void addListener(ObjectBufferListener<E> listener);

	public abstract void removeListener(ObjectBufferListener<E> listener);

	public abstract void clearListeners();

	/**
	 * @return the buffer direction.
	 */
	public abstract BufferDirection getDirection();

	/**
	 * Specifies a direction of the object buffer. The buffer direction affects
	 * how elements are added, removed and retrieved, and in which order they
	 * are iterated.
	 * 
	 * @author billing
	 * 
	 */
	public static enum BufferDirection {
		/**
		 * A forward buffer iterates from oldest to newest element.
		 */
		FORWARD,

		/**
		 * A backward buffer iterates from newest to oldest element.
		 */
		BACKWARD
	}
}