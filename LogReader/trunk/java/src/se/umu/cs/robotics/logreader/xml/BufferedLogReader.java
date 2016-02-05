/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.logreader.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.logreader.LogEvent;
import se.umu.cs.robotics.logreader.LogEventListener;
import se.umu.cs.robotics.logreader.LogEventProvider;
import se.umu.cs.robotics.logreader.filter.LogEventFilter;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class BufferedLogReader<M> implements IterableIterator<LogEvent<M>> {

    static Logger logger = Logger.getLogger(BufferedLogReader.class);
    private final LinkedBlockingQueue<LogEvent<M>> queue;
    private Thread thread;
    final LogSaxHandler<M> handler;
    final long timeOut = 1000;
    final long hasNextTimeOut = 50;

    public BufferedLogReader(String nameSpace, String logRootNode, int bufferSize) {
        this.queue = new LinkedBlockingQueue<LogEvent<M>>(bufferSize);
        this.handler = new LogSaxHandler<M>(nameSpace, logRootNode);
        this.handler.addEventListener(new LogEventListener() {

            public void eventFeedStart(LogEventProvider source) {
            }

            public void eventFeedEnd(LogEventProvider source) {
            }

            public void event(LogEventProvider source, LogEvent event) {
                try {
                    queue.put(event);
                } catch (InterruptedException ex) {
                    logger.warn(ex);
                }
            }
        });
    }

    public LogEventFilter getFilter() {
        return handler.getFilter();
    }

    public void setFilter(LogEventFilter filter) {
        handler.setFilter(filter);
    }

    public void addMessageHandler(MessageHandler<M> handler) {
        this.handler.addMessageHandler(handler);
    }

    public void removeMessageHandler(MessageHandler<M> handler) {
        this.handler.removeMessageHandler(handler);
    }

    public void clearMessageHandlers() {
        this.handler.clearMessageHandlers();
    }

    public void start(final File xmlLogFile) throws SAXException {
        if (thread == null) {
            final XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            thread = new Thread(new Runnable() {

                public void run() {
                    try {
                        final FileInputStream fi = new FileInputStream(xmlLogFile);
                        final InputStreamReader isr = new InputStreamReader(fi, "UTF-8");
                        final InputSource source = new InputSource(isr);
                        reader.parse(source);
                    } catch (IOException ex) {
                        logger.warn(ex);
                    } catch (SAXException ex) {
                        logger.warn(ex);
                    }
                    thread = null;
                }
            }, BufferedLogReader.class.getSimpleName());
            thread.start();
        }
    }

    public void stop() {
        thread = null;
    }

    public boolean hasNext() {
        if (queue.isEmpty()) {
            if (thread != null) {
                try {
                    thread.join(hasNextTimeOut);
                    return hasNext();
                } catch (InterruptedException ex) {
                    logger.warn("Log reader was interrupted when waiting for reader thread.", ex);
                    return hasNext();
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public LogEvent<M> next() {
        try {
            if (hasNext()) {
                LogEvent<M> next = queue.poll(timeOut, TimeUnit.MILLISECONDS);
                if (next == null) {
                    throw new NoSuchElementException();
                } else {
                    return next;
                }
            }
        } catch (InterruptedException ex) {
            logger.warn("Log reader was interrupted while polling from queue.",ex);
        }
        throw new NoSuchElementException();
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public Iterator<LogEvent<M>> iterator() {
        return this;
    }
}
