/*-------------------------------------------------------------------*\
 THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com
 
 Copyright � 2007 - 2009 Erik Billing
 Department of Computing Science, Ume� University, Sweden,  
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
