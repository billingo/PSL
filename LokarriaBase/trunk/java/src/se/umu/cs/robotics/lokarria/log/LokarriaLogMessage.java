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

/**
 * Log messages produced by the Lokarria client. These massages are handled by
 * the LokarriaLogLayout such that their properties is visible in the xml log.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public interface LokarriaLogMessage {

    MessageType messageType();

    public static enum MessageType {

        /**
         * A log message representing a post to the Lokarria (MRDS) server
         */
        POST, 
        
        /**
         * A log message representing a get from the Lokarria (MRDS) server
         */
        GET,

        /**
         * A log message representing status change of the Lokarria client
         */
        STATUS;

        public static MessageType fromString(String s) {
            if (s == null || s.equalsIgnoreCase("GET")) {
                return GET;
            } else if (s.equalsIgnoreCase("POST")) {
                return POST;
            } else {
                return STATUS;
            }
        }
    }
}
