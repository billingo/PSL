/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.collections.fuzzy;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.IterableIterator;

/**
 *
 * @author billing
 */
public class UnitIterator<E> implements IterableIterator<FuzzyItem<E>> {

    private Iterator<E> source;

    public UnitIterator(Iterator<E> source) {
        this.source = source;
    }

    public Iterator<FuzzyItem<E>> iterator() {
        return this;
    }

    public boolean hasNext() {
        return source.hasNext();
    }

    public UnitItem<E> next() {
        return new UnitItem<E>(source.next());
    }

    public void remove() {
        source.remove();
    }
}
