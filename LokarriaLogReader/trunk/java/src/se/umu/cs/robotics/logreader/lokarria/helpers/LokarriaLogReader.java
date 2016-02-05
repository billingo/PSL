/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.logreader.lokarria.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.iteration.position.LinkedPosition;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.logreader.LogEvent;
import se.umu.cs.robotics.logreader.lokarria.LokarriaMessageHandler;
import se.umu.cs.robotics.logreader.lokarria.SensoryMotorEventReader;
import se.umu.cs.robotics.logreader.xml.BufferedLogReader;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;

/**
 * Helper class for reading Lokarria log files.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LokarriaLogReader extends LinkedPositionIterator<SensoryMotorDistribution> {

    private final EventWrapperIterator wrapper;
    private final SensoryMotorSpace space;
    private final long integrationTimestep;
    private final File logFile;
    private final ArrayList<Long> timeStamps = new ArrayList<Long>();
    private int currentIndex = -1;

    private LokarriaLogReader(SensoryMotorSpace space, File logFile, EventWrapperIterator wrapper) {
        super(new SensoryMotorEventReader(space, wrapper, SensoryMotorSpace.DEFAULT_INTEGRATION_TIMESTEP));
        this.logFile = logFile;
        this.wrapper = wrapper;
        this.space = space;
        this.integrationTimestep = SensoryMotorSpace.DEFAULT_INTEGRATION_TIMESTEP;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public long getFirstTimeStamp() {
        if (timeStamps.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return timeStamps.get(0);
        }
    }

    public long getTimeStamp() {
        return getTimeStamp(currentIndex);
    }

    public long getTimeStamp(int index) {
        if (timeStamps.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return timeStamps.get(index);
        }
    }

    public long getRelativeTimeStamp(int index) {
        if (timeStamps.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return timeStamps.get(currentIndex) - timeStamps.get(0);
        }
    }

    @Override
    public LinkedPosition<SensoryMotorDistribution> next() {
        LinkedPosition<SensoryMotorDistribution> next = super.next();
        currentIndex++;
        if (currentIndex == timeStamps.size()) {
            timeStamps.add(wrapper.currentTimeStamp);
        }
        return next;
    }

    @Override
    public LinkedPosition<SensoryMotorDistribution> previous() {
        LinkedPosition<SensoryMotorDistribution> previous = super.previous();
        currentIndex--;
        return previous;
    }

    public File getSource() {
        return logFile;
    }

    public static LokarriaLogReader newReader(SensoryMotorSpace space, String logFile) throws SAXException {
        return newReader(space,new File(logFile));
    }

    public static LokarriaLogReader newReader(SensoryMotorSpace space, File logFile) throws SAXException {
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<LokarriaLogMessage>("http://www.cs.umu.se/robotics", "log", 200);
        reader.addMessageHandler(new LokarriaMessageHandler());
        EventWrapperIterator wrapper = new EventWrapperIterator(reader);
        reader.start(logFile);
        return new LokarriaLogReader(space, logFile, wrapper);
    }

    public static LinkedPositionIterator<SensoryMotorDistribution> read(SensoryMotorSpace space, String logFile) throws SAXException {
        return read(space, new File(logFile));
    }

    public static LinkedPositionIterator<SensoryMotorDistribution> read(SensoryMotorSpace space, File logFile) throws SAXException {
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<LokarriaLogMessage>("http://www.cs.umu.se/robotics", "log", 100);
        reader.addMessageHandler(new LokarriaMessageHandler());
        SensoryMotorEventReader eventReader = new SensoryMotorEventReader(space, reader, SensoryMotorSpace.DEFAULT_INTEGRATION_TIMESTEP);
        LinkedPositionIterator<SensoryMotorDistribution> events = new LinkedPositionIterator<SensoryMotorDistribution>(eventReader);
        reader.start(logFile);
        return events;
    }

    static class EventWrapperIterator implements Iterator<LogEvent<LokarriaLogMessage>> {

        private final BufferedLogReader<LokarriaLogMessage> reader;
        private long currentTimeStamp;

        public EventWrapperIterator(BufferedLogReader<LokarriaLogMessage> reader) {
            this.reader = reader;
        }

        public boolean hasNext() {
            return reader.hasNext();
        }

        public LogEvent<LokarriaLogMessage> next() {
            LogEvent<LokarriaLogMessage> event = reader.next();
            currentTimeStamp = event.timeStamp();
            return event;
        }

        public void remove() {
            reader.remove();
        }
    }
}
