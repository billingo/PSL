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

package se.umu.cs.robotics.probabilitydistribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import se.umu.cs.robotics.statespace.StateDimension;
import se.umu.cs.robotics.statespace.StateSpace;
import se.umu.cs.robotics.utils.StringTools;

/**
 *
 * @author billing
 */
public class GaussianSpaceDistribution<E> extends AbstractSpaceDistribution<E> {

    private final ArrayList<GaussianDistribution<E>> distributions;

    public GaussianSpaceDistribution(StateSpace<E> space, GaussianDistribution<E>... distributions) {
        super(space);
        this.distributions = (ArrayList)Arrays.asList(distributions);
    }
    public GaussianSpaceDistribution(StateSpace<E> space, double[] means, double[] stds) {
        super(space);
        this.distributions = new ArrayList<GaussianDistribution<E>>(means.length);
        for (int i=0; i<means.length; i++) {
            distributions.add(new GaussianDistribution<E>(space.getDimension(i), means[i], stds[i]));
        }
    }

    public GaussianDistribution<E> getDimension(int dim) {
        return distributions.get(dim);
    }

    public GaussianDistribution<E> getDimension(StateDimension<E> dim) {
        return getDimension(stateSpace().getDimensionIndex(dim));
    }


    public String toString() {
        return "("+StringTools.join(distributions,";")+")";
    }

    public Iterator<GaussianDistribution<E>> dimensions() {
        return distributions.iterator();
    }
}
