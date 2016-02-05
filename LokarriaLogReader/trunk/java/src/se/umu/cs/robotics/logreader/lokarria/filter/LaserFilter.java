
package se.umu.cs.robotics.logreader.lokarria.filter;

import org.apache.log4j.Logger;
import se.umu.cs.robotics.logreader.filter.SourceFilter;
import se.umu.cs.robotics.lokarria.laser.LaserOperations;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LaserFilter extends SourceFilter<LokarriaLogMessage> {

    public LaserFilter() {
        super(Logger.getLogger(LaserOperations.class));
    }

}
