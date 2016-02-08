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
import se.umu.cs.robotics.fpsl.FLhs.LhsIterator;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 * A Fuzzy rule describing the relation between a sequence of LHS elements and a future RHS element
 *
 * @author Erik Billing <billing@cs.umu.se>
 * @param <E> the element type
 */
public interface FHypothesis<E> {

    public int getId();

    /**
     * @return the LHS three node
     */
    public FLhs getLhs();

    /**
     * @return the leftmost LHS element of this hypothesis
     */
    public SpaceDistribution<E> getDistribution();

    /**
     * @return the RHS element of this hypothesis
     */
    public SpaceDistribution<E> getTarget();

    /**
     * Returns the root node in this hypothesis, i.e., the node holding the
     * rhs element. Returns null if this is the root
     * node.
     *
     * @return the parent node
     */
    public FLibrary<E> getLibrary();

    public FHypothesis<E> getParent();
    
    public FHypothesis<E> addChild(SpaceDistribution<E> lhsDistribution, SpaceDistribution<E> targetDistribution);
    
    /**
     * @return the number of LHS elements in the hypothesis.
     */
    public int length();

    public double integrationRange();

    public void hit(double value);

    public double hits();

    public void miss(double value);

    public double misses();

    public double getConfidence();

    /**
     * Iterates over all hypotheses in the hypothesis tree
     *
     * @author billing
     *
     * @param <E>
     */
    public static class HypothesisIterator<E> implements IterableIterator<FHypothesis<E>> {

        private final LhsIterator<E> i; // Primary iterator
        private Iterator<FHypothesis<E>> hi = null;

        protected HypothesisIterator(final List<FLhs<E>> roots) {
            i = new LhsIterator<E>(roots);
        }

        protected HypothesisIterator(final Iterator<FLhs<E>> childIterator) {
            i = new FLhs.LhsIterator<E>(childIterator);
        }
        
        protected HypothesisIterator(final LhsIterator<E> sourceIterator) {
            i = sourceIterator;
        }

        public boolean hasNext() {
            try {
                nextIterator();
            } catch (NoSuchElementException ex) {
                return false;
            }
            return true;
        }

        public FHypothesis<E> next() {
            nextIterator();
            return hi.next();
        }

        private void nextIterator() {
            while (hi == null || !hi.hasNext()) {
                hi = i.next().getHypotheses().iterator();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterator<FHypothesis<E>> iterator() {
            return this;
        }
    }

    public static class LeafIterator<E> extends HypothesisIterator<E> {

        protected LeafIterator(final List<FLhs<E>> roots) {
            super(new FLhs.LhsLeafIterator<E>(roots));
        }

    }

}
