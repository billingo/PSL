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

package se.umu.cs.robotics.logreader.xml;

import se.umu.cs.robotics.logreader.LogMessageListener;
import se.umu.cs.robotics.logreader.LogEventProvider;
import se.umu.cs.robotics.logreader.filter.PassAllFilter;
import se.umu.cs.robotics.logreader.LogEventListener;
import se.umu.cs.robotics.logreader.filter.LogEventFilter;
import se.umu.cs.robotics.logreader.LogEvent;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LogSaxHandler<M> extends DefaultHandler implements LogEventProvider {

    public final static String NS_LOG4J = "http://jakarta.apache.org/log4j";
    private final String logNameSpace;
    private final String logRoodNode;
    private LogEventFilter filter = new PassAllFilter();
    private final ArrayList<LogEventListener> listeners = new ArrayList<LogEventListener>();
    private final ArrayList<MessageHandler<M>> messageHandlers = new ArrayList<MessageHandler<M>>();
    private int currentDepth = 0;
    private int logDepth;
    private int currentEventDepth;
    private int currentMessageDepth;
    private SaxLogEvent currentEvent;
    private boolean ignoreEvent;
    private final LogMessageListener messageListener = new LogMessageListener<M>() {

        public void message(M message) {
            currentEvent.messages.add(message);
        }
    };

    public LogSaxHandler(String logNameSpace, String logRootNode) {
        this.logNameSpace = logNameSpace;
        this.logRoodNode = logRootNode;
    }

    public LogEventFilter getFilter() {
        return filter;
    }

    public void setFilter(LogEventFilter filter) {
        this.filter = filter;
    }

    public void addEventListener(LogEventListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(LogEventListener listener) {
        listeners.remove(listener);
    }

    public void clearEventListeners() {
        listeners.clear();
    }

    public void addMessageHandler(MessageHandler<M> handler) {
        messageHandlers.add(handler);
        handler.addMessageListener(messageListener);
    }

    public void removeMessageHandler(MessageHandler<M> handler) {
        messageHandlers.remove(handler);
        handler.removeMessageListener(messageListener);
    }

    public void clearMessageHandlers() {
        messageHandlers.clear();
    }

    @Override
    public void startDocument() throws SAXException {
        for (LogEventListener listener : listeners) {
            listener.eventFeedStart(this);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        for (LogEventListener listener : listeners) {
            listener.eventFeedEnd(this);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {        currentDepth++;
        if (logDepth > 0) {
            if (!ignoreEvent) {
                if (currentEvent == null) {
                    if (NS_LOG4J.equals(uri) && "event".equals(localName)) {
                        startEvent(attributes);
                    }
                } else {
                    if (currentMessageDepth == 0) {
                        currentMessageDepth = currentDepth;
                    }
                    if (currentMessageDepth <= currentDepth) {
                        for (MessageHandler<M> h : messageHandlers) {
                            h.startElement(uri, localName, qName, attributes);
                        }
                    }
                }
            }
        } else if (logNameSpace.equals(uri) && logRoodNode.equals(localName)) {
            logDepth = currentDepth;
        }
    }

    private void startEvent(Attributes attributes) throws SAXException {
        SaxLogEvent event = new SaxLogEvent(attributes);
        if (filter.passLogger(event.logger) && filter.passLevel(event.logLevel) && filter.passTimeStamp(event.timeStamp)) {
            this.currentEvent = event;
        } else {
            this.ignoreEvent = true;
        }
        currentEventDepth = currentDepth;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (logDepth > 0) {
            if (currentDepth == logDepth && logNameSpace.equals(uri) && logRoodNode.equals(localName)) {
                logDepth = 0;
            } else if (currentEvent != null) {
                if (currentDepth == currentEventDepth && NS_LOG4J.equals(uri) && "event".equals(localName)) {
                    endEvent();
                } else if (currentMessageDepth > 0) {
                    for (MessageHandler<M> h : messageHandlers) {
                        h.endElement(uri, localName, qName);
                    }
                    if (currentDepth == currentMessageDepth) {
                        currentMessageDepth = 0;
                    }
                }
            } else if (ignoreEvent) {
                if (NS_LOG4J.equals(uri) && "event".equals(localName)) {
                    ignoreEvent = false;
                }
            }
        }
        currentDepth--;
    }

    private void endEvent() {
        for (LogEventListener listener : listeners) {
            listener.event(this, currentEvent);
        }
        currentEvent = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentMessageDepth > 0 && currentDepth >= currentMessageDepth) {
            for (MessageHandler<M> h : messageHandlers) {
                h.characters(ch, start, length);
            }
        }
    }

    public static class SaxLogEvent<M> implements LogEvent<M> {

        private final Logger logger;
        private final Level logLevel;
        private final long timeStamp;
        private ArrayList<M> messages = new ArrayList<M>(1);
        private String nameSpace;

        private SaxLogEvent(Attributes attributes) {
            this.logger = Logger.getLogger(attributes.getValue("logger"));
            this.logLevel = Level.toLevel(attributes.getValue("level"));
            this.timeStamp = Long.parseLong(attributes.getValue("timestamp"));
        }

        public List<M> messages() {
            return messages;
        }

        public String nameSpace() {
            return nameSpace;
        }

        public Logger logger() {
            return logger;
        }

        public Level logLevel() {
            return logLevel;
        }

        public long timeStamp() {
            return timeStamp;
        }

        @Override
        public String toString() {
            return "Event{" + "timeStamp=" + timeStamp + "messages=" + messages + '}';
        }
    }
}
