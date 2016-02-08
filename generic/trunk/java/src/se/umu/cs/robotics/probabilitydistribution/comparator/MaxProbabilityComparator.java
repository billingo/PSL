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

package se.umu.cs.robotics.probabilitydistribution.comparator;

import java.util.ArrayList;
import java.util.Iterator;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class MaxProbabilityComparator<E> implements SpaceDistributionComparator<E> {

    public double compare(SpaceDistribution<E> pd1, SpaceDistribution<E> pd2) {
        double eqCount = 0;
        double dimensions = 0;
        final Iterator<? extends ProbabilityDistribution<E>> i1 = pd1.dimensions();
        final Iterator<? extends ProbabilityDistribution<E>> i2 = pd2.dimensions();
        while (i1.hasNext() && i2.hasNext()) {
            final ProbabilityDistribution<E> d1 = i1.next();
            final ProbabilityDistribution<E> d2 = i2.next();
            if (d1 != null && d2 != null) {
                dimensions++;
                IterableIterator<FuzzyItem<E>> max1 = d1.max();
                IterableIterator<FuzzyItem<E>> max2 = d2.max();
                ArrayList<E> max = new ArrayList<E>(1);
                for (FuzzyItem<E> m : max1) {
                    max.add(m.element());
                }
                for (FuzzyItem<E> m : max2) {
                    if (max.contains(m.element())) {
                        eqCount += 1/max.size();
                    }
                }
            }
        }
        return dimensions > 0 ? eqCount / dimensions : 0;
    }
}
