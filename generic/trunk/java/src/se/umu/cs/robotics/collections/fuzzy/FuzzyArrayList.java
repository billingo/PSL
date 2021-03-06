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
package se.umu.cs.robotics.collections.fuzzy;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author billing
 */
public class FuzzyArrayList<E> implements FuzzySet<E> {

    private ArrayList<FuzzyItem<E>> items = new ArrayList<FuzzyItem<E>>();

    public double get(E element) {
        if (element != null) {
            for (FuzzyItem i : items) {
                if (element.equals(i.element())) {
                    return i.value();
                }
            }
        }
        return 0;
    }

    public FuzzyItem<E> get(int i) {
        return items.get(i);
    }

    public Iterator<FuzzyItem<E>> iterator() {
        return items.iterator();
    }

    public void put(E element, double v) {
        put(new ConcreteFuzzyItem<E>(element, v));
    }

    public void put(FuzzyItem<E> item) {
        items.add(item);
    }

    public void putAll(FuzzySet<E> collection) {
        for (FuzzyItem<E> i : collection) {
            put(i);
        }
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public FuzzyItem<E> max() {
        FuzzyItem<E> item = null;
        for (FuzzyItem<E> i : this) {
            item = item == null || item.value() < i.value() ? i : item;
        }
        return item;
    }

    public FuzzyItem<E> min() {
        FuzzyItem<E> item = null;
        for (FuzzyItem<E> i : this) {
            item = item == null || item.value() > i.value() ? i : item;
        }
        return item;
    }

    public double sum() {
        double sum=0;
        for (FuzzyItem<E> i: this) {
            sum += i.value();
        }
        return sum;
    }
    /**
     * @param list
     * @return an iterator over all elements in this, not present in list
     */
//    public IterableIterator<FuzzyItem<E>> minusOrdered(final FuzzyArrayList<E> list) {
//        final Iterator<FuzzyItem<E>> iMy = iterator();
//        final Iterator<FuzzyItem<E>> iList = list.iterator();
//        return new IterableIterator<FuzzyItem<E>>() {
//
//            FuzzyItem<E> myItem;
//            FuzzyItem<E> listItem;
//
//            public Iterator<FuzzyItem<E>> iterator() {
//                return this;
//            }
//
//            public boolean hasNext() {
//                if (myItem != null) {
//                    return true;
//                } else if (iMy.hasNext()) {
//                    myItem = iMy.next();
//                }
//            }
//
//            public FuzzyItem<E> next() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void remove() {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        };
//    }
}
