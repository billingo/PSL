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

import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 * Iterates over the source iterator and returns probabiliy distributions, 
 * integrated over a number of course elemnts from the source. The integration
 * range depends on the distance from the initial position, such that
 * i = integrationGradient^|n| where n is the number of steps from the initial
 * source position and i is the integration range.
 *
 * @author billing
 */
public class IntegratingPositionIterator<E> implements PositionIterator<SpaceDistribution<E>> {

    private IntegratingIteratorPosition<E> pos;

    public IntegratingPositionIterator(IntegratingIteratorPosition<E> pos) {
        this.pos = pos;
    }

    public IntegratingPositionIterator(IteratorPosition<SpaceDistribution<E>> pos, double integrationGradient) {
        this.pos = new IntegratingIteratorPosition<E>(pos, integrationGradient);
    }

    public PositionIterator<SpaceDistribution<E>> iterator() {
        return this;
    }

    public boolean hasNext() {
        return pos.hasNext();
    }

    public IteratorPosition<SpaceDistribution<E>> next() {
        pos = pos.getNext();
        return pos;
    }

    public void remove() {
        throw new UnsupportedOperationException("Remove not supported.");
    }

    public boolean hasPrevious() {
        return pos.hasPrevious();
    }

    public IteratorPosition<SpaceDistribution<E>> previous() {
        pos = pos.getPrevious();
        return pos;
    }

    public IteratorPosition<SpaceDistribution<E>> getPosition() {
        return pos;
    }

    @Override
    public PositionIterator<SpaceDistribution<E>> clone() {
        return new IntegratingPositionIterator<E>(pos);
    }

}
