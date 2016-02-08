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

package se.umu.cs.robotics.iteration.position;

/**
 *
 * @author billing
 */
public abstract class AbstractIteratorPositionDelegate<E, S> implements IteratorPosition<E> {

    private final IteratorPosition<S> pos;

    public AbstractIteratorPositionDelegate(IteratorPosition<S> pos) {
        this.pos = pos;
    }

    public boolean hasNext() {
        return pos.hasNext();
    }

    public boolean hasPrevious() {
        return pos.hasPrevious();
    }

    public IteratorPosition<S> sourcePosition() {
        return pos;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == getClass()) {
            return ((AbstractIteratorPositionDelegate) pos).equals(pos);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return pos.hashCode() ^ 5001;
    }
}
