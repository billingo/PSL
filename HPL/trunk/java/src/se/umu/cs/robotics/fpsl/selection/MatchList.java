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

package se.umu.cs.robotics.fpsl.selection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import se.umu.cs.robotics.fpsl.FLhs;

/**
 * A list of matching elements sorted after lhs length, with the first element 
 * beeing the rightmost lhs element. 
 *
 * @author Erik Billing <erik.billing@his.se>
 */
public class MatchList<E> implements Iterable<FLhsMatch<E>> {

    private final ArrayList<ArrayList<FLhsMatch<E>>> matches = new ArrayList<>();

    public boolean add(FLhsMatch<E> match) {
        final int length = match.getLhs().length();
        while (matches.size() <= length) {
            matches.add(new ArrayList<FLhsMatch<E>>());
        }
        return matches.get(length).add(match);
    }

    public int size() {
        return matches.size();
    }

    public ArrayList<FLhsMatch<E>> get(int length) {
        return matches.get(length);
    }

    public int maxLhsLength() {
        return matches.size() - 1;
    }

    public ArrayList<FLhsMatch<E>> maxMatches(int length) {
        ArrayList<FLhsMatch<E>> maxMatches = new ArrayList<>();
        double maxMatch = 0;
        if (length >= 0 && matches.size() > length) {
            for (FLhsMatch<E> match : matches.get(length)) {
                FLhs<E> lhs = match.getLhs();
                if (maxMatch < match.getValue()) {
                    maxMatch = match.getValue();
                    maxMatches.clear();
                }
                if (maxMatch == match.getValue()) {
                    maxMatches.add(match);
                }
            }
        }
        return maxMatches;
    }

    @Override
    public Iterator<FLhsMatch<E>> iterator() {
        final Iterator<ArrayList<FLhsMatch<E>>> i = matches.iterator();
        return new Iterator<FLhsMatch<E>>() {

            private Iterator<FLhsMatch<E>> ci = null;

            private void nextIterator() {
                if (ci == null || !ci.hasNext()) {
                    ci = i.next().iterator();
                }
            }

            @Override
            public boolean hasNext() {
                try {
                    nextIterator();
                    return true;
                } catch (NoSuchElementException ex) {
                    return false;
                }
            }

            @Override
            public FLhsMatch<E> next() {
                nextIterator();
                return ci.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
}
