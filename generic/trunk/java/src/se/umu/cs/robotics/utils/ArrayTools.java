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

package se.umu.cs.robotics.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArrayTools {

    /**
     * @param values
     * @return index of the maximum value in values
     */
    public static int max(final double... values) {
        if (values == null || values.length == 0) {
            return -1;
        }
        double maxv = values[0];
        int maxi = 0;
        for (int i = 1; i < values.length; i++) {
            if (maxv < values[i]) {
                maxv = values[i];
                maxi = i;
            }
        }
        return maxi;
    }

    /**
     * @param values
     * @return index of the maximum value in values
     */
    public static int min(final double... values) {
        if (values == null || values.length == 0) {
            return -1;
        }
        double minv = values[0];
        int mini = 0;
        for (int i = 1; i < values.length; i++) {
            if (minv > values[i]) {
                minv = values[i];
                mini = i;
            }
        }
        return mini;
    }

    public static Double[] toDoubleArray(final double[] array) {
        Double[] a = new Double[array.length];
        for (int i = 0; i < a.length; i++) {
            a[i] = array[i];
        }
        return a;
    }

    public static Double[][] toDoubleArray(final double[][] array) {
        Double[][] a = new Double[array.length][];
        for (int i = 0; i < a.length; i++) {
            a[i] = toDoubleArray(array[i]);
        }
        return a;
    }

    public static double[] toDoubleArray(final List<Double> list) {
        double[] v = new double[list.size()];
        int i = 0;
        for (Double d : list) {
            v[i++] = d.doubleValue();
        }
        return v;
    }

    public static <E> Set<E> toSet(final E[] array) {
        HashSet<E> set = new HashSet<E>();
        for (E e : array) {
            set.add(e);
        }
        return set;
    }

    public static <E> E[] reverse(final E[] array) {
        E[] newArray = array.clone();
        for (int i = 0; i < array.length; i++) {
            newArray[newArray.length - 1 - i] = array[i];
        }
        return newArray;
    }

    public static <E> boolean contains(final E[] array, final E element) {
        if (element == null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < array.length; i++) {
                if (element == array[i] || element.equals(array[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <E> E[] join(final E[]... arrays) {
        int length = 0;
        for (E[] a : arrays) {
            length += a.length;
        }
        if (length == 0) {
            return null;
        } else {
            E[] array = Arrays.copyOf(arrays[0], length);
            int i = arrays[0].length;
            for (int a = 1; a < arrays.length; a++) {
                for (E e : arrays[a]) {
                    array[i++] = e;
                }
            }
            return array;
        }
    }

    public static <E> E[] join(final List<E[]> arrays) {
        int length = 0;
        for (E[] a : arrays) {
            length += a.length;
        }
        if (length == 0) {
            return null;
        } else {
            E[] array = null;
            int i = 0;
            for (E[] a : arrays) {
                if (array == null) {
                    array = Arrays.copyOf(a, length);
                    i = a.length;
                } else {
                    for (E e : a) {
                        array[i++] = e;
                    }
                }
            }
            return array;
        }
    }
}
