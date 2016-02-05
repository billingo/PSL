
package se.umu.cs.robotics.logreader;

import se.umu.cs.robotics.logreader.LogEventListener;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public interface LogEventProvider {

    void addEventListener(LogEventListener listener);

    void removeEventListener(LogEventListener listener);

    void clearEventListeners();
}
