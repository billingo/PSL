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
