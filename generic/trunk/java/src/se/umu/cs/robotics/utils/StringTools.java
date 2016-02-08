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

import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

public class StringTools {

    public static String join(final Object[] strings) {
        return join(strings, null);
    }

    public static String join(final Object[] strings, final String separator) {
        StringBuilder s = new StringBuilder();
        if (strings.length == 0) {
            return "";
        }
        s.append(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            if (separator != null && separator.length() > 0) {
                s.append(separator);
            }
            if (strings[i] != null) {
                s.append(strings[i].toString());
            } else {
                s.append("null");
            }
        }
        return s.toString();
    }

    public static String join(final byte[] strings) {
        return join(strings, null);
    }

    public static String join(final byte[] strings, final String separator) {
        StringBuilder s = new StringBuilder();
        if (strings.length == 0) {
            return "";
        }
        s.append(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            if (separator != null && separator.length() > 0) {
                s.append(separator);
            }
            s.append(Byte.toString(strings[i]));
        }
        return s.toString();
    }

    public static String join(final int[] strings) {
        return join(strings, null);
    }

    public static String join(final int[] strings, final String separator) {
        StringBuilder s = new StringBuilder();
        if (strings.length == 0) {
            return "";
        }
        s.append(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            if (separator != null && separator.length() > 0) {
                s.append(separator);
            }
            s.append(Integer.toString(strings[i]));
        }
        return s.toString();
    }

    public static String join(final double[] strings) {
        return join(strings, null);
    }

    public static String join(final double[] strings, final String separator) {
        StringBuilder s = new StringBuilder();
        if (strings.length == 0) {
            return "";
        }
        s.append(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            if (separator != null && separator.length() > 0) {
                s.append(separator);
            }
            s.append(Double.toString(strings[i]));
        }
        return s.toString();
    }
    
    public static String join(final String format, final double[] values) {
        return join(format, values, null);
    }

    public static String join(final String format, final double[] values, final String separator) {
        StringBuilder s = new StringBuilder();
        if (values.length == 0) {
            return "";
        }
        s.append(String.format(Locale.US, format, values[0]));
        for (int i = 1; i < values.length; i++) {
            if (separator != null && separator.length() > 0) {
                s.append(separator);
            }
            s.append(String.format(Locale.US, format, values[i]));
        }
        return s.toString();
    }

    public static String join(final Collection<?> objects) {
        return join(objects, null, null);
    }

    public static String join(final Collection<?> objects, final String separator) {
        return join(objects, separator, null);
    }

    public static String join(final Collection<?> objects, final String separator, final NumberFormat format) {
        Iterator<?> iter = objects.iterator();
        return join(iter, separator, format);
    }

    public static String join(final Iterator<?> iter, final String separator) {
        return join(iter, separator, null);
    }

    public static String join(final Iterator<?> iter, final String separator, final NumberFormat format) {
        StringBuilder s = new StringBuilder();
        while (true) {
            Object e = iter.next();
            if (e == null) {
                s.append("null");
            } else if (format != null && e instanceof Number) {
                s.append(format.format(e));
            } else {
                s.append(e.toString());
            }
            if (iter.hasNext()) {
                if (separator != null && separator.length() > 0) {
                    s.append(separator);
                }
            } else {
                break;
            }
        }
        return s.toString();
    }

    public static String stringAfter(final String s, final String sep) {
        int sepPos = s.indexOf(sep);
        if (sepPos < 0) {
            return s;
        } else {
            return s.substring(sepPos + sep.length());
        }
    }

    public static String stringAfterLast(final String s, final String sep) {
        int sepPos = s.lastIndexOf(sep);
        if (sepPos < 0) {
            return s;
        } else {
            return s.substring(sepPos + sep.length());
        }
    }

    public static String stringBefore(final String s, final String sep) {
        int sepPos = s.indexOf(sep);
        if (sepPos < 0) {
            return s;
        } else {
            return s.substring(0, sepPos);
        }
    }

    public static String stringBeforeLast(final String s, final String sep) {
        int sepPos = s.lastIndexOf(sep);
        if (sepPos < 0) {
            return s;
        } else {
            return s.substring(0, sepPos);
        }
    }

    public static String repeat(final String s, final int repetitions) {
        return repeat(s, repetitions, null);
    }

    public static String repeat(final String s, final int repetitions, final String separator) {
        StringBuilder str = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < repetitions; i++) {
            if (first) {
                first = false;
            } else if (separator != null) {
                str.append(separator);
            }
            str.append(s);
        }
        return str.toString();
    }

    public static NumberFormat defaultNumberFomrat() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat;
    }

    public static Character[] toCharacterArray(final String s) {
        Character[] c = new Character[s.length()];
        for (int i = 0; i < s.length(); i++) {
            c[i] = s.charAt(i);
        }
        return c;
    }

    public static Character[] toCharacterArray(final char... chars) {
        Character[] c = new Character[chars.length];
        for (int i = 0; i < chars.length; i++) {
            c[i] = new Character(chars[i]);
        }
        return c;
    }

    public static String printArray(final double[] array, final List<?> horizontalHader) {
        return printArray(array, horizontalHader, defaultNumberFomrat());
    }

    public static String printArray(final double[] array, final List<?> horizontalHader, final NumberFormat format) {
        double[][] a = {array};
        return printArray(a, horizontalHader, null, format);
    }

    public static String printArray(final double[][] array, final List<?> horizontalHeader) {
        return printArray(array, horizontalHeader, null);
    }

    public static String printArray(final double[][] array, final List<?> horizontalHeader, final List<?> verticalHeader) {
        return printArray(array, horizontalHeader, verticalHeader, defaultNumberFomrat());
    }

    /**
     * Generates a spreadsheet style representation of the specified double
     * array, similar to the standard Matlab representation of arrays.
     *
     * @param array the array to be printed
     * @param horizontalHeader top header
     * @param verticalHeader left header
     * @param format the number foratter used to represent double values
     * @return array as string
     */
    public static String printArray(final double[][] array, final List<?> horizontalHeader, final List<?> verticalHeader, final NumberFormat format) {
        StringBuilder s = new StringBuilder();
        int minColWidth = 5;
        int verticalHeaderSize = getVerticalHeaderSize(verticalHeader);

        /*
         * Generating horizontal header
         */
        int[] cols;
        if (horizontalHeader != null) {
            cols = new int[horizontalHeader.size()];
            int col = 0;
            int i = 0;
            s.append(repeat(" ", verticalHeaderSize));
            for (Object o : horizontalHeader) {
                String colHeader = o.toString();
                s.append(colHeader);
                s.append(repeat(" ", minColWidth - colHeader.length()));
                cols[i++] = col;
                col = (colHeader.length() > minColWidth ? colHeader.length() : minColWidth);
            }
            s.append('\n');
        } else if (array.length > 0) {
            cols = new int[array[0].length];
            for (int col = 0; col < cols.length; col++) {
                cols[col] = minColWidth;
            }
        } else {
            return "";
        }

        /*
         * Printing rows
         */
        Iterator<?> leftHeader = null;
        if (verticalHeaderSize > 0) {
            leftHeader = verticalHeader.iterator();
        }
        for (int row = 0; row < array.length; row++) {
            if (verticalHeaderSize > 0) {
                String h = leftHeader.next().toString() + ": ";
                s.append(h);
                s.append(repeat(" ", verticalHeaderSize - h.length()));
            }
            for (int col = 0; col < array[row].length; col++) {
                String value = format.format(array[row][col]);
                s.append(value);
                if (col < array[row].length - 1) {
                    s.append(repeat(" ", cols[col + 1] - value.length()));
                }
            }
            if (row < array.length - 1) {
                s.append('\n');
            }
        }
        return s.toString();
    }

    private static int getVerticalHeaderSize(final List<?> verticalHeader) {
        int verticalHeaderSize = 0;
        if (verticalHeader != null) {
            for (Object o : verticalHeader) {
                int len = o.toString().length();
                verticalHeaderSize = len > verticalHeaderSize ? len : verticalHeaderSize;
            }
        }
        return verticalHeaderSize;
    }

    /**
     * Limits the length of the string to specified max length.
     *
     * @param s
     * @param maxLength
     * @param endString inserted at the end of s if s overrides maxLength
     * @return the truncated string if at most maxLength characters
     */
    public static String truncate(final String s, final int maxLength, final String endString) {
        if (s.length() > maxLength) {
            if (endString == null) {
                return s.substring(0, maxLength - 1);
            } else {
                return s.substring(0, maxLength - 1 - endString.length()) + endString;
            }
        } else {
            return s;
        }
    }

    public static String trunc(final String s, final int maxLength) {
        return truncate(s, maxLength, null);
    }

    public static Iterator<Character> iterator(final String s) {
        return new StringIterator(s);
    }

    public static class StringIterator implements Iterator<Character>, Iterable<Character> {

        private final String s;
        private int i;

        public StringIterator(final String s) {
            this.s = s;
        }

        @Override
        public boolean hasNext() {
            return i < s.length();
        }

        @Override
        public Character next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return s.charAt(i++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterator<Character> iterator() {
            return this;
        }
    }
}
