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

package se.umu.cs.robotics.lokarria.fpsl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xml.sax.SAXException;
import se.umu.cs.robotics.fpsl.FLibrary;
import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.fpsl.FPslParameters;
import se.umu.cs.robotics.fpsl.selection.FHypothesisMatch;
import se.umu.cs.robotics.fpsl.selection.FHypothesisSelection;
import se.umu.cs.robotics.fpsl.selection.FHypothesisSelector;
import se.umu.cs.robotics.fpsl.selection.MaxPredictionSelection;
import se.umu.cs.robotics.fpsl.selection.MaxProductSelector;
import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorSpace;
import se.umu.cs.robotics.lokarria.statespace.SpeedDimension;
import se.umu.cs.robotics.probabilitydistribution.FuzzyDistribution;
import se.umu.cs.robotics.probabilitydistribution.GenericSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SingleStateDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.utils.MathTools;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LokarriaPslPredictor {

    int PREDICTION_STEPS = 1;
    private final SensoryMotorSpace space;
    private final FLibrary<Double> library;
    private final FHypothesisSelector<Double> selector;
    private final boolean predictMotors;
    private final boolean predictSensors;

    public LokarriaPslPredictor(SensoryMotorSpace space, boolean predictSensors, boolean predictMotors) {
        this(space,predictSensors,predictMotors, new FPslParameters());
    }

    public LokarriaPslPredictor(SensoryMotorSpace space, boolean predictSensors, boolean predictMotors, FPslParameters parameters) {
        this(space,predictMotors,predictMotors,parameters,"LokarriaPslPredictor");
    }
    
    public LokarriaPslPredictor(SensoryMotorSpace space, boolean predictSensors, boolean predictMotors, FPslParameters parameters, String ... contexts) {
        this.space = space;
        this.predictSensors = predictSensors;
        this.predictMotors = predictMotors;
        this.library = new FLibrary<>(this.space, parameters, contexts);
        this.selector = new MaxProductSelector<>(library);
    }

    public LokarriaPslTrainer train(File file) throws SAXException {
        final LokarriaPslTrainer trainer = new LokarriaPslTrainer(library, file);
        trainer.start();
        return trainer;
    }

    public List<LokarriaPslTrainer> train(List<File> files) throws SAXException, InterruptedException {
        ArrayList<LokarriaPslTrainer> trainers = new ArrayList<LokarriaPslTrainer>();
        for (File file : files) {
            LokarriaPslTrainer trainer = train(file);
            trainer.await();
            trainers.add(trainer);
        }
        return trainers;
    }

    public Predictions predict(PositionIterator<SensoryMotorDistribution> events) {
        events.next();
        Predictions predictions = new Predictions();
        while (events.hasNext()) {
            IteratorPosition<SensoryMotorDistribution> p = events.next();
            SensoryMotorDistribution next = p.element();
            if ((next.getMotorDistribution() != null && predictMotors) || (next.getSensoryDistribution() != null && predictSensors)) {
                predict(p, predictions);
            }
        }
        return predictions;
    }

    private void predict(IteratorPosition<SensoryMotorDistribution> p, Predictions predictions) {
        FPrediction prediction = new FPrediction(selector, p.getPrevious(), true);
        for (int i = 1; i < PREDICTION_STEPS; i++) {
            if (p.hasNext()) {
                prediction = prediction.getNext();
                p = p.getNext();
            } else {
                return;
            }
        }
        predictions.add(prediction.getSelection(), p.element());
    }

    public FLibrary<Double> getLibrary() {
        return library;
    }

    public boolean isPredictingMotors() {
        return predictMotors;
    }

    public boolean isPredictingSensors() {
        return predictSensors;
    }

    public class Predictions {

        private ArrayList<FHypothesisSelection<Double>> predictions = new ArrayList<FHypothesisSelection<Double>>();
        private ArrayList<SensoryMotorDistribution> facit = new ArrayList<SensoryMotorDistribution>();

        public void add(FHypothesisSelection<Double> prediction, SensoryMotorDistribution correct) {
            predictions.add(prediction);
            facit.add(correct);

        }

        public SensoryMotorDistribution getCorrect(int index) {
            return facit.get(index);
        }

        public FHypothesisSelection<Double> getPrediction(int index) {
            return predictions.get(index);
        }

        public ArrayList<Double> getAverageLaserDistances() {
            ArrayList<Double> distances = new ArrayList<Double>(predictions.size());
            Iterator<FHypothesisSelection<Double>> pi = predictions.iterator();
            Iterator<SensoryMotorDistribution> fi = facit.iterator();
            while (pi.hasNext()) {
                ArrayList<Double> dists = new ArrayList<Double>();
                final FHypothesisSelection<Double> selection = pi.next();
                SpaceDistribution prediction = selection.getTarget();
                final GenericSpaceDistribution<Double> sensoryDistribution = fi.next().getSensoryDistribution();
                if (sensoryDistribution == null || selection.size() == 0) {
                    distances.add(null);
                } else {
                    Iterator<? extends ProbabilityDistribution<Double>> dims = sensoryDistribution.dimensions();
                    while (dims.hasNext()) {
                        ProbabilityDistribution<Double> dist = dims.next();
                        ProbabilityDistribution<Double> pDist = prediction.getDimension(dist.getDimension());
                        if (dist != null && pDist != null) {
                            Double distance = Math.abs(centerOfDist(pDist) - centerOfDist(dist));
                            dists.add(distance);
                        }
                    }
                    distances.add(MathTools.mean(dists));
                }
            }
            return distances;
        }

        protected ArrayList<Double> getSpeedErrors(SpeedDimension dim) {
            ArrayList<Double> errors = new ArrayList<Double>(predictions.size());
            Iterator<FHypothesisSelection<Double>> pi = predictions.iterator();
            Iterator<SensoryMotorDistribution> fi = facit.iterator();
            while (pi.hasNext()) {
                final FHypothesisSelection<Double> selection = pi.next();
                SpaceDistribution prediction = selection.getTarget();
                GenericSpaceDistribution<Double> motorDistribution = fi.next().getMotorDistribution();
                if (motorDistribution == null || selection.size() == 0) {
                    errors.add(null);
                } else {
                    ProbabilityDistribution<Double> dist = motorDistribution.getDimension(dim);
                    ProbabilityDistribution<Double> pDist = prediction.getDimension(dim);
                    if (dist != null && pDist != null) {
                        Double distance = Math.abs(centerOfDist(pDist) - centerOfDist(dist));
                        errors.add(distance);
                    } else {
                        errors.add(null);
                    }
                }
            }
            return errors;
        }

        public ArrayList<Double> getAngularSpeedErrors() {
            return getSpeedErrors(space.getMotorSpace().getAngularDimension());
        }

        public ArrayList<Double> getLinearSpeedErrors() {
            return getSpeedErrors(space.getMotorSpace().getLinearDimension());
        }

        public ArrayList<Double> getIntersections() {
            ArrayList<Double> intersections = new ArrayList<Double>(predictions.size());
            Iterator<FHypothesisSelection<Double>> pi = predictions.iterator();
            Iterator<SensoryMotorDistribution> fi = facit.iterator();
            while (pi.hasNext()) {
                double intersection = pi.next().getTarget().intersection(fi.next());
                intersections.add(intersection);
            }
            return intersections;
        }

        public ArrayList<Double> getIntersections(double threshold) {
            ArrayList<Double> intersections = new ArrayList<Double>(predictions.size());
            Iterator<FHypothesisSelection<Double>> pi = predictions.iterator();
            Iterator<SensoryMotorDistribution> fi = facit.iterator();
            while (pi.hasNext()) {
                double intersection = pi.next().getTarget().intersection(fi.next());
                intersections.add(intersection >= threshold ? 1d : 0d);
            }
            return intersections;
        }

        public ArrayList<Double> getMaxComparison() {
            ArrayList<Double> intersections = new ArrayList<Double>(predictions.size());
            Iterator<FHypothesisSelection<Double>> pi = predictions.iterator();
            Iterator<SensoryMotorDistribution> fi = facit.iterator();
            ArrayList<Double> dimMatch = new ArrayList<Double>();
            while (pi.hasNext()) {
                SpaceDistribution pElement = pi.next().getTarget();
                SensoryMotorDistribution fElement = fi.next();
                Iterator<? extends ProbabilityDistribution<Double>> pDimensions = pElement.dimensions();
                Iterator<? extends ProbabilityDistribution<Double>> fDimensions = fElement.dimensions();
                dimMatch.clear();
                while (pDimensions.hasNext()) {
                    final ProbabilityDistribution<Double> pNext = pDimensions.next();
                    final ProbabilityDistribution<Double> fNext = fDimensions.next();
                    if (pNext != null && fNext != null) {
                        Double pMax = pNext.max().next().element();
                        Double fMax = fNext.max().next().element();
                        dimMatch.add(1d - pNext.getDimension().comparator().distance(pMax, fMax));
                    }
                }
                intersections.add(MathTools.mean(dimMatch));
            }
            return intersections;
        }

//        public ArrayList<Double> getBestComparison() {
//            ArrayList<Double> intersections = new ArrayList<Double>(predictions.size());
//            Iterator<FHypothesisSelection<Double>> pi = predictions.iterator();
//            Iterator<SensoryMotorDistribution> fi = facit.iterator();
//            while (pi.hasNext()) {
//                FLhsMatch bestMatch = pi.next().bestMatch();
//                double intersection = bestMatch == null ? 0 : bestMatch.getHypothesis().getTarget().intersection(fi.next());
//                intersections.add(intersection);
//            }
//            return intersections;
//        }

        public Iterable<FHypothesisSelection<Double>> getPredictions() {
            return predictions;
        }

        public int size() {
            return predictions.size();
        }

        /**
         * @return the average hypothesis length for selected hypotheses in each prediction.
         */
        public double[] getHypothesesLengths() {
            double[] len = new double[predictions.size()];
            int i = 0;
            for (FHypothesisSelection<Double> selection : predictions) {
                ArrayList<Integer> clen = new ArrayList<Integer>();
                for (FHypothesisMatch<Double> match: selection.getTargetHypotheses()) {
                    clen.add(match.getHypothesis().length());
                }
                len[i++] = clen.isEmpty() ? 0d : MathTools.mean(clen);
            }

            return len;
        }

        public int[] getHypothesisMatchCounts() {
            int[] len = new int[predictions.size()];
            int i = 0;
            for (FHypothesisSelection<Double> selection : predictions) {
                for (FHypothesisMatch<Double> match: selection.getTargetHypotheses()) {
                    len[i]++;
                }
                i++;
            }
            return len;
        }

        public int[] getHypothesisConsiderationCounts() {
            int[] counts = new int[predictions.size()];
            int i = 0;
            for (FHypothesisSelection<Double> selection : predictions) {
                counts[i++] = ((MaxPredictionSelection<Double>) selection).getMatches().size();
            }
            return counts;
        }

        public double getAverageMatch() {
            double match = 0;
            double count = 0;
            for (FHypothesisSelection<Double> selection : predictions) {
                match += selection.size() > 0 ? 1d : 0d;
                count ++;
            }
            return match/count;
        }
    }

    public static double centerOfDist(ProbabilityDistribution<Double> dist) {
        if (dist instanceof SingleStateDistribution) {
            return ((SingleStateDistribution<Double>) dist).getState();
        } else {
            return ((FuzzyDistribution) dist).defuzzify();
        }
    }
}
