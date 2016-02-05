/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.probabilitydistribution;

import java.util.ArrayList;
import java.util.Iterator;
import se.umu.cs.robotics.statespace.StateDimension;
import se.umu.cs.robotics.statespace.StateSpace;
import se.umu.cs.robotics.utils.StringTools;

/**
 *
 * @author billing
 */
public class ArraySpaceDistribution<E> extends AbstractSpaceDistribution<E> {

    private final ArrayList<ArrayDistribution<E>> distributions;

    public ArraySpaceDistribution(StateSpace<E> space) {
        super(space);
        this.distributions = new ArrayList<ArrayDistribution<E>>(space.size());
        for (int d=0; d<space.size(); d++){
            this.distributions.add(new ArrayDistribution<E>(space.getDimension(d)));
        }
    }

    /**
     * Creates a new ArraySpaceDistribution from the source distributions.
     *
     * @param sourceDistributions
     */
    public ArraySpaceDistribution(SpaceDistribution<E> sourceDistributions) {
        super(sourceDistributions.stateSpace());
        final StateSpace<E> space = sourceDistributions.stateSpace();
        this.distributions = new ArrayList<ArrayDistribution<E>>(space.size());
        for (int d=0; d<space.size(); d++){
            ArrayDistribution<E> dim = new ArrayDistribution<E>(space.getDimension(d));
            dim.add(sourceDistributions.getDimension(d), 1d);
            this.distributions.add(dim);
        }
    }

    public ProbabilityDistribution<E> getDimension(int dim) {
        return distributions.get(dim);
    }

    public ProbabilityDistribution<E> getDimension(StateDimension<E> dim) {
        return getDimension(stateSpace().getDimensionIndex(dim));
    }

    @Override
    public String toString() {
        return "(" + StringTools.join(distributions, ";") + ")";
    }

    /**
     * Adding (non-proportional) each dimension in pdspace to corresponding dimension in this space,
     * using the given value.
     */
    public void add(SpaceDistribution<E> pdspace, double value) {
        for (int i=0; i<stateSpace().size(); i++) {
            distributions.get(i).add(pdspace.getDimension(i),value);
        }
    }

    /**
     * Adding each dimension in pdspace to corresponding dimension in this space,
     * using the given value. The addidion is proportional, meaning that the a
     * value of 1 will produce a true 50/50 combination of the two spaces.
     *
     * @param pdspace
     * @param value
     */
    public void addProportional(SpaceDistribution<E> pdspace, double value) {
        for (int i=0; i<stateSpace().size(); i++) {
            distributions.get(i).addProportional(pdspace.getDimension(i),value);
        }
    }

    public Iterator<ArrayDistribution<E>> dimensions() {
        return distributions.iterator();
    }
}
