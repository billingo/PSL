/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package se.umu.cs.robotics.iteration.position;

/**
 * The ArrayPositionIterator initiate using this PrePosition. The iterator
 * will stay in the preposition until next() or previous() is called the first
 * time. The PrePosition always retuns the starting element, i.e., next() == previous().
 *
 * @author billing
 */
public class ArrayPrePosition<E> extends ArrayPosition<E> {

    public ArrayPrePosition(E[] array, int index) {
        super(array,index);
    }

    @Override
    public ArrayPosition<E> getPrevious() {
        return super.clone();
    }

    @Override
    public ArrayPosition<E> getNext() {
        return super.clone();
    }

    @Override
    public boolean hasNext() {
        final int i = getIndex();
        return i >= 0 && i < getArray().length;
    }

    @Override
    public boolean hasPrevious() {
        return hasNext();
    }

}
