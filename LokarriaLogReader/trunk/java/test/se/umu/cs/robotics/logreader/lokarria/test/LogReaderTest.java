package se.umu.cs.robotics.logreader.lokarria.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import org.xml.sax.InputSource;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.logreader.LogEvent;
import se.umu.cs.robotics.logreader.LogEventListener;
import se.umu.cs.robotics.logreader.LogEventProvider;
import se.umu.cs.robotics.logreader.LogPlayer;
import se.umu.cs.robotics.logreader.xml.BufferedLogReader;
import se.umu.cs.robotics.logreader.xml.LogSaxHandler;
import se.umu.cs.robotics.logreader.lokarria.LokarriaMessageHandler;
import se.umu.cs.robotics.logreader.lokarria.SensoryMotorEventReader;
import se.umu.cs.robotics.logreader.lokarria.filter.DifferentialDriveFilter;
import se.umu.cs.robotics.logreader.lokarria.filter.LaserFilter;
import se.umu.cs.robotics.logreader.lokarria.filter.SensoryMotorFilter;
import se.umu.cs.robotics.lokarria.differentialdrive.ConcreteDifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.lokarria.laser.LaserArrayList;
import se.umu.cs.robotics.lokarria.log.JsonMessage;
import se.umu.cs.robotics.log.LogConfigurator;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;
import se.umu.cs.robotics.lokarria.statespace.DifferentialDriveSpace;
import se.umu.cs.robotics.lokarria.statespace.LaserSpace;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.utils.MathTools;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LogReaderTest {

    public LogReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        LogConfigurator.configure();
    }

    @After
    public void tearDown() {
        LogConfigurator.shutdown();
    }

    @Test
    public void testSaxHandler() throws SAXException, FileNotFoundException, IOException {
        XMLReader xr = XMLReaderFactory.createXMLReader();
        LogSaxHandler handler = new LogSaxHandler("http://se.umu.cs.robotics.lokarria", "log");
        handler.addMessageHandler(new LokarriaMessageHandler());
        handler.addEventListener(new LogEventListener<LokarriaLogMessage>() {

            public void eventFeedStart(LogEventProvider source) {
            }

            public void eventFeedEnd(LogEventProvider source) {
            }

            public void event(LogEventProvider source, LogEvent<LokarriaLogMessage> event) {
//                System.out.println(event.timeStamp());
            }
        });
        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);

        FileInputStream fi = new FileInputStream(new File("logs/DrivingToTheTV.log.xml"));
        InputStreamReader reader = new InputStreamReader(fi, "UTF-8");
        xr.parse(new InputSource(reader));
//        FileReader r = new FileReader("logs/DrivingToTheKitchen.log.xml");
//        System.out.println(r.getEncoding());
//        xr.parse(new InputSource(r));
    }

    @Test
    public void testLaserDiff() throws SAXException, FileNotFoundException, IOException, ParseException {
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<LokarriaLogMessage>("http://se.umu.cs.robotics.lokarria", "log", 10);
        reader.addMessageHandler(new LokarriaMessageHandler());
        LaserFilter laserFilter = new LaserFilter();
        DifferentialDriveFilter driveFilter = new DifferentialDriveFilter();

        reader.start(new File("logs/DrivingToTheTV3.log.xml"));
//        reader.start(new File("logs/Benjamin.log.xml"));
        boolean driving = false;
        ArrayList<Long> delays = new ArrayList<Long>();
        LaserArray lastLaser = null;
        LogEvent<LokarriaLogMessage> lastEvent = null;

        for (LogEvent<LokarriaLogMessage> e : reader) {
            if (laserFilter.passLogger(e.logger())) {
                LaserArray laser = null;
                for (LokarriaLogMessage m : e.messages()) {
                    laser = LaserArrayList.fromJSON(((JsonMessage) m).toJSON());
                }
                if (lastLaser != null && laser.timeStamp() != lastLaser.timeStamp()) {
                    delays.add(laser.timeStamp() - lastLaser.timeStamp());
                    lastLaser = laser;
                    lastEvent = e;
                } else if (lastLaser == null) {
                    lastLaser = laser;
                    lastEvent = e;
                }
            } else if (driveFilter.passLogger(e.logger())) {
                DifferentialDriveCommand command = null;
                for (LokarriaLogMessage m : e.messages()) {
                    command = ConcreteDifferentialDriveCommand.fromJSON(((JsonMessage) m).toJSON());
                }
                if (ConcreteDifferentialDriveCommand.stop().equals(command)) {
                    driving = false;
                } else {
                    driving = true;
                }
            }
        }
        System.out.println(delays);
        System.out.println(MathTools.mean(delays));
    }

    @Test
    public void testLaserSpace() throws SAXException, FileNotFoundException, IOException, ParseException {
        LaserSpace space = new LaserSpace(10);
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<LokarriaLogMessage>("http://se.umu.cs.robotics.lokarria", "log", 10);
        reader.addMessageHandler(new LokarriaMessageHandler());
        reader.setFilter(new LaserFilter());

        reader.start(new File("logs/DrivingToTheTV.log.xml"));

        for (LogEvent<LokarriaLogMessage> e : reader) {
            LaserArray laser = null;
            for (LokarriaLogMessage m : e.messages()) {
                laser = LaserArrayList.fromJSON(((JsonMessage) m).toJSON());
            }
//            for (LaserEcho echo: laser) {
//                System.out.print(Math.round(echo.getDistance())+";");
//            }
//            System.out.println("");
            SpaceDistribution<Double> pd = space.newDistribution(laser);
            System.out.println(pd);
        }
    }

    @Test
    public void testMotorSpace() throws SAXException, FileNotFoundException, IOException, ParseException {
        DifferentialDriveSpace space = new DifferentialDriveSpace(15, 10);
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<LokarriaLogMessage>("http://se.umu.cs.robotics.lokarria", "log", 10);
        reader.addMessageHandler(new LokarriaMessageHandler());
        reader.setFilter(new DifferentialDriveFilter());

        reader.start(new File("logs/DrivingToTheTV.log.xml"));

        for (LogEvent<LokarriaLogMessage> e : reader) {
            DifferentialDriveCommand command = null;
            for (LokarriaLogMessage m : e.messages()) {
                command = ConcreteDifferentialDriveCommand.fromJSON(((JsonMessage) m).toJSON());
            }
            SpaceDistribution<Double> pd = space.newDistribution(command);
            System.out.println(pd);
        }
    }

    @Test
    public void testSensoryMotorSpace() throws SAXException, FileNotFoundException, IOException, ParseException {
        LaserSpace sensorSpace = new LaserSpace(10);
        SensoryMotorSpace space = new SensoryMotorSpace(new DifferentialDriveSpace(15, 10), new LaserSpace(10));
        BufferedLogReader<LokarriaLogMessage> laserReader = new BufferedLogReader<LokarriaLogMessage>("http://se.umu.cs.robotics.lokarria", "log", 10);
        BufferedLogReader<LokarriaLogMessage> logReader = new BufferedLogReader<LokarriaLogMessage>("http://se.umu.cs.robotics.lokarria", "log", 10);
        SensoryMotorEventReader reader = new SensoryMotorEventReader(space, logReader, 50);
        laserReader.addMessageHandler(new LokarriaMessageHandler());
        logReader.addMessageHandler(new LokarriaMessageHandler());
        laserReader.setFilter(new LaserFilter());
        logReader.setFilter(new SensoryMotorFilter());

        laserReader.start(new File("logs/DrivingToTheTV.log.xml"));
        logReader.start(new File("logs/DrivingToTheTV.log.xml"));

        for (SpaceDistribution<Double> pd : reader) {
            LogEvent<LokarriaLogMessage> laserEvent = laserReader.next();
            LaserArray laser = null;
            for (LokarriaLogMessage m : laserEvent.messages()) {
                laser = LaserArrayList.fromJSON(((JsonMessage) m).toJSON());
            }
//            for (LaserEcho echo: laser) {
//                System.out.print(Math.round(echo.getDistance())+";");
//            }
//            System.out.println("");
            SpaceDistribution<Double> laserPd = sensorSpace.newDistribution(laser);
            System.out.println(laserPd);
            System.out.println(pd);
        }
    }

    @Test
    public void testBufferedLogReader() throws SAXException, FileNotFoundException, IOException {
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<LokarriaLogMessage>("http://se.umu.cs.robotics.lokarria", "log", 10);
        reader.addMessageHandler(new LokarriaMessageHandler());
        reader.start(new File("logs/DrivingToTheKitchen.log.xml"));
        for (int i = 0; i < 10; i++) {
            reader.next();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Test
    public void testLogPlayer() throws SAXException, FileNotFoundException, IOException {
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<LokarriaLogMessage>("http://se.umu.cs.robotics.lokarria", "log", 100);
        reader.addMessageHandler(new LokarriaMessageHandler());
        reader.start(new File("logs/DelayTest.log.xml"));
        LogPlayer<LokarriaLogMessage> player = new LogPlayer<LokarriaLogMessage>(reader);
        player.addEventListener(new LogEventListener<LokarriaLogMessage>() {

            long lastTime;

            public void eventFeedStart(LogEventProvider source) {
            }

            public void eventFeedEnd(LogEventProvider source) {
            }

            public void event(LogEventProvider source, LogEvent<LokarriaLogMessage> event) {
                long currentTime = System.currentTimeMillis();
                System.out.println("Time since last time stamp: " + (currentTime - lastTime));
                System.out.println(event);
                lastTime = currentTime;
            }
        });
        player.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
