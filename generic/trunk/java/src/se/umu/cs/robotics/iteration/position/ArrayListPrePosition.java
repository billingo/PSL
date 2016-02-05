/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.iteration.position;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * The ArrayListPositionIterator initiate using this PrePosition. The iterator 
 * will stay in the preposition until next() or previous() is called the first 
 * time. The PrePosition always retuns the starting element, i.e., next() == previous(). 
 *
 * @author billing
 */
public class ArrayListPrePosition<E> extends ArrayListPosition<E> {

    public ArrayListPrePosition(ArrayList<E> list, int index) {
        super(list, index);
    }

    @Override
    public ArrayListPosition<E> getNext() {
        if (hasNext()) {
            return new ArrayListPosition<E>(getArray(), getIndex());
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public ArrayListPosition<E> getPrevious() {
        return getNext();
    }

    @Override
    public boolean hasNext() {
        return getIndex() >= 0 && getIndex() < getArray().size();
    }

    @Override
    public boolean hasPrevious() {
        return hasNext();
    }
}
