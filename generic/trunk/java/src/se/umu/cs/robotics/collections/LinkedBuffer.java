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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * An ObjectBuffer based on a LinkedList
 * 
 * @author billing
 * 
 * @param <E>
 */
public class LinkedBuffer<E> extends LinkedList<E> implements ObjectBuffer<E> {
	private static final long serialVersionUID = 1L;

	private final List<ObjectBufferListener<E>> listeners = new LinkedList<ObjectBufferListener<E>>();
	private int capacity;

	private final BufferDirection direction;

	public LinkedBuffer(final BufferDirection direction, final int capacity) {
		this.capacity = capacity;
		this.direction = direction;
	}

	@Override
	public boolean add(final E e) {
		switch (direction) {
		case FORWARD:
			addLast(e);
			break;
		default:
			addFirst(e);
		}
		signalAdded(e);
		return true;
	}

	@Override
	public void add(final int index, final E element) {
		super.add(index, element);
		signalAdded(element);
		limitSize();
	}

	@Override
	public void addFirst(final E e) {
		super.addFirst(e);
		limitSize();
	}

	@Override
	public void addLast(final E e) {
		super.addLast(e);
		limitSize();
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) {
		switch (direction) {
		case FORWARD:
			for (E e : c) {
				super.addLast(e);
			}
			break;
		default:
			for (E e : c) {
				super.addFirst(e);
			}
			break;
		}
		limitSize();
		return !c.isEmpty();
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends E> c) {
		int i = index;
		for (E e : c) {
			super.add(i++, e);
		}
		limitSize();
		return !c.isEmpty();
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public void setCapacity(final int capacity) {
		this.capacity = capacity;
		signalCapacityChanged();
		limitSize();
	}

	private void limitSize() {
		int size = size();
		if (this.direction == BufferDirection.FORWARD) {
			for (int i = capacity; i < size; i++) {
				signalLost(removeFirst());
			}
		} else {
			for (int i = capacity; i < size; i++) {
				signalLost(removeLast());
			}
		}
	}

	@Override
	public BufferDirection getDirection() {
		return direction;
	}

	private void signalAdded(final E element) {
		for (ObjectBufferListener<E> l : listeners) {
			l.elementAdded(element);
		}
	}

	private void signalLost(final E element) {
		for (ObjectBufferListener<E> l : listeners) {
			l.elementLost(element);
		}
	}

	private void signalCapacityChanged() {
		for (ObjectBufferListener<E> l : listeners) {
			l.capacityChanged(capacity);
		}
	}

	@Override
	public void addListener(final ObjectBufferListener<E> listener) {
		listeners.add(listener);
	}

	@Override
	public void clearListeners() {
		listeners.clear();
	}

	@Override
	public void removeListener(final ObjectBufferListener<E> listener) {
		listeners.remove(listener);
	}

}
