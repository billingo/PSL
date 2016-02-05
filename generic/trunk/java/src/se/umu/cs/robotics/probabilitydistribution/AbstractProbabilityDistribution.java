/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.probabilitydistribution;

import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.statespace.StateDimension;

/**
 *
 * @author billing
 */
public abstract class AbstractProbabilityDistribution<E> implements ProbabilityDistribution<E> {

    private final StateDimension<E> dimension;

    public AbstractProbabilityDistribution(StateDimension<E> dimension) {
        this.dimension = dimension;
    }

    @Override
    public StateDimension<E> getDimension() {
        return dimension;
    }

    @Override
    public double intersection(ProbabilityDistribution<E> pd) {
        if (pd instanceof SingleStateDistribution) {
            return pd.intersection(this);
        } else {
            double overlap = 0;
            for (FuzzyItem<E> item: nonZeroStates()) {
                overlap += Math.min(item.value(), pd.getProbability(item.element()));
            }
            return overlap;
        }
    }
}
