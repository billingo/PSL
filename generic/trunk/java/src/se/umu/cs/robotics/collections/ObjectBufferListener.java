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

package se.umu.cs.robotics.collections;

public interface ObjectBufferListener<E> {
	/**
	 * Signals that an element has been put on the buffer.
	 * 
	 * @param e added element
	 */
	public void elementAdded(E e);

	/**
	 * Signals that an element has gone out of the buffer. If an element is lost
	 * due to that a new element is added to the buffer, this method is called
	 * after elementAdded.
	 * 
	 * @param e the lost element
	 */
	public void elementLost(E e);

	/**
	 * Signals that the capacity of the buffer has changed.
	 * 
	 * As a result of the changed capacity, elements in the buffer may be lost.
	 * Consequently, elementLost may be called after the buffer capacity has
	 * changed.
	 * 
	 * @param capacity the new capacity
	 */
	public void capacityChanged(int capacity);
}
