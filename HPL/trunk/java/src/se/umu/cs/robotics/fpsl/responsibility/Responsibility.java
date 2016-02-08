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

import java.util.Arrays;
import java.util.Locale;
import se.umu.cs.robotics.fpsl.FContext;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.hpl.log.AbstractHplLogMessage;
import se.umu.cs.robotics.utils.MathTools;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class Responsibility<E> extends AbstractHplLogMessage<E> {

    private final double scaling;
    private final double minConfidence;
    private final double[] responsibilities;
    private final double[] predictionErrors;
    private final FLibrary<E> library;

    public Responsibility(FLibrary<E> library) {
        this(library, library.getParameters().responsibilityUpdateGradient());
    }

    public Responsibility(FLibrary<E> library, double scaling) {
        this.library = library;
        this.scaling = scaling;
        this.minConfidence = library.getParameters().responsibilityMinConfidence();
        this.responsibilities = new double[library.getContexts().size()];
        this.predictionErrors = null;
        initResponsibilities();
    }

    public Responsibility(Responsibility<E> priors, double... predictionErrors) {
        this.library = priors.library;
        this.scaling = priors.scaling;
        this.minConfidence = library.getParameters().responsibilityMinConfidence();
        this.responsibilities = Arrays.copyOf(priors.responsibilities, priors.responsibilities.length);
        this.predictionErrors = predictionErrors;
        updateResponsibilities();
    }

    private void initResponsibilities() {
        for (int i = 0; i < responsibilities.length; i++) {
            responsibilities[i] = 1d / ((double) responsibilities.length);
        }
    }

    private void updateResponsibilities() {
        double[] v = new double[responsibilities.length];
        double sum = 0;
        for (int i = 0; i < v.length; i++) {
            final double e = predictionErrors[i];
            v[i] = responsibilities[i] * Math.exp(-1d * e * e / (2d * scaling * scaling));
            if (v[i] < minConfidence) {
                v[i] = minConfidence;
            }
            sum += v[i];
        }
        for (int i = 0; i < v.length; i++) {
            responsibilities[i] = v[i] / sum;
        }
    }

    public double get(int context) {
        return responsibilities[context];
    }

    public double[] get() {
        return responsibilities;
    }
    
    public int maxContext() {
        return MathTools.maxIndex(responsibilities);
    }

    public Responsibility<E> next(double... predictionErrors) {
        return new Responsibility<E>(this, predictionErrors);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Responsibilities: ");
        for (int i = 0; i < responsibilities.length; i++) {
            s.append(String.format(Locale.US, "%.3f", responsibilities[i]));
            if (i < responsibilities.length - 1) {
                s.append("; ");
            }
        }
        return s.toString();
    }

    public int getContextCount() {
        return responsibilities.length;
    }

    public boolean isNan() {
        for (double r : responsibilities) {
            if (Double.isNaN(r)) {
                return true;
            }
        }
        return false;
    }

    public String toXML() {
        StringBuilder s = new StringBuilder();
        s.append(messageStartTag("ContextEvent"));
        s.append(String.format(Locale.US, "<hpl:ResponsibilitySignal updateGradient=\"%.4f\">", scaling));
        for (int c = 0; c < responsibilities.length; c++) {
            FContext context = library.getContext(c);
            if (predictionErrors == null) {
                s.append(String.format(Locale.US, "\r\n\t\t<hpl:Responsibility context=\"%s\" responsibility=\"%.4f\"/>", context.getName(), responsibilities[c]));
            } else {
                s.append(String.format(Locale.US, "\r\n\t\t<hpl:Responsibility context=\"%s\" responsibility=\"%.4f\" predictionError=\"%.4f\"/>", context.getName(), responsibilities[c],predictionErrors[c]));
            }
        }
        s.append("\r\n\t</hpl:ResponsibilitySignal>\r\n");
        s.append(messageEndTag());
        return s.toString();
    }

    public void propagateToLibrary() {
        for (int c = 0; c < responsibilities.length; c++) {
            library.getContext(c).setResponsibility(responsibilities[c]);
        }
    }
}
