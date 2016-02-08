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


package se.umu.cs.robotics.psl;

import java.text.NumberFormat;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.LinkedReverseIterator;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.psl.comparison.DefaultHypothesisSelector;
import se.umu.cs.robotics.psl.comparison.HypothesisSelector;
import se.umu.cs.robotics.psl.comparison.HypothesisSelector.Selection;
import se.umu.cs.robotics.psl.exceptions.HypothesisLengthLimit;
import se.umu.cs.robotics.psl.exceptions.PslError;
import se.umu.cs.robotics.psl.listener.LearningListener;
import se.umu.cs.robotics.psl.listener.PerformanceListener;

public class Psl<E> {
	public static final boolean USE_COMPLETE_CONF_COMPUTATION = false;
	public static final double ROOT_CREATION_THROESHOLD = 0.85;

	protected Logger logger = Logger.getLogger("se.umu.robotics");
	public static NumberFormat format = NumberFormat.getInstance(Locale.US);

	protected Library<E> library;

	protected int sequenceBufferPos = 0;

	protected Thread trainingThread = null;
	protected StandardTrainer<E> trainingReference;
	protected HypothesisSelector<E> selector = new DefaultHypothesisSelector<E>();

	public Psl(final Library<E> library) {
		this.library = library;

		format.setMaximumIntegerDigits(1);
		format.setMaximumFractionDigits(2);
	}

	public synchronized TrainingReference<E> train(final PositionIterator<E> data) {
		if (trainingThread != null)
			throw new PslError("Allready running");
		trainingReference = new StandardTrainer<E>(this, data);
		trainingThread = new Thread(trainingReference);
		trainingThread.start();

		return trainingReference;
	}

	public HypothesisSelector<E> getSelector() {
		return selector;
	}

	public void setSelector(final HypothesisSelector<E> selector) {
		this.selector = selector;
	}

	public void stop() {
		trainingThread = null;
	}

	/**
	 * Growing specified root sequence
	 * 
	 * @param root
	 * @param data
	 * @param teachAt
	 * @return the new sequence created, if any
	 */
	protected PslNode<E> grow(final PslNode<E> root, final E[] data, final int teachAt) {

		PslNode<E> newSeq = null;
		try {
			/*
			 * Grow sequence
			 */
			if (teachAt < root.length()) {
				/*
				 * No previous data to grow on, ignore.
				 */
			} else {
				try {
					newSeq = new PslNode<E>(root, data[teachAt - root.length()]);
				} catch (HypothesisLengthLimit e) {
					logger.warning("Max sequence length reached!");
				}
			}
		} catch (PslError e) {
			logger.warning(e.getMessage());
		}
		return newSeq;
	}

	public Library<E> getLibrary() {
		return library;
	}

	public boolean isRunning() {
		return trainingThread != null;
	}

	public TrainingReference<E> getCurrentTrainingReference() {
		return trainingReference;
	}

	public abstract static class TrainingReference<E> {

		private final Psl<E> learner;

		public TrainingReference(final Psl<E> learner) {
			this.learner = learner;
		}

		/**
		 * Inform all listeners that training started.
		 */
		protected void trainingStarted() {
			for (LearningListener<E> listener : learner.listeners)
				listener.trainingStarted(this);
		}

		/**
		 * Inform all listeners that training finished.
		 */
		protected void trainingComplete() {
			for (LearningListener<E> listener : learner.listeners)
				listener.trainingComplete(this);
		}

		public Psl<E> getLearner() {
			return learner;
		}

		public Library<E> getLibrary() {
			return learner.getLibrary();
		}

		/**
		 * Locks the thread until training is complete.
		 */
		public void await() {
			try {
				if (learner.trainingThread != null)
					learner.trainingThread.join();
			} catch (InterruptedException e) {
				learner.logger.warning(e.toString());
			}
		}
	}

	public static class StandardTrainer<E> extends TrainingReference<E> implements Runnable {

		private final PositionIterator<E> data;

		public StandardTrainer(final Psl<E> learner, final PositionIterator<E> data) {
			super(learner);
			this.data = data;
		}

		/**
		 * Runs training and populates the library.
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			trainingStarted();
			Lock lock = getLibrary().writeLock();
			lock.lock();
			try {
				while (data.hasNext()) {
					if (super.learner.trainingThread == null)
						break;

					/*
					 * Try to predict current row based on previous data
					 */
					IteratorPosition<E> p = data.next();
					teach(new LinkedReverseIterator<E>(data.clone()), p.element());
				}
				super.learner.trainingThread = null;
				super.learner.library.sort();
			} finally {
				lock.unlock();
			}
			trainingComplete();
		}

		/**
		 * Returns the data source iterator. Use of this iterator may affect
		 * training!
		 * 
		 * @return source data iterator.
		 */
		public PositionIterator<E> getIterator() {
			return data;
		}

		/**
		 * Performs a prediction based on data and teaches library based on
		 * facit.
		 * 
		 * @param model the training data
		 * @param facit the correct prediction to be learned
		 * @return the winner hypothesis (new hypothesis if created)
		 */
		public Hypothesis<E> teach(final PositionIterator<E> i, final E facit) {
			if (!i.hasNext()) {
				trainingMiss(null);
				return null;
			}
			HypothesisList<E> match = super.learner.library.match(i.clone());
			IteratorPosition<E> p = i.next();
			Selection<E> selection = super.learner.selector.newTeachingSelection();
			selection.addAll(match);
			Hypothesis<E> selected = selection.select();
			Hypothesis<E> winner;
			if (selected == null) {
				selected = super.learner.library.getRoot(facit, true);
				winner = grow(p, selected);
				trainingMiss(null);
			} else if (facit.equals(selected.getTarget())) {
				trainingHit(selected);
				winner = selected;
			} else {
				trainingMiss(selected);
				HypothesisList<E> rhsSubset = match.getRhsSubset(facit);
				selected = rhsSubset.getBest();
				if (selected == null) {
					selected = super.learner.library.getRoot(facit, true);
				} else {
					try {
						for (int order = 0; order < selected.length(); order++) {
							p = i.next();
						}
					} catch (NoSuchElementException e) {
						return null;
					}
				}
				winner = grow(p, selected);
			}
			return winner;
		}

		private Hypothesis<E> grow(final IteratorPosition<E> p, final Hypothesis<E> selected) {
			try {
				Hypothesis<E> h = new PslNode<E>(selected, p.element());
				sequenceCreated(h);
				return h;
			} catch (HypothesisLengthLimit e) {
				return null;
			}
		}

		private void trainingHit(final Hypothesis<E> h) {
			if (h != null)
				h.hit();
			for (LearningListener<E> listener : super.learner.listeners)
				listener.hypothesisUpdated(this, h);
			for (PerformanceListener<E> listener : super.learner.performanceListeners) {
				listener.hit(h);
			}
		}

		private void trainingMiss(final Hypothesis<E> h) {
			if (h != null)
				h.miss();
			for (LearningListener<E> listener : super.learner.listeners)
				listener.hypothesisUpdated(this, h);
			for (PerformanceListener<E> listener : super.learner.performanceListeners) {
				listener.miss(h);
			}
		}

		/**
		 * Inform all listeners about sequence creation.
		 */
		private void sequenceCreated(final Hypothesis<E> h) {
			for (LearningListener<E> listener : super.learner.listeners)
				listener.hypothesisCreated(this, h);
		}

	}

	protected Set<LearningListener<E>> listeners = new LinkedHashSet<LearningListener<E>>();
	protected Set<PerformanceListener<E>> performanceListeners = new LinkedHashSet<PerformanceListener<E>>();

	public void addPerformanceListener(final PerformanceListener<E> listener) {
		performanceListeners.add(listener);
	}

	public void removePerformanceListener(final PerformanceListener<E> listener) {
		performanceListeners.remove(listener);
	}

	public void clearPerformanceListeners() {
		performanceListeners.clear();
	}

	/**
	 * Add a new learning listener
	 * 
	 * @param listener
	 */
	public void addListener(final LearningListener<E> listener) {
		listeners.add(listener);
	}

	public void removeListener(final LearningListener<E> listener) {
		listeners.remove(listener);
	}

	public void clearListeners() {
		listeners.clear();
	}
}