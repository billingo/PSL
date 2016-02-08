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

package se.umu.cs.robotics.logreader.lokarria;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.logreader.LogMessageListener;
import se.umu.cs.robotics.logreader.xml.AbstractMessageHandler;
import se.umu.cs.robotics.lokarria.log.JsonMessage;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;
import se.umu.cs.robotics.lokarria.log.XmlMessage;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LokarriaMessageHandler extends AbstractMessageHandler<LokarriaLogMessage> {

    public static final String LOKARRIA_NAME_SPACE = "http://www.cs.umu.se/robotics/lokarria";
    static Logger logger = Logger.getLogger(LokarriaMessageHandler.class);
    private StringBuilder buffer = new StringBuilder();
    private StringMessage message;

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (message == null) {
            if (LOKARRIA_NAME_SPACE.equals(uri) && "message".equals(localName)) {
                String contentType = atts.getValue("contentType");
                if (contentType == null) {
                    // Support for older logs
                    contentType = atts.getValue("type");
                }
                String messageType = atts.getValue("messageType");
                if ("text/xml".equals(contentType)) {
                    message = new XmlMessageImpl(messageType);
                } else if ("text/json".equals(contentType)) {
                    message = new JsonMessageImpl(messageType);
                } else {
                    message = new StringMessage(messageType);
                }
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (message != null) {
            if (LOKARRIA_NAME_SPACE.equals(uri) && "message".equals(localName)) {
                message.data = buffer.toString();
                for (LogMessageListener<LokarriaLogMessage> listener : listeners) {
                    listener.message(message);
                }
                buffer.setLength(0);
                message = null;
            }
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (message != null) {
            buffer.append(ch, start, length);
        }
    }

    public static class StringMessage implements LokarriaLogMessage {

        String data;
        final MessageType type;

        private StringMessage(String type) {
            this.type = MessageType.fromString(type);
        }

        @Override
        public String toString() {
            return data;
        }

        public MessageType messageType() {
            return type;
        }
    }

    public static class XmlMessageImpl extends StringMessage implements XmlMessage {

        private XmlMessageImpl(String type) {
            super(type);
        }

        public String toXML() {
            return data;
        }
    }

    public static class JsonMessageImpl extends StringMessage implements JsonMessage {

        private JsonMessageImpl(String type) {
            super(type);
        }

        public String toJSON() {
            return data;
        }
    }
}
