package se.umu.cs.robotics.iteration.position;

import java.util.NoSuchElementException;

/**
 * Represents a specific position in an array.
 * 
 * @author billing
 * @param <E>
 */
public class ArrayPosition<E> implements IteratorPosition<E> {

	private final E[] array;
	private final int index;

	public ArrayPosition(final E[] array, final int index) {
		this.array = array;
		this.index = index;
	}

	@Override
	public E element() {
		try {
			return array[index];
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchElementException(Integer.toString(index));
		}
	}

	@Override
	public ArrayPosition<E> getNext() {
		if (hasNext()) {
			return new ArrayPosition<E>(array, index + 1);
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public ArrayPosition<E> getPrevious() {
		if (hasPrevious()) {
			return new ArrayPosition<E>(array, index - 1);
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public boolean hasNext() {
		return index + 1 < array.length;
	}

	@Override
	public boolean hasPrevious() {
		return index > 0;
	}

	@Override
	public String toString() {
		try {
			return index + ":" + element();
		} catch (NoSuchElementException e) {
			return index + ":?";
		}
	}

	public E[] getArray() {
		return array;
	}

	public int getIndex() {
		return index;
	}

        public ArrayPosition<E> clone() {
            return new ArrayPosition<E>(array,index);
        }
}
