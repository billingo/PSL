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

package se.umu.cs.robotics.collections;

import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.iteration.position.LinkedPosition;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Sequence comparison utility class.
 * 
 * Implements methods for intelligent sequence comparison.
 * 
 * @author billing
 * 
 * @param <E> element type of sequences to compare
 */
public class SequenceComparator<E> {

	private final double temporalCost;
	private final double costThreshold;

	/**
	 * Instantiates the sequence comparator.
	 * 
	 * @param temporalCost distance between two neighboring elements
	 * @param temporalCostThreshold the maximum cost to compute between two elements
	 */
	public SequenceComparator(final double temporalCost, final double temporalCostThreshold) {
		this.temporalCost = temporalCost;
		this.costThreshold = temporalCostThreshold;
	}

	/**
	 * Compares the two sequences s1 and s2 and computing a distance measure
	 * between them. The method implements a minimum distance measurement using
	 * A* search. The method aims to compare the carecaristics of the sequences
	 * even if the sequences does not contain exactly the same number of
	 * elements.
	 * 
	 * Example: Let s1 = [1, 2, 3, 4, 5] and that s2 = [1, 1.5, 2, 2.5, 3, 5]
	 * The temporalCost is set to 0.1 and costThreshold set to 1. The sequences
	 * have similar carecaristics but different number of elements. The
	 * comparison will go as follows:
	 * 
	 * The distance between s1[0] and s2[0] = 0; distance between s1[1] and
	 * s2[1] = 0.5; distance between s1[1] and s2[2] = 0.1 due to the temporal
	 * distance of staying in position 1 in s1; distance between s1[2] and s2[3]
	 * = 0.5; distance between s1[3] and s2[4] = 1.0 and finally the distance
	 * between s1[4] and s2[5] = 0; Total distance equals 2.1.
	 * 
	 * Non Double elements are compared using the equals method. Equal elements
	 * are considered at a distance of 0 and non equals at a distance of 1. If
	 * an element implements the {@link Relatable} interface, it is compared
	 * using the relationTo method.
	 * 
	 * @param s1
	 * @param s2
	 * @return a distance measure between the two sequences s1 and s2
	 */
	public double distance(final Iterator<E> s1, final Iterator<E> s2) {
		PriorityQueue<QueueState<E>> queue = new PriorityQueue<QueueState<E>>();
		ArrayList<QueueState<E>> visitied = new ArrayList<QueueState<E>>();

		LinkedPositionIterator<E> i1 = new LinkedPositionIterator<E>(s1);
		LinkedPositionIterator<E> i2 = new LinkedPositionIterator<E>(s2);
		LinkedPosition<E> firstPosition = i1.next();
		LinkedPosition<E> firstReference = i2.next();
		queue.add(new QueueState<E>(firstPosition, firstReference, computeCost(firstPosition.element(), firstReference.element())));

		QueueState<E> current = null;
		QueueState<E> goal = new QueueState<E>(firstPosition.getLast(), firstReference.getLast(), Double.MAX_VALUE);

		while (!queue.isEmpty()) {
			current = queue.poll();
			visitied.add(current);
			// System.out.println("Investigating " + current);
			if (current.equals(goal))
				return current.getCost();
			addAll(queue, visitied, current);

		}
		return Double.MAX_VALUE;
	}

	/**
	 * Adds all positions to queue that are reachable from p1 given the current
	 * p2, with the corresponding costs.
	 * 
	 * @param queue
	 * @param visitied
	 * @param p1
	 * @param p2
	 */
	private void addAll(final PriorityQueue<QueueState<E>> queue, final List<QueueState<E>> visitied, final QueueState<E> state) {

		LinkedPosition<E> p1 = state.getPosition();
		LinkedPosition<E> p2 = state.getReference();

		if (p1.hasNext()) {
			double cost = Math.abs(temporalCost) + computeCost(p1.getNext().element(), p2.element());
			if (cost <= costThreshold) {
				addState(queue, visitied, new QueueState<E>(p1.getNext(), p2, state.getCost() + cost));
			}
		}
		if (p2.hasNext()) {
			double cost = Math.abs(temporalCost) + computeCost(p1.element(), p2.getNext().element());
			if (cost <= costThreshold) {
				addState(queue, visitied, new QueueState<E>(p1, p2.getNext(), state.getCost() + cost));
			}
		}
		if (p1.hasNext() && p2.hasNext()) {
			double cost = computeCost(p1.getNext().element(), p2.getNext().element());
			if (cost <= costThreshold) {
				addState(queue, visitied, new QueueState<E>(p1.getNext(), p2.getNext(), state.getCost() + cost));
			}
		}
	}

	private void addState(final PriorityQueue<QueueState<E>> queue, final List<QueueState<E>> visitied, final QueueState<E> state) {
		Iterator<QueueState<E>> i = queue.iterator();
		while (i.hasNext()) {
			QueueState<E> s = i.next();
			if (s.equals(state)) {
				if (s.getCost() > state.getCost()) {
					i.remove();
				} else {
					return;
				}
				break;
			}
		}

		for (QueueState<E> s : visitied) {
			if (s.equals(state) && s.getCost() < state.getCost()) {
				return;
			}
		}

		queue.add(state);
	}

	private double computeCost(final E element, final E reference) {
		if (element == reference || element.equals(reference)) {
			return 0d;
		} else if (element instanceof Double && reference instanceof Double) {
			return Math.abs(((Double) element) - ((Double) reference));
		} else if (element instanceof Relatable && reference instanceof Relatable) {
			double relation = ((Relatable) element).relationTo(reference);
			return 0.5d - relation / 2d;
		} else {
			return 1d;
		}
	}
}

class QueueState<E> implements Comparable<QueueState<?>> {

	private final double cost;
	private final LinkedPosition<E> position;
	private final LinkedPosition<E> reference;

	public QueueState(final LinkedPosition<E> position, final LinkedPosition<E> reference, final double cost) {
		this.position = position;
		this.reference = reference;
		this.cost = cost;
	}

	@Override
	public int compareTo(final QueueState<?> o) {
		if (cost < o.cost)
			return -1;
		else if (cost > o.cost)
			return 1;
		else
			return 0;
	}

	public double getCost() {
		return cost;
	}

	public LinkedPosition<E> getPosition() {
		return position;
	}

	public LinkedPosition<E> getReference() {
		return reference;
	}

	@Override
	public String toString() {
		return position.element() + " ~ " + reference.element() + ": " + cost;
	}

	public boolean equals(final QueueState<?> s) {
		return (position == s.position && reference == s.reference);
	}

}