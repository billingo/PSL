/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
