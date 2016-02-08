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

import java.util.List;
import se.umu.cs.robotics.fpsl.selection.FLhsMatch;
import se.umu.cs.robotics.fpsl.selection.FHypothesisSelection;
import java.util.NoSuchElementException;
import org.apache.log4j.Logger;
import se.umu.cs.robotics.fpsl.collection.FHypothesisSet;
import se.umu.cs.robotics.fpsl.selection.FHypothesisSelector;
import se.umu.cs.robotics.iteration.position.GenericPositionIterator;
import se.umu.cs.robotics.iteration.position.GenericReverseIterator;
import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.probabilitydistribution.FuzzyDistribution;
import se.umu.cs.robotics.probabilitydistribution.FuzzySpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.iteration.IntegratingPositionIterator;

/**
 *
 * @author billing
 */
public class FPrediction<E> implements IteratorPosition<SpaceDistribution<E>> {

    static Logger logger = Logger.getLogger(FPrediction.class);
    private final FHypothesisSelector<E> selector;
    private final IteratorPosition<SpaceDistribution<E>> previous;
    private final PositionIterator<SpaceDistribution<E>> source;
    private FHypothesisSet<E> intersections;
    private final FHypothesisSelection<E> selection;
    private boolean hasNext = true;
    private SpaceDistribution<E> element;
    private final FPslParameters parameters;

    public FPrediction(FHypothesisSelector<E> selector, IteratorPosition<SpaceDistribution<E>> previous, boolean reverseData) {
        this.selector = selector;
        this.previous = reverseData ? previous : null;
        this.parameters = selector.getLibrary().getParameters();
        final double integrationGradient = parameters.getIntegrationGradient();
        if (integrationGradient != 1d) {
            IntegratingPositionIterator<E> integratingSource = new IntegratingPositionIterator<>(previous, integrationGradient);
            previous = integratingSource.getPosition();
        }
        if (reverseData) {
            this.source = new GenericReverseIterator<>(previous, true);
        } else {
            this.source = new GenericPositionIterator<>(previous, true);
        }
        this.selection = match();
    }

    private FHypothesisSelection<E> match() {
        return selector.lhsSelect(this.source.clone());
    }

    @Override
    public IteratorPosition<SpaceDistribution<E>> getPrevious() {
        return previous;
    }

    @Override
    public FPrediction<E> getNext() {
        if (hasNext) {
            hasNext = false;
            final FPrediction<E> prediction = new FPrediction<>(selector, this, true);
            hasNext = true;
            return prediction;
        } else {
            throw new NoSuchElementException();
        }
    }

    public FHypothesisSet<E> intersections() {
        return intersections;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public boolean hasPrevious() {
        return true;
    }

    public PositionIterator<SpaceDistribution<E>> iterator() {
        return new GenericPositionIterator<>(this);
    }

    public FHypothesisSelection<E> getSelection() {
        return selection;
    }

    @Override
    public SpaceDistribution<E> element() {
        element = selection.getTarget();
        return element;
    }
    
    public SpaceDistribution<E> element(FContext context) {
        element = selection.getTarget(context);
        return element;
    }

    public List<FHypothesis<E>> teach(SpaceDistribution<E> correctDistributions) {
        boolean correct = selection.isCorrect(correctDistributions);
        List<FHypothesis<E>> createdHypotheses = selection.updateConfidences(correctDistributions);
        if (!correct) {
            FHypothesis<E> newHypothesis = teachIncorrect(correctDistributions);
            if (newHypothesis != null) {
                createdHypotheses.add(newHypothesis);
            }
        }
        return createdHypotheses;
    }

    private FHypothesis<E> teachIncorrect(SpaceDistribution<E> correctDistributions) {
        double thresholdCorrect = selector.getLibrary().getParameters().thresholdCorrect();
        FHypothesis<E> parent = null;
        FLhsMatch<E> parentMatch = null;
        FLhsMatch<E> bestMatch = null;
        double parentValue = 0;

        for (FLhsMatch<E> m : selection.getMatches()) {
            double value = m.getValue();
            final FLhs<E> lhs = m.getLhs();
            if (value >= thresholdCorrect) {
                for (FHypothesis<E> h : lhs.getHypotheses()) {
                    double intersection = correctDistributions.intersection(h.getTarget());
                    if (intersection >= thresholdCorrect && value >= thresholdCorrect) {
                        if (parent == null || parentValue < value || (parentValue == value && parent.length() < h.length()) || (parentValue == value && parent.length() == h.length() && parent.getConfidence() < h.getConfidence())) {
                            parent = h;
                            parentMatch = m;
                        }
                    }
                }
                if (bestMatch == null || bestMatch.getValue() < value) {
                    bestMatch = m;
                }
            }
        }

        if (parent != null) {
            // Extends an existing hypothesis
            boolean createHypothesis = false;
            final FLhs<E> parentLhs = parentMatch.getLhs();
            for (int length = parentLhs.length(); length <= selection.getMatches().maxLhsLength(); length++) {
                for (FLhsMatch<E> m : selection.getMatches()) {
                    final FLhs<E> lhs = m.getLhs();
                    for (FHypothesis<E> h : lhs.getHypotheses()) {
                        if (h != parent && (h.length() > parent.length() || m.getValue() >= parentMatch.getValue())) {
                            createHypothesis = true;
                            break;
                        }
                    }
                }
            }
            if (createHypothesis) {
                return createHypothesis(parent, correctDistributions);
            }
        } else if (source.hasNext()) {
            // Creates a new root hypothesis
            FLhs<E> lhs = bestMatch == null ? null : bestMatch.getLhs();
            if (lhs == null) {
                SpaceDistribution<E> v = toLinguisticValue(source.clone().next().element());
                lhs = selector.getLibrary().addRoot(v);
            }
            FHypothesis<E> createdHypothesis = lhs.addHypothesis(correctDistributions);
            createdHypothesis.hit(1d);
            return createdHypothesis;
        }
        return null;
    }

    private FHypothesis<E> createHypothesis(FHypothesis<E> parent, SpaceDistribution<E> rhsDistribution) {
        FHypothesis<E> createdHypothesis = null;
        if (parent.hits() >= parameters.parentMinimumHitCount() || parent.misses() >= parameters.parentMinimumMissCount()) {
            PositionIterator<SpaceDistribution<E>> i = source.clone();
            for (int t = 0; t < parent.length(); t++) {
                IteratorPosition<SpaceDistribution<E>> next = i.next();
            }
            if (i.hasNext()) {
                SpaceDistribution<E> lhsDistribution = toLinguisticValue(i.next().element());
                createdHypothesis = parent.addChild(lhsDistribution, rhsDistribution);
                createdHypothesis.hit(1d);
            }
        }
        return createdHypothesis;

    }

    private SpaceDistribution<E> toLinguisticValue(SpaceDistribution<E> v) {
        if (parameters.useLinguisticValues()) {
            if (FuzzyDistribution.isFuzzy()) {
                v = (SpaceDistribution<E>) FuzzySpaceDistribution.newLinguisticValue((SpaceDistribution<Double>) v);
            }
        }
        return v;
    }

    @Override
    public String toString() {
        return "Prediction" + element().toString();
    }
}
