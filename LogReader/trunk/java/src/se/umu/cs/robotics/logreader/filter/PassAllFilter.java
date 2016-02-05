
package se.umu.cs.robotics.logreader.filter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Passes all events that pass all underlying filters
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class PassAllFilter<M> implements LogEventFilter<M> {

    private LogEventFilter<M>[] filters;

    public PassAllFilter(LogEventFilter<M>... filters) {
        this.filters = filters;
    }

    public boolean passLogger(Logger logger) {
        for (LogEventFilter<M> filter: filters) {
            if (!filter.passLogger(logger)) return false;
        }
        return true;
    }

    public boolean passLevel(Level level) {
        for (LogEventFilter<M> filter: filters) {
            if (!filter.passLevel(level)) return false;
        }
        return true;
    }

    public boolean passTimeStamp(long timeStamp) {
        for (LogEventFilter<M> filter: filters) {
            if (!filter.passTimeStamp(timeStamp)) return false;
        }
        return true;
    }

    public boolean passMessage(M message) {
        for (LogEventFilter<M> filter: filters) {
            if (!filter.passMessage(message)) return false;
        }
        return true;
    }

}
