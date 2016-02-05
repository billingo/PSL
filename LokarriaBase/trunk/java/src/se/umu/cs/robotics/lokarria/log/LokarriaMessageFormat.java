/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright 2011 Benjamin Fonooni and Erik Billing
 billing@cs.umu.se, fonooni@cs.umu.se
Department of Computing Science, Umea University, Sweden,
(http://www.cognitionreversed.com).

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
package se.umu.cs.robotics.lokarria.log;

import se.umu.cs.robotics.log.AbstractMessageFormat;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public abstract class LokarriaMessageFormat extends AbstractMessageFormat {

    public static String NAME_SPACE = "http://www.cs.umu.se/robotics/lokarria";

    protected void renderMessageStartTag(StringBuffer buf, String contentType, LokarriaLogMessage.MessageType messageType) {
        buf.append('<');
        buf.append(nsID);
        buf.append(":message contentType=\"");
        buf.append(contentType);
        buf.append("\" messageType=\"");
        buf.append(messageType);
        buf.append("\">\r\n");
    }

    public String getNameSpace() {
        return NAME_SPACE;
    }

    public String getPreferredNameSpaceID() {
        return "lokarria";
    }
}
