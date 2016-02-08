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

package se.umu.cs.robotics.fpsl;

import se.umu.cs.robotics.hpl.HplPropertyLoader;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class FPslParameters {

    /**
     * Multiplier of initial tolerance (cone width). 1 = minimum tolerance (based on dimension resolution).
     */
    double INITIAL_COMPARATOR_TOLERANCE = 1;
    FuzzyAnd FUZZY_AND = FuzzyAnd.MIN;
    private final int maxHypothesisLength;
    private double integrationGradient;
    private final double thresholdCorrect;
    private final double thresholdConfidence;
    private final int parentMinimumHitCount;
    private final int parentMinimumMissCount;
    private final boolean useLinguisticValues;
    private final boolean propagateConfidenceChangeToParents;
    private final double responsibilityUpdateGradient;
    private final double responsibilityMinConfidence;
    
    public static enum FuzzyAnd {

        MIN, PRODUCT
    }
    
    public static enum DefuzzyficationFunction {
        CENTER_OF_SUM, MAX, MAX_PRODUCT, MAX_PROBABILITY
    }

    public FPslParameters() {
        maxHypothesisLength = HplPropertyLoader.loadIntegerProperty("fpsl.maxHypothesisLength");
        thresholdCorrect = HplPropertyLoader.loadDoubleProperty("fpsl.thresholdCorrect");
        thresholdConfidence = HplPropertyLoader.loadDoubleProperty("fpsl.thresholdConfidence");
        parentMinimumHitCount = HplPropertyLoader.loadIntegerProperty("fpsl.growth.hitcount");
        parentMinimumMissCount = HplPropertyLoader.loadIntegerProperty("fpsl.growth.misscount");
        useLinguisticValues = HplPropertyLoader.loadBooleanProperty("fpsl.useLinguisticValues");
        propagateConfidenceChangeToParents = HplPropertyLoader.loadBooleanProperty("fpsl.propagateConfidenceChangeToParents");
        integrationGradient = HplPropertyLoader.loadDoubleProperty("fpsl.integrationGradient");
        responsibilityUpdateGradient = HplPropertyLoader.loadDoubleProperty("fpsl.responsibilityUpdateGradient");
        responsibilityMinConfidence = HplPropertyLoader.loadDoubleProperty("fpsl.responsibilityMinConfidence");
    }

    public FuzzyAnd andOperator() {
        return FUZZY_AND;
    }

    public double getComparatorTolerance() {
        return INITIAL_COMPARATOR_TOLERANCE;
    }

    public double thresholdCorrect() {
        return thresholdCorrect;
    }
    
    public double thresholdConfidence() {
        return thresholdConfidence;
    }
    
    public int maxHypothesisLength() {
        return maxHypothesisLength;
    }

    public double getIntegrationGradient() {
        return integrationGradient;
    }

    public void setIntegrationGradient(double i) {
        this.integrationGradient = i;
    }

    public boolean propagateConfidenceChangeToParents() {
        return propagateConfidenceChangeToParents;
    }

    public int parentMinimumHitCount() {
        return parentMinimumHitCount;
    }

    public int parentMinimumMissCount() {
        return parentMinimumMissCount;
    }

    public boolean useLinguisticValues() {
        return useLinguisticValues;
    }
    
    public DefuzzyficationFunction defuzzyficationFunction() {
        return DefuzzyficationFunction.MAX_PRODUCT;
//        return DefuzzyficationFunction.MAX_PROBABILITY;
    }
    
    public double responsibilityUpdateGradient() {
        return responsibilityUpdateGradient;
    }
    
    public double responsibilityMinConfidence() {
        return responsibilityMinConfidence;
    }
}
