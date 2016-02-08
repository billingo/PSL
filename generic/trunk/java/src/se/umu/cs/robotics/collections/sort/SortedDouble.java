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
package se.umu.cs.robotics.collections.sort;

/**
 *
 * @author billing
 */
public class SortedDouble implements SortedElement {

    private int originalPosition;
    private double v;
    private SorterOrder order;

    public SortedDouble(int originalPosition, double value, SorterOrder order) {
        this.originalPosition = originalPosition;
        this.v = value;
        this.order = order;
    }

    public int originalPosition() {
        return originalPosition;
    }

    public double value() {
        return v;
    }

    public int compareTo(SortedElement t) {
        if (t instanceof SortedDouble) {
            double diff = v - ((SortedDouble) t).v;
            switch (order) {
                case REVERSED_NATURAL:
                    return diff > 0 ? -1 : diff < 0 ? 1 : 0;
                default: //NATURAL
                    return diff > 0 ? 1 : diff < 0 ? -1 : 0;
            }
        } else {
            return 0;
        }
    }
}
