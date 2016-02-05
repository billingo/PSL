/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.iteration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author billing
 */
public class ArrayListIterator<E> implements IterableIterator<E> {

    private final ArrayList<E> array;
    private int i;
    private int stop;

    public ArrayListIterator(final ArrayList<E> array) {
        this(array, 0);
    }

    public ArrayListIterator(final ArrayList<E> array, final int startIndex) {
        this(array, startIndex, array.size());
    }

    public ArrayListIterator(final ArrayList<E> array, final int startIndex, final int stopIndex) {
        this.array = array;
        this.i = startIndex;
        this.stop = stopIndex;
    }

    @Override
    public boolean hasNext() {
        return i < stop;
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        } else {
            return array.get(i++);
        }
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
