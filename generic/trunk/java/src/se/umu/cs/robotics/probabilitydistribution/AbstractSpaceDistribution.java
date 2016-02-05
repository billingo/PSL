/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.probabilitydistribution;

import se.umu.cs.robotics.statespace.StateSpace;

/**
 *
 * @author billing
 */
public abstract class AbstractSpaceDistribution<E> implements SpaceDistribution<E> {

    private final StateSpace<E> space;

    public AbstractSpaceDistribution(StateSpace<E> space) {
        this.space = space;
    }

    @Override
    public StateSpace<E> stateSpace() {
        return space;
    }

    @Override
    public double intersection(SpaceDistribution<E> pd) {
        return space.comparator().intersection(this, pd);
    }

    @Override
    public boolean isUniform() {
        for (int d = 0; d < space.size(); d++) {
            final ProbabilityDistribution<E> dimension = getDimension(d);
            if (dimension != null && !dimension.isUniform()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append('(');
        boolean first = true;
        for (int dim = 0; dim < space.size(); dim++) {
            if (first) {
                first = false;
            } else {
                s.append(';');
            }
            final ProbabilityDistribution<E> dist = getDimension(dim);
            if (dist == null || dist.isUniform()) {
                s.append("-");
            } else {
                s.append(dist);
            }
        }
        s.append(')');
        return s.toString();
    }
}
