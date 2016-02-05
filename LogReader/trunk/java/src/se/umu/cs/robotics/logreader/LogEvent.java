
package se.umu.cs.robotics.logreader;

import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public interface LogEvent<M> {

    public Logger logger();

    public Level logLevel();

    public long timeStamp();

    public List<M> messages();

    public String nameSpace();
}
