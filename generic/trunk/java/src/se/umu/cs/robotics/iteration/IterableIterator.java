package se.umu.cs.robotics.iteration;

import java.util.Iterator;

/**
 * Combines the {@link Iterator} and {@link Iterable} interfaces such that the iterator can be
 * used with new style for-loops.
 * 
 * @author billing
 * 
 * @param <E>
 */
public interface IterableIterator<E> extends Iterable<E>, Iterator<E> {

}
