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

import java.util.List;

import se.umu.cs.robotics.psl.PslNode;
import se.umu.cs.robotics.psl.Library;
import se.umu.cs.robotics.psl.Hypothesis;

public class RelatedLibrary<E> extends Library<E> {

	public RelatedLibrary() {
		super();
	}

	public RelatedLibrary(final int maxHypothesisLength) {
		super(maxHypothesisLength);
	}

	public void resetRelations() {
		for (Hypothesis<E> h : getRoots()) {
			((RelatedHypothesis<E>) h).clearRelations();
		}
	}

	/**
	 * TODO May not be in use anymore
	 * 
	 * @param hypothesis
	 * @param occuranceThreshold
	 * @return
	 */
	public double[] getRelationalSequence(final List<PslNode<E>> hypothesis, final int occuranceThreshold) {
		List<Hypothesis<E>> all = getHypothesesByOccurance(hypothesis, occuranceThreshold);
		double[] seq = new double[hypothesis.size()];
		if (seq.length == 0)
			return seq;
		int pos = 0;
		for (PslNode<E> h : hypothesis) {
			seq[pos] = all.indexOf(h);
			pos++;
		}
		return seq;
	}
}
