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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractHypothesis<E> implements Hypothesis<E> {

	/**
	 * The hypothesis condition element (left hand side)
	 */
	private final E element;

	/**
	 * List of all children (more specific hypothesis)
	 */
	private final HashMap<E, Hypothesis<E>> childs = new HashMap<E, Hypothesis<E>>();

	/**
	 * Each Hypothesis gains its own id upon creation. Similar to hash, but more
	 * readable.
	 */
	private int id;
	private static int idCount = 0;

	public AbstractHypothesis(final E element) {
		this.element = element;
		assignId();
	}

	private void assignId() {
		id = idCount++;
	}

	/**
	 * Matches this hypothesis and all children to given data.
	 * 
	 * @param data iterator
	 * @return true if all elements in sequence matches given data
	 */
	public List<Hypothesis<E>> match(final Iterator<E> data) {
		if (!data.hasNext()) {
			return new ArrayList<Hypothesis<E>>(length());
		}
		E e = data.next();
		Hypothesis<E> child = childs.get(e);
		if (child == null) {
			return new ArrayList<Hypothesis<E>>(length());
		} else {
			List<Hypothesis<E>> match = child.match(data);
			match.add(child);
			return match;
		}
	}

	public E getElement() {
		return element;
	}

	public Hypothesis<E> getChild(final E element) {
		return childs.get(element);
	}

	public void setChild(final Hypothesis<E> h) {
		childs.put(h.getElement(), h);
	}

	public boolean hasChild(final E e) {
		return childs.containsKey(e);
	}

	public boolean hasChilds() {
		return !childs.isEmpty();
	}

	public Iterator<Hypothesis<E>> iterator() {
		return new HypothesisIterator<E>(childs.values().iterator());
	}

	public int getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(final Hypothesis<E> s) {
		if (s == null)
			return 0;
		return s.length() - length();
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("H" + getId() + ":" + toString(element));
		Hypothesis<E> h = getParent();
		while (h != null) {
			E e = h.getElement();
			Hypothesis<E> p = h.getParent();
			if (p == null) {
				s.append(" => ");
			} else {
				s.append(',');
			}
			s.append(toString(e));
			h = p;
		}
		return s.toString();
	}

	private String toString(final E e) {
		if (e == null)
			return "null";
		else if (e instanceof Double)
			return Psl.format.format(e);
		else
			return e.toString();
	}

}
