
package se.umu.cs.robotics.logreader;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public interface LogEventListener<M> {

    void eventFeedStart(LogEventProvider source);

    void eventFeedEnd(LogEventProvider source);

    void event(LogEventProvider source, LogEvent<M> event);
}
