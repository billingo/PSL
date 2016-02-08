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

package se.umu.cs.robotics.fpsl.junit;

import java.util.Locale;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import se.umu.cs.robotics.fpsl.FHypothesis;
import se.umu.cs.robotics.fpsl.selection.FLhsMatch;
import se.umu.cs.robotics.probabilitydistribution.iteration.SingleStateDistributionPositionIterator;
import java.util.ArrayList;
import se.umu.cs.robotics.fpsl.selection.MaxPredictionSelector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.iteration.position.ArrayPositionIterator;
import se.umu.cs.robotics.log.LogConfigurator;
import se.umu.cs.robotics.probabilitydistribution.FuzzyDistribution;
import se.umu.cs.robotics.probabilitydistribution.SingleStateSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.iteration.SpacePositionIterator;
import se.umu.cs.robotics.statespace.FloatDimension;
import se.umu.cs.robotics.statespace.FloatSpace;
import se.umu.cs.robotics.statespace.StateSpace;
import se.umu.cs.robotics.utils.ArrayTools;
import se.umu.cs.robotics.utils.MathTools;
import se.umu.cs.robotics.utils.StringTools;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ContextTest {

    final static int SQUARE_LENGTH = 10;
    static double[] square;
    static double[] sinus;
    static double[] triangle = {1, 2, 3, 4, 5, 4, 3, 2, 1, 2, 3, 4, 5, 4, 3, 2, 1, 2, 3, 4, 5, 4, 3, 2, 1, 2, 3, 4, 5, 4, 3, 2, 1, 2, 3, 4, 5, 4, 3, 2, 1, 2, 3, 4, 5, 4, 3, 2, 1, 2, 3, 4, 5, 4, 3, 2, 1, 2, 3, 4, 5, 4, 3, 2, 1, 2, 3, 4, 5, 4, 3, 2, 1, 2, 3, 4, 5};
    static StateSpace<Double> space = new FloatSpace(new FloatDimension(-5, 5, 0, 10));
    FLibrary<Double> library = new FLibrary<Double>(space);
    MaxPredictionSelector<Double> selector = new MaxPredictionSelector<Double>(library);

    public ContextTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        LogConfigurator.configure();
    }

    @BeforeClass
    public static void setUpSquare() throws Exception {
        square = new double[200];
        for (int i = 0; i < square.length; i++) {
            square[i] = i % (SQUARE_LENGTH * 2) >= SQUARE_LENGTH ? 2 : 3;
        }
    }

    @BeforeClass
    public static void setUpSinus() throws Exception {
        sinus = new double[200];
        for (int i = 0; i < sinus.length; i++) {
            double v = ((double) i) * Math.PI / 10d;
            sinus[i] = Math.cos(v) * 5;
//            sinus[i] = Math.signum(Math.sin(v))*5;
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        LogConfigurator.shutdown();
    }

    @After
    public void after() {
        library.getContext(1).setResponsibility(1.0);
        library.getContext(2).setResponsibility(0.0);
        System.out.println(String.format("Libnrary in context 1, size: %d", library.size()));
        System.out.println(library);
        library.getContext(1).setResponsibility(0.0);
        library.getContext(2).setResponsibility(1.0);
        System.out.println(String.format("Libnrary in context 2, size: %d", library.size()));
        System.out.println(library);
    }

    SpacePositionIterator<Double> iterate(double[] array, int startPos) {
        ArrayPositionIterator<Double> i = new ArrayPositionIterator<Double>(ArrayTools.toDoubleArray(array), startPos);
        SingleStateDistributionPositionIterator<Double> singleStateIterator = new SingleStateDistributionPositionIterator<Double>(space.getDimension(0), i);
        return new SpacePositionIterator<Double>(space, singleStateIterator);
    }

    public void setUpLibrary(double[] data) {
        for (int i = 0; i < data.length; i++) {
            if (i > 0) {
                teach(data, i);
            }
        }
    }

    public void teach(double[] data, int index) {
        FPrediction<Double> prediction = new FPrediction<Double>(selector, iterate(data, index - 1).previous(), true);
        FuzzyDistribution d = ((FuzzyDistribution) prediction.element().getDimension(0));
        List<FHypothesis<Double>> createdHypotheses = prediction.teach(new SingleStateSpaceDistribution<Double>(space, data[index]));
        for (FHypothesis<Double> h: createdHypotheses) {
            System.out.println(index + (library.getContext(1).getResponsibility() == 1 ? "A" : "B") + ": " + h);
        }
    }

    @After
    public void tearDown() {
    }

    void switchContext(int i) {
        if ((i) % (SQUARE_LENGTH * 2) >= SQUARE_LENGTH) {
            library.activateOnly(2);
        } else {
            library.activateOnly(1);
        }
    }

    ArrayList<double[]> predict(String name, double[] data) throws IOException {
        System.out.println(String.format("Running %s test...", name));
        ArrayList<double[]> predictions = new ArrayList<double[]>();
        double[] error = new double[data.length];

        int contextA = library.createNewContext("A", 1d);
        int contextB = library.createNewContext("B", 0d);
        library.getContext(0).setResponsibility(0);

        for (int e = 1; e < data.length; e++) {
            switchContext(e);
            teach(data, e);
            double[] pred = new double[data.length];
            for (int i = 1; i < data.length; i++) {
                switchContext(i);
                double v = data[i];
                FPrediction<Double> prediction = new FPrediction<Double>(selector, iterate(data, i - 1).previous(), true);
                FuzzyDistribution<Double> dist = (FuzzyDistribution<Double>) prediction.element().getDimension(0);
                double p = dist.defuzzify();
                pred[i] = p;
                error[i] = Math.abs(p - v);
//                if (e==150) {
//                    System.out.println(String.format("%d: %.1f %.1f", i, library.getResponsibility(contextA), library.getResponsibility(contextB)));
//                    System.out.println(prediction.getSelection().toString());
//                }

            }
            predictions.add(pred);
        }
        System.out.println(String.format("Error: %.3f", MathTools.mean(error)));
        return predictions;
    }

//    @Test
    public void canPredictSquare() throws IOException {
        ArrayList<double[]> predictions = predict("square", square);
        PyPlot plot = new PyPlot("square");
        plot.render(square, predictions);
        plot.close();
    }

//    @Test
    public void canPredictTriangle() throws IOException {
        ArrayList<double[]> predictions = predict("triangle", triangle);
        PyPlot plot = new PyPlot("triangle");
        plot.render(triangle, predictions);
        plot.close();
    }

    @Test
    public void canPredictSinus() throws IOException {
        ArrayList<double[]> predictions = predict("sinus", sinus);
        PyPlot plot = new PyPlot("sinus");
        plot.render(sinus, predictions);
        plot.close();
    }

//    @Test
    public void canInterpolate() {
        System.out.println("Running interpolation test...");
        setUpLibrary(square);
        double[] middle = {2.5, 2.5};
        for (int i = 0; i < 3; i++) {
            if (i > 0) {
                FPrediction<Double> prediction = new FPrediction<Double>(selector, iterate(middle, i - 1).previous(), true);
                FuzzyDistribution<Double> dist = (FuzzyDistribution<Double>) prediction.element().getDimension(0);
                System.out.println(dist);
                assertEquals(2.5, dist.defuzzify(), 0.1);
            }
        }
    }

    class PyPlot {

        FileWriter out;

        public PyPlot(String name) throws IOException {
            File file = new File(String.format("plots/%s.%s.py", ContextTest.class.getSimpleName(), name));
            out = new FileWriter(file);
        }

        void render(double[] data, double[] predictions) throws IOException {
            renderHead();
            out.write("data = ");
            renderData(out, data);
            out.write("\n");
            out.write("predictions = ");
            renderData(out, predictions);
            out.write("\n\n");

            out.write("import numpy as np\n");
            out.write("from matplotlib import pyplot\n");
            out.write("from matplotlib.lines import Line2D\n");
            out.write("from matplotlib.patches import Patch, Rectangle\n");
            out.write("from matplotlib.font_manager import FontProperties\n");
            out.write("\n");
            out.write("fig = pyplot.figure()\n");
            out.write("ax = fig.add_subplot(211)\n");
            out.write("lines = ax.plot(np.array([[data[i],predictions[i]] for i in range(len(data))]), '-o', ms=6, lw=1, alpha=0.7, picker=3)\n");
            out.write("pyplot.show()\n");

        }

        void render(double[] data, List<double[]> predictions) throws IOException {
            renderHead();

            out.write("data = ");
            renderData(out, data);
            out.write("\n");
            out.write("predictions = (\n");
            for (double[] pred : predictions) {
                renderData(out, pred);
                out.write(",\n");
            }
            out.write(")\n\n");

            renderImports();

            out.write("fig = pyplot.figure()\n");
            out.write("ax = fig.add_subplot(111)\n");
            out.write("pyplot.title('PSL predictions during training')\n");
            out.write("lineData, = ax.plot(data, '-o', lw=1, alpha=0.7)\n");
            out.write("linePredictions, = ax.plot([], [], '-o', lw=1, alpha=0.7, animated=True)\n");
            out.write("lineTime, = ax.plot([], [], '-o', lw=1, alpha=0.7, animated=True)\n");
            out.write("def render():\n");
            out.write("\tbackground = fig.canvas.copy_from_bbox(ax.bbox)\n");

            out.write("\tfor i in range(len(predictions)):\n");
            out.write("\t\tfig.canvas.restore_region(background)\n");
            out.write("\t\tlinePredictions.set_data(range(len(predictions[i])), predictions[i])\n");
            out.write("\t\tlineTime.set_data([i,i],[5,-5])\n");
            out.write("\n");
            out.write("\t\t# just draw the animated artist\n");
            out.write("\t\tax.draw_artist(linePredictions)\n");
            out.write("\t\tax.draw_artist(lineTime)\n");
            out.write("\t\t# just redraw the axes rectangle\n");
            out.write("\t\tfig.canvas.blit(ax.bbox)\n");
            out.write("\t\ttime.sleep(0.05)\n");
            out.write("\n");
            out.write("manager = pyplot.get_current_fig_manager()\n");
            out.write("manager.window.after(100, render)\n");
            out.write("pyplot.show()\n");

        }

        private void renderHead() throws IOException {
            out.write("'''\n");
            out.write("\tPlot generated by ");
            out.write(this.getClass().getSimpleName());
            out.write("\n\t");
            out.write(new Date().toString());
            out.write("\n'''\n\n");
        }

        private void renderImports() throws IOException {
            out.write("import numpy as np\n");
            out.write("import sys, time\n");
            out.write("from matplotlib import pyplot\n");
            out.write("from matplotlib.lines import Line2D\n");
            out.write("from matplotlib.patches import Patch, Rectangle\n");
            out.write("from matplotlib.font_manager import FontProperties\n");
            out.write("\n");
        }

        private void renderData(FileWriter out, double[] data) throws IOException {
            out.write("[");
            out.write(StringTools.join(data, ","));
            out.write("]");
        }

        public void close() throws IOException {
            out.close();
        }
    }
}
