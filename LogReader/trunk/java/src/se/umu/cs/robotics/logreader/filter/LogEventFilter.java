
package se.umu.cs.robotics.logreader.filter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public interface LogEventFilter<M> {

    boolean passLogger(Logger logger);

    boolean passLevel(Level level);

    boolean passTimeStamp(long timeStamp);

    boolean passMessage(M message);
    
}
