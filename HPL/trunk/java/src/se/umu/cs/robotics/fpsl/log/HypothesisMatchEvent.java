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


package se.umu.cs.robotics.fpsl.log;

import se.umu.cs.robotics.fpsl.FHypothesis;
import se.umu.cs.robotics.fpsl.selection.FHypothesisMatch;
import se.umu.cs.robotics.fpsl.selection.FLhsMatch;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class HypothesisMatchEvent<E> extends AbstractHypothesisEvent<E> implements FHypothesisMatch<E> {

    private final double match;

    public HypothesisMatchEvent(FHypothesis<E> hypothesis, double match) {
        super(hypothesis);
        this.match = match;
    }

    public double getValue() {
        return match;
    }

    public String toXML() {
        StringBuilder s = new StringBuilder();
        s.append(messageStartTag());
        s.append(String.format("<hpl:HypothesisMatch match=\"%.4f\">",match));
        s.append(getHypothesis());
        s.append("</hpl:HypothesisMatch>");
        s.append(messageEndTag());
        return s.toString();
    }

}
