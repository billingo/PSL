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

package se.umu.cs.robotics.statespace;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.ArrayIterator;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.probabilitydistribution.SingleStateSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.iteration.SpaceIterator;
import se.umu.cs.robotics.probabilitydistribution.iteration.SpacePositionIterator;
import se.umu.cs.robotics.utils.StringTools;

/**
 * A state space consisting of {@link CharDimension}s.
 *
 * @author Erik Billing
 */
public class CharSpace extends AbstractStateSpace<Character> {

    private final CharDimension[] dimensions;

    public CharSpace(CharDimension... dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public Iterator<StateDimension<Character>> iterator() {
        return new ArrayIterator<StateDimension<Character>>(dimensions);
    }

    public PositionIterator<SpaceDistribution<Character>> iterator(String... s) {
        if (s.length != dimensions.length) {
            throw new IllegalArgumentException("Must have exactly one string per dimension");
        }
        PositionIterator[] iterators = new PositionIterator[dimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            iterators[i] = dimensions[i].iterator(s[i]);
        }
        return new SpacePositionIterator<Character>(this,iterators);
    }

    @Override
    public int size() {
        return dimensions.length;
    }

    @Override
    public int getDimensionIndex(StateDimension<Character> dim) {
        for (int i = 0; i < dimensions.length; i++) {
            if (dimensions[i].equals(dim)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public StateDimension<Character> getDimension(int id) {
        return dimensions[0];
    }

    public SingleStateSpaceDistribution<Character> newSingleState(Character... chars) {
        return new SingleStateSpaceDistribution<Character>(this, chars);
    }

    public SingleStateSpaceDistribution<Character> newSingleState(char... chars) {
        return newSingleState(StringTools.toCharacterArray(chars));
    }

    public SingleStateSpaceDistribution<Character> newSingleState(String s) {
        return newSingleState(StringTools.toCharacterArray(s));
    }

    private Character getState(int dim, int[] stateIds) {
        if (stateIds.length == 0) {
            return dimensions[dim].defaultState();
        } else {
            return dimensions[dim].getState(stateIds[dim]);
        }
    }
}
