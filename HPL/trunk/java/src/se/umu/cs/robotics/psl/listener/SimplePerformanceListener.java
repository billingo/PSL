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
package se.umu.cs.robotics.psl.listener;

import se.umu.cs.robotics.psl.Hypothesis;

public class SimplePerformanceListener<E> implements PerformanceListener<E> {
	private int hits;
	private int misses;

	@Override
	public void hit(final Hypothesis<E> h) {
		hits++;
	}

	@Override
	public void miss(final Hypothesis<E> h) {
		misses++;
	}

	public int getHits() {
		return hits;
	}

	public int getMisses() {
		return misses;
	}

	public double getPerformance() {
		return hits == 0 ? 0 : (double) hits / (double) (hits + misses);
	}

	public void reset() {
		hits = 0;
		misses = 0;
	}

	public int getTotalCount() {
		return hits + misses;
	}
}
