/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.logreader.lokarria;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.logreader.LogEvent;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LokarriaMessageIterator implements IterableIterator<LokarriaLogMessage> {

    private final Iterator<LogEvent<LokarriaLogMessage>> source;
    private LogEvent<LokarriaLogMessage> currentEvent;
    private Iterator<LokarriaLogMessage> currentMessageIterator;

    public LokarriaMessageIterator(Iterator<LogEvent<LokarriaLogMessage>> source) {
        this.source = source;
    }

    public Iterator<LokarriaLogMessage> iterator() {
        return this;
    }

    private void feed(boolean force) {
        while ((currentMessageIterator == null || !currentMessageIterator.hasNext()) && (force || source.hasNext())) {
            currentEvent = source.next();
            currentMessageIterator = currentEvent.messages().iterator();
        }
    }

    public boolean hasNext() {
        feed(false);
        return currentMessageIterator != null && currentMessageIterator.hasNext();
    }

    public LokarriaLogMessage next() {
        feed(true);
        return currentMessageIterator.next();
    }

    public LogEvent<LokarriaLogMessage> currentEvent() {
        return currentEvent;
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }
}
