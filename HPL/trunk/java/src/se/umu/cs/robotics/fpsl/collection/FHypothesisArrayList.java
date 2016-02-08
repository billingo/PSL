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

package se.umu.cs.robotics.fpsl.collection;

import se.umu.cs.robotics.collections.fuzzy.ConcreteFuzzyItem;
import se.umu.cs.robotics.collections.fuzzy.FuzzyArrayList;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.fpsl.FHypothesis;
import se.umu.cs.robotics.probabilitydistribution.ArraySpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.StateSpace;

/**
 *
 * @author billing
 */
public class FHypothesisArrayList<E> extends FuzzyArrayList<FHypothesis<E>> implements FHypothesisSet<E> {

    private final StateSpace stateSpace;

    public FHypothesisArrayList(StateSpace space) {
        this.stateSpace = space;
    }

    public SpaceDistribution<E> getTarget() {
        ArraySpaceDistribution<E> dists = new ArraySpaceDistribution<E>(stateSpace);

        for (FuzzyItem<FHypothesis<E>> item : this) {
            final FHypothesis<E> e = item.element();
            final double impact = item.value();
            if (impact > 0) {
                dists.add(e.getTarget(), impact);
            }
        }

        return dists;
    }

    /**
     * Returns a subset with an RHS match to spd larger or equal to threshiold.
     * @param spd
     * @param threshold
     * @return
     */
    public FHypothesisSet<E> rhsSubset(SpaceDistribution<E> spd, FuzzyFilter<FHypothesis<E>> filter) {
        FHypothesisArrayList<E> subSet = new FHypothesisArrayList<E>(stateSpace);
        for (FuzzyItem<FHypothesis<E>> item : this) {
            FHypothesis<E> e = item.element();
            double intersection = spd.intersection(e.getTarget());
            if (filter.include(e, intersection) > 0) {
                subSet.put(new ConcreteFuzzyItem<FHypothesis<E>>(e, intersection * item.value()));
            }
        }
        return subSet;
    }

    public FHypothesisSet<E> rhsSubset(SpaceDistribution<E> spd) {
        return rhsSubset(spd, new DummyFilter<FHypothesis<E>>());
    }
}
