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
