/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.probabilitydistribution.iteration;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.position.AbstractIteratorPositionDelegate;
import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;

/**
 *
 * @author billing
 */
public class MinimumProbabilityPositionIterator<E> implements PositionIterator<E> {

    private final PositionIterator<ProbabilityDistribution<E>> source;

    public MinimumProbabilityPositionIterator(PositionIterator<ProbabilityDistribution<E>> source) {
        this.source = source;
    }

    public boolean hasPrevious() {
        return source.hasPrevious();
    }

    public IteratorPosition<E> previous() {
        return new MinPosition<E>(source.previous());
    }

    public IteratorPosition<E> getPosition() {
        return new MinPosition<E>(source.getPosition());
    }

    @Override
    public PositionIterator<E> clone() {
        return new MaximumProbabilityPositionIterator<E>(source.clone());
    }

    public Iterator<IteratorPosition<E>> iterator() {
        return this;
    }

    public boolean hasNext() {
        return source.hasNext();
    }

    public IteratorPosition<E> next() {
        return new MinPosition<E>(source.next());
    }

    public void remove() {
        source.remove();
    }

    private static class MinPosition<E> extends AbstractIteratorPositionDelegate<E, ProbabilityDistribution<E>> {

        private final E element;

        public MinPosition(IteratorPosition<ProbabilityDistribution<E>> pos) {
            super(pos);
            this.element = pos.element().min().next().element();
        }

        public IteratorPosition<E> getPrevious() {
            return new MinPosition<E>(sourcePosition().getPrevious());
        }

        public IteratorPosition<E> getNext() {
            return new MinPosition<E>(sourcePosition().getNext());
        }

        public E element() {
            return element;
        }
    }
}
