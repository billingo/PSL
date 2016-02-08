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

package se.umu.cs.robotics.probabilitydistribution.iteration;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.probabilitydistribution.ArrayDistribution;
import se.umu.cs.robotics.probabilitydistribution.ArraySpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 * Iterates over the source iterator and returns probabiliy distributions, integrated over a number of course elemnts from the source. The integration range increases with integrationGradient. 
 *
 * @author billing
 */
public class IntegratingIterator<E> implements IterableIterator<SpaceDistribution<E>> {

    private final Iterator<SpaceDistribution<E>> source;
    private final double integrationGradient;
    private double integrationRange;

    public IntegratingIterator(Iterator<SpaceDistribution<E>> source, double integrationGradient) {
        this(source, integrationGradient, 0);
    }

    public IntegratingIterator(Iterator<SpaceDistribution<E>> source, double integrationGradient, double integrationRange) {
        this.source = source;
        this.integrationGradient = integrationGradient;
        this.integrationRange = integrationRange;
    }

    public Iterator<SpaceDistribution<E>> iterator() {
        return this;
    }

    public boolean hasNext() {
        return source.hasNext();
    }

    public SpaceDistribution<E> next() {
        SpaceDistribution<E> src = source.next();
        ArraySpaceDistribution<E> pd = new ArraySpaceDistribution<E>(src.stateSpace());
        pd.add(src, 1d);
        for (int i = 0; i < integrationRange && source.hasNext(); i++) {
            pd.add(source.next(), 1d);
        }
        integrationRange = stepIntegrationTime(integrationRange, integrationGradient);
        return pd;
    }

    private void addDistribution(ArraySpaceDistribution<E> myDistribution, SpaceDistribution<E> src) {
        Iterator<ArrayDistribution<E>> myDimensions = myDistribution.dimensions();
        Iterator<? extends ProbabilityDistribution<E>> srcDimensions = src.dimensions();
        while (myDimensions.hasNext()) {
            myDimensions.next().add(srcDimensions.next(), 1);
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("Remove not supported.");
    }

    public static double stepIntegrationTime(double integrationTime, double gradient) {
        if (integrationTime==0) {
            return 1;
        } else {
            return integrationTime*gradient;
        }
    }
}
