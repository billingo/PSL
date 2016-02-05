/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package se.umu.cs.robotics.frequency;

import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.StateSpace;

/**
 *
 * @author billing
 */
public interface Frequency<E> extends IterableIterator<SpaceDistribution<E>> {

    StateSpace<E> stateSpace();

}
