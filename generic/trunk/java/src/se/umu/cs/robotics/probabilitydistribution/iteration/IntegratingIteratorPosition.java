/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.probabilitydistribution.iteration;

import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.probabilitydistribution.ArraySpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.StateSpace;

/**
 *
 * @author billing
 */
public class IntegratingIteratorPosition<E> implements IteratorPosition<SpaceDistribution<E>> {

    private SpaceDistribution<E> dists;
    private IntegratingIteratorPosition<E> previous;
    private IntegratingIteratorPosition<E> next;
    private IteratorPosition<SpaceDistribution<E>> startPosition;
    private IteratorPosition<SpaceDistribution<E>> endPosition;
    private final double integrationGradient;
    private final double integrationRange;
    private int integrationRangeUsed;

    public IntegratingIteratorPosition(final IteratorPosition<SpaceDistribution<E>> pos, double integrationGradient) {
        this.startPosition = pos;
        this.endPosition = pos;
        this.integrationRange = 1;
        this.integrationGradient = integrationGradient;
        this.dists = pos.element();
    }

    public IntegratingIteratorPosition(final IteratorPosition<SpaceDistribution<E>> startPosition, double integrateNext, double integrationGradient) {
        this.startPosition = startPosition;
        this.integrationRange = integrateNext;
        this.integrationGradient = integrationGradient;
        integrateNext(integrateNext);
    }

    public IntegratingIteratorPosition(double integratePrevious, final IteratorPosition<SpaceDistribution<E>> endPosition, double integrationGradient) {
        this.endPosition = endPosition;
        this.integrationRange = integratePrevious;
        this.integrationGradient = integrationGradient;
        integratePrevious(integratePrevious);
    }

    private IntegratingIteratorPosition(final IntegratingIteratorPosition<E> previous, double integrateNext) {
        this.previous = previous;
        this.startPosition = previous.endPosition.getNext();
        this.integrationRange = integrateNext;
        this.integrationGradient = previous.integrationGradient;
        integrateNext(integrateNext);
    }

    private IntegratingIteratorPosition(double integratePrevious, final IntegratingIteratorPosition<E> next) {
        this.next = next;
        this.endPosition = next.startPosition.getPrevious();
        this.integrationRange = integratePrevious;
        this.integrationGradient = next.integrationGradient;
        integratePrevious(integratePrevious);
    }

    public StateSpace<E> stateSpace() {
        return dists.stateSpace();
    }

    private void integrateNext(double limit) {
        if (limit < 2) {
            this.dists = startPosition.element();
            this.endPosition = startPosition;
            this.integrationRangeUsed = 1;
        } else {
            this.integrationRangeUsed = 0;
            IteratorPosition<SpaceDistribution<E>> pos = startPosition;
            SpaceDistribution<E> e = pos.element();
            ArraySpaceDistribution<E> spd = new ArraySpaceDistribution<E>(e.stateSpace());
            while (integrationRangeUsed < limit) {
                spd.add(e, 1);
                integrationRangeUsed++;
                if (pos.hasNext()) {
                    pos = pos.getNext();
                    e = pos.element();
                } else {
                    break;
                }
            }
            this.endPosition = pos;
            this.dists = spd;
        }
    }

    private void integratePrevious(double limit) {
        if (limit < 2) {
            this.dists = endPosition.element();
            this.startPosition = endPosition;
            this.integrationRangeUsed = 1;
        } else {
            this.integrationRangeUsed = 0;
            IteratorPosition<SpaceDistribution<E>> pos = endPosition;
            SpaceDistribution<E> e = pos.element();
            ArraySpaceDistribution<E> spd = new ArraySpaceDistribution<E>(e.stateSpace());
            while (integrationRangeUsed <= limit - 1) {
                spd.add(e, 1);
                integrationRangeUsed++;
                if (pos.hasPrevious()) {
                    pos = pos.getPrevious();
                    e = pos.element();
                } else {
                    break;
                }
            }
            this.startPosition = pos;
            this.dists = spd;
        }
    }

    public IntegratingIteratorPosition<E> getPrevious() {
        if (previous == null) {
            previous = new IntegratingIteratorPosition<E>(IntegratingIterator.stepIntegrationTime(integrationRange, integrationGradient), this);
        }
        return previous;
    }

    public IntegratingIteratorPosition<E> getNext() {
        if (next == null) {
            next = new IntegratingIteratorPosition<E>(this, IntegratingIterator.stepIntegrationTime(integrationRange, integrationGradient));
        }
        return next;
    }

    public SpaceDistribution<E> element() {
        return dists;
    }

    public boolean hasNext() {
        return next != null || endPosition.hasNext();
    }

    public boolean hasPrevious() {
        return previous != null || startPosition.hasPrevious();
    }

    public double getExponent() {
        return integrationGradient;
    }

    @Override
    public String toString() {
        return "Int" + integrationRangeUsed + dists;
    }
}
