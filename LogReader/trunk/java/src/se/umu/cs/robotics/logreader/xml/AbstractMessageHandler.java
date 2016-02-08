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

import java.util.ArrayList;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.logreader.LogMessageListener;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public abstract class AbstractMessageHandler<M> implements MessageHandler<M> {

    protected ArrayList<LogMessageListener<M>> listeners = new ArrayList<LogMessageListener<M>>();

    public void setDocumentLocator(Locator locator) {
    }

    public void addMessageListener(LogMessageListener<M> listener) {
        listeners.add(listener);
    }

    public void removeMessageListener(LogMessageListener<M> listener) {
        listeners.remove(listener);
    }

    public void clearMessageListeners() {
        listeners.clear();
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    public void skippedEntity(String name) throws SAXException {
    }

}
