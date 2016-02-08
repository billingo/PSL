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
package se.umu.cs.robotics.logreader;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import org.apache.log4j.Logger;

/**
 * Replays log events, timed to their time stamps such that the relative timings of the log events are reproduced.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LogPlayer<M> extends AbstractLogEventProvider {

    static Logger logger = Logger.getLogger(LogPlayer.class);
    private Iterator<LogEvent<M>> source;
    private Timer timer = new Timer("Log player");
    private CountDownLatch running;
    private long period = 10;
    private long timeOffset;
    private LogEvent<M> next;

    public LogPlayer(Iterator<LogEvent<M>> source) {
        this.source = source;
    }

    public void start() {
        if (running == null) {
            running = new CountDownLatch(1);
            timer.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {
                    reportEvent();
                }
            }, 0, period);
        } else {
            logger.warn("LogPlayer allready running!");
        }
    }

    public void stop() {
        if (running == null) {
            logger.warn("LogPlayer not running, cannot stop!");
        } else {
            timer.cancel();
            running.countDown();
            running = null;
        }
    }

    private void reportEvent() {
        try {
            final long currentTime = System.currentTimeMillis();
            if (next == null) {
                next = source.next();
                timeOffset = currentTime - next.timeStamp();
            }
            while (currentTime - next.timeStamp() > timeOffset + 5) {
                for (LogEventListener<M> listenr : listeners) {
                    listenr.event(this, next);
                }
                next = source.next();
            }
        } catch (NoSuchElementException e) {
            stop();
        }
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void await() throws InterruptedException {
        if (running != null) {
            running.await();
        }
    }
}
