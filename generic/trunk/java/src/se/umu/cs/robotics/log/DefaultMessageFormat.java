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

import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Formats any event as a log4j:message. 
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class DefaultMessageFormat extends AbstractMessageFormat {

    public boolean renderMessage(StringBuffer buffer, LoggingEvent event) {
        renderMessageStartTag(buffer, "text/text");
        buffer.append("<![CDATA[");
        // Append the rendered message. Also make sure to escape any
        // existing CDATA sections.
        Transform.appendEscapingCDATA(buffer, event.getRenderedMessage());
        buffer.append("]]>");
        renderMessageEndTag(buffer);
        return true;
    }

    public String getNameSpace() {
        return "http://jakarta.apache.org/log4j";
    }

    public String getPreferredNameSpaceID() {
        return "log4j";
    }
}