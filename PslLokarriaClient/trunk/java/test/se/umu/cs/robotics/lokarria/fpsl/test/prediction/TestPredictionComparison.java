
package se.umu.cs.robotics.lokarria.fpsl.test.prediction;

import java.io.IOException;
import se.umu.cs.robotics.lokarria.fpsl.LokarriaPslPredictor.Predictions;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.lokarria.fpsl.LokarriaPslPredictor;
import org.junit.Test;
import se.umu.cs.robotics.lokarria.fpsl.plots.PredictionComparisonPlot;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class TestPredictionComparison extends LokarriaPslPredictionTest {

    LokarriaPslPredictor tvPredictor = new LokarriaPslPredictor(space, true, true);
    LokarriaPslPredictor corridorPredictor = new LokarriaPslPredictor(space, true, true);
    LokarriaPslPredictor fridgePredictor = new LokarriaPslPredictor(space, true, true);

    public TestPredictionComparison() {
    }

    @Test
    public void canCompare() throws InterruptedException, SAXException, IOException {
//        train(tvPredictor,"FromFridgeToTV1");
//        train(tvPredictor,"FromFridgeToTV2");
//        train(tvPredictor,"FromFridgeToTV3");
//        train(tvPredictor,"FromFridgeToTV1");
//        train(tvPredictor,"FromFridgeToTV2");
//        train(tvPredictor,"FromFridgeToTV3");
//        train(tvPredictor,"FromFridgeToTV1");
//        train(tvPredictor,"FromFridgeToTV2");
//        train(tvPredictor,"FromFridgeToTV3");
        train(tvPredictor, "DrivingToTheTV4");
        train(tvPredictor, "DrivingToTheTV5");
        train(tvPredictor, "DrivingToTheTV6");
        train(tvPredictor, "DrivingToTheTV7");
        train(tvPredictor, "DrivingToTheTV8");
        train(tvPredictor, "DrivingToTheTV9");
        train(tvPredictor, "DrivingToTheTV4");
        train(tvPredictor, "DrivingToTheTV5");
        train(tvPredictor, "DrivingToTheTV6");
        train(tvPredictor, "DrivingToTheTV7");
        train(tvPredictor, "DrivingToTheTV8");
        train(tvPredictor, "DrivingToTheTV9");
        train(corridorPredictor, "DrivingAroundTheCorridor3");
        train(corridorPredictor, "DrivingAroundTheCorridor4");
        train(corridorPredictor, "DrivingAroundTheCorridor5");
//        train(fridgePredictor, "DriveArroundTheKitchen1");
//        train(fridgePredictor, "DriveArroundTheKitchen2");
//        train(fridgePredictor, "DriveArroundTheKitchen1");
        train(fridgePredictor, "DriveArroundTheKitchen2");
        train(fridgePredictor, "DriveArroundTheKitchen2");
//        train(fridgePredictor, "DriveArroundTheKitchen3");

        //        LinkedPositionIterator<SensoryMotorDistribution> events = readEvents("DrivingToTheTV9");
        LinkedPositionIterator<SensoryMotorDistribution> events = readEvents("FromBedroomCornerToKitchenAndTV");
        Predictions tvPredictions = tvPredictor.predict(events.clone());
        Predictions corridorPredictions = corridorPredictor.predict(events.clone());
        Predictions fridgePredictions = fridgePredictor.predict(events.clone());
        PredictionComparisonPlot plot = new PredictionComparisonPlot("Comparison");
        plot.render(tvPredictions, corridorPredictions, fridgePredictions);
        plot.renderLegends("TV","Corridor","Kitchen");
        plot.close();
    }
}
