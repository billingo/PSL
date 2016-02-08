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

package se.umu.cs.robotics.fpsl;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public interface FLhs<E> {

    boolean isRoot();

    SpaceDistribution<E> getDistribution();

    List<FLhs<E>> getChildren();

    FLhs<E> getChild(SpaceDistribution<E> lhsDistribution, double intersectionThreshold);

    FLibrary<E> getLibrary();

    List<FHypothesis<E>> getHypotheses();

    FLhs<E> getParent();

    FLhs<E> addChild(SpaceDistribution<E> lhsDistribution);

    FHypothesis<E> addHypothesis(SpaceDistribution<E> target);

    FHypothesis<E> addHypothesis(FHypothesis<E> hypothesis);

    int length();

    double integrationRange();

    /**
     * @return a hierarchical iterator over all child elements to the current node
     */
    Iterator<FLhs<E>> iterator();

    public static class LhsIterator<E> implements IterableIterator<FLhs<E>> {

        private final Iterator<FLhs<E>> i; // Primary iterator
        private Iterator<FLhs<E>> ci = null; // Current child iterator
        private FLhs<E> currentElement;

        protected LhsIterator(final List<FLhs<E>> roots) {
            i = roots.iterator();
        }

        protected LhsIterator(final Iterator<FLhs<E>> childIterator) {
            i = childIterator;
        }

        public boolean hasNext() {
            if (ci != null && ci.hasNext()) {
                return true;
            }
            if (i.hasNext()) {
                return true;
            }
            return false;
        }

        public FLhs<E> next() {
            if (ci != null && ci.hasNext()) {
                return ci.next();
            } else {
                FLhs<E> lhs = i.next();
                ci = lhs.iterator();
                return lhs;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Iterator<FLhs<E>> iterator() {
            return this;
        }
    }

    public static class LhsLeafIterator<E> implements IterableIterator<FLhs<E>> {

        private final LhsIterator<E> i; // Primary iterator
        private FLhs<E> currentElement;

        protected LhsLeafIterator(final List<FLhs<E>> roots) {
            i = new LhsIterator<E>(roots);
        }

        protected LhsLeafIterator(final Iterator<FLhs<E>> childIterator) {
            i = new LhsIterator<E>(childIterator);
        }

        public boolean hasNext() {
            try {
                nextElement();
            } catch (NoSuchElementException ex) {
                return false;
            }
            return true;
        }

        public FLhs<E> next() {
            FLhs<E> lhs = nextElement();
            currentElement = null;
            return lhs;
        }

        private FLhs<E> nextElement() {
            while (currentElement == null || !currentElement.getChildren().isEmpty()) {
                currentElement = i.next();
            }
            return currentElement;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Iterator<FLhs<E>> iterator() {
            return this;
        }
    }
}
