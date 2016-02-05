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
