
package se.umu.cs.robotics.logreader;

import java.util.ArrayList;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public abstract class AbstractLogEventProvider implements LogEventProvider {

    protected ArrayList<LogEventListener> listeners = new ArrayList<LogEventListener>();

    public void addEventListener(LogEventListener listener) {
        listeners.add(listener);
    }

    public void removeEventListener(LogEventListener listener) {
        listeners.remove(listener);
    }

    public void clearEventListeners() {
        listeners.clear();
    }

}
