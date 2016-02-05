
package se.umu.cs.robotics.probabilitydistribution.iteration;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;

/**
 *
 * @author billing
 */
public class MaximumProbabilityIterator<E> implements IterableIterator<E> {

    private final Iterator<ProbabilityDistribution<E>> source;

    public MaximumProbabilityIterator(Iterator<ProbabilityDistribution<E>> source) {
        this.source = source;
    }

    public Iterator<E> iterator() {
        return this;
    }

    public boolean hasNext() {
        return source.hasNext();
    }

    public E next() {
        return source.next().max().next().element();
    }

    public void remove() {
        source.remove();
    }

}
