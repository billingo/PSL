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

import java.io.File;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.logreader.LogEvent;
import se.umu.cs.robotics.logreader.LogEventListener;
import se.umu.cs.robotics.logreader.LogEventProvider;
import se.umu.cs.robotics.logreader.LogPlayer;
import se.umu.cs.robotics.logreader.lokarria.filter.DifferentialDriveFilter;
import se.umu.cs.robotics.logreader.xml.BufferedLogReader;
import se.umu.cs.robotics.lokarria.differentialdrive.ConcreteDifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveOperations;
import se.umu.cs.robotics.lokarria.log.JsonMessage;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LokarriaLogController {

    static Logger logger = Logger.getLogger(LokarriaLogController.class);
    private final BufferedLogReader<LokarriaLogMessage> reader;
    private final LogPlayer<LokarriaLogMessage> player;
    private final DifferentialDriveOperations ddop;
    private final LogEventListener<LokarriaLogMessage> listener = new LogEventListener<LokarriaLogMessage>() {

        public void eventFeedStart(LogEventProvider source) {
        }

        public void eventFeedEnd(LogEventProvider source) {
        }

        public void event(LogEventProvider source, LogEvent<LokarriaLogMessage> event) {
            for (LokarriaLogMessage m : event.messages()) {
                try {
                    String json = ((JsonMessage) m).toJSON();
                    DifferentialDriveCommand command = ConcreteDifferentialDriveCommand.fromJSON(json);
                    ddop.postCommand(command);
                } catch (ParseException ex) {
                    logger.warn(ex);
                }
            }
        }
    };

    public LokarriaLogController() {
        reader = new BufferedLogReader<LokarriaLogMessage>("http://www.cs.umu.se/robotics", "log", 100);
        reader.addMessageHandler(new LokarriaMessageHandler());
        reader.setFilter(new DifferentialDriveFilter());
        player = new LogPlayer<LokarriaLogMessage>(reader);
        player.addEventListener(listener);
        ddop = new DifferentialDriveOperations();
    }

    public void play(File source) throws SAXException {
        reader.start(source);
        player.start();
    }

    public LogPlayer<LokarriaLogMessage> getPlayer() {
        return player;
    }

    public BufferedLogReader<LokarriaLogMessage> getReader() {
        return reader;
    }

    public void await() throws InterruptedException {
        player.await();
    }
}
