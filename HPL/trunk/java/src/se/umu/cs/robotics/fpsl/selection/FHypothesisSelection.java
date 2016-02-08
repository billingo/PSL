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

import java.util.ArrayList;
import java.util.List;
import se.umu.cs.robotics.fpsl.FContext;
import se.umu.cs.robotics.fpsl.FHypothesis;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public interface FHypothesisSelection<E> {

    /**
     * @return rhs distribution produced by combining all selected hypotheses
     */
    SpaceDistribution<E> getTarget();
    
    SpaceDistribution<E> getTarget(FContext context);
    
    List<FHypothesisMatch<E>> getTargetHypotheses();
    
    List <FHypothesisMatch<E>> getTargetHypotheses(FContext context);
            
    
    MatchList<E> getMatches();
    
    List<FLhsMatch<E>> getMatches(int length);
    
    int size();

    /**
     * Updates the confidence values of all hypotheses in the selection.
     *
     * @param targetDistribution is compared with the rhs of each hypothesis to compute hit/miss values
     */
    ArrayList<FHypothesis<E>> updateConfidences(SpaceDistribution<E> targetDistribution);
    
    ArrayList<FHypothesis<E>> updateConfidences(SpaceDistribution<E> targetDistribution, FContext context);

    /**
     * Compares targetDistribution with the distribution returned from getTarget and decides if it is good enought.
     * @param targetDistribution the descired distribution
     * @return true if good enough, false otherwise
     */
    boolean isCorrect(SpaceDistribution<E> targetDistribution);

}
