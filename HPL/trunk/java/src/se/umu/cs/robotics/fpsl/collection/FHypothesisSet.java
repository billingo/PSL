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

import se.umu.cs.robotics.collections.fuzzy.FuzzySet;
import se.umu.cs.robotics.fpsl.FHypothesis;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 * @author billing
 */
public interface FHypothesisSet<E> extends FuzzySet<FHypothesis<E>> {

    /**
     * @return the target distribution produced by all Hypotheses
     */
    public SpaceDistribution getTarget();

    /**
     * @param spd
     * @return a subset with an RHS intersection to spd
     */
    public FHypothesisSet<E> rhsSubset(SpaceDistribution<E> spd);

    /**
     * @param spd
     * @param filter (may be null to indicate no filtering)
     * @return a subset with an RHS intersection to spd larger or equal to threshiold
     */
    public FHypothesisSet<E> rhsSubset(SpaceDistribution<E> spd, FuzzyFilter<FHypothesis<E>> filter);

}
