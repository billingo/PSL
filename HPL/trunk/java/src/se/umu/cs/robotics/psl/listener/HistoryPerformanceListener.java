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

import java.util.ArrayList;

import se.umu.cs.robotics.psl.Hypothesis;

public class HistoryPerformanceListener<E> extends SimplePerformanceListener<E> {

	private final ArrayList<Double> performances = new ArrayList<Double>();

	@Override
	public void hit(final Hypothesis<E> h) {
		super.hit(h);
		performances.add(1d);
	}

	@Override
	public void miss(final Hypothesis<E> h) {
		super.miss(h);
		performances.add(0d);
	}

	public double[] getAccumulatedPerformance() {
		double[] p = new double[performances.size()];
		int i = 0;
		double acc = 0;
		for (Double d : performances) {
			acc = (acc * i) / (i + 1d);
			acc += d / (i + 1d);
			p[i] = acc;
			i++;
		}
		return p;
	}

	@Override
	public void reset() {
		super.reset();
		performances.clear();
	}
}
