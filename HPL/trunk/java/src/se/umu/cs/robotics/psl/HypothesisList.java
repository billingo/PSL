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

import java.util.LinkedList;

import se.umu.cs.robotics.psl.Hypothesis.HypothesisIterator;
import se.umu.cs.robotics.psl.comparison.DefaultHypothesisSelector;
import se.umu.cs.robotics.psl.comparison.HypothesisSelector;
import se.umu.cs.robotics.psl.comparison.HypothesisSelector.Selection;

public class HypothesisList<E> extends LinkedList<Hypothesis<E>> {
	private static final long serialVersionUID = 1L;

	public Hypothesis<E> getBest() {
		DefaultHypothesisSelector<E> selector = new DefaultHypothesisSelector<E>();
		return getBest(selector);
	}

	public Hypothesis<E> getBest(final HypothesisSelector<E> selector) {
		Selection<E> selection = selector.newSelection();
		selection.addAll(this);
		return selection.select();
	}

	public HypothesisIterator<E> iterRecursive() {
		return new HypothesisIterator<E>(this);
	}

	public HypothesisList<E> getRhsSubset(final E rhs) {
		HypothesisList<E> subset = new HypothesisList<E>();
		for (Hypothesis<E> h : this) {
			if (h.getTarget().equals(rhs)) {
				subset.add(h);
			}
		}
		return subset;
	}

}
