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
package se.umu.cs.robotics.psl.rl;

import java.util.ArrayList;
import java.util.HashMap;

import se.umu.cs.robotics.psl.Library;
import se.umu.cs.robotics.psl.Predictor;
import se.umu.cs.robotics.psl.Hypothesis;

/**
 * Extends the standard predictor with reinforcement learning functionality.
 * 
 * @author billing
 * 
 * @param <E> element type
 */
public class ReinforcedPredictor<E> extends Predictor<E> {
	public static final boolean USE_ONLINE_PROPAGATION = false;

	private final HashMap<Hypothesis<E>, Reinforcement<E>> reinforcement = new HashMap<Hypothesis<E>, Reinforcement<E>>();

	private ReinforcementPropagator<E> propagator;

	private Reinforcement<E> pasedReinforcement;

	public ReinforcedPredictor(final Library<E> library) {
		super(library);
	}

	public ReinforcementPropagator<E> getPropagator() {
		return propagator;
	}

	public void setPropagator(final ReinforcementPropagator<E> propagator) {
		this.propagator = propagator;
	}

	/**
	 * Propagates information over all hypotheses.
	 */
	private synchronized void propagate() {
		ArrayList<Reinforcement<E>> rs = new ArrayList<Reinforcement<E>>();
		for (Reinforcement<E> r : this.reinforcement.values()) {
			rs.add(r);
		}
		for (Reinforcement<E> r : rs) {
			propagator.propagate(r);
		}
		// for (Reinforcement<E> r : this.reinforcement.values()) {
		// r.commit();
		// }
	}

	/**
	 * Propagates reward for one hypothesis.
	 * 
	 * @param h
	 */
	private void propagate(final Hypothesis<E> h) {
		if (h != null) {
			Reinforcement<E> r = get(h);
			propagator.propagate(r);

			Reinforcement<E> future = r;
			while (future != null) {
				Reinforcement<E> past = pasedReinforcement;
				while (past != null) {
					past.addFuture(future);
					past = get(past.getHypothesis().getParent());
				}
				future = get(future.getHypothesis().getParent());
			}
			pasedReinforcement = r;
		}
		// propagate();
	}

	@Override
	public E predict() {
		E prediction = super.predict();
		// if (lastHypothesis != null) {
		// Reinforcement<E> r = lastReinforcement();
		// if (r.enforcementNeeded()) {
		// r.enforce(buffer.iterator());
		// }
		// }
		return prediction;
	}

	public Reinforcement<E> get(final Hypothesis<E> h) {
		if (h == null)
			return null;
		Reinforcement<E> r = reinforcement.get(h);
		if (r == null && h.getParent() != null) {
			r = get(h.getParent()).clone(h);
			reinforcement.put(h, r);
		}
		if (r == null) {
			r = new Reinforcement<E>(h);
			reinforcement.put(h, r);
		}
		return r;
	}

	@Override
	public PredictionTrainer<E> teach(final E o) {
		PredictionTrainer<E> trainer = super.teach(o);
		Hypothesis<E> trainedHypothesis = trainer.getTrainedHypothesis();
		if (USE_ONLINE_PROPAGATION)
			propagate(trainedHypothesis);
		return trainer;
	}

	/**
	 * Confirms the last prediction.
	 */
	@Override
	public void reward() {
		super.reward();
		if (USE_ONLINE_PROPAGATION)
			propagate(lastHypothesis);
	}

	@Override
	public void enforce() {
		super.enforce();
		if (USE_ONLINE_PROPAGATION)
			propagate(lastHypothesis);
	}

	public Reinforcement<E> lastReinforcement() {
		return lastHypothesis == null ? null : get(lastHypothesis);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Reinforcement<E> r : reinforcement.values()) {
			sb.append(r);
			sb.append('\n');
		}
		return sb.toString();
	}

	public Reinforcement<E> getPastReinforcement() {
		return pasedReinforcement;
	}
}
