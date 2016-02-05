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
public class MaximumProbabilityPositionIterator<E> implements PositionIterator<E> {

    private final PositionIterator<ProbabilityDistribution<E>> source;

    public MaximumProbabilityPositionIterator(PositionIterator<ProbabilityDistribution<E>> source) {
        this.source = source;
    }

    public boolean hasPrevious() {
        return source.hasPrevious();
    }

    public IteratorPosition<E> previous() {
        return new MaxPosition<E>(source.previous());
    }

    public IteratorPosition<E> getPosition() {
        return new MaxPosition<E>(source.getPosition());
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
        return new MaxPosition<E>(source.next());
    }

    public void remove() {
        source.remove();
    }

    private static class MaxPosition<E> extends AbstractIteratorPositionDelegate<E, ProbabilityDistribution<E>> {

        private final E element;

        public MaxPosition(IteratorPosition<ProbabilityDistribution<E>> pos) {
            super(pos);
            this.element = pos.element().max().next().element();
        }

        public IteratorPosition<E> getPrevious() {
            return new MaxPosition<E>(sourcePosition().getPrevious());
        }

        public IteratorPosition<E> getNext() {
            return new MaxPosition<E>(sourcePosition().getNext());
        }

        public E element() {
            return element;
        }
    }
}
