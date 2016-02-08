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

package se.umu.cs.robotics.fpsl.selection;

import se.umu.cs.robotics.fpsl.FLhs;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 * An Hypothesis Selector is used to create selections from the library.
 *
 * @author Erik Billing <billing@cs.umu.se>
 * @param <E> the element distribution type
 */
public interface FHypothesisSelector<E> {

    /**
     * @return library from which selections are made.
     */
    FLibrary<E> getLibrary();

    /**
     * Performs a selection based on the hypotheses left hand side.
     *
     * @param data an iterator that returns elements in reversed temporal order, used to match the lhs
     * @return a custom FHypothesisSelection
     */
    FHypothesisSelection<E> lhsSelect(final PositionIterator<SpaceDistribution<E>> data);

    FHypothesisSelection<E> lhsSelect(FLhs<E> root, final PositionIterator<SpaceDistribution<E>> data);

    /**
     * A selection returned by the selection methods of the selector.
     *
     * @param <E> the element distribution type
     */
    
}
