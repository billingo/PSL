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

import se.umu.cs.robotics.fpsl.FContext.Confidence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.collections.fuzzy.FuzzySet;
import se.umu.cs.robotics.collections.fuzzy.UnitIterator;
import se.umu.cs.robotics.fpsl.FHypothesis.HypothesisIterator;
import se.umu.cs.robotics.fpsl.FHypothesis.LeafIterator;
import se.umu.cs.robotics.fpsl.log.ContextCreated;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.StateSpace;

/**
 * The SLibrary holds all hypothesizes and implements methods to match these
 * hypothesizes against given data, in order to do prediction.
 *
 * @author billing
 *
 * @param <E>
 */
public class FLibrary<E> implements FuzzySet<FHypothesis<E>> {

    final static Logger logger = Logger.getLogger(FLibrary.class);
    private final StateSpace<E> stateSpace;

    /*
     * Holds the root of all sequences
     */
    private final List<FLhs<E>> roots = new CopyOnWriteArrayList<FLhs<E>>();
    private final List<FContext> contexts = new CopyOnWriteArrayList<FContext>();
    /**
     * The maximum allowed length of hypothesizes.
     */
    private final FPslParameters parameters;

    public FLibrary(StateSpace<E> stateSpace) {
        this(stateSpace, new FPslParameters());
    }

    public FLibrary(StateSpace stateSpace, final FPslParameters parameters) {
        this(stateSpace, parameters, "Behavior");
    }

    public FLibrary(StateSpace stateSpace, FPslParameters parameters, String... contexts) {
        this.stateSpace = stateSpace;
        this.parameters = parameters;
        if (contexts !=null) {
	        for (int i = 0; i < contexts.length; i++) {
	            if (contexts[i] != null) {
	                createNewContext(contexts[i], i == 0 ? 1d : 0d);
	            }
	        }
        }
    }

    public StateSpace<E> stateSpace() {
        return stateSpace;
    }

    /**
     * @return all roots
     */
    public List<FLhs<E>> getRoots() {
        return roots;
    }

    public List<FContext> getContexts() {
        return contexts;
    }

    public FContext getContext(int index) {
        return contexts.get(index);
    }

    public FContext getContext(String name) {
        for (FContext c : contexts) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        throw new IndexOutOfBoundsException(name);
    }

    public int getContextCount() {
        return contexts.size();
    }

    public ArrayList<String> getContextNames() {
        ArrayList<String> contextNames = new ArrayList<String>(contexts.size());
        for (FContext c : contexts) {
            contextNames.add(c.getName());
        }
        return contextNames;
    }

    public int size() {
        int cnt = 0;
        UnitIterator<FHypothesis<E>> i = this.iterator();
        while (i.hasNext()) {
            if (i.next().element().length() > 0) {
                cnt++;
            }
        }
        return cnt;
    }

    public boolean isEmpty() {
        for (FuzzyItem<FHypothesis<E>> i : this) {
            if (i.element().length() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return an iterator over all hypotheses in the library.
     */
    public UnitIterator<FHypothesis<E>> iterator() {
        return new UnitIterator<FHypothesis<E>>(new HypothesisIterator<E>(roots));
    }

    /**
     * @return an iterator over all leaf hypothesizes in the library.
     */
    public HypothesisIterator<E> iterLeaves() {
        return new LeafIterator<E>(roots);
    }

    public HypothesisIterator<E> hypotheses() {
        return new HypothesisIterator<E>(roots);
    }

    /**
     * Calculates the hypothesis confidence based on the combined context responsibility.
     * 
     * @param hypothesis
     * @return confidence
     */
    public double getConfidence(FHypothesis<E> hypothesis) {
        double confidence = 0;
        double sum = 0;
        for (FContext context : contexts) {
            double responsibility = context.getResponsibility();
            Confidence c = context.getConfidence(hypothesis, true);
            if (c != null) {
                confidence += c.getValue() * responsibility;
            }
            sum += responsibility;
        }
        return sum > 0 ? confidence / sum : 0;
    }

    public void hypothesisHit(FHypothesis<E> hypothesis, double hit) {
        for (FContext context : contexts) {
            FHypothesis<E> h = hypothesis;
            do {
                context.hit(h, hit * context.getResponsibility());
                h = h.getParent();
            } while (parameters.propagateConfidenceChangeToParents() && h != null && h.length() > 0);
        }
    }

    public void hypothesisMiss(FHypothesis<E> hypothesis, double miss) {
        for (FContext context : contexts) {
            FHypothesis<E> h = hypothesis;
            do {
                context.miss(h, miss * context.getResponsibility());
                h = h.getParent();
            } while (parameters.propagateConfidenceChangeToParents() && h != null && h.length() > 0);
        }
    }

    public double getHypothesisHits(FHypothesis<E> h) {
        double hits = 0;
        double sum = 0;
        for (FContext context : contexts) {
            double responsibility = context.getResponsibility();
            Confidence confidence = context.getConfidence(h, true);
            if (confidence != null) {
                hits += responsibility * confidence.getHits();
                sum += responsibility;
            }
        }
        if (sum > 0d) {
            return hits / sum;
        } else {
            return 0d;
        }
    }

    public double getHypothesisMisses(FHypothesis<E> h) {
        double misses = 0;
        double sum = 0;
        for (FContext context : contexts) {
            double responsibility = context.getResponsibility();
            final Confidence confidence = context.getConfidence(h, true);
            if (confidence != null) {
                misses += responsibility * confidence.getMisses();
                sum += responsibility;
            }
        }
        if (sum > 0) {
            return misses / sum;
        } else {
            return 0d;
        }
    }

    /**
     * Creates a new context with specified name and responsibility
     * @param name
     * @param responsibility
     * @return the index of the new context
     */
    public int createNewContext(String name, double responsibility) {
        final FContext context = new FContext(name, responsibility);
        contexts.add(context);
        final int index = contexts.size() - 1;
        logger.info(new ContextCreated(context, index));
        return index;
    }

    public void activateOnly(int context) {
        for (int c = 0; c < contexts.size(); c++) {
            contexts.get(c).setResponsibility(c == context ? 1d : 0d);
        }
    }

    public void activateOnly(String context) {
        activateOnly(context, false);
    }
    
    public void activateOnly(String context, boolean createContextIfMissing) {
        int selectedContexts = 0;
        for (FContext c : contexts) {
            final boolean match = c.getName().equals(context);
            selectedContexts += match ? 1 : 0;
            c.setResponsibility(match ? 1d : 0d);
        }
        if (selectedContexts == 0 && createContextIfMissing) {
            createNewContext(context, 1d);
        }
    }

    /**
     * Adds new root LHS node (always one element long)
     *
     * @param data
     */
    public FLhs<E> addRoot(final SpaceDistribution<E> data) {
        FLhsNode<E> root = new FLhsNode<E>(this, data);
        roots.add(root);
        return root;
    }

    /**
     * @param id
     * @return the hypothesis with specified id, otherwise null
     */
    public FuzzyItem<FHypothesis<E>> get(final int id) {
        for (FuzzyItem<FHypothesis<E>> item : this) {
            if (item.element().getId() == id) {
                return item;
            }
        }
        return null;
    }

    public double get(FHypothesis<E> h) {
        /*
         * All hypotheses in library has unit membership.
         */
        return h != null && h.getLibrary() == this ? 1 : 0;
    }

    @Override
    public String toString() {
        if (roots.isEmpty()) {
            return "Empty library";
        }

        StringBuilder b = new StringBuilder();
        for (FuzzyItem<FHypothesis<E>> item : this) {
            b.append(item.toString());
            b.append("\n");
        }
        return b.toString();
    }

    public FPslParameters getParameters() {
        return parameters;
    }

    public void put(FHypothesis<E> element, double v) {
        throw new UnsupportedOperationException("Putting elements not supported by the library. Use addRoot.");
    }

    public void put(FuzzyItem<FHypothesis<E>> item) {
        throw new UnsupportedOperationException("Putting elements not supported by the library. Use addRoot.");
    }

    public void putAll(FuzzySet<FHypothesis<E>> collection) {
        throw new UnsupportedOperationException("Putting elements not supported by the library. Use addRoot.");
    }

    public FuzzyItem<FHypothesis<E>> max() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public FuzzyItem<FHypothesis<E>> min() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public double sum() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clear() {
        roots.clear();
        for (FContext c : contexts) {
            c.clear();
        }
    }
}
