package se.umu.cs.robotics.logreader.lokarria;

import java.util.Iterator;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.logreader.LogEvent;
import se.umu.cs.robotics.lokarria.core.JsonOperations.JsonException;
import se.umu.cs.robotics.lokarria.differentialdrive.ConcreteDifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveOperations;
import se.umu.cs.robotics.lokarria.laser.LaserArrayList;
import se.umu.cs.robotics.lokarria.laser.LaserOperations;
import se.umu.cs.robotics.lokarria.log.JsonMessage;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;

/**
 * Reads messages form a message iterator and converts them to Lokarria sensor and motor objects.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LokarriaMessageReader implements IterableIterator<Object> {

    static Logger logger = Logger.getLogger(LokarriaMessageReader.class);
    private final LokarriaMessageIterator messages;
    private static Logger ddoLogger = Logger.getLogger(DifferentialDriveOperations.class);
    private static Logger laserLogger = Logger.getLogger(LaserOperations.class);

    public LokarriaMessageReader(LokarriaMessageIterator messages) {
        this.messages = messages;
    }

    public Iterator<Object> iterator() {
        return this;
    }

    public boolean hasNext() {
        return messages.hasNext();
    }

    public Object next() {
        try {
            LokarriaLogMessage message = messages.next();
            LogEvent<LokarriaLogMessage> currentEvent = messages.currentEvent();
            if (ddoLogger.equals(currentEvent.logger())) {
                return ConcreteDifferentialDriveCommand.fromJSON(((JsonMessage) message).toJSON());
            } else if (laserLogger.equals(currentEvent.logger())) {
                return LaserArrayList.fromJSON(((JsonMessage) message).toJSON());
            } else {
                return message;
            }
        } catch (JsonException ex) {
            logger.warn(ex);
            return null;
        } catch (ParseException ex) {
            logger.warn(ex);
            return null;
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
