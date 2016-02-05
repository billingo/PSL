
package se.umu.cs.robotics.logreader.lokarria.filter;

import org.apache.log4j.Logger;
import se.umu.cs.robotics.logreader.filter.SourceFilter;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveOperations;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;

/**
 * A source based log event filter that accepts events only from the DifferentialDriveOperations.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class DifferentialDriveFilter extends SourceFilter<LokarriaLogMessage> {

    public DifferentialDriveFilter() {
        super(Logger.getLogger(DifferentialDriveOperations.class));
    }

}
