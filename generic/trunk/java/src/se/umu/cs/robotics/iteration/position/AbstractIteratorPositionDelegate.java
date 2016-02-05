/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.iteration.position;

/**
 *
 * @author billing
 */
public abstract class AbstractIteratorPositionDelegate<E, S> implements IteratorPosition<E> {

    private final IteratorPosition<S> pos;

    public AbstractIteratorPositionDelegate(IteratorPosition<S> pos) {
        this.pos = pos;
    }

    public boolean hasNext() {
        return pos.hasNext();
    }

    public boolean hasPrevious() {
        return pos.hasPrevious();
    }

    public IteratorPosition<S> sourcePosition() {
        return pos;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == getClass()) {
            return ((AbstractIteratorPositionDelegate) pos).equals(pos);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return pos.hashCode() ^ 5001;
    }
}
