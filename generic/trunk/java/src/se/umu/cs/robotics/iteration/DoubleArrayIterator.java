package se.umu.cs.robotics.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

import se.umu.cs.robotics.iteration.IterableIterator;

/**
 * Iterator over an array of Doubles.
 *
 * @author billing
 */
public class DoubleArrayIterator implements IterableIterator<Double> {

	private final double[] array;
	private int i;

	public DoubleArrayIterator(final double[] array) {
		this.array = array;
	}

	public DoubleArrayIterator(final double[] array, final int startIndex) {
		this.array = array;
		this.i = startIndex;
	}

	@Override
	public boolean hasNext() {
		return i < array.length;
	}

	@Override
	public Double next() {
		if (!hasNext())
			throw new NoSuchElementException();
		return array[i++];
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Double> iterator() {
		return this;
	}
}
