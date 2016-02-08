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

package se.umu.cs.robotics.fpsl.selection;

import java.util.Locale;
import se.umu.cs.robotics.fpsl.FHypothesis;
import se.umu.cs.robotics.fpsl.log.AbstractHypothesisEvent;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ConcreteHypothesisMatch<E> extends AbstractHypothesisEvent<E> implements FHypothesisMatch<E> {

    final double value;

    public ConcreteHypothesisMatch(FHypothesis<E> h, double matchValue) {
        super(h);
        this.value = matchValue;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "(%.3f:%s)",value,getHypothesis().toString());
    }

    @Override
    public String toXML() {
        StringBuilder s = new StringBuilder();
        s.append(messageStartTag());
        s.append("<hpl:HypothesisSelection match=\"");
        s.append(Double.toString(value));
        s.append("\">");
        s.append(getHypothesis());
        s.append("</hpl:HypothesisSelection>");
        s.append(messageEndTag());
        return s.toString();
    }
}
