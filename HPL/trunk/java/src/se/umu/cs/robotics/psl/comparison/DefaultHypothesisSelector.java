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

import java.util.List;
import java.util.Random;

import se.umu.cs.robotics.psl.Hypothesis;

/**
 * Default selector used by PSL is no other selector is specified.
 * 
 * @author billing
 * 
 * @param <E> element type
 */
public class DefaultHypothesisSelector<E> implements HypothesisSelector<E> {

	private static Random random = new Random();

	public DefaultSelection<E> newSelection() {
		return new DefaultSelection<E>();
	}

	public Selection<E> newTeachingSelection() {
		return new DefaultSelection<E>();
	}

	public static <E> int compare(final Hypothesis<E> h1, final Hypothesis<E> h2) {
		int s = compareStrength(h1, h2);
		if (s == 0) {
			long t = h1.getUpdateId() - h2.getUpdateId();
			return t < 0 ? -1 : t > 0 ? 1 : 0;
		} else {
			return s;
		}
	}

	public static <E> int compareLength(final Hypothesis<E> h1, final Hypothesis<E> h2) {
		if (h1 == null && h2 == null) {
			return 0;
		} else if (h1 == null) {
			return -h2.length();
		} else if (h2 == null) {
			return h1.length();
		} else {
			return h1.length() - h2.length();
		}
	}

	public static <E> int compareStrength(final Hypothesis<E> h1, final Hypothesis<E> h2) {
		int len = compareLength(h1, h2);
		if (len == 0) {
			double conf = h1.getConfidence() - h2.getConfidence();
			return conf > 0 ? 1 : conf < 0 ? -1 : 0;
		} else {
			return len;
		}
	}

	public static class DefaultSelection<E> implements Selection<E> {
		private Hypothesis<E> selected;

		@Override
		public Hypothesis<E> select() {
			return selected;
		}

		@Override
		public void add(final Hypothesis<E> h) {
			int c = compare(selected, h);
			if (c < 0 || (c == 0 && random.nextBoolean())) {
				selected = h;
			}
		}

		@Override
		public void addAll(final List<Hypothesis<E>> hypotheses) {
			for (Hypothesis<E> h : hypotheses) {
				add(h);
			}
		}
	}
}
