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
