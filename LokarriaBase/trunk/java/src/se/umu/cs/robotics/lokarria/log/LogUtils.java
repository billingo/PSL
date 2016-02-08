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

import org.apache.log4j.Logger;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage.MessageType;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LogUtils {

    public static void logJSON(Logger logger, final String json, final MessageType type) {
        logger.info(new JsonMessage() {

            public String toJSON() {
                return json;
            }

            public MessageType messageType() {
                return type;
            }

            @Override
            public String toString() {
                return toJSON();
            }
        });
    }

    public static void logXML(Logger logger, final String xml, final MessageType type) {
        logger.info(new XmlMessage() {

            public String toXML() {
                return xml;
            }

            public MessageType messageType() {
                return type;
            }

            @Override
            public String toString() {
                return toXML();
            }
        });
    }
}
