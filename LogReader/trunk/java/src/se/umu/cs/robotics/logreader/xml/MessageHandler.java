/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
