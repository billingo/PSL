/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package se.umu.cs.robotics.probabilitydistribution.iteration;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 * @author billing
 */
public class DimensionIterator<E> implements IterableIterator<ProbabilityDistribution<E>> {

    private final Iterator<SpaceDistribution<E>> source;
    private final int dimension;

    public DimensionIterator(Iterator<SpaceDistribution<E>> source, int dimension) {
        this.source = source;
        this.dimension = dimension;
    }

    public Iterator<ProbabilityDistribution<E>> iterator() {
        return this;
    }

    public boolean hasNext() {
        return source.hasNext();
    }

    public ProbabilityDistribution<E> next() {
        return source.next().getDimension(dimension);
    }

    public void remove() {
        source.remove();
    }

}
