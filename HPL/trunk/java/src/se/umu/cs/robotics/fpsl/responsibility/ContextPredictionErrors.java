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

package se.umu.cs.robotics.fpsl.responsibility;

import java.util.Iterator;
import java.util.List;
import se.umu.cs.robotics.fpsl.FContext;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.fpsl.selection.FHypothesisMatch;
import se.umu.cs.robotics.fpsl.selection.FHypothesisSelection;
import se.umu.cs.robotics.fpsl.selection.MaxPredictionSelection;
import se.umu.cs.robotics.fpsl.selection.MaxProductMatch;
import se.umu.cs.robotics.fpsl.selection.MaxProductSelection;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 * @author Erik Billing <erik.billing@his.se>
 */
public class ContextPredictionErrors<E> {
    private final double[] errors;
    private final double[] confidences;
    private final FLibrary<E> library;
    
    public ContextPredictionErrors(FLibrary<E> library, FHypothesisSelection<E> selection, SpaceDistribution<E> correctDistribution) {
        this.errors = new double[library.getContextCount()];
        this.confidences = new double[library.getContextCount()];
        this.library = library;
        if (selection != null) {
            for (int c = 0; c < errors.length; c++) {
                FContext context = library.getContext(c);
                List<FHypothesisMatch<E>> targetHypotheses = selection.getTargetHypotheses(context);
                FHypothesisMatch<E> targetMatch = ((MaxProductSelection<E>)selection).selectMatch(targetHypotheses);
                SpaceDistribution<E> predictedDistribution = targetMatch == null ? null : targetMatch.getHypothesis().getTarget();

                double sum = 0;
                int dimensions = 0;
                Iterator<? extends ProbabilityDistribution<E>> dims = correctDistribution.dimensions();
                while (dims.hasNext() && predictedDistribution != null) {
                    ProbabilityDistribution<E> dist = dims.next();
                    if (dist != null) {
                        ProbabilityDistribution<E> pDist = predictedDistribution.getDimension(dist.getDimension());
                        if (pDist != null) {
                            double distance = 1d-dist.intersection(pDist);
                            sum += distance;
                            dimensions++;
                        }
                    }
                }
                errors[c] = dimensions == 0 ? 0 : sum / dimensions;
                confidences[c] = targetMatch == null ? 0 : ((MaxProductMatch)targetMatch).getConfidence();
            }
        }
    }

    public double getError(int i) {
        return errors[i];
    }
    
    public double getConfidence(int i) {
        return confidences[i];
    }
    
    public void setError(int i, double error) {
        errors[i] = error;
    }
    
    public int size() {
        return errors.length;
    }
    
    public FContext getContext(int i) {
        return library.getContext(i);
    }
}
