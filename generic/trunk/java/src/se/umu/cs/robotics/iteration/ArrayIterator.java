package se.umu.cs.robotics.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A standard iterator over an object array, implementing the IterableIterator interface.
 * 
 * @author billing
 * @param <E>
 */
public class ArrayIterator<E> implements IterableIterator<E> {

    private ArrayIterator<E[]> arrays;
    private E[] array;
    private int i;
    private int stop;

    public ArrayIterator(final E[]... arrays) {
        this(arrays.length > 0 ? arrays[0] : null, 0);
        if (arrays.length > 1) {
            this.arrays = new ArrayIterator<E[]>(arrays,1);
        }
    }

    public ArrayIterator(final E[] array, final int startIndex) {
        this(array, startIndex, array.length);
    }

    public ArrayIterator(final E[] array, final int startIndex, final int stopIndex) {
        this.array = array;
        this.i = startIndex;
        this.stop = stopIndex;
    }

    @Override
    public boolean hasNext() {
        return i < stop || (arrays != null && arrays.hasNext());
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        if (i >= stop) {
            array = arrays.next();
            stop = array.length;
            i=0;
        }
        return array[i++];
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }
}
