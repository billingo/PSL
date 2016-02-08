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
package se.umu.cs.robotics.hpl;

import java.util.HashMap;
import java.util.LinkedHashMap;

import se.umu.cs.robotics.psl.Hypothesis;
import se.umu.cs.robotics.psl.Library;

public class HistogramContext<A, E> extends PredictiveContext<A, E> {

	public static double BAYES_SCALING = 0.7;

	public HistogramContext(final LinkedHashMap<A, Library<E>> libraries) {
		super(libraries);
	}

	// @Override
	// public double deltaResponsibility(final ContextMatch<A, E> match) {
	// Step<A, E> step = match.getStep();
	// HashHistogram<E> priors = getPriors(step.getAction(), match.getMatch());
	//
	// double conf = relation(match.getActivation());
	// double denominator = 2 * BAYES_SCALING * BAYES_SCALING;
	//
	// double distance = 1 - conf * (0.01 +
	// priors.get(step.getHypothesis().getTarget(), true));
	// // double distance = 1 - conf;
	// double dw = Math.exp(-(distance * distance) / denominator);
	// return dw;
	// }
	/**
	 * Computes the confidence value for given step.
	 * 
	 * @see Haruno 2003, Eq. 1.
	 */
	@Override
	public double deltaResponsibility(final ContextMatch<A, E> match) {
		double denominator = 2 * BAYES_SCALING * BAYES_SCALING;

		HashMap<Hypothesis<E>, Double> confs = super.confidences(match.getStep().getAction(), match.getMatch());
		Double conf = confs.get(match.getStep().getHypothesis());
		double distance = 1 - (conf == null ? 0 : conf);
		double dw = Math.exp(-(distance * distance) / denominator);
		return dw;
	}

}
