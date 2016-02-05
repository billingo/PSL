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
