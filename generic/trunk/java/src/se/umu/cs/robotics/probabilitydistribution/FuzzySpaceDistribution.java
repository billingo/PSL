/*
 *  Copyright (C) 2011 Erik Billing <billing@cs.umu.se>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.umu.cs.robotics.probabilitydistribution;

import java.util.ArrayList;
import java.util.Iterator;
import se.umu.cs.robotics.statespace.FloatDimension;
import se.umu.cs.robotics.statespace.StateDimension;
import se.umu.cs.robotics.statespace.StateSpace;
import se.umu.cs.robotics.statespace.comparator.LinearDoubleComparator;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class FuzzySpaceDistribution<E> extends AbstractSpaceDistribution<E> {

    private final ArrayList<FuzzyDistribution<E>> distributions;

    public FuzzySpaceDistribution(StateSpace<E> space) {
        super(space);
        distributions = new ArrayList<FuzzyDistribution<E>>(space.size());
        for (int d = 0; d < space.size(); d++) {
            distributions.add(new FuzzyDistribution<E>(space.getDimension(d)));
        }
    }

    public FuzzySpaceDistribution(StateSpace<E> space, ArrayList<FuzzyDistribution<E>> distributions) {
        super(space);
        this.distributions = distributions;
    }

    public FuzzySpaceDistribution(SpaceDistribution<E> distribution) {
        super(distribution.stateSpace());
        StateSpace<E> space = distribution.stateSpace();
        this.distributions = new ArrayList<FuzzyDistribution<E>>(space.size());
        for (int d = 0; d < space.size(); d++) {
            ProbabilityDistribution<E> dist = distribution.getDimension(d);
            if (dist == null) {
                distributions.add(new FuzzyDistribution<E>(space.getDimension(d)));
            } else {
                distributions.add(new FuzzyDistribution<E>(dist));
            }
        }
    }

    public FuzzySpaceDistribution(SpaceDistribution<E> distribution, double tolerance) {
        super(distribution.stateSpace());
        StateSpace<E> space = distribution.stateSpace();
        this.distributions = new ArrayList<FuzzyDistribution<E>>(space.size());
        for (int d = 0; d < space.size(); d++) {
            StateDimension<E> dimension = space.getDimension(d);
            LinearDoubleComparator dimComparator = (LinearDoubleComparator) dimension.comparator();
            LinearDoubleComparator comparator = new LinearDoubleComparator(tolerance * dimComparator.getTolerance(), dimComparator.minTolerance());

            ProbabilityDistribution<E> dist = distribution.getDimension(d);
            if (dist == null) {
                distributions.add(new FuzzyDistribution<E>(space.getDimension(d), comparator));
            } else {
                distributions.add(new FuzzyDistribution<E>(dist, comparator));
            }
        }
    }

    public ProbabilityDistribution<E> getDimension(int dim) {
        return distributions.get(dim);
    }

    public ProbabilityDistribution<E> getDimension(StateDimension<E> dim) {
        return getDimension(stateSpace().getDimensionIndex(dim));
    }

    /**
     * Adding (non-proportional) each dimension in pdspace to corresponding dimension in this space,
     * using the given weight.
     */
    public void add(SpaceDistribution<E> pdspace, double weight) {
        for (int i = 0; i < stateSpace().size(); i++) {
            FuzzyDistribution<E> myDimension = distributions.get(i);
            ProbabilityDistribution<E> pdDimension = pdspace.getDimension(i);
            if (pdDimension != null) {
                if (myDimension == null) {
                    myDimension = new FuzzyDistribution<E>(stateSpace().getDimension(i));
                    distributions.set(i, myDimension);
                }
                myDimension.add(pdDimension, weight);
            }
        }
    }

    public Iterator<FuzzyDistribution<E>> dimensions() {
        return distributions.iterator();
    }

    public static FuzzySpaceDistribution<Double> newLinguisticValue(SpaceDistribution<Double> dist) {
        StateSpace<Double> space = dist.stateSpace();
        ArrayList<FuzzyDistribution<Double>> dimensions = new ArrayList<FuzzyDistribution<Double>>();
        final Iterator<? extends ProbabilityDistribution<Double>> i = dist.dimensions();
        while (i.hasNext()) {
            dimensions.add(FuzzyDistribution.newLinquisticValue(i.next()));
        }
        return new FuzzySpaceDistribution(space, dimensions);
    }
}
