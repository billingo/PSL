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
package se.umu.cs.robotics.utils;

public class MathTools {

    public static int max(final int... values) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < values.length; i++) {
            max = max > values[i] ? max : values[i];
        }
        return max;
    }
    
    public static double max(final double... values) {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < values.length; i++) {
            max = max > values[i] ? max : values[i];
        }
        return max;
    }
    
    public static int maxIndex(final double... values) {
        double max = Double.MIN_VALUE;
        int pos = -1;
        for (int i = 0; i < values.length; i++) {
            if (max < values[i]) {
                max = values[i];
                pos = i;
            }
        }
        return pos;
    }

    public static double mean(final double... values) {
        return sum(values) / values.length;
    }

    public static double mean(final int... values) {
        return ((double) sum(values)) / ((double) values.length);
    }

    public static double mean(final Iterable<?> values) {
        double sum = 0;
        int count = 0;
        for (Object v : values) {
            if (v == null) {
                continue;
            } else if (v instanceof Double) {
                sum += (Double) v;
            } else if (v instanceof Integer) {
                sum += (Integer) v;
            } else if (v instanceof Long) {
                sum += (Long) v;
            } else if (v instanceof Iterable) {
                sum += mean((Iterable<?>) v);
            }
            count++;
        }
        return sum / count;
    }

    public static double mean(final Object... values) {
        double sum = 0;
        int count = values.length;
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Double) {
                sum += (Double) values[i];
            } else if (values[i] instanceof Object[]) {
                sum += mean((Object[]) values[i]);
            } else if (values[i] instanceof Iterable) {
                sum += mean((Iterable<?>) values[i]);
            }
        }
        return sum / count;
    }

    public static double sum(final double... values) {
        double s = 0;
        for (double v : values) {
            s += v;
        }
        return s;
    }

    public static int sum(final int... values) {
        int s = 0;
        for (int v : values) {
            s += v;
        }
        return s;
    }

    public static double sum(final Iterable<?> values) {
        double sum = 0;
        for (Object o : values) {
            if (o instanceof Number) {
                sum += ((Number) o).doubleValue();
            } else {
                throw new IllegalArgumentException(o + " is not a number");
            }
        }
        return sum;
    }

    public static double log(final double x, final double base) {
        if (base == 10) {
            return Math.log10(x);
        }
        if (base == Math.E) {
            return Math.log(x);
        }
        return Math.log(x) / Math.log(base);
    }

    /**
     * Correct (MATLAB) implementation of mod
     */
    public static double mod(final double x, final double y) {
        return x - Math.floor(x / y) * y;
    }

    public static double rad2deg(final double rad) {
        return rad / Math.PI * 180;
    }

    public static double deg2rad(final double deg) {
        return deg / 180 * Math.PI;
    }

    public static double euclideanDistance(final double dx, final double dy) {
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double euclideanDistance(final double x1, final double y1, final double x2, final double y2) {
        return euclideanDistance(x1 - x2, y1 - y2);
    }

    /**
     * Computes the Wilson score confidence of positive and negative ratings
     *
     * @param pos the number of positive ratings
     * @param n the total number of ratings
     * @param power the confidence value (0.1 -> 95%)
     * @return the wilson socre
     */
    public static double wilsonScore(final double pos, final double n, final double power) {
        double x = 1d - power / 2d;
        double mean = 0d;
        double std = 1d;
        double z = (x - mean) / std;
        double phat = pos / n;
        return (phat + z * z / (2d * n) - z * Math.sqrt((phat * (1 - phat) + z * z / (4d * n)) / n)) / (1 + z * z / n);
    }
}
