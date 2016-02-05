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
