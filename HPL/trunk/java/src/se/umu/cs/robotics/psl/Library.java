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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import se.umu.cs.robotics.iteration.position.ElementIterator;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.psl.Hypothesis.HypothesisIterator;
import se.umu.cs.robotics.psl.Hypothesis.LeafIterator;
import se.umu.cs.robotics.utils.MathTools;

/**
 * The SLibrary holds all hypothesizes and implements methods to match these
 * hypothesizes against given data, in order to do prediction.
 * 
 * @author billing
 * 
 * @param <E>
 */
public class Library<E> implements Iterable<Hypothesis<E>> {
	/*
	 * Holds the root of all sequences
	 */
	private final ArrayList<Hypothesis<E>> roots = new ArrayList<Hypothesis<E>>();
	private final Logger logger = Logger.getLogger("se.umu.robotics");
	private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

	/**
	 * The maximum allowed length of hypothesizes.
	 */
	private final int hypothesisMaxLength;

	public Library() {
		this.hypothesisMaxLength = Integer.MAX_VALUE;
	}

	public Library(final int maxHypothesisLength) {
		this.hypothesisMaxLength = maxHypothesisLength;
	}

	/**
	 * Returns a root hypothesis matching given element.
	 * 
	 * @param element
	 * @return matching root sequence if any, otherwise null
	 */
	public Hypothesis<E> getRoot(final E element) {
		for (Hypothesis<E> s : roots) {
			if (element == null) {
				if (s.getElement() == null)
					return s;
			} else if (element.equals(s.getElement()))
				return s;
		}
		return null;
	}

	/**
	 * Returns a root hypothesis matching given element.
	 * 
	 * @param element
	 * @param createIfMissing if true, a new root will be created and returned,
	 *            if not previously existing in library
	 * @return matching root sequence if any, otherwise null
	 */
	public Hypothesis<E> getRoot(final E element, final boolean createIfMissing) {
		Hypothesis<E> root = getRoot(element);
		if (root == null) {
			root = addRoot(element);
		}
		return root;
	}

	protected ArrayList<Hypothesis<E>> getRoots() {
		return roots;
	}

	public int size() {
		int cnt = 0;
		HypothesisIterator<E> i = this.iterator();
		while (i.hasNext()) {
			if (i.next().length() > 0)
				cnt++;
		}
		return cnt;
	}

	public Hypothesis<E> getLongestSequence() {
		Hypothesis<E> h = null;
		for (Hypothesis<E> s : this) {
			if (h == null || s.length() > h.length())
				h = s;
		}
		return h;
	}

	/**
	 * @return an iterator over all hypotheses in the library.
	 */
	public HypothesisIterator<E> iterator() {
		return new HypothesisIterator<E>(roots);
	}

	/**
	 * @return an iterator over all leaf hypothesizes in the library.
	 */
	public HypothesisIterator<E> iterLeaves() {
		return new LeafIterator<E>(roots);
	}

	/**
	 * @param minLength
	 * @return an iterator over all hypotheses in the library with specified
	 *         minimum length.
	 */
	public HypothesisIterator<E> hypotheses(final int minLength) {
		return new HypothesisIterator<E>(roots, minLength);
	}

	public HypothesisIterator<E> hypotheses() {
		return hypotheses(1);
	}

	public void sort() {
		Collections.sort(roots);
	}

	/**
	 * Adds new root sequence (always one element long)
	 * 
	 * @param data
	 */
	public Hypothesis<E> addRoot(final E data) {
		if (roots.contains(data)) {
			return null;
		}
		Hypothesis<E> newSequence = new RootNode<E>(this, data);
		roots.add(newSequence);
		return newSequence;
	}

	/**
	 * Compares given data array to all sequences in library
	 * 
	 * @param data an iterator that returns elements backwards in time (first
	 *            returned element is first compared lhs element)
	 * @return a list will all matching hypotheses.
	 */
	public HypothesisList<E> match(final Iterator<E> data) {
		return match(new LinkedPositionIterator<E>(data));
	}

	public HypothesisList<E> match(final PositionIterator<E> data) {
		HypothesisList<E> list = new HypothesisList<E>();
		for (Hypothesis<E> h : roots) {
			list.addAll(h.match(new ElementIterator<E>(data.clone())));
		}
		return list;
	}

	/**
	 * @param id
	 * @return the hypothesis with specified id, otherwise null
	 */
	public Hypothesis<E> get(final int id) {
		for (Hypothesis<E> h : this) {
			if (h.getId() == id) {
				return h;
			}
		}
		return null;
	}

	/**
	 * Generates a flat list of all hypothesis in sequence tree.
	 * 
	 * @return list of hypothesis
	 */
	public HypothesisList<E> getHypotheses() {
		HypothesisList<E> hyps = new HypothesisList<E>();
		for (Hypothesis<E> h : this)
			hyps.add(h);
		return hyps;
	}

	/**
	 * A weird method, should be rewritten/removed
	 */
	public List<Hypothesis<E>> getHypothesesByOccurance(final List<PslNode<E>> sequence, final int occuranceThreshold) {
		List<Hypothesis<E>> hyps = getHypotheses();
		if (occuranceThreshold > 1) {
			int[] counts = new int[hyps.size()];
			for (Hypothesis<E> h : sequence) {
				counts[hyps.indexOf(h)]++;
			}
			Iterator<Hypothesis<E>> iter = hyps.iterator();
			for (int count : counts) {
				iter.next();
				if (count < occuranceThreshold)
					iter.remove();
			}
		}
		return hyps;
	}

	@Override
	public String toString() {
		if (roots.size() == 0)
			return "{}";

		StringBuffer b = new StringBuffer();
		for (Hypothesis<E> s : this) {
			b.append(s.toString());
			b.append("\n");
		}
		return b.toString();
	}

	public int hypothesisMaxLength() {
		return hypothesisMaxLength;
	}

	public double averageSequenceLength() {
		ArrayList<Integer> sizes = new ArrayList<Integer>();
		for (Hypothesis<E> h : this) {
			if (!h.hasChilds())
				sizes.add(h.length());
		}
		return MathTools.mean(sizes);
	}

	public Logger getLogger() {
		return logger;
	}

	/**
	 * The read lock should be used when accessing the library in a
	 * multithreaded application.
	 * 
	 * @return the lock for reading
	 */
	public Lock readLock() {
		return lock.readLock();
	}

	/**
	 * The write lock should be used when modifying the library in a
	 * multithreaded application.
	 * 
	 * @return the lock for changing the library
	 */
	public Lock writeLock() {
		return lock.writeLock();
	}
}
