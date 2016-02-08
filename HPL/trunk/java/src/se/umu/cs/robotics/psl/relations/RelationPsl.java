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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import se.umu.cs.robotics.iteration.position.LinkedPosition;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.iteration.position.LinkedReverseIterator;
import se.umu.cs.robotics.psl.HypothesisList;
import se.umu.cs.robotics.psl.Library;
import se.umu.cs.robotics.psl.Psl;
import se.umu.cs.robotics.psl.Hypothesis;

public class RelationPsl<E> extends Psl<E> {
	public RelationPsl(final Library<E> library) {
		super(library);
	}

	public class RelationTrainer extends TrainingReference<E> implements Runnable {

		private final Iterator<E> data;
		private final List<Double> trainingPerforamce = new ArrayList<Double>();

		RelationTrainer(final Psl<E> learner, final Iterator<E> data) {
			super(learner);
			this.data = data;
		}

		@Override
		public void run() {
			trainingStarted();
			LinkedPositionIterator<E> i = new LinkedPositionIterator<E>(data);
			Hypothesis<E> prediction = null;
			LinkedPosition<E> position = null;
			while (i.hasNext()) {
				if (trainingThread == null)
					break;

				if (position != null) {
					if (prediction != null && position.element().equals(prediction.getElement())) {
						trainingPerforamce.add(1d);
					} else {
						trainingPerforamce.add(0d);
					}
				}

				/*
				 * Try to predict next element
				 */
				position = i.next();
				HypothesisList<E> match = getLibrary().match(new LinkedReverseIterator<E>(i));
				prediction = match.getBest();
			}

			trainingThread = null;
			getLibrary().sort();
			trainingComplete();
		}

	}

}