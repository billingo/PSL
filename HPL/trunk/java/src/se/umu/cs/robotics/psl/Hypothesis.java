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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public interface Hypothesis<E> extends Iterable<Hypothesis<E>>, Comparable<Hypothesis<E>> {

	public int getId();

	public long getUpdateId();

	/**
	 * @return the leftmost LHS element of this hypothesis
	 */
	public E getElement();

	/**
	 * @return the RHS element of this hypothesis
	 */
	public E getTarget();

	/**
	 * Returns the rightmost element of the lhs of this hypothesis. If this node
	 * is the rightmost element, the method returns <code>this</code>.
	 * 
	 * @return the root element of the lhs tree
	 */
	public Hypothesis<E> getRoot();

	/**
	 * Returns the parent node in this hypothesis, i.e., the node holding the
	 * lhs element to the right of this node. Returns null if this is the root
	 * node.
	 * 
	 * @return the parent node
	 */
	public Hypothesis<E> getParent();

	public Library<E> getLibrary();

	public Hypothesis<E> getChild(E e);

	public void setChild(Hypothesis<E> h);

	public boolean hasChild(E e);

	public boolean hasChilds();

	public int length();

	public void hit();

	public void miss();

	public double getConfidence();

	public boolean lhsEquals(Hypothesis<E> h);

	public List<Hypothesis<E>> match(final Iterator<E> data);

	/**
	 * Iterates over all hypotheses in the hypothesis tree
	 * 
	 * @author billing
	 * 
	 * @param <E>
	 */
	public static class HypothesisIterator<E> implements Iterator<Hypothesis<E>>, Iterable<Hypothesis<E>> {
		private final Iterator<Hypothesis<E>> i; // Primary iterator
		private Iterator<Hypothesis<E>> ci = null; // Current child iterator
		private final int minLength;

		protected HypothesisIterator(final List<Hypothesis<E>> roots) {
			i = roots.iterator();
			minLength = 0;
		}

		protected HypothesisIterator(final Iterator<Hypothesis<E>> childIterator) {
			i = childIterator;
			minLength = 0;
		}

		protected HypothesisIterator(final List<Hypothesis<E>> roots, final int minLength) {
			i = roots.iterator();
			this.minLength = minLength;
		}

		protected HypothesisIterator(final Iterator<Hypothesis<E>> childIterator, final int minLength) {
			i = childIterator;
			this.minLength = minLength;
		}

		public boolean hasNext() {
			if (ci != null && ci.hasNext())
				return true;
			if (i.hasNext())
				return true;
			return false;
		}

		public Hypothesis<E> next() {
			Hypothesis<E> h;
			if (ci != null && ci.hasNext()) {
				h = ci.next();
			} else {
				h = i.next();
				/*
				 * The new iterator will be used next time next is called.
				 */
				ci = h.iterator();
			}
			if (h.length() >= minLength) {
				return h;
			} else {
				return next();
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<Hypothesis<E>> iterator() {
			return this;
		}

	}

	public static class LeafIterator<E> extends HypothesisIterator<E> {

		private Hypothesis<E> currentElement = null;

		protected LeafIterator(final List<Hypothesis<E>> roots) {
			super(roots);
		}

		@Override
		public boolean hasNext() {
			try {
				nextElement();
			} catch (NoSuchElementException e) {
				return false;
			}
			return true;
		}

		@Override
		public Hypothesis<E> next() {
			if (currentElement == null)
				nextElement();
			Hypothesis<E> h = currentElement;
			currentElement = null;
			return h;
		}

		private void nextElement() {
			while (currentElement == null || currentElement.hasChilds()) {
				currentElement = super.next();
			}
		}

	}

	public static class ElementIterator<E> implements Iterator<E>, Iterable<E> {

		private final boolean includeRhs;
		private Hypothesis<E> hypothesis;

		public ElementIterator(final PslNode<E> h, final boolean includeRhs) {
			this.hypothesis = h;
			this.includeRhs = includeRhs;
		}

		@Override
		public boolean hasNext() {
			return hypothesis != null && (includeRhs || hypothesis.length() > 0);
		}

		@Override
		public E next() {
			Hypothesis<E> h = hypothesis;
			if (hasNext()) {
				hypothesis = h.getParent();
				return h.getElement();
			}
			throw new NoSuchElementException();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<E> iterator() {
			return this;
		}

	}

	/**
	 * Iterates over all elements in the Hypothesis, starting with the rightmost
	 * element and finishing at the leaf (leftmost element).
	 * 
	 * @author billing
	 * 
	 */
	public static class RightToLeftIterator<E> extends ElementIterator<E> {

		LinkedList<E> elements = new LinkedList<E>();

		public RightToLeftIterator(final PslNode<E> h, final boolean includeRhs) {
			super(h, includeRhs);
			while (super.hasNext()) {
				elements.push(super.next());
			}
		}

		@Override
		public boolean hasNext() {
			return !elements.isEmpty();
		}

		@Override
		public E next() {
			if (!elements.isEmpty()) {
				return elements.pop();
			}
			throw new NoSuchElementException();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<E> iterator() {
			return this;
		}

	}

}
