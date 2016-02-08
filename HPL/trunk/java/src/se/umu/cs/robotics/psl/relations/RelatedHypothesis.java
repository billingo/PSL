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

import java.util.HashMap;

import se.umu.cs.robotics.psl.PslNode;
import se.umu.cs.robotics.psl.exceptions.HypothesisLengthLimit;

public class RelatedHypothesis<E> extends PslNode<E> {

	private HypothesisContext relations = new HypothesisContext();

	public RelatedHypothesis(final E element) {
		super(element);
	}

	public RelatedHypothesis(final PslNode<E> postSequence, final E element) throws HypothesisLengthLimit {
		super(postSequence, element);
	}

	public void addRelation(final PslNode<E> h) {
		if (h == null)
			return;
		relations.addCoOccurance(h);
		PslNode<E> parent = (PslNode<E>) getParent();
		if (parent != null && parent instanceof RelatedHypothesis)
			((RelatedHypothesis<E>) parent).addRelation(h);
	}

	public void influence(final DynamicRelation<E> context) {
		relations.influence(context);
	}

	public void clearRelations() {
		relations = new HypothesisContext();
	}

	public class HypothesisContext implements HypothesisRelation<E> {
		private final HashMap<PslNode<E>, Integer> coOccurances = new HashMap<PslNode<E>, Integer>();
		private final int totalOccuranceCount = 0;

		@Override
		public double getPrior(final PslNode<E> hypothesis) {
			Integer occurances = coOccurances.get(hypothesis);
			return (occurances == null || totalOccuranceCount == 0) ? 0d : ((double) occurances) / (double) totalOccuranceCount;
		}

		public void influence(final DynamicRelation<E> context) {
			// System.out.println("Context size: " + coOccurances.size());
			for (PslNode<E> h : coOccurances.keySet()) {
				context.activate(h, getPrior(h));
			}
		}

		public void addCoOccurance(final PslNode<E> h) {
			if (h == null)
				return;
			Integer v = coOccurances.get(h);
			coOccurances.put(h, v == null ? 1 : v + 1);
			addCoOccurance((PslNode<E>) h.getParent());
		}

	}
}
