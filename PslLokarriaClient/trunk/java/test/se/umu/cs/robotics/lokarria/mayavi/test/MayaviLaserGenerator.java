/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.lokarria.mayavi.test;

import java.io.Writer;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;
import se.umu.cs.robotics.logreader.xml.BufferedLogReader;
import se.umu.cs.robotics.logreader.lokarria.LokarriaMessageHandler;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;
import se.umu.cs.robotics.lokarria.statespace.LaserSpace;
import se.umu.cs.robotics.lokarria.statespace.DifferentialDriveSpace;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.log.LogConfigurator;
import se.umu.cs.robotics.logreader.lokarria.LokarriaMessageIterator;
import se.umu.cs.robotics.logreader.lokarria.LokarriaMessageReader;
import se.umu.cs.robotics.logreader.lokarria.filter.LaserFilter;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.lokarria.mayavi.LaserNumpyConverter;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class MayaviLaserGenerator {

    public static final String OUTPUT_SCRIPT_NAME = "lasersurface.py";
    public static final String SCRIPT_NAME = "laser";
    public static final String INPUT_FILE = "logs/DrivingOut1.log.xml";

    final SensoryMotorSpace space = new SensoryMotorSpace(new DifferentialDriveSpace(15, 10), new LaserSpace(5));

    public MayaviLaserGenerator() {
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
    public void generateLaserSurfaceScript() throws IOException, SAXException {
        Writer out = new FileWriter(new File(OUTPUT_SCRIPT_NAME));
        LaserNumpyConverter laserConverter = new LaserNumpyConverter(SCRIPT_NAME);

        out.write("from numpy import *\n");
        out.write("from enthought.mayavi import mlab\n");
        
        BufferedLogReader<LokarriaLogMessage> reader = new BufferedLogReader<LokarriaLogMessage>("http://www.cs.umu.se/robotics", "log", 100);
        reader.addMessageHandler(new LokarriaMessageHandler());
        reader.setFilter(new LaserFilter());
        reader.start(new File(INPUT_FILE));

        Iterator<LaserArray> echoes = (IterableIterator) new LokarriaMessageReader(new LokarriaMessageIterator(reader));
        laserConverter.writePointArray(out,echoes);
        laserConverter.writeAngularLines(out);
        out.write("src = mlab.pipeline.scalar_scatter(laser.x, laser.y, laser.t, laser.t)\n");
        out.write("src.mlab_source.dataset.lines = laser.connections\n");
        out.write("lines = mlab.pipeline.stripper(src)\n");
        out.write("mlab.pipeline.surface(lines, colormap='Accent', line_width=1, opacity=.4)\n");

        out.close();
    }
}
