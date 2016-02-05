package se.umu.cs.robotics.iteration.position;

import java.util.Iterator;

/**
 * A general purpose position iterator. May be used togeather with most implementations of the IteratorPosition interface.
 *
 * @author billing
 */
public class GenericPositionIterator<E> implements PositionIterator<E> {

    protected IteratorPosition<E> position;
    protected boolean inPreposition;

    public GenericPositionIterator(final IteratorPosition<E> position) {
        this(position, false);
    }

    public GenericPositionIterator(final IteratorPosition<E> position, boolean inPreposition) {
        this.position = position;
        this.inPreposition = inPreposition;
    }

    @Override
    public Iterator<IteratorPosition<E>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return inPreposition || position != null && position.hasNext();
    }

    public boolean hasPrevious() {
        return inPreposition || position != null && position.hasPrevious();
    }

    public IteratorPosition<E> getPosition() {
        return inPreposition ? null : position;
    }

    @Override
    public IteratorPosition<E> previous() {
        if (inPreposition) {
            inPreposition = false;
        } else {
            position = position.getPrevious();
        }
        return position;
    }

    public IteratorPosition<E> next() {
        if (inPreposition) {
            inPreposition = false;
        } else {
            position = position.getNext();
        }
        return position;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GenericPositionIterator<E> clone() {
        return new GenericPositionIterator<E>(this.position, inPreposition);
    }
}
