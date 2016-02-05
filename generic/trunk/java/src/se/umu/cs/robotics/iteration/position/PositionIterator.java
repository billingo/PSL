/*-------------------------------------------------------------------*\
 THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

 Copyright 2007 - 2010 Erik Billing
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

package se.umu.cs.robotics.iteration.position;

import se.umu.cs.robotics.iteration.IterableIterator;


/**
 * An {@link java.util.Iterator} that does not return the sequence element directly, but
 * rather the current position in sequence as an {@link IteratorPosition}. In
 * this way, several positions in the underlying sequence can be kept
 * simultaneously, iterators can be cloned and supports going backwards in
 * sequence.
 * 
 * The PositionIterator extends the IterableIterator, meaning that it 
 * support iterations over itself.
 *
 * @author Erik Billing <billing@cs.umu.se>
 * 
 * @param <E> the element type
 */
public interface PositionIterator<E> extends IterableIterator<IteratorPosition<E>> {

	/**
	 * @return true if the there exists a previous element in sequence
	 */
	public boolean hasPrevious();

	/**
	 * @return the previous element in sequence
	 */
	public IteratorPosition<E> previous();

	/**
	 * @return the current position of the iterator (does not affect the
	 *         iterator)
	 */
	public IteratorPosition<E> getPosition();

	public PositionIterator<E> clone();

}
