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

import java.util.ArrayList;
import java.util.List;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.iteration.IntegratingIterator;
import se.umu.cs.robotics.utils.StringTools;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class FLhsNode<E> implements FLhs<E> {

    private final SpaceDistribution<E> pd;
    private final FLibrary<E> library;
    private final FLhs<E> parent;
    private final List<FLhs<E>> childs = new ArrayList<FLhs<E>>();
    private final List<FHypothesis<E>> hypotheses = new ArrayList<FHypothesis<E>>();
    private final int length;
    private final double integrationRange;

    public FLhsNode(FLibrary<E> library, SpaceDistribution<E> pd) {
        this.pd = pd;
        this.library = library;
        this.parent = null;
        this.length = 1;
        this.integrationRange = 1;
    }

    public FLhsNode(FLhs<E> parent, SpaceDistribution<E> pd) {
        this.pd = pd;
        this.library = parent.getLibrary();
        this.parent = parent;
        this.length = parent.length() + 1;
        this.integrationRange = IntegratingIterator.stepIntegrationTime(parent.integrationRange(), parent.getLibrary().getParameters().getIntegrationGradient());
    }

    public boolean isRoot() {
        return parent == null;
    }

    public SpaceDistribution<E> getDistribution() {
        return pd;
    }

    public FLhs.LhsIterator<E> iterator() {
        return new FLhs.LhsIterator<E>(childs);
    }

    public List<FLhs<E>> getChildren() {
        return childs;
    }

    public FLibrary<E> getLibrary() {
        return library;
    }

    public List<FHypothesis<E>> getHypotheses() {
        return hypotheses;
    }

    public FLhs<E> getParent() {
        return parent;
    }

    public FHypothesis<E> addHypothesis(SpaceDistribution<E> target) {
        return addHypothesis(new FRhsNode<E>(this, target));
    }

    public FHypothesis<E> addHypothesis(FHypothesis<E> h) {
        hypotheses.add(h);
        return h;
    }

    public int length() {
        return length;
    }

    public double integrationRange() {
        return integrationRange;
    }

    public FLhs<E> addChild(SpaceDistribution<E> lhsDistribution) {
        final FLhsNode<E> child = new FLhsNode<E>(this, lhsDistribution);
        childs.add(child);
        return child;
    }

    public FLhs<E> getChild(SpaceDistribution<E> lhsDistribution, double intersectionThreshold) {
        FLhs<E> best = null;
        double bestValue = 0;
        for (FLhs<E> lhs : childs) {
            double intersection = lhsDistribution.intersection(lhs.getDistribution());
            if (intersection >= intersectionThreshold && (best == null || (bestValue < intersection))) {
                best = lhs;
                bestValue = intersection;
            }
        }
        return best;
    }

    public ArrayList<FLhs<E>> asList() {
        ArrayList<FLhs<E>> list = new ArrayList<FLhs<E>>(length);
        FLhs<E> lhs = this;
        do {
            list.add(lhs);
            lhs = lhs.getParent();
        } while (lhs != null);
        return list;
    }

    @Override
    public String toString() {
        if (length == 1) {
            return "[" + pd.toString() + "]";
        } else {
            StringBuilder s = new StringBuilder("[");
            FLhs<E> lhs = this;
            do {
                s.append(lhs.getDistribution().toString());
                lhs = lhs.getParent();
                if (lhs != null) {
                    s.append(",");
                }
            } while (lhs != null);
            s.append("]");
            return s.toString();
        }
    }
}
