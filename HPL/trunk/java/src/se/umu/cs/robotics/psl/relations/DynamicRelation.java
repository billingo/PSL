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


package se.umu.cs.robotics.psl.relations;

import java.util.concurrent.ConcurrentHashMap;

import se.umu.cs.robotics.psl.PslNode;

/**
 * The DynamicContext holds prior probabilities to all Hypothesises in a
 * library. All priors decays from its original values and must continually get
 * influence from the execution of other (or the same) sequence.
 * 
 * @author billing
 * 
 */
public class DynamicRelation<E> implements HypothesisRelation<E> {
	public final static double DEFAULT_PRIOR = 0.1;
	// public final static double PRIOR_ACTIVATION = 0.5;
	public final static double PRIOR_DECAY = 0.1;

	private final ConcurrentHashMap<PslNode<E>, Double> priors = new ConcurrentHashMap<PslNode<E>, Double>();

	@Override
	public double getPrior(final PslNode<E> hypothesis) {
		Double prior = priors.get(hypothesis);
		return prior == null ? DEFAULT_PRIOR : prior;
	}

	/**
	 * Inputs influence from the execution of a hypothesis.
	 * 
	 * @param h
	 * @param influence (0 < x <= 1)
	 */
	public void activate(final PslNode<E> h, final double influence) {
		Double prior = priors.get(h);
		double p = prior == null ? DEFAULT_PRIOR : prior;
		p += (1 - p) * influence;
		priors.put(h, p);
	}

	public void decayAll() {
		for (PslNode<E> h : priors.keySet()) {
			decay(h);
		}
	}

	private void decay(final PslNode<E> h) {
		Double prior = priors.get(h);
		double p = prior == null ? DEFAULT_PRIOR : prior;
		p -= p * PRIOR_DECAY;
		if (p < DEFAULT_PRIOR) {
			priors.remove(h);
		} else {
			priors.put(h, p);
		}
	}

	public void reset() {
		priors.clear();
	}

}
