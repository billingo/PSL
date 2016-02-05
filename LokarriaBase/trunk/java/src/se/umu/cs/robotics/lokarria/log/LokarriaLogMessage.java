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
