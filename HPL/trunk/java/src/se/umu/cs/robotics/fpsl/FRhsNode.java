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

import java.util.Locale;
import org.apache.log4j.Logger;
import se.umu.cs.robotics.fpsl.log.HypothesisCreated;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class FRhsNode<E> implements FHypothesis<E> {

    final static Logger logger = Logger.getLogger(FRhsNode.class);
    
    /*
     * The hypothesis condition element (left hand side)
     */
    private final SpaceDistribution<E> targetDistribution;
    private final FLhs<E> lhs;
    private final FHypothesis<E> parent;
    /*
     * Each Hypothesis gains its own id upon creation. Similar to hash, but more
     * readable.
     */
    private final int id;
    private static int idCount = 0;

    public FRhsNode(FLhs<E> lhs, SpaceDistribution<E> pd) {
        if (lhs.isRoot()) {
            this.lhs = lhs;
            this.parent = null;
            this.targetDistribution = pd;
            this.id = idCount++;
        } else {
            throw new IllegalArgumentException("LHS must be a root node!");
        }
        logger.info(new HypothesisCreated<E>(this));
    }

    private FRhsNode(FLhs<E> lhs, SpaceDistribution<E> pd, FRhsNode<E> parent) {
        this.lhs = lhs;
        this.parent = parent;
        this.targetDistribution = pd;
        this.id = idCount++;
        logger.info(new HypothesisCreated<E>(this));
    }

    public int getId() {
        return id;
    }

    public SpaceDistribution<E> getDistribution() {
        return lhs.getDistribution();
    }

    public SpaceDistribution<E> getTarget() {
        return targetDistribution;
    }

    public FLibrary<E> getLibrary() {
        return lhs.getLibrary();
    }

    public int length() {
        return lhs.length();
    }

    public void hit(double value) {
        getLibrary().hypothesisHit(this, value);
    }

    public void miss(double value) {
        getLibrary().hypothesisMiss(this, value);
    }

    public double getConfidence() {
        return getLibrary().getConfidence(this);
    }

    public double hits() {
        return getLibrary().getHypothesisHits(this);
    }

    public double misses() {
        return getLibrary().getHypothesisMisses(this);
    }

    public double integrationRange() {
        return lhs.integrationRange();
    }

    public FLhs getLhs() {
        return lhs;
    }

    public FHypothesis<E> getParent() {
        return parent;
    }

    public FHypothesis<E> addChild(SpaceDistribution<E> lhsDistribution, SpaceDistribution<E> targetDistribution) {
        double thresholdCorrect = getLibrary().getParameters().thresholdCorrect();
        if (targetDistribution.intersection(this.targetDistribution)<thresholdCorrect) {
            throw new IllegalArgumentException("Child target distribution must intersect parent's target!");
        }
        FLhs<E> lhsChild = lhs.getChild(lhsDistribution, thresholdCorrect);
        if (lhsChild == null) {
            lhsChild = lhs.addChild(lhsDistribution);
        }
        return lhsChild.addHypothesis(new FRhsNode<E>(lhsChild,targetDistribution,this));
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "H%.2f:%s -> %s",getConfidence(),lhs.toString(),targetDistribution.toString());
    }
    
}
