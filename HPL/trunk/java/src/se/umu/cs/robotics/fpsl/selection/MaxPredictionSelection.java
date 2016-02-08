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

package se.umu.cs.robotics.fpsl.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import se.umu.cs.robotics.probabilitydistribution.FuzzySpaceDistribution;
import org.apache.log4j.Logger;
import se.umu.cs.robotics.fpsl.FContext;
import se.umu.cs.robotics.fpsl.FHypothesis;
import se.umu.cs.robotics.fpsl.FLhs;
import se.umu.cs.robotics.fpsl.FLhsNode;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.fpsl.FPslParameters;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 * Selects hypotheses producing maximum predictability. Default selector for prediction.
 *
 * @author Erik Billing <billing@cs.umu.se>
 * @param <E>
 */
public class MaxPredictionSelection<E> extends AbstractHypothesisSelection<E> {

    static final Logger logger = Logger.getLogger(MaxPredictionSelection.class);
    static final Random rnd = new Random();
    
    private final MatchList<E> matches;
    private List<FHypothesisMatch<E>> targetHypotheses;
    private PositionIterator<SpaceDistribution<E>> data;
    private final double thresholdConfidence;
    private final FPslParameters.DefuzzyficationFunction defuzzyficationFunction;

    private SpaceDistribution<E> maxDist;
    private double maxConfidence;
    private double maxPower;
    
    private MaxPredictionSelection(FLibrary<E> library) {
        super(library);
        this.thresholdConfidence = library.getParameters().thresholdConfidence();
        this.defuzzyficationFunction = library.getParameters().defuzzyficationFunction();
        this.matches = new MatchList<>();
    }
    
    protected MaxPredictionSelection(FLibrary<E> library, PositionIterator<SpaceDistribution<E>> data) {
        this(library);
        this.data = data;
        for (FLhs<E> root : library.getRoots()) {
            matchLhsTree(root, data.clone(), 1);
        }
    }

    protected MaxPredictionSelection(FLibrary<E> library, FLhs<E> root, PositionIterator<SpaceDistribution<E>> data) {
        this(library);
        this.data = data;
        matchLhsTree(root, data.clone(), 1);
    }

    protected double matchValue(double intersection, double prior) {
        switch (library.getParameters().andOperator()) {
            case MIN:
                return Math.min(intersection, prior);
            default:
                return intersection * prior;
        }
    }

    protected void matchLhsTree(final FLhs<E> lhs, PositionIterator<SpaceDistribution<E>> data, double prior) {
        if (data.hasNext()) {
            final double value;
            final SpaceDistribution<E> dist = data.next().element();
            final double intersect = lhs.getDistribution().intersection(dist);
            value = matchValue(intersect, prior);
            if (value > 0) {
                matches.add(new ConcreteLhsMatch<>(lhs, value));
                for (FLhs<E> child : lhs.getChildren()) {
                    matchLhsTree(child, data.clone(), value);
                }
            }
        }
    }

    protected FLhsMatch<E> maxItem(FLhsMatch<E> item1, FLhsMatch<E> item2) {
        if (item1 == null) {
            return item2;
        } else if (item2 == null) {
            return item1;
        } else {
            double v1 = item1.getValue();
            double v2 = item2.getValue();
            if (v1 == v2) {
                FLhs<E> lhs1 = item1.getLhs();
                FLhs<E> lhs2 = item2.getLhs();
                return lhs1.length() > lhs2.length() ? item1 : item2;
            } else if (v1 > v2) {
                return item1;
            } else {
                return item2;
            }
        }
    }

    /**
     * Computes the fuzzy distribution based on the <b>squared</b> match and confidence values.
     * @return
     */
    @Override
    public SpaceDistribution<E> getTarget() {
        return getTarget(null);
    }

    @Override
    public SpaceDistribution<E> getTarget(FContext context) {
        switch (defuzzyficationFunction) {
            case CENTER_OF_SUM:
                return getFuzzyTarget(context);
            case MAX_PRODUCT:
                return getMaxProductTarget(context);
            case MAX_PROBABILITY:
                return getMaxProbableTarget(context);
            default:
                // A amx function
                return getMaxTarget(context);
        }
    }

    private SpaceDistribution<E> getMaxTarget(FContext context) {
        SpaceDistribution maxDistribution = null;
        double maxConfidence = 0;
        for (FHypothesisMatch<E> hMatch : getTargetHypotheses(context)) {
            FHypothesis<E> h = hMatch.getHypothesis();
            final double c;
            if (context != null) {
                c = context.getConfidenceValue(h) * context.getConfidence(h).getHits();
            } else {
                c = h.getConfidence() * h.hits();
            }
            if (c >= maxConfidence) {
                maxDistribution = h.getTarget();
                maxConfidence = c;
            }
        }
        return maxDistribution != null ? maxDistribution : new FuzzySpaceDistribution<>(library.stateSpace());
    }
    
    private SpaceDistribution<E> getMaxProductTarget(FContext context) {
        maxDist = null;
        maxPower = 0;
        maxConfidence = 0;
        for (FLhsMatch<E> hMatch : matches) {
            FLhs<E> lhs = hMatch.getLhs();
            int length = lhs.length();
            for (FHypothesis<E> h: lhs.getHypotheses()) {
                double confidence = context == null ? h.getConfidence() : context.getConfidenceValue(h);
                double power = hMatch.getValue() * hMatch.getValue() * length * confidence;
                if (power > maxPower) {
                    maxDist = h.getTarget();
                    maxPower = power;
                    maxConfidence = confidence;
                }
            }
        }
        return maxDist != null ? maxDist : new FuzzySpaceDistribution<>(library.stateSpace());
    }
    
    private SpaceDistribution<E> getMaxProbableTarget(FContext context) {
        maxDist = null;
        maxPower = 0;
        maxConfidence = 0;
        ArrayList<Double> wheel = new ArrayList<>();
        ArrayList<SpaceDistribution<E>> dists = new ArrayList<>();
        double wheelsum = 0;
        
        for (FLhsMatch<E> hMatch : matches) {
            FLhs<E> lhs = hMatch.getLhs();
            int length = lhs.length();
            for (FHypothesis<E> h: lhs.getHypotheses()) {
                double confidence = context == null ? h.getConfidence() : context.getConfidenceValue(h);
                double power = hMatch.getValue() * hMatch.getValue() * length * confidence;
                power = power * power;
                if (power > maxPower) {
                    maxDist = h.getTarget();
                    maxPower = power;
                    maxConfidence = confidence;
                }
                wheel.add(power);
                dists.add(h.getTarget());
                wheelsum += power;
            }
        }
        
        double win = rnd.nextDouble()*wheelsum;
        for (int i=0; i<wheel.size(); i++) {
            if (wheel.get(i)>=win) {
                return dists.get(i);
            }
        }
                
        return new FuzzySpaceDistribution<>(library.stateSpace());
    }
    
    public double getMaxConfidence() {
        return maxConfidence;
    }
    
    public double getMaxPower() {
        return maxPower;
    }

    private FuzzySpaceDistribution<E> getFuzzyTarget(FContext context) {
        FuzzySpaceDistribution<E> target = new FuzzySpaceDistribution<>(library.stateSpace());
        for (FHypothesisMatch<E> hMatch : getTargetHypotheses(context)) {
            FHypothesis<E> h = hMatch.getHypothesis();
            final double c;
            if (context != null) {
                c = context.getConfidenceValue(h) * context.getConfidence(h).getHits();
            } else {
                c = h.getConfidence() * h.hits();
            }
            final double v = hMatch.getValue();
            target.add(h.getTarget(), c * v);
        }
        return target;
    }

    @Override
    public List<FHypothesisMatch<E>> getTargetHypotheses() {
        return getTargetHypotheses(null);
    }

    @Override
    public List<FHypothesisMatch<E>> getTargetHypotheses(FContext context) {
        //FIX!
        if (context == null && targetHypotheses != null) {
            return targetHypotheses;
        }

        ArrayList<FHypothesisMatch<E>> hypotheses = new ArrayList<FHypothesisMatch<E>>();

        int length = matches.maxLhsLength();
        do {
            ArrayList<FLhsMatch<E>> maxMatches = matches.maxMatches(length);

            for (FLhsMatch<E> match : maxMatches) {
                FLhs<E> lhs = match.getLhs();
                for (FHypothesis<E> h : lhs.getHypotheses()) {
                    final double c;
                    if (context != null) {
                        c = context.getConfidenceValue(h);
                    } else {
                        c = h.getConfidence();
                    }
                    if (c > thresholdConfidence) {
                        final ConcreteHypothesisMatch<E> hMatch = new ConcreteHypothesisMatch<E>(h, match.getValue());
                        hypotheses.add(hMatch);
                        logger.debug(hMatch);
                    }
                }
            }
            length--;
        } while (length > 0 && hypotheses.isEmpty());

        if (context == null) {
            targetHypotheses = hypotheses;
        }
        return hypotheses;
    }

    public ArrayList<FHypothesis<E>> updateConfidences(SpaceDistribution<E> targetDistribution) {
        return updateConfidences(targetDistribution, null);
    }

    public ArrayList<FHypothesis<E>> updateConfidences(SpaceDistribution<E> targetDistribution, FContext context) {
        ArrayList<FHypothesis<E>> createdHypotheses = new ArrayList<FHypothesis<E>>(0);

        for (FLhsMatch<E> match : matches) {
            final FLhs<E> lhs = match.getLhs();
            for (FHypothesis<E> h : lhs.getHypotheses()) {
                double intersection = targetDistribution.intersection(h.getTarget());
                double v = match.getValue();
//                v = v * v;
                final double changeHit = intersection * v;
                final double changeMiss = (1 - intersection) * v;

                if (context == null) {
                    h.hit(changeHit);
                    h.miss(changeMiss);
                } else {
                    context.hit(h, changeHit);
                    context.miss(h, changeMiss);
                }
            }
        }
        return createdHypotheses;
    }

    private FHypothesis<E> growMatchingHypothesis(FHypothesis<E> hypothesis) {
        double thresholdCorrect = library.getParameters().thresholdCorrect();
        SpaceDistribution<E> rhsDistribution = hypothesis.getTarget();
        FHypothesis<E> parent = null;
        FLhsMatch<E> root = null;
        for (FLhsMatch<E> match : matches) {
            FLhs<E> lhs = match.getLhs();
            if (match.getValue() >= thresholdCorrect) {
                if (lhs.length() < hypothesis.length()) {
                    if (lhs.isRoot() && (root == null || match.getValue() > root.getValue())) {
                        root = match;
                    }
                    for (FHypothesis<E> h : lhs.getHypotheses()) {
                        if (rhsDistribution.intersection(h.getTarget()) >= thresholdCorrect) {
                            if (parent == null || parent.length() < h.length()) {
                                parent = h;
                            }
                        }
                    }
                }
            }
        }
        if (parent != null) {
            final PositionIterator<SpaceDistribution<E>> history = data.clone();
            for (int i = 0; i < parent.length(); i++) {
                history.next();
            }
            final SpaceDistribution<E> e = history.next().element();
            return parent.addChild(e, rhsDistribution);
        } else if (root != null) {
            FLhs<E> lhs = root.getLhs();
            return lhs.addHypothesis(rhsDistribution);
        }
        return null;
    }

    private FLhs<E> getMatchingLhs(final int length) {
        double thresholdCorrect = library.getParameters().thresholdCorrect();
        FLhsMatch<E> match = bestMatch(length, thresholdCorrect, true);
        final FLhs<E> lhs;
        if (match == null) {
            lhs = new FLhsNode<E>(library, data.clone().next().element());
        } else if (match.getLhs().length() < length) {
            final PositionIterator<SpaceDistribution<E>> history = data.clone();
            for (int i = 0; i < match.getLhs().length(); i++) {
                history.next();
            }
            lhs = new FLhsNode<E>(match.getLhs(), history.next().element());
        } else {
            lhs = match.getLhs();
        }
        return lhs;
    }

    public boolean isCorrect(SpaceDistribution<E> targetDistribution) {
        return targetDistribution.intersection(getTarget()) > library.getParameters().thresholdCorrect();
    }

    public MatchList<E> getMatches() {
        return matches;
    }

    public List<FLhsMatch<E>> getMatches(int length) {
        return matches.get(length);
    }

    /**
     * @param length consider only lhs of specified length
     * @return the best matching lhs with specified length
     */
    private FLhsMatch<E> bestMatch(int length, double minMatch, boolean useShorterIfMissing) {
        FLhsMatch<E> bestMatch = null;
        for (FLhsMatch<E> match : matches) {
            if (match.getValue() >= minMatch && (bestMatch == null || bestMatch.getValue() < match.getValue())) {
                bestMatch = match;
            }
        }
        if (bestMatch == null && useShorterIfMissing && length > 1) {
            return bestMatch(length - 1, minMatch, useShorterIfMissing);
        } else {
            return bestMatch;
        }
    }

    @Override
    public String toString() {
        return matches.toString();
    }

    public int size() {
        return matches.size();
    }
}
