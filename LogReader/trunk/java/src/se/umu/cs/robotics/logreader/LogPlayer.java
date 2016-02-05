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
