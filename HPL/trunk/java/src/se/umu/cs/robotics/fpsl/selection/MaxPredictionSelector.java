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

import org.apache.log4j.Logger;
import se.umu.cs.robotics.fpsl.FLhs;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;


/**
 * The standard selector.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class MaxPredictionSelector<E> implements FHypothesisSelector<E> {

    static final Logger logger = Logger.getLogger(MaxPredictionSelector.class);
    private FLibrary<E> library;

    public MaxPredictionSelector(FLibrary<E> library) {
        this.library = library;
    }

    public FHypothesisSelection<E> lhsSelect(PositionIterator<SpaceDistribution<E>> data) {
        return new MaxPredictionSelection(library, data);
    }

    public FHypothesisSelection<E> lhsSelect(FLhs<E> root, PositionIterator<SpaceDistribution<E>> data) {
        return new MaxPredictionSelection(library, root, data);
    }

    public FLibrary<E> getLibrary() {
        return library;
    }

}
