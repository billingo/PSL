/*
 *  Copyright (C) 2010 Erik Billing <billing@cs.umu.se>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.umu.cs.robotics.frequency;

import java.util.Iterator;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.StateSpace;

public class SpikeFrequency<E> implements Frequency<E> {

    private Frequency<E> source;
    private SpaceDistribution<E> last;
    private SpaceDistribution<E> interSpikeDistribution;

    public SpikeFrequency(Frequency<E> source, SpaceDistribution<E> interSpikeDistribution) {
        this.source = source;
        this.interSpikeDistribution = interSpikeDistribution;
    }

    @Override
    public SpaceDistribution<E> next() {
        SpaceDistribution<E> d = source.next();
        if (last == null || last.intersection(d)<1) {
            last = d;
            return d;
        } else {
            return interSpikeDistribution;
        }
    }

    public StateSpace<E> stateSpace() {
        return source.stateSpace();
    }

    public Iterator<SpaceDistribution<E>> iterator() {
        return this;
    }

    public boolean hasNext() {
        return source.hasNext();
    }

    public void remove() {
        source.remove();
    }
}
