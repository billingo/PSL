/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package se.umu.cs.robotics.iteration.position;

import se.umu.cs.robotics.iteration.position.IteratorPosition;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import se.umu.cs.robotics.collections.*;

/**
 * Represents a position within a specific ArrayList.
 * 
 * @author billing
 */
public class ArrayListPosition<E> implements IteratorPosition<E> {

	private final ArrayList<E> array;
	private final int index;

	public ArrayListPosition(final ArrayList<E> list, final int index) {
		this.array = list;
		this.index = index;
	}

	@Override
	public E element() {
		try {
			return array.get(index);
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchElementException(Integer.toString(index));
		}
	}

	@Override
	public ArrayListPosition<E> getNext() {
		if (hasNext()) {
			return new ArrayListPosition<E>(array, index + 1);
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public ArrayListPosition<E> getPrevious() {
		if (hasPrevious()) {
			return new ArrayListPosition<E>(array, index - 1);
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public boolean hasNext() {
		return index + 1 < array.size();
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

	public ArrayList<E> getArray() {
		return array;
	}

	public int getIndex() {
		return index;
	}
}