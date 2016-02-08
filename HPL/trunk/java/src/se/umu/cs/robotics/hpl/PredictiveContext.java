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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;

import se.umu.cs.robotics.utils.ArrayTools;
import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.histogram.HashHistogram;
import se.umu.cs.robotics.histogram.Histogram;
import se.umu.cs.robotics.psl.Hypothesis;
import se.umu.cs.robotics.psl.HypothesisList;
import se.umu.cs.robotics.psl.Library;

public class PredictiveContext<A, E> implements Context<A, E> {
	private static final long serialVersionUID = 1L;

	public static final double LEARNING_RATE = 0.01;
	public static double BAYES_SCALING = 0.7;
	public static final double LEARNING_RATE_PRIORS = 0.2;

	private static Random random = new Random();

	private final LinkedHashMap<A, Library<E>> libraries;
	private final Histogram<Step<A, E>> relations = new HashHistogram<Step<A, E>>();
	private final HashMap<Step<A, E>, Double> priors = new HashMap<Step<A, E>, Double>();

	private double responsibility;

	public PredictiveContext(final LinkedHashMap<A, Library<E>> libraries) {
		this.libraries = libraries;
		initPriors(libraries);
	}

	public PredictiveContext(final LinkedHashMap<A, Library<E>> libraries, final PositionIterator<Step<A, E>> steg) {
		this(libraries);
		for (IteratorPosition<Step<A, E>> p : steg) {
			add(p.element(), 1d);
		}
	}

	private void initPriors(final LinkedHashMap<A, Library<E>> libraries) {
		for (Entry<A, Library<E>> e : libraries.entrySet()) {
			for (Hypothesis<E> h : e.getValue()) {
				Step<A, E> step = new Step<A, E>(e.getKey(), h);
				priors.put(step, 1d);
			}
		}
	}

	public double getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(final double d) {
		responsibility = d;
	}

	public void add(final A action, final Hypothesis<E> h, final double value) {
		add(new Step<A, E>(action, h), value);
	}

	public void add(final Step<A, E> steg, final Double value) {
		double w = relations.get(steg);
		relations.put(steg, w + value);
	}

	public void put(final Step<A, E> steg, final Double value) {
		relations.put(steg, value);
	}

	public double getPrior(final Step<A, E> step) {
		return priors.get(step);
	}

	public void putPrior(final Step<A, E> key, final double d) {
		priors.put(key, d);
	}

	/**
	 * Returns prior probabilities for possible future hypotheses.
	 * 
	 * @param action the current action
	 * @param steps the previous steps
	 * @return
	 */
	public HashHistogram<E> getPriors(final A action, final HypothesisList<E> match) {
		HashHistogram<E> priors = new HashHistogram<E>();
		HashMap<E, Hypothesis<E>> map = new HashMap<E, Hypothesis<E>>();
		for (Hypothesis<E> h : match) {
			E e = h.getTarget();
			Step<A, E> steg = new Step<A, E>(action, h);
			double w = getPrior(steg);
			Hypothesis<E> ph = map.get(e);
			if (ph == null || h.length() > ph.length() || (h.length() == ph.length() && h.getConfidence() > ph.getConfidence())) {
				priors.put(e, w);
				map.put(e, h);
			}
		}
		return priors;
	}

	public void putPriors(final ContextMatch<A, E> match) {
		A action = match.getStep().getAction();
		E target = match.getStep().getTarget();
		for (Hypothesis<E> h : match.getMatch()) {
			Step<A, E> step = new Step<A, E>(action, h);
			double newPrior = h.getTarget().equals(target) ? 1 : 0;
			double curPrior = getPrior(step);
			double dp = (newPrior - curPrior) * responsibility * LEARNING_RATE_PRIORS;
			// System.out.println(curPrior + ":" + dp);
			putPrior(step, curPrior + dp);
		}
	}

	public Double get(final Step<A, E> key) {
		return relations.get(key);
	}

	public Double get(final Step<A, E> key, final boolean normaliozed) {
		return relations.get(key, normaliozed);
	}

	public Library<E> getLibrary(final A action) {
		return libraries.get(action);
	}

	public double[] getVector() {
		ArrayList<Double> v = new ArrayList<Double>();
		for (Entry<A, Library<E>> e : libraries.entrySet()) {
			Library<E> library = e.getValue();
			Lock lock = library.readLock();
			lock.lock();
			try {
				for (Hypothesis<E> h : library.hypotheses()) {
					Step<A, E> key = new Step<A, E>(e.getKey(), h);
					v.add(get(key, true));
				}
			} finally {
				lock.unlock();
			}
		}
		return ArrayTools.toDoubleArray(v);
	}

	public double[] getPriors() {
		ArrayList<Double> v = new ArrayList<Double>();
		for (Entry<A, Library<E>> e : libraries.entrySet()) {
			Library<E> library = e.getValue();
			Lock lock = library.readLock();
			lock.lock();
			try {
				for (Hypothesis<E> h : library.hypotheses()) {
					Step<A, E> key = new Step<A, E>(e.getKey(), h);
					v.add(getPrior(key));
				}
			} finally {
				lock.unlock();
			}
		}
		return ArrayTools.toDoubleArray(v);
	}

	public double relation(final Histogram<Step<A, E>> c) {
		double r = 0;
		for (Step<A, E> steg : relations.keySet()) {
			double v = get(steg, true);
			double dv = c.get(steg, true);
			r += Math.min(v, dv);
		}
		return r;
	}

	/**
	 * Returns normalized confidence values for possible future states.
	 * 
	 * @param action the current action
	 * @param steps the previous steps
	 * @return
	 */
	protected HashMap<Hypothesis<E>, Double> confidences(final A action, final HypothesisList<E> match) {
		HashMap<Hypothesis<E>, Double> sel = new HashMap<Hypothesis<E>, Double>();
		double sum = 0;
		for (Hypothesis<E> h : match) {
			Step<A, E> steg = new Step<A, E>(action, h);
			double w = get(steg);
			sel.put(h, w);
			sum += w;
		}

		if (sum > 0) {
			for (Entry<Hypothesis<E>, Double> e : sel.entrySet()) {
				e.setValue(e.getValue() / sum);
			}
		}
		return sel;
	}

	/**
	 * Computes the confidence value for given step.
	 * 
	 * @see Haruno 2003, Eq. 1.
	 */
	public double deltaResponsibility(final ContextMatch<A, E> match) {
		double denominator = 2 * BAYES_SCALING * BAYES_SCALING;
		HashMap<Hypothesis<E>, Double> confs = confidences(match.getStep().getAction(), match.getMatch());
		Double conf = confs.get(match.getStep().getHypothesis());
		double distance = 1 - (conf == null ? 0 : conf);
		double dw = Math.exp(-(distance * distance) / denominator);
		return dw;
	}

	/**
	 * Modifies the hypotheses weights of the context such that it becomes
	 * closer to the specified context.
	 * 
	 * @param facit a correct context
	 */
	public void updateWeights(final Histogram<Step<A, E>> facit) {
		if (facit.size() > 0) {
			for (Entry<A, Library<E>> e : libraries.entrySet()) {
				Library<E> library = e.getValue();
				library.readLock().lock();
				try {
					for (Hypothesis<E> h : library) {
						if (h.length() > 0) {
							Step<A, E> key = new Step<A, E>(e.getKey(), h);
							Double myWeight = get(key, true);
							Double facitWeight = facit.get(key, true);
							double lr = responsibility * LEARNING_RATE;
							double dw = (facitWeight - myWeight) * lr * relations.getSum();
							add(key, dw);
						}
					}
				} finally {
					library.readLock().unlock();
				}
			}
		}
	}

	public void clear() {
		relations.clear();
	}

	@Override
	public int size() {
		return relations.size();
	}

	public Set<Step<A, E>> steps() {
		return relations.keySet();
	}

}
