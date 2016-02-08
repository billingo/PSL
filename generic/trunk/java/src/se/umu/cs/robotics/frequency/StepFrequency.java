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
package se.umu.cs.robotics.frequency;

import java.util.Iterator;
import java.util.NoSuchElementException;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.StateSpace;

/**
 * A frequency that varies between the states in the statespace starting with
 * state index 0 and steps up to the maximum state index levels, changing state
 * every stepLength step.
 *
 * E.g., StepFrequency(IntegerSpace,20,2,3) produce 00112211001122110011.
 *
 * @author Erik Billing <billing@cs.umu.se>
 * @param <E>
 */
public class StepFrequency<E> implements Frequency<E> {

    private StateSpace<E> space;
    private int level = 0;
    private int step = 0;
    private final int stepLength;
    private final int length;
    private final int maxLevel;
    private int levelChange = 1;

    public StepFrequency(StateSpace<E> space, final int length, final int stepLength, final int levels) {
        this.space = space;
        this.length = length;
        this.stepLength = stepLength;
        this.maxLevel = levels - 1;
    }

    public StateSpace<E> stateSpace() {
        return space;
    }

    @Override
    public boolean hasNext() {
        return step < length;
    }

    @Override
    public SpaceDistribution<E> next() {
        if (hasNext()) {
            if (step > 0 && step % stepLength == 0) {
                level += levelChange;
                if (level % maxLevel == 0) {
                    levelChange = levelChange * -1;
                } else if (level > maxLevel) {
                    level = 0;
                }
            }
            int[] ids = new int[space.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = level;
            }
            step++;
            return space.newDistribution(ids);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
    }

    public int getCurrentStep() {
        return step;
    }

    public int getCurrentLevel() {
        return level;
    }

    public int getStepLength() {
        return stepLength;
    }

    public int getLevelCount() {
        return maxLevel + 1;
    }

    public int length() {
        return length;
    }

    public Iterator<SpaceDistribution<E>> iterator() {
        return this;
    }
}
