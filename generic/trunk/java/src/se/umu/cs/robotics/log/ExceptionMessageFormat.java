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

package se.umu.cs.robotics.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Formats any Throwable.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ExceptionMessageFormat extends AbstractMessageFormat {

    public boolean renderMessage(StringBuffer buf, LoggingEvent event) {
        Object message = event.getMessage();
        if (message instanceof Throwable) {
            Throwable ex = (Throwable) message;
            renderMessageStartTag(buf, "text/text");
            buf.append("<![CDATA[");
            Transform.appendEscapingCDATA(buf, getStackTrace(ex));
            buf.append("]]>");
            renderMessageEndTag(buf);
            return true;
        } else {
            return false;
        }

    }

    private String getStackTrace(Throwable message) {
        StringWriter s = new StringWriter();
        message.printStackTrace(new PrintWriter(s));
        return s.toString();
    }

    public String getNameSpace() {
        return "http://jakarta.apache.org/log4j";
    }

    public String getPreferredNameSpaceID() {
        return "log4j";
    }
}
