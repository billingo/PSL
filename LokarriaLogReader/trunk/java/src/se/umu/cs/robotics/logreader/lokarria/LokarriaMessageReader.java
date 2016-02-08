/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright (C) 2007 - 2015  Erik Billing, <http://www.his.se/erikb>
School of Informatics, University of Skovde, Sweden

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
\*-------------------------------------------------------------------*/
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
