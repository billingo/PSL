/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.lokarria.fpsl.test.prediction;

import se.umu.cs.robotics.lokarria.fpsl.LokarriaPslPredictor.Predictions;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.log.LogConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.fpsl.FPslParameters;
import se.umu.cs.robotics.statespace.comparator.UniformDoubleComparator;
import se.umu.cs.robotics.utils.MathTools;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ToTheTVGradientTest extends LokarriaPslPredictionTest {

    static Writer out;
    static double[] integrationGradients = {1, 1.5, 2, 2.5, 3, 4, 5};

    public ToTheTVGradientTest() {
    }

    @BeforeClass
    public static void openOutputFile() throws Exception {
        out = new FileWriter(new File("plots/ToTheTVGradientTest.py"));
    }

    @AfterClass
    public static void closeOutputFile() throws Exception {
        out.close();
    }

    @Before
    @Override
    public void setUpSourceFiles() {
        sourceFiles.add("ToTheTV.train.1");
        sourceFiles.add("ToTheTV.train.2");
        sourceFiles.add("ToTheTV.train.3");
        sourceFiles.add("ToTheTV.train.4");
//        sourceFiles.add("ToTheTV.train.5");
//        sourceFiles.add("ToTheTV.train.6");
//        sourceFiles.add("ToTheTV.train.7");
//        sourceFiles.add("ToTheTV.train.8");
//        sourceFiles.add("ToTheTV.train.9");
//        sourceFiles.add("ToTheTV.train.10");
    }

    @Before
    @Override
    public void setUpPredictor() {
    }

    @Test
    public void testAll() throws SAXException, IOException, InterruptedException {
        out.write("NaN = 0\n\n");
        out.write("predictionPerformances = {\n");
        for (double integrationGradient : integrationGradients) {
            System.out.println("INTEGRATION GRADIENT: "+integrationGradient);
            FPslParameters parameters = new FPslParameters();
            parameters.setIntegrationGradient(integrationGradient);
            out.write(String.format(Locale.US,"%.2f:{\n",integrationGradient));
            for (int i = 0; i < sourceFiles.size(); i++) {
                System.out.println("Testing "+sourceFiles.get(i));
                setUpPredictor(parameters);
                trainAllFilesExcept(i);
                LinkedPositionIterator<SensoryMotorDistribution> events = readEvents(sourceFiles.get(i));
                Predictions predictions = predictor.predict(events);
                out.write(String.format(Locale.US, "'%s':{'laserDistance':%.4f,'angularSpeedError':%.4f,'linearSpeedError':%.4f,'coverage':%.4f},\n",sourceFiles.get(i),MathTools.mean(predictions.getAverageLaserDistances()), MathTools.mean(predictions.getAngularSpeedErrors()), MathTools.mean(predictions.getLinearSpeedErrors()), predictions.getAverageMatch()));
            }
            out.write("},\n");
        }
        out.write("}\n");
    }
}
