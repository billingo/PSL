/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright (C) 2007 - 2015  Erik Billing, <http://www.his.se/erikb>
School of Informatics, University of Skovde, Sweden

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
\*-------------------------------------------------------------------*/

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
