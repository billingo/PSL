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

package se.umu.cs.robotics.lokarria.fpsl.test.prediction;

import java.util.ArrayList;
import se.umu.cs.robotics.statespace.comparator.LinearDoubleComparator;
import se.umu.cs.robotics.lokarria.fpsl.LokarriaPslPredictor.Predictions;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import org.xml.sax.SAXException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.fpsl.FPslParameters;
import se.umu.cs.robotics.hpl.HplPropertyLoader;
import se.umu.cs.robotics.io.WriterFormat;
import se.umu.cs.robotics.utils.MathTools;
import se.umu.cs.robotics.utils.StringTools;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class TestDrivingToTheTVAll extends LokarriaPslPredictionTest {
    public static final Double integrationGradient = new Double(HplPropertyLoader.loadProperty("fpsl.integrationGradient"));
    
    static WriterFormat out;
    static double[] toleranceMultipliers = {0.2, 0.4, 0.6, 0.8, 1.0, 1.2, 1.4, 1.6, 1.8, 2.0};
//    static double[] toleranceMultipliers = {0.6, 0.8, 1.0};

    public TestDrivingToTheTVAll() {
    }
    
    @BeforeClass
    public static void printInfo() {
        System.out.println("Driving to the TV - Prediction Test");
        System.out.println("Running test over with the following tolerance pultipliers: "+StringTools.join(toleranceMultipliers,", "));
        System.out.println(String.format(Locale.US,"Using integration gradient: %.1f",integrationGradient));
        
    }
    
    @BeforeClass
    public static void openOutputFile() throws Exception {
        final String targetFile = String.format(Locale.US,"%s/TestDrivingToTheTVAll.int%.1f.py",HplPropertyLoader.loadProperty("plot.path"),integrationGradient);
        out = new WriterFormat(new FileWriter(new File(targetFile)));
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
        sourceFiles.add("ToTheTV.train.5");
        sourceFiles.add("ToTheTV.train.6");
        sourceFiles.add("ToTheTV.train.7");
        sourceFiles.add("ToTheTV.train.8");
        sourceFiles.add("ToTheTV.train.9");
        sourceFiles.add("ToTheTV.train.10");
    }
    
    @Before
    @Override
    public void setUpPredictor() {
    }
    
    @Test
    public void testAll() throws SAXException, IOException, InterruptedException {
        ArrayList<Object> trainingHistory = new ArrayList<Object>();
        out.write("NaN = 0\n\n");
        out.write("integrationGradient = %.1f\n\n",integrationGradient);
        out.write("predictionPerformances = {\n");
        for (double toleranceMultiplier : toleranceMultipliers) {
            trainingHistory.add(toleranceMultiplier);
            FPslParameters parameters = new FPslParameters();
            LinearDoubleComparator.TOLERANCE_MULTIPLIER = toleranceMultiplier;
            out.write("%.2f:{\n", toleranceMultiplier);
            for (int i = 0; i < sourceFiles.size(); i++) {
                trainingHistory.add(sourceFiles.get(i));
                System.out.println(String.format(Locale.US,"Testing %s using tolerance multiplier: %.2f.",sourceFiles.get(i),toleranceMultiplier));
                setUpPredictor(parameters);
                trainingHistory.add(trainAllFilesExcept(i));
                LinkedPositionIterator<SensoryMotorDistribution> events = readEvents(sourceFiles.get(i));
                Predictions predictions = predictor.predict(events);
                out.write("'%s':{'laserDistance':%.4f,'angularSpeedError':%.4f,'linearSpeedError':%.4f,'coverage':%.4f},\n", sourceFiles.get(i), MathTools.mean(predictions.getAverageLaserDistances()), MathTools.mean(predictions.getAngularSpeedErrors()), MathTools.mean(predictions.getLinearSpeedErrors()), predictions.getAverageMatch());
            }
            out.write("},\n");
        }
        out.write("}\n");
        renderTrainingHistory(trainingHistory);
    }

    private void renderTrainingHistory(ArrayList<Object> trainingHistory) throws IOException {
        out.write("\ntrainingHistory = {\n");
        boolean first = true;
        for (Object th : trainingHistory) {
            if (th instanceof Double) {
                if (first) {
                    first = false;
                } else {
                    out.write("\t},\n");
                }
                out.write("\t%.2f:{\n", th);
            } else if (th instanceof String) {
                out.write("\t'%s':", th);
            } else {
                out.write("\t[\n");
                for (TrainingStatus ts : (ArrayList<TrainingStatus>) th) {
                    out.write("\t\t");
                    out.write(ts.toString());
                    out.write(",\n");
                }
                out.write("\t],\n");
            }
            
        }
        out.write("\t}\n}\n");
    }
}
