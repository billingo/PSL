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

import java.util.Iterator;

import se.umu.cs.robotics.psl.Hypothesis;
import se.umu.cs.robotics.psl.HypothesisList;
import se.umu.cs.robotics.psl.Predictor;
import se.umu.cs.robotics.psl.comparison.HypothesisSelector;

public class StepIterator<A, E> implements Iterator<Step<A, E>> {

	private final Iterator<E> source;
	private final Predictor<E> predictor;
	private final A action;
	private double lastError = 0;

	public StepIterator(final Predictor<E> predictor, final Iterator<E> source, final A action) {
		this.predictor = predictor;
		this.source = source;
		this.action = action;
	}

	@Override
	public boolean hasNext() {
		return source.hasNext();
	}

	@Override
	public Step<A, E> next() {
		E data = source.next();
		HypothesisSelector<E> selector = predictor.getSelector();
		HypothesisList<E> predictions = predictor.predictions();
		HypothesisList<E> correct = predictions.getRhsSubset(data);

		Hypothesis<E> best = predictions.getBest(selector);
		if (best != null && data.equals(best.getTarget())) {
			lastError = 0;
		} else {
			lastError = 1;
		}

		Step<A, E> step = new Step<A, E>(action, correct.getBest(selector));
		predictor.feed(data);
		return step;
	}

	@Override
	public void remove() {
		source.remove();
	}

	public double lastError() {
		return lastError;
	}

}
