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
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public interface MessageHandler<M> {

    public void addMessageListener(LogMessageListener<M> listener);

    public void removeMessageListener(LogMessageListener<M> listener);

    public void clearMessageListeners();

    public void setDocumentLocator(Locator locator);

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException;

    public void endElement(String uri, String localName, String qName) throws SAXException;

    public void characters(char[] ch, int start, int length) throws SAXException;

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException;

    public void skippedEntity(String name) throws SAXException;
}
