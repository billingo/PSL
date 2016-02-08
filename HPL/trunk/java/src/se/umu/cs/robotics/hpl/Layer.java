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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;

import se.umu.cs.robotics.utils.ArrayTools;
import se.umu.cs.robotics.collections.LinkedBuffer;
import se.umu.cs.robotics.collections.ObjectBuffer;
import se.umu.cs.robotics.collections.ObjectBufferListener;
import se.umu.cs.robotics.collections.ObjectBuffer.BufferDirection;
import se.umu.cs.robotics.histogram.HashHistogram;
import se.umu.cs.robotics.histogram.Histogram;
import se.umu.cs.robotics.hpl.Context.ContextMatch;
import se.umu.cs.robotics.psl.Hypothesis;
import se.umu.cs.robotics.psl.Library;
import se.umu.cs.robotics.psl.comparison.DefaultHypothesisSelector;
import se.umu.cs.robotics.psl.comparison.HypothesisSelector;

/**
 * A PSL Context layer with action type A and state type E.
 * 
 * @author billing
 * 
 * @param <A>
 * @param <E>
 */
public class Layer<A, E> {
	public static boolean PARENT_ACTIVATION = true;

	private static Random random = new Random();

	private final ArrayList<Context<A, E>> contexts = new ArrayList<Context<A, E>>();
	private final ObjectBuffer<Step<A, E>> steps;
	private final HashHistogram<Step<A, E>> activation;

	private final LinkedHashMap<A, Library<E>> libraries;

	public Layer(final A action, final Library<E> libraries, final int size, final int contextLength) {
		this(new LibraryMap<A, E>(action, libraries), size, contextLength);
	}

	/**
	 * @param libraries a hash map associating each library with an action
	 * @param size the number of contexts in this layer
	 * @param contextLength the temporal length of the activation context
	 */
	public Layer(final LinkedHashMap<A, Library<E>> libraries, final int size, final int contextLength) {
		this.libraries = libraries;
		for (int i = 0; i < size; i++) {
			Context<A, E> context = new HistogramContext<A, E>(libraries);
			contexts.add(context);
		}
		initResponsibilities();
		initRandomContextWeights(libraries);
		activation = new HashHistogram<Step<A, E>>();
		steps = new LinkedBuffer<Step<A, E>>(BufferDirection.BACKWARD, contextLength);
		steps.addListener(new ObjectBufferListener<Step<A, E>>() {

			@Override
			public void capacityChanged(final int capacity) {}

			private void activate(Step<A, E> s, final double d) {
				do {
					double m = PARENT_ACTIVATION && s.hasHypothesis() ? s.getHypothesis().length() : 1;
					m = 1; // Do not use proportional activation
					activation.put(s, activation.get(s) + d * m);
					s = s.getParent();
				} while (PARENT_ACTIVATION && s.hasHypothesis());
			}

			@Override
			public void elementAdded(final Step<A, E> e) {
				activate(e, 1d);
			}

			@Override
			public void elementLost(final Step<A, E> e) {
				activate(e, -1d);
			}
		});
	}

	private void initResponsibilities() {
		double i = 1;
		for (Context<A, E> c : contexts) {
			// c.setResponsibility(1 / ++i);
			c.setResponsibility(1d / contexts.size());
		}
	}

	private void initRandomContextWeights(final LinkedHashMap<A, Library<E>> libraries) {
		for (Entry<A, Library<E>> e : libraries.entrySet()) {
			Library<E> library = e.getValue();
			library.readLock().lock();
			try {
				for (Hypothesis<E> h : library) {
					if (h.length() > 0) {
						Step<A, E> key = new Step<A, E>(e.getKey(), h);
						for (Context<A, E> c : contexts) {
							c.add(key, 1d);
						}
					}
				}
			} finally {
				library.readLock().unlock();
			}
		}
	}

	public void step(final Step<A, E> step) {
		if (step.hasHypothesis()) {
			steps.add(step);
		}
	}

	public Context.ContextMatch<A, E> matchContext(final Step<A, E> step) {
		return new Context.ContextMatch<A, E>(libraries.get(step.getAction()), step, steps, activation);
	}

	/**
	 * Updates the responsibility signal for each context to best match the
	 * current situation. Specifically, responsibility signals are updated using
	 * Bayes Rule (Eq. 1 from Haruno 2003.) in proportion to their prediction
	 * error.
	 * 
	 * @param step the current step (before added to steps).
	 */
	public double[] updateResponsibilities(final ContextMatch<A, E> match) {
		final Step<A, E> step = match.getStep();

		double[] dw = new double[contexts.size()];
		double sum = 0;

		if (step.hasHypothesis()) {
			E e = step.getTarget();

			for (int i = 0; i < contexts.size(); i++) {
				Context<A, E> c = contexts.get(i);

				dw[i] = c.deltaResponsibility(match);
				sum += c.getResponsibility() + dw[i];
			}
			for (int i = 0; i < contexts.size(); i++) {
				Context<A, E> c = contexts.get(i);
				// c.setResponsibility(0.01 + 0.99 * c.getResponsibility() *
				// dw[i] / sum);
				double resp = c.getResponsibility();
				double diff = (dw[i] + resp) / sum;
				c.setResponsibility(diff);
			}
		}
		return dw;
	}

	public void updatePriors(final ContextMatch<A, E> match) {
		final Step<A, E> step = match.getStep();
		final Context<A, E> context = getActiveContext();
		if (context != null && step.hasHypothesis()) {
			context.putPriors(match);
		}
	}

	public double getPrior(final Step<A, E> step) {
		Context<A, E> context = getActiveContext();
		return context.getPrior(step);
	}

	public double[] getPriorError(final ContextMatch<A, E> match) {
		final Step<A, E> step = match.getStep();
		double[] priors = new double[contexts.size()];
		if (step.hasHypothesis()) {
			E e = step.getTarget();

			Context<A, E> activeContext = getActiveContext();
			for (int i = 0; i < contexts.size(); i++) {
				Context<A, E> c = contexts.get(i);
				if (c == activeContext) {
					/*
					 * All inactive contexts has a prior error = 0
					 */
					Histogram<E> p = c.getPriors(step.getAction(), match.getMatch());
					double r = c.getResponsibility();
					priors[i] = r * (1 - p.get(e, true));
				}
			}
		}
		return priors;
	}

	/**
	 * Updates the context profile weights according to the passed sequence of
	 * steps, in proportion to their respective responsibilities. Contexts with
	 * high responsibility learns more.
	 * 
	 * @see Haruno 2003, Eq. 2.
	 */
	public void updateWeights(final Step<A, E> step) {
		getActiveContext().updateWeights(activation);
	}

	public Library<E> getLibrary(final A action) {
		return libraries.get(action);
	}

	public double[] getResponsibilities() {
		double[] responsibilities = new double[contexts.size()];
		for (int i = 0; i < responsibilities.length; i++) {
			responsibilities[i] = contexts.get(i).getResponsibility();
		}
		return responsibilities;
	}

	public ArrayList<Context<A, E>> getContexts() {
		return contexts;
	}

	/**
	 * The context with highest responsibility is active.
	 * 
	 * @return the context with highest responsibility
	 */
	public Context<A, E> getActiveContext() {
		Context<A, E> context = null;
		double responsibility = -1;
		for (Context<A, E> c : contexts) {
			if (c.getResponsibility() > responsibility) {
				context = c;
				responsibility = c.getResponsibility();
			}
		}
		return context;
	}

	public double[][] getContextVectors() {
		double[][] acts = new double[contexts.size()][];
		for (int i = 0; i < acts.length; i++) {
			acts[i] = contexts.get(i).getVector();
		}
		return acts;
	}

	public double[][] getContextPriors() {
		double[][] acts = new double[contexts.size()][];
		for (int i = 0; i < acts.length; i++) {
			acts[i] = contexts.get(i).getPriors();
		}
		return acts;
	}

	public HashHistogram<Step<A, E>> getActivation() {
		return activation;
	}

	public double[] getActivationVector() {
		ArrayList<Double> v = new ArrayList<Double>();
		for (Entry<A, Library<E>> e : libraries.entrySet()) {
			Library<E> library = e.getValue();
			Lock lock = library.readLock();
			lock.lock();
			try {
				for (Hypothesis<E> h : library) {
					if (h.length() > 0) {
						Step<A, E> key = new Step<A, E>(e.getKey(), h);
						v.add(activation.get(key, true));
					}
				}
			} finally {
				lock.unlock();
			}
		}
		return ArrayTools.toDoubleArray(v);
	}

	public void resetActivation() {
		steps.clear();
		activation.clear();
		initResponsibilities();
	}

	public int size() {
		return contexts.size();
	}

	public int contextLength() {
		return steps.getCapacity();
	}

	@Override
	public String toString() {
		return "ContextLayer with " + contexts.size() + " contexts";
	}

	private static class LibraryMap<A, E> extends LinkedHashMap<A, Library<E>> {
		LibraryMap(final A action, final Library<E> library) {
			super();
			put(action, library);
		}
	}

	public HypothesisSelector<E> getSelector(final A action) {
		return new LayerHypothesisSelector(action);
	}

	private class LayerHypothesisSelector implements HypothesisSelector<E> {

		private final A action;

		private LayerHypothesisSelector(final A action) {
			super();
			this.action = action;
		}

		public Selection<E> newSelection() {
			return new LayerHypothesisSelection();
		}

		public Selection<E> newTeachingSelection() {
			return new LayerHypothesisSelection();
		}

		private class LayerHypothesisSelection implements Selection<E> {
			private Step<A, E> selected;

			@Override
			public Hypothesis<E> select() {
				return selected == null ? null : selected.getHypothesis();
			}

			@Override
			public void add(final Hypothesis<E> h) {
				Step<A, E> s = new Step<A, E>(action, h);
				int c = selected == null ? -1 : compare(s);
				if (c < 0 || (c == 0 && random.nextBoolean())) {
					selected = s;
				}
			}

			@Override
			public void addAll(final List<Hypothesis<E>> hypotheses) {
				for (Hypothesis<E> h : hypotheses) {
					add(h);
				}
			}

			int compare(final Step<A, E> s) {
				int strengthcomp = compareStrength(s);
				if (strengthcomp == 0) {
					long t = selected.getHypothesis().getUpdateId() - s.getHypothesis().getUpdateId();
					return t < 0 ? -1 : t > 0 ? 1 : 0;
				} else {
					return strengthcomp;
				}
			}

			int compareStrength(final Step<A, E> s) {
				Hypothesis<E> hMe = selected.getHypothesis();
				Hypothesis<E> hNew = s.getHypothesis();
				int lencomp = DefaultHypothesisSelector.compareLength(hMe, hNew);
				if (lencomp == 0) {
					double myBelief = hMe.getConfidence() * getPrior(selected);
					double newBelief = hNew.getConfidence() * getPrior(s);
					double conf = myBelief - newBelief;
					return conf > 0 ? 1 : conf < 0 ? -1 : 0;
				} else {
					return lencomp;
				}
			}
		}

	}
}
