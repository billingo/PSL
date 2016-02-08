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

import se.umu.cs.robotics.iteration.IterableIterator;

/**
 * Iterator over an array of Doubles.
 *
 * @author billing
 */
public class DoubleArrayIterator implements IterableIterator<Double> {

	private final double[] array;
	private int i;

	public DoubleArrayIterator(final double[] array) {
		this.array = array;
	}

	public DoubleArrayIterator(final double[] array, final int startIndex) {
		this.array = array;
		this.i = startIndex;
	}

	@Override
	public boolean hasNext() {
		return i < array.length;
	}

	@Override
	public Double next() {
		if (!hasNext())
			throw new NoSuchElementException();
		return array[i++];
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Double> iterator() {
		return this;
	}
}
