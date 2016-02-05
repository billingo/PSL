/*
 *  Copyright (C) 2011 Erik Billing <billing@cs.umu.se>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.umu.cs.robotics.log;

import org.apache.log4j.spi.LoggingEvent;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public abstract class AbstractMessageFormat implements LogMessageFormat {

    protected String nsID;

    public AbstractMessageFormat() {
        this.nsID = getPreferredNameSpaceID();
    }

    public String getNameSpaceID() {
        return nsID;
    }

    public void setNameSpaceID(String nameSpaceID) {
        nsID = nameSpaceID;
    }

    protected void renderMessageStartTag(StringBuffer buf, String contentType) {
        buf.append('<');
        buf.append(nsID);
        buf.append(":message contentType=\"");
        buf.append(contentType);
        buf.append("\">\r\n");
    }

    protected void renderMessageEndTag(StringBuffer buf) {
        buf.append("</");
        buf.append(nsID);
        buf.append(":message>\r\n");
    }

}
