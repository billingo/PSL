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

import org.apache.log4j.spi.LoggingEvent;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public interface LogMessageFormat {

    /**
     * If the format handles the specified LogEvent, it writes the event to buffer as XML and returns true. If the format is not applicable to the specified event, it does not write anything and returns false. The latter indicates that the event should be formatted by another format.
     * @param buffer
     * @param event
     * @return
     */
    public boolean renderMessage(StringBuffer buffer, LoggingEvent event);

    public String getNameSpace();

    public String getNameSpaceID();

    public void setNameSpaceID(String nameSpaceID);

    public String getPreferredNameSpaceID();
}
