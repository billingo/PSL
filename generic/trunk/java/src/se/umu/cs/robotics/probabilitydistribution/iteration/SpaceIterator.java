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
package se.umu.cs.robotics.probabilitydistribution.iteration;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.StateSpace;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SpaceIterator<E> implements IterableIterator<SpaceDistribution<E>> {

    private StateSpace<E> space;
    private Iterator<ProbabilityDistribution<E>>[] sources;

    public SpaceIterator(StateSpace<E> stateSpace, Iterator<ProbabilityDistribution<E>>... sources) {
        this.space = stateSpace;
        this.sources = sources;
    }

    public Iterator<SpaceDistribution<E>> iterator() {
        return this;
    }

    public boolean hasNext() {
        for (Iterator<ProbabilityDistribution<E>> i : sources) {
            if (!i.hasNext()) {
                return false;
            }
        }
        return true;
    }

    public SpaceDistribution<E> next() {
        ProbabilityDistribution[] distributions = new ProbabilityDistribution[space.size()];
        for (int i = 0; i < distributions.length; i++) {
            distributions[i] = sources[i].next();
        }
        return space.newDistribution(distributions);
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }
}
