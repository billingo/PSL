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
import org.xml.sax.SAXException;
import org.junit.Test;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class TestLokarriaPslPredictorPlot extends LokarriaPslPredictionTest {

    public TestLokarriaPslPredictorPlot() {
    }

    @Test
    public void canPredictSelf() throws SAXException, IOException, InterruptedException {
        int index = 0;
        train(predictor, "DrivingToTheTV5");
        train(predictor, "DrivingToTheTV5");
        train(predictor, "DrivingToTheTV5");
        train(predictor, "DrivingToTheTV5");
        train(predictor, "DrivingToTheTV5");
        train(predictor, "DrivingToTheTV5");
        train(predictor, "DrivingToTheTV5");
        train(predictor, "DrivingToTheTV5");
        test("DrivingToTheTV5", index);
    }

//    @Test
    public void canPredict2() throws SAXException, IOException, InterruptedException {
        int index = 2;
        trainAllFilesExcept(index,3);
        test(sourceFiles.get(index), index);
        test("DrivingAroundTheCorridor3", index);
    }

//    @Test
    public void canPredict3() throws SAXException, IOException, InterruptedException {
        int index = 3;
        trainAllFilesExcept(index,3);
        test(sourceFiles.get(index), index);
        test("DrivingAroundTheCorridor3", index);
    }

//    @Test
    public void canPredict4() throws SAXException, IOException, InterruptedException {
        int index = 4;
        trainAllFilesExcept(index,3);
        test(sourceFiles.get(index), index);
        test("DrivingAroundTheCorridor3", index);
    }

//    @Test
    public void canPredict5() throws SAXException, IOException, InterruptedException {
        int index = 5;
        trainAllFilesExcept(index,3);
        test(sourceFiles.get(index), index);
        test("DrivingAroundTheCorridor3", index);
    }
}
