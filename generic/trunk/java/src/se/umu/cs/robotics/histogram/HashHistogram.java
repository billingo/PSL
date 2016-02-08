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


package se.umu.cs.robotics.histogram;

import java.util.HashMap;

public class HashHistogram<K> extends HashMap<K, Double> implements Histogram<K> {
	private static final long serialVersionUID = 1L;

	private double sum;

	public HashHistogram() {
		super();
	}

	@Override
	public Double get(final Object key) {
		return get(key, false);
	}

	public Double get(final Object key, final boolean normalized) {
		Double v = super.get(key);
		if (normalized) {
			return v == null || sum <= 0 ? 0d : v / sum;
		} else {
			return v == null ? 0d : v;
		}
	}

	@Override
	public Double put(final K key, final Double value) {
		Double v = super.put(key, value);
		this.sum += value - (v == null ? 0d : v);
		return v;
	}

	public double getSum() {
		return sum;
	}

	@Override
	public void clear() {
		super.clear();
		sum = 0;
	}
}
