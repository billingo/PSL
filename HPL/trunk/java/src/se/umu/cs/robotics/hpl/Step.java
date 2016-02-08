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

import se.umu.cs.robotics.psl.Hypothesis;

/**
 * A Step is a combination of an action A and an hypothesis, where the
 * hypothesis is to predict the consequences of taking action A.
 * 
 * @author billing
 * 
 * @param <A> action type
 * @param <E> hypothesis element type
 */
public class Step<A, E> {
	private final A a;
	private final Hypothesis<E> h;

	public Step(final A a, final Hypothesis<E> h) {
		this.a = a;
		this.h = h;
	}

	public A getAction() {
		return a;
	}

	public Hypothesis<E> getHypothesis() {
		return h;
	}

	public E getTarget() {
		return h == null ? null : h.getTarget();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final Object o) {
		if (o instanceof Step) {
			Step steg = (Step) o;
			return a.equals(steg.a) && ((h == null && steg.h == null) || h.equals(steg.h));
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hHash = h == null ? 100000 : h.hashCode();
		return a.hashCode() ^ hHash;
	}

	@Override
	public String toString() {
		return hashCode() + ":" + a + ":" + h;
	}

	public boolean hasHypothesis() {
		return h != null;
	}

	public Step<A, E> getParent() {
		return new Step<A, E>(a, h == null ? null : h.getParent());
	}

}
