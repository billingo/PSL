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

import java.util.Map;

public interface Histogram<K> extends Map<K, Double> {

	/**
	 * Returns the value stored for specified key, or 0 if this map contains no
	 * mapping for the key. If normalized is true, the returned value is
	 * normalized such that the sum of all values stored values equals 1.
	 * 
	 * @param key
	 * @param noramlized specifies whether the returned value is to be
	 *            normalized or not
	 * @return the value for specified key
	 */
	Double get(final Object key, final boolean noramlized);

	/**
	 * @return the sum of all stored values (unnormalized)
	 */
	double getSum();
}
