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
import se.umu.cs.robotics.iteration.position.ArrayPositionIterator;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SingleStateDistribution;
import se.umu.cs.robotics.probabilitydistribution.iteration.SingleStateDistributionPositionIterator;
import se.umu.cs.robotics.statespace.comparator.ComparableComparator;
import se.umu.cs.robotics.statespace.comparator.StateComparator;
import se.umu.cs.robotics.utils.StringTools;

/**
 * A {@link StateDimension} representing the Latin alphabet.
 * @author billing
 */
public class CharDimension implements StateDimension<Character> {

    public static final String ALPHABET = "0123456789 ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ_.,;?!*";
    public static final char DEFAULT_CHARACTER = '*';
    private static int[] ALPHABET_INDEX = null;

    public int size() {
        return ALPHABET.length();
    }

    private synchronized void generateIndex() {
        if (ALPHABET_INDEX == null) {
            ALPHABET_INDEX = new int[1024];
            int defaultId = 0;
            for (int i = 0; i < ALPHABET.length(); i++) {
                if (ALPHABET.charAt(i) == DEFAULT_CHARACTER) {
                    defaultId = i;
                    break;
                }
            }
            for (int i = 0; i < ALPHABET_INDEX.length; i++) {
                ALPHABET_INDEX[i] = defaultId;
            }
            for (int i = 0; i < ALPHABET.length(); i++) {
                ALPHABET_INDEX[ALPHABET.charAt(i)] = i;
                ALPHABET_INDEX[Character.toLowerCase(ALPHABET.charAt(i))] = i;
            }
        }
    }

    public int getIndex(Character state) {
        if (ALPHABET_INDEX == null) {
            generateIndex();
        }
        char c = state.charValue();
        return ALPHABET_INDEX[c < 256 ? c : DEFAULT_CHARACTER];
    }

    public Character defaultState() {
        return DEFAULT_CHARACTER;
    }

    public Iterator<Character> iterator() {
        return new Iterator<Character>() {

            int i = 0;

            public boolean hasNext() {
                return i < ALPHABET.length() - 1;
            }

            public Character next() {
                return new Character(ALPHABET.charAt(i++));
            }

            public void remove() {
                throw new UnsupportedOperationException("Can not remove characters from ALPHABET");
            }
        };
    }

    public PositionIterator<ProbabilityDistribution<Character>> iterator(String s) {
        final PositionIterator<Character> arrayPositionIterator = new ArrayPositionIterator<Character>(StringTools.toCharacterArray(s));
        return new SingleStateDistributionPositionIterator<Character>(this, arrayPositionIterator);
    }

    public Character getState(int position) {
        return new Character(ALPHABET.charAt(position));
    }

    public Character firstState() {
        return getState(0);
    }

    public Character lastState() {
        return getState(size() - 1);
    }

    public StateComparator<Character> comparator() {
        return new ComparableComparator<Character>();
    }
}
