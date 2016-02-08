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

import java.util.Iterator;
import java.util.concurrent.locks.Lock;

import se.umu.cs.robotics.collections.LinkedBuffer;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.collections.ObjectBuffer;
import se.umu.cs.robotics.collections.ObjectBuffer.BufferDirection;
import se.umu.cs.robotics.psl.Psl.StandardTrainer;
import se.umu.cs.robotics.psl.Psl.TrainingReference;
import se.umu.cs.robotics.psl.comparison.HypothesisSelector;
import se.umu.cs.robotics.psl.comparison.HypothesisSelector.Selection;
import se.umu.cs.robotics.psl.exceptions.HypothesisLengthLimit;
import se.umu.cs.robotics.psl.listener.EmptyLearningListener;
import se.umu.cs.robotics.psl.listener.LearningListener;
import se.umu.cs.robotics.psl.listener.PerformanceListener;

public class Predictor<E> {
	protected Library<E> library;
	protected Psl<E> learner;
	protected LinkedBuffer<E> buffer;

	protected E lastPrediction = null;
	protected Hypothesis<E> lastHypothesis = null;

	public Predictor(final Library<E> library) {
		this(library, 50);
	}

	public Predictor(final Library<E> library, final int bufferLengh) {
		this.library = library;
		this.learner = new Psl<E>(library);
		this.buffer = new LinkedBuffer<E>(BufferDirection.BACKWARD, bufferLengh);

		this.addListener(new EmptyLearningListener<E>() {

			@Override
			public void hypothesisCreated(final TrainingReference<E> ref, final Hypothesis<E> h) {
				lastHypothesis = h;
				lastPrediction = h.getTarget();
			}
		});
	}

	public HypothesisSelector<E> getSelector() {
		return learner.getSelector();
	}

	public void setSelector(final HypothesisSelector<E> selector) {
		learner.setSelector(selector);
	}

	/**
	 * Creates a prediction based on passed events. The selected hypothesis can
	 * be retrieved using lastHypothesis.
	 * 
	 * @return the next predicted element
	 */
	public E predict() {
		HypothesisList<E> match = predictions();
		Selection<E> selection = learner.getSelector().newSelection();
		selection.addAll(match);
		return setPrediction(selection.select());
	}

	public E setPrediction(final Hypothesis<E> hypothesis) {
		lastHypothesis = hypothesis;
		lastPrediction = hypothesis == null ? null : hypothesis.getTarget();
		return lastPrediction;
	}

	/**
	 * @return all hypotheses with an lhs matching the current buffer
	 */
	public HypothesisList<E> predictions() {
		return library.match(buffer.iterator());
	}

	/**
	 * @return all hypotheses with an rhs matching the last element in buffer,
	 *         and an lhs matching previous elements in buffer.
	 */
	public HypothesisList<E> currents() {
		Iterator<E> i = buffer.iterator();
		E e = i.next();
		HypothesisList<E> match = library.match(i);
		return match.getRhsSubset(e);
	}

	/**
	 * Confirms the last prediction.
	 */
	public void reward() {
		if (lastPrediction != null) {
			buffer.add(lastPrediction);
			lastHypothesis.hit();
		}
	}

	/**
	 * Confirms the last prediction and grows last hypothesis.
	 * 
	 * @return
	 */
	public void enforce() {
		reward();
		if (lastPrediction != null) {
			E el = null;
			int i = lastHypothesis.length();
			for (E e : buffer) {
				if (i < -1) {
					break;
				} else {
					el = e;
					i--;
				}
			}
			try {
				lastHypothesis = new PslNode<E>(lastHypothesis, el);
			} catch (HypothesisLengthLimit ex) {
				System.err.println(ex);
			}
		}
	}

	/**
	 * Corrects the last prediction.
	 * 
	 * @param o the correct element
	 */
	public PredictionTrainer<E> teach(final E o) {
		if (lastHypothesis != null)
			lastHypothesis.miss();
		PredictionTrainer<E> trainer = new PredictionTrainer<E>(learner, new LinkedPositionIterator<E>(buffer.iterator()), o);
		trainer.run();
		buffer.add(o);
		return trainer;
	}

	public void addPerformanceListener(final PerformanceListener<E> listener) {
		learner.addPerformanceListener(listener);
	}

	public void removePerformanceListener(final PerformanceListener<E> listener) {
		learner.removePerformanceListener(listener);
	}

	public void clearPerformanceListeners() {
		learner.clearPerformanceListeners();
	}

	public void addListener(final LearningListener<E> listener) {
		learner.addListener(listener);
	}

	public void removeListener(final LearningListener<E> listener) {
		learner.removeListener(listener);
	}

	public void clearListeners() {
		learner.clearListeners();
	}

	public static class PredictionTrainer<E> extends StandardTrainer<E> {

		private final E targetElement;
		private Hypothesis<E> trainedHypothesis;

		public PredictionTrainer(final Psl<E> learner, final LinkedPositionIterator<E> data, final E targetElement) {
			super(learner, data);
			this.targetElement = targetElement;
		}

		@Override
		public void run() {
			Lock lock = getLibrary().writeLock();
			lock.lock();
			try {
				trainedHypothesis = teach(getIterator(), targetElement);
			} finally {
				lock.unlock();
			}
		}

		public Hypothesis<E> getTrainedHypothesis() {
			return trainedHypothesis;
		}

	}

	/**
	 * Adds given element to buffer without updating the library.
	 * 
	 * @param o
	 */
	public void feed(final E o) {
		buffer.add(o);
	}

	/**
	 * Reset the predictor buffer.
	 */
	public void reset() {
		buffer.clear();
		lastHypothesis = null;
		lastPrediction = null;
	}

	/**
	 * @return the element buffer of this predictor, i.e., the sequence of
	 *         passed events.
	 */
	public ObjectBuffer<E> getBuffer() {
		return buffer;
	}

	public Psl<E> getLearner() {
		return learner;
	}

	public Library<E> getLibrary() {
		return library;
	}

	/**
	 * @return an estimate of the probability for a correct prediction (in the
	 *         range 0 to 1)
	 */
	public double predictionQuality() {
		if (lastHypothesis == null)
			return 0;
		else
			return lastHypothesis.getConfidence();
	}

	public E lastElement() {
		if (buffer.isEmpty()) {
			return null;
		} else {
			return buffer.getFirst();
		}
	}

	public Hypothesis<E> lastHypothesis() {
		return lastHypothesis;
	}

	@Override
	public Predictor<E> clone() {
		Predictor<E> predictor = new Predictor<E>(library, buffer.getCapacity());
		for (E e : buffer) {
			predictor.buffer.addLast(e);
		}
		return predictor;
	}

	/**
	 * @return true if there are no elements in the predictor's buffer, false
	 *         otherwise.
	 */
	public boolean isEmpty() {
		return buffer.isEmpty();
	}

}
