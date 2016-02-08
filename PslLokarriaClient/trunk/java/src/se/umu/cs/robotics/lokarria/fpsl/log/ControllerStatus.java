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


package se.umu.cs.robotics.lokarria.fpsl.log;

import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.hpl.log.AbstractHplLogMessage;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ControllerStatus<E> extends AbstractHplLogMessage<E> {

    private final int matchCount;
    private final Object msg;

    public ControllerStatus(FPrediction<E> prediction) {
        this(prediction,null);
    }

    public ControllerStatus(FPrediction<E> prediction, Object msg) {
        this.matchCount = prediction.getSelection().size();
        this.msg = msg;
    }

    public String toXML() {
        StringBuilder s = new StringBuilder();
        s.append(messageStartTag("PslControllerMessage"));
        s.append(String.format("<hpl:ControllerStatus matchCount=\"%d\"/>",matchCount));
        s.append(messageEndTag());
        return s.toString();
    }

    public Object getMessage() {
        return msg;
    }

    public int matchCount() {
        return matchCount;
    }

}
