/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.probabilitydistribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import se.umu.cs.robotics.statespace.StateDimension;
import se.umu.cs.robotics.statespace.StateSpace;
import se.umu.cs.robotics.utils.StringTools;

/**
 *
 * @author billing
 */
public class GaussianSpaceDistribution<E> extends AbstractSpaceDistribution<E> {

    private final ArrayList<GaussianDistribution<E>> distributions;

    public GaussianSpaceDistribution(StateSpace<E> space, GaussianDistribution<E>... distributions) {
        super(space);
        this.distributions = (ArrayList)Arrays.asList(distributions);
    }
    public GaussianSpaceDistribution(StateSpace<E> space, double[] means, double[] stds) {
        super(space);
        this.distributions = new ArrayList<GaussianDistribution<E>>(means.length);
        for (int i=0; i<means.length; i++) {
            distributions.add(new GaussianDistribution<E>(space.getDimension(i), means[i], stds[i]));
        }
    }

    public GaussianDistribution<E> getDimension(int dim) {
        return distributions.get(dim);
    }

    public GaussianDistribution<E> getDimension(StateDimension<E> dim) {
        return getDimension(stateSpace().getDimensionIndex(dim));
    }


    public String toString() {
        return "("+StringTools.join(distributions,";")+")";
    }

    public Iterator<GaussianDistribution<E>> dimensions() {
        return distributions.iterator();
    }
}
