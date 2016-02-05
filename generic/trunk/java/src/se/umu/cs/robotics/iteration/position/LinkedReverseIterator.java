package se.umu.cs.robotics.iteration.position;

import java.util.Iterator;

public class LinkedReverseIterator<E> extends LinkedPositionIterator<E> {

	public LinkedReverseIterator(final PositionIterator<E> sourceIterator) {
		super(new Iterator<E>() {

			@Override
			public boolean hasNext() {
				return sourceIterator.hasPrevious();
			}

			@Override
			public E next() {
				return sourceIterator.previous().element();
			}

			@Override
			public void remove() {
				sourceIterator.remove();
			}

		});
	}

	private LinkedReverseIterator(final LinkedPosition<E> p) {
		super(p);
	}

	@Override
	public LinkedReverseIterator<E> clone() {
		return new LinkedReverseIterator<E>(getPosition());
	}
}
