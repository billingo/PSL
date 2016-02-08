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
import se.umu.cs.robotics.fpsl.FContext;
import se.umu.cs.robotics.fpsl.FHypothesis;
import se.umu.cs.robotics.fpsl.FLhs;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.probabilitydistribution.FuzzySpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 * @author Erik Billing <erik.billing@his.se>
 */
public class MaxProductSelection<E> extends AbstractHypothesisSelection<E> {

    private final static Random rnd = new Random();
    private final MatchList<E> matches;
    private final double thresholdCorrect;
    
    private MaxProductSelection(FLibrary<E> library) {
        super(library);
        this.matches = new MatchList<>();
        this.thresholdCorrect = library.getParameters().thresholdCorrect();
    }
    
    protected MaxProductSelection(FLibrary<E> library, PositionIterator<SpaceDistribution<E>> data) {
        this(library);
        for (FLhs<E> root : library.getRoots()) {
            matchLhsTree(root, data.clone(), 1);
        }
    }

    protected MaxProductSelection(FLibrary<E> library, FLhs<E> root, PositionIterator<SpaceDistribution<E>> data) {
        this(library);
        matchLhsTree(root, data.clone(), 1);
    }
    
    private void matchLhsTree(final FLhs<E> lhs, PositionIterator<SpaceDistribution<E>> data, double prior) {
        if (data.hasNext()) {
            final double value;
            final SpaceDistribution<E> dist = data.next().element();
            final double intersect = lhs.getDistribution().intersection(dist);
            value = Math.min(intersect, prior);
            if (value > 0) {
                matches.add(new ConcreteLhsMatch<>(lhs, value));
                for (FLhs<E> child : lhs.getChildren()) {
                    matchLhsTree(child, data.clone(), value);
                }
            }
        }
    }
    
    @Override
    public SpaceDistribution<E> getTarget() {
        return getTarget(null);
    }

    @Override
    public SpaceDistribution<E> getTarget(FContext context) {
//        List<FHypothesisMatch<E>> targetHypotheses = getTargetHypotheses(context);
//        FHypothesisMatch<E> targetMatch = selectMatch(targetHypotheses);
//        return targetMatch == null ? new FuzzySpaceDistribution<>(library.stateSpace()) : targetMatch.getHypothesis().getTarget();
        SelectionWheel<E> hypotheses = getAllHypotheses(context);
        SpaceDistribution<E> target = hypotheses.getTarget();
        return target == null ? new FuzzySpaceDistribution<>(library.stateSpace()) : target;
    }
    
    public FHypothesisMatch<E> selectMatch(List<FHypothesisMatch<E>> targetHypotheses) {
        switch (targetHypotheses.size()) {
            case 0:
                return null;
            case 1:
                return targetHypotheses.get(0);
            default:
                return targetHypotheses.get(rnd.nextInt(targetHypotheses.size()));
        }
    }

    @Override
    public SelectionWheel<E> getTargetHypotheses() {
        return getTargetHypotheses(null);
    }

    @Override
    public SelectionWheel<E> getTargetHypotheses(FContext context) {
        SelectionWheel<E> targets = new SelectionWheel<>();
        double maxPower = 0;
        
        for (FLhsMatch<E> hMatch : matches) {
            FLhs<E> lhs = hMatch.getLhs();
            int length = lhs.length();
            double value = hMatch.getValue();
            for (FHypothesis<E> h: lhs.getHypotheses()) {
                double confidence = context == null ? h.getConfidence() : context.getConfidenceValue(h);
                double power = value * value * length * confidence;
                if (power > maxPower) {
                    targets.clear();
                    maxPower = power;
                }
                if (power >= maxPower) {
                    targets.add(new MaxProductMatch<>(h,value,confidence,power));
                }
            }
        }
        return targets;
    }
    
    public SelectionWheel<E> getAllHypotheses(FContext context) {
        SelectionWheel<E> targets = new SelectionWheel<>();
        
        for (FLhsMatch<E> hMatch : matches) {
            FLhs<E> lhs = hMatch.getLhs();
            int length = lhs.length();
            double value = hMatch.getValue();
            for (FHypothesis<E> h: lhs.getHypotheses()) {
                double confidence = context == null ? h.getConfidence() : context.getConfidenceValue(h);
                double power = value * value * length * confidence;
                targets.add(new MaxProductMatch<>(h,value,confidence,power));
            }
        }
        return targets;
    }

    @Override
    public MatchList<E> getMatches() {
        return matches;
    }

    @Override
    public List<FLhsMatch<E>> getMatches(int length) {
        return matches.get(length);
    }

    @Override
    public String toString() {
        return matches.toString();
    }

    @Override
    public int size() {
        return matches.size();
    }

    @Override
    public ArrayList<FHypothesis<E>> updateConfidences(SpaceDistribution<E> targetDistribution) {
        return updateConfidences(targetDistribution, null);
    }

    @Override
    public ArrayList<FHypothesis<E>> updateConfidences(SpaceDistribution<E> targetDistribution, FContext context) {
        ArrayList<FHypothesis<E>> createdHypothesis = new ArrayList<>();
        for (FLhsMatch<E> match : matches) {
            final FLhs<E> lhs = match.getLhs();
            for (FHypothesis<E> h : lhs.getHypotheses()) {
                double intersection = targetDistribution.intersection(h.getTarget());
                double v = match.getValue();
                v = v * v;
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
        return createdHypothesis;
    }

    @Override
    public boolean isCorrect(SpaceDistribution<E> targetDistribution) {
        return targetDistribution.intersection(getTarget()) > thresholdCorrect;
    }
    
}
