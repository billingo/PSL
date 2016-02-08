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

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;

import se.umu.cs.robotics.psl.Hypothesis;
import se.umu.cs.robotics.psl.PslNode;
import se.umu.cs.robotics.psl.exceptions.HypothesisLengthLimit;

public class Reinforcement<E> {

	public static final double INITIAL_VALUE = 0;

	private static NumberFormat format = NumberFormat.getInstance(Locale.US);

	private final Hypothesis<E> state;
	private final HashSet<Reinforcement<E>> futures = new HashSet<Reinforcement<E>>();

	// private double reward;
	// private double punishment;
	private double value = INITIAL_VALUE;
	private final ExpectedChange change = ExpectedChange.Unknown;
	private boolean enforceWhenPossible = false;

	// private double dReward;
	// private double dPunishment;

	public Reinforcement() {
		state = null;
	}

	public Reinforcement(final Hypothesis<E> h) {
		state = h;
	}

	// public double getReward() {
	// return reward;
	// }
	//
	// public double getPunishment() {
	// return punishment;
	// }

	public double getValue() {
		return value;
	}

	public void updateValue(final double value) {
		// if (value > 0) {
		// if (change == ExpectedChange.Unknown) {
		// change = ExpectedChange.Positive;
		// } else if (change == ExpectedChange.Negative) {
		// enforceWhenPossible = true;
		// }
		// } else if (value < 0) {
		// if (change == ExpectedChange.Unknown) {
		// change = ExpectedChange.Negative;
		// } else if (change == ExpectedChange.Positive) {
		// enforceWhenPossible = true;
		// }
		// }
		this.value += value;
	}

	public boolean enforcementNeeded() {
		return enforceWhenPossible;
	}

	public void enforcementNeeded(final boolean needed) {
		enforceWhenPossible = needed;
	}

	public void enforce(final Iterator<E> data) {
		int i = 0;
		try {
			while (i < state.length()) {
				data.next();
				i++;
			}
			E next = data.next();
			if (state.getChild(next) == null) {
				new PslNode<E>(state, next);
				enforceWhenPossible = false;
			}
		} catch (NoSuchElementException e) {
			// Swallow
		} catch (HypothesisLengthLimit e) {
			System.err.println(e);
		}
	}

	/**
	 * Adds a possible future associated with this state. Used by the Bellman
	 * Eq. for propagation of rewards.
	 * 
	 * @param r
	 */
	public void addFuture(final Reinforcement<E> r) {
		futures.add(r);
	}

	public Set<Reinforcement<E>> getFutures() {
		return futures;
	}

	// public double betaSample() {
	// BetaDistribution b = new BetaDistribution(punishment + 1, reward + 1);
	// return b.cumulative(random.nextDouble());
	// }

	@Override
	public String toString() {
		// return "B" + format.format(reward) + "/" + format.format(punishment)
		// + "  " + state.toString();
		return "B" + format.format(value) + "  " + state.toString();
	}

	public Reinforcement<E> clone(final Hypothesis<E> h) {
		Reinforcement<E> r = new Reinforcement<E>(h);
		// r.reward = reward;
		// r.punishment = punishment;
		r.value = value;
		// r.dvalue = dvalue;
		return r;
	}

	public Hypothesis<E> getHypothesis() {
		return state;
	}

	private enum ExpectedChange {
		Unknown, Positive, Negative
	}
}
