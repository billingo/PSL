/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright 2007 - 2010 Erik Billing
Department of Computing Science, Umea University, Sweden,
(www.cs.umu.se/~billing/).

LICENSE:

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston,
MA 02111-1307, USA.
\*-------------------------------------------------------------------*/
package se.umu.cs.robotics.probabilitydistribution;

import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.statespace.StateDimension;

/**
 * The ProbabilityDistribution implements a PD over all states in a
 * {@link StateDimension}.
 * 
 * @author billing
 * 
 * @param <E>
 */
public interface ProbabilityDistribution<E> {

    StateDimension<E> getDimension();

    double getProbability(E state);

    double getProbability(int pos);

    double intersection(ProbabilityDistribution<E> pd);

    // public double[] getArray();
    /**
     * @return one of the items with lowest probability
     */
    IterableIterator<FuzzyItem<E>> min();

    /**
     * @return one of the items with highest probability
     */
    IterableIterator<FuzzyItem<E>> max();

    IterableIterator<FuzzyItem<E>> nonZeroStates();

    boolean isUniform();
}
