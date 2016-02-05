/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.probabilitydistribution.iteration;

import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 * Iterates over the source iterator and returns probabiliy distributions, 
 * integrated over a number of course elemnts from the source. The integration
 * range depends on the distance from the initial position, such that
 * i = integrationGradient^|n| where n is the number of steps from the initial
 * source position and i is the integration range.
 *
 * @author billing
 */
public class IntegratingPositionIterator<E> implements PositionIterator<SpaceDistribution<E>> {

    private IntegratingIteratorPosition<E> pos;

    public IntegratingPositionIterator(IntegratingIteratorPosition<E> pos) {
        this.pos = pos;
    }

    public IntegratingPositionIterator(IteratorPosition<SpaceDistribution<E>> pos, double integrationGradient) {
        this.pos = new IntegratingIteratorPosition<E>(pos, integrationGradient);
    }

    public PositionIterator<SpaceDistribution<E>> iterator() {
        return this;
    }

    public boolean hasNext() {
        return pos.hasNext();
    }

    public IteratorPosition<SpaceDistribution<E>> next() {
        pos = pos.getNext();
        return pos;
    }

    public void remove() {
        throw new UnsupportedOperationException("Remove not supported.");
    }

    public boolean hasPrevious() {
        return pos.hasPrevious();
    }

    public IteratorPosition<SpaceDistribution<E>> previous() {
        pos = pos.getPrevious();
        return pos;
    }

    public IteratorPosition<SpaceDistribution<E>> getPosition() {
        return pos;
    }

    @Override
    public PositionIterator<SpaceDistribution<E>> clone() {
        return new IntegratingPositionIterator<E>(pos);
    }

}
