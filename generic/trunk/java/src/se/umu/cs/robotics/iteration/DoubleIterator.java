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

package se.umu.cs.robotics.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator over an range of Doubles.
 * 
 * @author billing
 */
public class DoubleIterator implements IterableIterator<Double> {

    private double v = 0;
    private final double start;
    private final double stop;
    private final double step;

    public DoubleIterator(final double start, final double stop) {
        this.start = start;
        this.stop = stop;
        this.step = 1;
        this.v = start;
    }

    public DoubleIterator(final double start, final double stop, final double step) {
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.v = start;
    }

    @Override
    public Iterator<Double> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return v <= stop;
    }

    @Override
    public Double next() {
        if (hasNext()) {
            double curv = v;
            v+=step;
            return curv;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
