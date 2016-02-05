/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright 2007 - 2009 Erik Billing
Department of Computing Science, Umeå University, Sweden,
(www.cs.umu.se/~billing/).                               

LICENSE:

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
MA 02111-1307, USA.
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
