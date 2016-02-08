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
