/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package se.umu.cs.robotics.probabilitydistribution.iteration;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.iteration.position.ElementIterator;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;

/**
 *
 * @author billing
 */
public class MinimumProbabilityIterator<E> implements IterableIterator<E> {

    private final Iterator<ProbabilityDistribution<E>> source;

    public MinimumProbabilityIterator(Iterator<ProbabilityDistribution<E>> source) {
        this.source = source;
    }

    public Iterator<E> iterator() {
        return this;
    }

    public boolean hasNext() {
        return source.hasNext();
    }

    public E next() {
        return source.next().min().next().element();
    }

    public void remove() {
        source.remove();
    }

}

