package se.umu.cs.robotics.iteration.position;

/**
 * A general purpose reversed position iterator. May be used togeather with most implementations of the IteratorPosition interface.
 *
 * The reversed iterator iterates over the position elements in reversed order. 
 *
 * @author billing
 */
public class GenericReverseIterator<E> extends GenericPositionIterator<E> {

    public GenericReverseIterator(final IteratorPosition<E> position) {
        super(position);
    }

    public GenericReverseIterator(final IteratorPosition<E> position, boolean inPreposition) {
        super(position, inPreposition);
    }

    @Override
    public boolean hasNext() {
        return super.hasPrevious();
    }

    @Override
    public boolean hasPrevious() {
        return super.hasNext();
    }

    @Override
    public IteratorPosition<E> next() {
        return super.previous();
    }

    @Override
    public IteratorPosition<E> previous() {
        return super.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GenericReverseIterator<E> clone() {
        return new GenericReverseIterator<E>(position, inPreposition);
    }
}
