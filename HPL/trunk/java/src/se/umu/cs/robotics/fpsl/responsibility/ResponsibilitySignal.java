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

package se.umu.cs.robotics.fpsl.responsibility;

/**
 * Implements Eq. 1 from Haruno et. al. Hierarchical MOSAIC for movement generation. 2003
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ResponsibilitySignal {

    public static final double MIN_CONFIDENCE = 0.001;
    private final double[] responsibilities;
    private final double scaling;

    public ResponsibilitySignal(int moduleCount, double scaling) {
        this.responsibilities = new double[moduleCount];
        this.scaling = scaling;
        initResponsibilities();
    }

    private void initResponsibilities() {
        for (int i = 0; i < responsibilities.length; i++) {
            responsibilities[i] = 1d / ((double) responsibilities.length);
        }
    }

    public double get(int moduleIndex) {
        return responsibilities[moduleIndex];
    }

    public void update(double... predictionError) {
        double[] v = new double[responsibilities.length];
        double sum = 0;
        for (int i = 0; i < v.length; i++) {
            final double e = predictionError[i];
            v[i] = responsibilities[i] * Math.exp(-1d * e * e / (2d * scaling * scaling));
            if (v[i] < MIN_CONFIDENCE) {
                v[i] = MIN_CONFIDENCE;
            }
            sum += v[i];
        }
        for (int i = 0; i < v.length; i++) {
            responsibilities[i] = v[i] / sum;
        }
    }
}
