/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright 2011 Benjamin Fonooni and Erik Billing
 billing@cs.umu.se, fonooni@cs.umu.se
Department of Computing Science, Umea University, Sweden,
(http://www.cognitionreversed.com).

LICENSE:

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston,
MA 02111-1307, USA.
\*-------------------------------------------------------------------*/
package se.umu.cs.robotics.lokarria.laser;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import org.apache.log4j.Logger;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage.MessageType;

import static se.umu.cs.robotics.lokarria.log.LogUtils.logXML;

/**
 * A Timer based reader that conqinously polls laser readings from the server. The most recent value is retrieved by calling getEchoes().
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LaserReader {

    static Logger logger = Logger.getLogger(LaserReader.class);

    private static LaserReader instance;

    private final LaserOperations laser = new LaserOperations();
    private final Timer timer = new Timer("LaserReader");
    private TimerTask task = null;
    private LaserArray reading;
    private final long requestDelay = 50;
    private long requestCount;
    private long startTime;
    private CountDownLatch latch = new CountDownLatch(1);

    private LaserReader() {
    }

    public void start() {
        if (task == null) {
            requestCount = 0;
            startTime = 0;

            logXML(logger, "<lokarria:LaserReaderEvent type=\"start\"/>", MessageType.GET);
            task = new TimerTask() {

                @Override
                public void run() {
                    if (startTime == 0) {
                        startTime = System.currentTimeMillis();
                    }
                    reading = laser.getEchoes();
                    if (requestCount==0) {
                        latch.countDown();
                    }
                    requestCount++;
                }
            };
            timer.scheduleAtFixedRate(task, 0, requestDelay);
        }
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
            latch = new CountDownLatch(1);
            logXML(logger, "<lokarria:LaserReaderEvent type=\"stop\"/>", MessageType.GET);
            reading = null;
        }
    }

    public void shutdown() {
        stop();
        timer.cancel();
    }

    public boolean isReady() {
        return timer != null && requestCount > 0;
    }

    /**
     * Wait for echoes to become available.
     *
     * @throws InterruptedException
     */
    public void await() throws InterruptedException {
        latch.await();
    }

    public static LaserReader getInstance() {
        if (instance == null) {
            instance = new LaserReader();
        }
        return instance;
    }

    public LaserArray getEchoes() {
        return reading;
    }

    /**
     * @return the timer request delay in milliseconds
     */
    public long getRequestDelay() {
        return requestDelay;
    }

    /**
     * @return the average update frequency
     */
    public double getUpdateFrequency() {
        return (1000.0 * requestCount) / (System.currentTimeMillis() - startTime);
    }
}
