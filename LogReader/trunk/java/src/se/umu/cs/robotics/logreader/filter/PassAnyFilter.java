
package se.umu.cs.robotics.logreader.filter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Passes all events that pass at least one of underlying filter
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class PassAnyFilter<M> implements LogEventFilter<M> {

    private LogEventFilter<M>[] filters;

    public PassAnyFilter(LogEventFilter<M>... filters) {
        this.filters = filters;
    }

    public boolean passLogger(Logger logger) {
        for (LogEventFilter<M> filter: filters) {
            if (filter.passLogger(logger)) return true;
        }
        return false;
    }

    public boolean passLevel(Level level) {
        for (LogEventFilter<M> filter: filters) {
            if (filter.passLevel(level)) return true;
        }
        return false;
    }

    public boolean passTimeStamp(long timeStamp) {
        for (LogEventFilter<M> filter: filters) {
            if (filter.passTimeStamp(timeStamp)) return true;
        }
        return false;
    }

    public boolean passMessage(M message) {
        for (LogEventFilter<M> filter: filters) {
            if (filter.passMessage(message)) return true;
        }
        return false;
    }

}
