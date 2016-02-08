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


package se.umu.cs.robotics.psl.comparison;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.psl.Hypothesis;
import se.umu.cs.robotics.psl.Library;
import se.umu.cs.robotics.psl.Predictor;
import se.umu.cs.robotics.psl.Psl;
import se.umu.cs.robotics.psl.Psl.TrainingReference;
import se.umu.cs.robotics.psl.listener.EmptyLearningListener;
import se.umu.cs.robotics.psl.listener.LearningListener;
import se.umu.cs.robotics.psl.listener.PerformanceListener;

/**
 * A set of data comparison methods.
 * 
 * @author billing
 * 
 * @param <E>
 */
public class PslComparator<E> {

	private final Library<E> library;
	private final Psl<E> learner;
	protected Set<LearningListener<E>> listeners = new LinkedHashSet<LearningListener<E>>();

	private final HashMap<String, PslDataProfile<E>> vectors = new HashMap<String, PslDataProfile<E>>();

	public PslComparator() {
		this.library = new Library<E>(0);
		this.learner = new Psl<E>(library);

		this.learner.addListener(new EmptyLearningListener<E>() {

			@Override
			public void hypothesisCreated(final TrainingReference<E> learner, final Hypothesis<E> newSequence) {
				for (LearningListener<E> l : listeners) {
					l.hypothesisCreated(learner, newSequence);
				}
			}

			@Override
			public void trainingComplete(final TrainingReference<E> learner) {
			/*
			 * Called from the train-method.
			 */
			}

			@Override
			public void trainingStarted(final TrainingReference<E> learner) {
				for (LearningListener<E> l : listeners) {
					l.trainingStarted(learner);
				}
			}

		});
	}

	public Psl.TrainingReference<E> train(final String dataName, final LinkedPositionIterator<E> data) {
		final Psl.TrainingReference<E> trainRef = learner.train(data.clone());

		Thread occuranceUpdater = new Thread(new Runnable() {

			@Override
			public void run() {
				trainRef.await();
				vectors.put(dataName, new PslDataProfile<E>(library, data));
				for (LearningListener<E> l : listeners) {
					l.trainingComplete(trainRef);
				}
			}

		});
		occuranceUpdater.start();
		return trainRef;
	}

	/**
	 * Add a new SLearning listener
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

	public void addPerformanceListener(final PerformanceListener<E> l) {
		learner.addPerformanceListener(l);
	}

	public void removePerformanceListener(final PerformanceListener<E> l) {
		learner.removePerformanceListener(l);
	}

	public void clearPerformanceListeners() {
		learner.clearPerformanceListeners();
	}

	public PslDataProfile<E> getProfile(final String... names) throws PslProfileException {
		if (names.length == 1 && false) {
			return vectors.get(names[0]);
		} else {
			ArrayList<PslDataProfile<E>> list = new ArrayList<PslDataProfile<E>>();
			for (String name : names) {
				if (vectors.containsKey(name)) {
					list.add(vectors.get(name));
				}
			}
			return new PslDataProfile<E>(list);
		}
	}

	/**
	 * @param data
	 * @return the vector profile for specified data sequence
	 */
	public PslDataProfile<E> getProfile(final PositionIterator<E> data) {
		return new PslDataProfile<E>(library, data);
	}

	public Predictor<E> getPredictor() {
		return new Predictor<E>(library);
	}

	public Library<E> getLibrary() {
		return library;
	}

}
