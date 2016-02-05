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
