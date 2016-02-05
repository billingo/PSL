/*
 *  Copyright (C) 2010 Erik Billing <billing@cs.umu.se>
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
package se.umu.cs.robotics.statespace;

import java.util.ArrayList;
import java.util.Iterator;
import se.umu.cs.robotics.probabilitydistribution.GenericSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SingleStateSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.comparator.DimensionComparator;
import se.umu.cs.robotics.statespace.comparator.MinDimensionComparator;
import se.umu.cs.robotics.statespace.comparator.ProductDimensionComparator;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public abstract class AbstractStateSpace<E> implements StateSpace<E> {

    private final DimensionComparator comparator;

    public AbstractStateSpace() {
        this.comparator = new MinDimensionComparator();
    }

    public AbstractStateSpace(DimensionComparator comparator) {
        this.comparator = comparator;
    }

    public SpaceDistribution<E> newDistribution() {
        ArrayList<E> dimensions = new ArrayList<E>(size());
        for (int i=0; i<size(); i++) {
            dimensions.add(getDimension(i).defaultState());
        }
        return new SingleStateSpaceDistribution<E>(this,(E[])dimensions.toArray());
    }

    public SpaceDistribution<E> newDistribution(int... stateId) {
        ArrayList<E> dimensions = new ArrayList<E>(stateId.length);
        for (int i=0; i<stateId.length; i++) {
            dimensions.add(getDimension(i).getState(stateId[i]));
        }
        return new SingleStateSpaceDistribution<E>(this,(E[])dimensions.toArray());
    }

    public SpaceDistribution<E> newDistribution(ProbabilityDistribution<E>... dimensions) {
        Iterator<StateDimension<E>> i = iterator();
        for (ProbabilityDistribution<E> d : dimensions) {
            if (d.getDimension() != i.next()) {
                throw new IllegalArgumentException("Specified distributions must match space dimensions");
            }
        }
        return new GenericSpaceDistribution<E>(this,dimensions);
    }
    
    public DimensionComparator comparator() {
        return comparator;
    }

}
