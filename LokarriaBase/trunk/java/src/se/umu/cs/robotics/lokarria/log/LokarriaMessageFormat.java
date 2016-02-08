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
