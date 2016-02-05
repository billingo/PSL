
package se.umu.cs.robotics.logreader.filter;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SourceFilter<M> implements LogEventFilter<M> {

    private final Logger[] acceptedLoggers;

    public SourceFilter(Logger... acceptedLoggers) {
        this.acceptedLoggers = acceptedLoggers;
    }

    public boolean passLogger(final Logger logger) {
        Category c = logger;
        while (c != null) {
            for (Logger l : acceptedLoggers) {
                if (l == c) {
                    return true;
                }
            }
            c = c.getParent();
        }
        return false;
    }

    public boolean passLevel(Level level) {
        return true;
    }

    public boolean passTimeStamp(long timeStamp) {
        return true;
    }

    public boolean passMessage(M message) {
        return true;
    }
}
