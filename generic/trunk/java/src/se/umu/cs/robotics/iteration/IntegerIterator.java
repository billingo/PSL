package se.umu.cs.robotics.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator over a range of Integers.
 * 
 * @author billing
 */
public class IntegerIterator implements IterableIterator<Integer> {

    private int current;
    private final int stop;

    /**
     * Creates an iterator that iterates from start to stop, including both. 
     * 
     * @param start
     * @param stop 
     */
    public IntegerIterator(int start, int stop) {
        current = start;
        this.stop = stop;
    }

    @Override
    public Iterator<Integer> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return current <= stop;
    }

    @Override
    public Integer next() {
        if (hasNext()) {
            return current++;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
