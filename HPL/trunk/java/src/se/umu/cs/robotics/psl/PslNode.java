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
import java.util.Locale;

import se.umu.cs.robotics.psl.exceptions.ChildAllreadyExcists;
import se.umu.cs.robotics.psl.exceptions.HypothesisLengthLimit;

/**
 * A node in a SLearning hypothesis, representing a single data element with a
 * parent (post hypothesis) and a number of children (previous hypothesis). An
 * {@link PslNode} with a null parent is the last element in the sequence.
 * 
 * @author billing
 * 
 */
public class PslNode<E> extends AbstractHypothesis<E> {

	private static NumberFormat format = NumberFormat.getInstance(Locale.US);

	/**
	 * The number of LHS conditions
	 */
	private final int length;

	/**
	 * Parent (less specific hypothesis)
	 */
	private Hypothesis<E> parent = null;

	private double hits = 1;
	private double misses = 0;

	/**
	 * This counter is used by hypotheses when comparing time since last update.
	 * Each time a hypothesis is updated is receives a new updateId based on
	 * this count.
	 */
	private static long updateCount = 0;
	private long updateId;

	public PslNode(final Hypothesis<E> parent, final E element) throws HypothesisLengthLimit {
		super(element);
		this.parent = parent;
		this.length = parent.length() + 1;
		update();

		if (parent.hasChild(element)) {
			throw new ChildAllreadyExcists(parent, element);
		} else if (getLibrary().hypothesisMaxLength() < length) {
			throw new HypothesisLengthLimit(this);
		} else {
			parent.setChild(this);
		}
	}

	public PslNode(final E element) {
		super(element);
		this.length = 0;
		update();
	}

	public long getUpdateId() {
		return updateId;
	}

	/**
	 * Compares two doubles and computes a comparison score (-1 to 1). 0% diff
	 * -> 1 score 10% diff -> 0 score 50% diff -> -0.67 score 100% diff -> -0.82
	 * score
	 * 
	 * @param data 0 <= x <= 1
	 * @param reference 0 <= x <= 1
	 * @return comparison score
	 */
	public static double compareElement(final Double data, final Double reference) {
		double diff = Math.abs(((Double) data) - ((Double) reference));
		return 2d / (1d + diff * 10) - 1d;

		// old way
		// scoreHitt()+2*scoreMiss()*diff;
	}

	public Hypothesis<E> getRoot() {
		if (parent == null)
			return this;
		return parent.getRoot();
	}

	public E getTarget() {
		return getRoot().getElement();
	}

	public int length() {
		return length;
	}

	public String shortName() {
		return "H" + getId() + "#" + format.format(getConfidence()) + ": " + length();
	}

	public Hypothesis<E> getParent() {
		return parent;
	}

	private void update() {
		updateId = updateCount++;
	}

	public void hit() {
		update();
		hits++;
	}

	public void miss() {
		update();
		misses++;
	}

	public double getConfidence() {
		// return MathTools.sum(performance) / performance.getCapacity();
		// return hits;
		return hits / (hits + misses);
		// BetaDistribution b = new BetaDistribution(misses + 0.1, hits + 0.1);
		// return b.cumulative(random.nextDouble());
	}

	public Library<E> getLibrary() {
		return parent.getLibrary();
	}

	public boolean lhsEquals(final Hypothesis<E> h) {
		if (parent == null)
			return h.getParent() == null;
		else if (h.length() == length && getElement().equals(h.getElement())) {
			return parent.lhsEquals(h.getParent());
		}
		return false;
	}

}
