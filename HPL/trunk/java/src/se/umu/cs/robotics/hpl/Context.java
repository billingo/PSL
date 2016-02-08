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

import java.util.Set;

import se.umu.cs.robotics.collections.ObjectBuffer;
import se.umu.cs.robotics.histogram.Histogram;
import se.umu.cs.robotics.psl.HypothesisList;
import se.umu.cs.robotics.psl.Library;

public interface Context<A, E> {

	public double[] getVector();

	public Double get(Step<A, E> step);

	public Double get(final Step<A, E> key, final boolean normaliozed);

	public void put(Step<A, E> step, Double value);

	public void add(Step<A, E> step, Double value);

	public void clear();

	public double getResponsibility();

	public void setResponsibility(double v);

	/**
	 * Updates the responsibility signal based on specified step and a selection
	 * of hypotheses matching current history.
	 * 
	 * @param step
	 * @param match
	 * @return the responsibility difference or prediction error, depending on
	 *         the implementation
	 */

	public double deltaResponsibility(final ContextMatch<A, E> match);

	public double relation(Histogram<Step<A, E>> c);

	public int size();

	/**
	 * Modifies the hypotheses weights of the context such that it becomes
	 * closer to the specified context.
	 * 
	 * @param facit a correct context
	 */
	public void updateWeights(Histogram<Step<A, E>> act);

	/**
	 * Sets the prior probability (top-down) relation for specified step.
	 * 
	 * @param step
	 * @param prior
	 */
	public void putPrior(final Step<A, E> step, final double prior);

	/**
	 * Returns the prior probability (top-down) relation for specified step.
	 * 
	 * @param step
	 * @return
	 */
	public double getPrior(Step<A, E> step);

	public Histogram<E> getPriors(final A action, final HypothesisList<E> match);

	public double[] getPriors();

	public void putPriors(final ContextMatch<A, E> match);

	/**
	 * @return a set of all steps to which this context has relations.
	 */
	public Set<Step<A, E>> steps();

	public static class ContextMatch<A, E> {
		private final Library<E> library;
		private final Step<A, E> step;
		private final ObjectBuffer<Step<A, E>> steps;
		private HypothesisList<E> match;
		private final Histogram<Step<A, E>> act;

		public ContextMatch(final Library<E> library, final Step<A, E> step, final ObjectBuffer<Step<A, E>> steps, final Histogram<Step<A, E>> activation) {
			this.library = library;
			this.step = step;
			this.steps = steps;
			this.act = activation;
		}

		public Library<E> getLibrary() {
			return library;
		}

		public Step<A, E> getStep() {
			return step;
		}

		public Histogram<Step<A, E>> getActivation() {
			return act;
		}

		public HypothesisList<E> getMatch() {
			if (match == null) {
				match = library.match(new StepElementIterator<A, E>(steps.iterator()));
			}
			return match;
		}
	}
}
