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

package se.umu.cs.robotics.collections.fuzzy;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * An item in a FuzzySet representing an element paird with a membership value.
 *
 * @author Erik Billing
 */
public class ConcreteFuzzyItem<E> implements FuzzyItem<E> {

    private final static NumberFormat format = NumberFormat.getInstance(Locale.US);

    protected E element;
    protected double value;

    public ConcreteFuzzyItem(E element, double value) {
        this.element = element;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    public E element() {
        return element;
    }

    /**
     * {@inheritDoc}
     */
    public double value() {
        return value;
    }

    @Override
    public String toString() {
        return element + ":"+format.format(value);
    }


}
