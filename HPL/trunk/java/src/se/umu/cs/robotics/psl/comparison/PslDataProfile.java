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


package se.umu.cs.robotics.psl.comparison;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.LinkedReverseIterator;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.hpl.ContextWeight;
import se.umu.cs.robotics.psl.Hypothesis;
import se.umu.cs.robotics.psl.HypothesisList;
import se.umu.cs.robotics.psl.Library;
import se.umu.cs.robotics.psl.PslNode;

/**
 * Describes occurrence frequencies for all hypothesizes in a library. This
 * frequency vector constitutes a profile for given data sequence that can be
 * used to compare data files.
 * 
 * @author billing
 * 
 */
public class PslDataProfile<E> extends HashMap<Hypothesis<E>, ContextWeight> {
	private static final long serialVersionUID = 1L;

	private double vectorSum = 0;

	private final Library<E> library;

	public PslDataProfile(final Library<E> lib) {
		this.library = lib;
	}

	public PslDataProfile(final Library<E> lib, final PositionIterator<E> data) {
		super();
		this.library = lib;
		calculateFrequencyVector(data);
	}

	public PslDataProfile(final List<PslDataProfile<E>> srcProfiles) throws PslProfileException {
		super();

		if (srcProfiles.size() > 0) {
			library = srcProfiles.get(0).library;
		} else {
			throw new PslProfileException("Must instantiate form at least one source profile!");
		}
		for (PslDataProfile<E> p : srcProfiles) {
			if (p.library != library) {
				throw new PslProfileException("Error while creating joint profile! Can't combine profiles from several libraries.");
			}
			for (Entry<Hypothesis<E>, ContextWeight> e : p.entrySet()) {
				add(e.getKey(), e.getValue().getValue());
			}
		}
	}

	/**
	 * Increases the count of specified {@link PslNode} h.
	 * 
	 * @param h
	 */
	public void add(final Hypothesis<E> h, final double value) {
		ContextWeight count = get(h);
		if (count == null) {
			count = new ContextWeight();
			put(h, count);
		}
		count.add(value);
		vectorSum += value;
	}

	private void calculateFrequencyVector(final PositionIterator<E> data) {
		for (IteratorPosition<E> p : data) {
			HypothesisList<E> match = library.match(new LinkedReverseIterator<E>(data.clone()));
			// System.out.println(i + ": " + match.toString());
			updateFrequencyVector(match);
		}
	}

	private void updateFrequencyVector(final HypothesisList<E> comp) {
		Hypothesis<E> h = comp.getBest();
		if (h != null) {
			double strength = 1;
			while (h.length() > 1) {
				add(h, strength);
				strength = strength / 2d;
				h = h.getParent();
				/*
				 * Remove this break to activate parent freq. calculation with
				 * falloff
				 */
				break;
			}
		}
	}

	public double[] getVector() {
		double[] v = new double[library.size()];
		int i = 0;
		for (Hypothesis<E> h : library) {
			if (h.length() > 0) {
				ContextWeight count = get(h);
				v[i++] = count == null ? 0d : count.getValue() / vectorSum;
				// v[i++] = count == null ? 0d : count.getValue();
			}
		}
		return v;
	}

	public double relation(final PslDataProfile<E> d) {
		double r = 0;
		if (d.library == library) {
			for (Entry<Hypothesis<E>, ContextWeight> e : entrySet()) {
				double v = e.getValue().getValue() / vectorSum;
				double dv = d.get(e.getKey()).getValue() / d.vectorSum;
				r += Math.min(v, dv);
			}
		}
		return r;
	}

}
