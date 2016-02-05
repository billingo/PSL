/*-------------------------------------------------------------------*\
 THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com
 
 Copyright 2007 - 2009 Erik Billing
 Department of Computing Science, Umea University, Sweden,
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

package se.umu.cs.robotics.collections;

import java.util.HashMap;
import java.util.HashSet;

/**
 * HashMap that can store multiple values for each key, using a HashSet
 * 
 * @author billing
 * 
 * @param <K>
 * @param <V>
 */
public class MultiMap<K, V> extends HashMap<K, HashSet<V>> {
	private static final long serialVersionUID = 1L;

	/**
	 * @return a hash set (in case of empty value for specified key, an empty
	 *         hash set is returned)
	 */
	@Override
	public HashSet<V> get(final Object key) {
		HashSet<V> set = super.get(key);
		if (set == null) {
			return new HashSet<V>();
		}
		return set;
	}

	public void add(final K key, final V value) {
		HashSet<V> v = super.get(key);
		if (v == null) {
			v = new HashSet<V>();
			super.put(key, v);
		}
		v.add(value);
	}
}
