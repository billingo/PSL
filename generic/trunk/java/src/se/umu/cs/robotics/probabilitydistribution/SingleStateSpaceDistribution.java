/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.probabilitydistribution;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.ArrayIterator;
import se.umu.cs.robotics.statespace.StateDimension;
import se.umu.cs.robotics.statespace.StateSpace;
import se.umu.cs.robotics.utils.StringTools;

/**
 *
 * @author billing
 */
public class SingleStateSpaceDistribution<E> extends AbstractSpaceDistribution<E> {

    private final E[] dimensions;

    public SingleStateSpaceDistribution(StateSpace<E> space, E... dimensions) {
        super(space);
        this.dimensions = dimensions;
    }

    public ProbabilityDistribution<E> getDimension(int dim) {
        if (dimensions[dim] == null) {
            return null;
        } else {
            return new SingleStateDistribution<E>(stateSpace().getDimension(dim), dimensions[dim]);
        }
    }

    public E getState(int dim) {
        return dimensions[dim];
    }

    public ProbabilityDistribution<E> getDimension(StateDimension<E> dim) {
        return new SingleStateDistribution<E>(dim, dimensions[stateSpace().getDimensionIndex(dim)]);
    }

    @Override
    public String toString() {
        return "(" + StringTools.join(dimensions, ";") + ")";
    }

    public Iterator<ProbabilityDistribution<E>> dimensions() {
        final ArrayIterator<E> i = new ArrayIterator<E>(dimensions);
        final Iterator<StateDimension<E>> dims = stateSpace().iterator();
        return new Iterator<ProbabilityDistribution<E>>() {

            public boolean hasNext() {
                return i.hasNext();
            }

            public ProbabilityDistribution<E> next() {
                return new SingleStateDistribution<E>(dims.next(), i.next());
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }
}
