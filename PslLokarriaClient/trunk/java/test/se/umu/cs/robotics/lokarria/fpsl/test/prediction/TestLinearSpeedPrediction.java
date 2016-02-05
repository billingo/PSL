/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.lokarria.fpsl.test.prediction;

import se.umu.cs.robotics.lokarria.fpsl.plots.PyPlot;
import se.umu.cs.robotics.utils.MathTools;
import se.umu.cs.robotics.probabilitydistribution.iteration.SingleStateDistributionPositionIterator;
import se.umu.cs.robotics.iteration.position.ArrayPositionIterator;
import java.io.IOException;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SingleStateSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.FuzzyDistribution;
import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.statespace.StateSpace;
import se.umu.cs.robotics.statespace.FloatSpace;
import java.util.ArrayList;
import org.junit.Test;
import se.umu.cs.robotics.probabilitydistribution.SingleStateDistribution;
import se.umu.cs.robotics.probabilitydistribution.iteration.DimensionPositionIterator;
import se.umu.cs.robotics.probabilitydistribution.iteration.SpacePositionIterator;
import se.umu.cs.robotics.utils.ArrayTools;
import se.umu.cs.robotics.fpsl.selection.FHypothesisSelector;
import se.umu.cs.robotics.fpsl.selection.MaxProductSelector;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class TestLinearSpeedPrediction extends LokarriaPslPredictionTest {

    final StateSpace<Double> linearSpace = new FloatSpace(space.getMotorSpace().getLinearDimension());
    final FLibrary<Double> library = new FLibrary<>(linearSpace);
    final FHypothesisSelector<Double> selector = new MaxProductSelector<>(library);

    public TestLinearSpeedPrediction() {
    }

    public void trainLibrary(double[] data, int repeat) {
        for (int epoch = 0; epoch < repeat; epoch++) {
            for (int i = 0; i < data.length; i++) {
                if (i > 0) {
                    teach(data, i);
                }
            }
        }
    }

    SpacePositionIterator<Double> iterate(double[] array, int startPos) {
        ArrayPositionIterator<Double> i = new ArrayPositionIterator<Double>(ArrayTools.toDoubleArray(array), startPos);
        SingleStateDistributionPositionIterator<Double> singleStateIterator = new SingleStateDistributionPositionIterator<Double>(linearSpace.getDimension(0), i);
        return new SpacePositionIterator<Double>(linearSpace, singleStateIterator);
    }

    public void teach(double[] data, int index) {
        FPrediction<Double> prediction = new FPrediction<Double>(selector, iterate(data, index - 1).previous(), true);
        FuzzyDistribution d = ((FuzzyDistribution) prediction.element().getDimension(0));
        prediction.teach(new SingleStateSpaceDistribution<Double>(linearSpace, data[index]));
    }

    private SpacePositionIterator<Double> readSpeeds() throws SAXException {
        LinkedPositionIterator source = readEvents("DrivingToTheTV5");
        DimensionPositionIterator<Double> events = new DimensionPositionIterator<Double>(source, 1);
        SpacePositionIterator<Double> speeds = new SpacePositionIterator<Double>(linearSpace, events);
        return speeds;
    }

    private double[] readSpeedsToArray() throws SAXException {
        SpacePositionIterator<Double> i = readSpeeds();
        ArrayList<Double> list = new ArrayList<Double>();
        while (i.hasNext()) {
            final ProbabilityDistribution<Double> dimension = i.next().element().getDimension(0);
            final Double state = ((SingleStateDistribution<Double>) dimension).getState();
            list.add(state);
        }
        return ArrayTools.toDoubleArray(list);
    }

    ArrayList<double[]> predict(String name, double[] data) throws IOException {
        System.out.println(String.format("Running %s test...", name));
        ArrayList<double[]> predictions = new ArrayList<double[]>();
        double[] error = new double[data.length];

        for (int e = 1; e < data.length; e++) {
//            teach(data, e);
            double[] pred = new double[data.length];
            for (int i = 1; i < data.length; i++) {
                double v = data[i];
                if (i > 0) {
                    FPrediction<Double> prediction = new FPrediction<Double>(selector, iterate(data, i - 1).previous(), true);
                    FuzzyDistribution<Double> dist = (FuzzyDistribution<Double>) prediction.element().getDimension(0);
                    double p = dist.defuzzify();
                    pred[i] = p;
                    error[i] = Math.abs(p - v);
                }
            }
            predictions.add(pred);
        }
        System.out.println(String.format("Error: %.3f", MathTools.mean(error)));
        return predictions;
    }

    @Test
    public void canPredictLinearSpeed() throws SAXException, IOException {
        double[] speeds = readSpeedsToArray();
        trainLibrary(speeds, 10);
        ArrayList<double[]> predictions = predict("linear", speeds);
        PyPlot plot = new PyPlot(this.getClass().getSimpleName());
        plot.render(speeds, predictions);
        plot.close();
    }
}


