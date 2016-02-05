/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright 2011 Benjamin Fonooni and Erik Billing
 fonooni@cs.umu.se, billing@cs.umu.se
Department of Computing Science, Umea University, Sweden,
(http://www.cognitionreversed.com).

LICENSE:

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston,
MA 02111-1307, USA.
\*-------------------------------------------------------------------*/
package se.umu.cs.robotics.lokarria.statespace;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.ArrayIterator;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.probabilitydistribution.DualStateDistribution;
import se.umu.cs.robotics.probabilitydistribution.FuzzyDistribution;
import se.umu.cs.robotics.probabilitydistribution.GenericSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SingleStateDistribution;
import se.umu.cs.robotics.probabilitydistribution.SingleStateSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.StateDimension;
import se.umu.cs.robotics.statespace.StateSpace;
import se.umu.cs.robotics.statespace.comparator.DimensionComparator;
import se.umu.cs.robotics.statespace.comparator.LinearDoubleComparator;
import se.umu.cs.robotics.statespace.comparator.MinDimensionComparator;
import se.umu.cs.robotics.statespace.comparator.StateComparator;
import se.umu.cs.robotics.statespace.comparator.UniformDoubleComparator;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SensoryMotorSpace implements StateSpace<Double> {

    public static final boolean CONCATENATE_SENSORS_AND_MOTORS = true;
    public static final int DEFAULT_INTEGRATION_TIMESTEP = 50;

    private final DifferentialDriveSpace ddrive;
    private final LaserSpace laser;
    private final DimensionComparator comparator;

    public SensoryMotorSpace(DifferentialDriveSpace ddrive, LaserSpace laser) {
        this.ddrive = ddrive;
        this.laser = laser;
        this.comparator = newDimensionComparator();
    }

    public static DimensionComparator newDimensionComparator() {
        return new MinDimensionComparator();
//        return new ProductDimensionComparator();
//        return new SquareLinearDimensionComparator();
//        return new YagerDimensionComparator();
    }

    public static StateComparator<Double> newStateComparator(double min, double max, double tolerance) {
        if (FuzzyDistribution.isFuzzy()) {
            return new LinearDoubleComparator(tolerance, tolerance);
//            return new SquareDoubleComparator(tolerance * 2, tolerance * 2);
//            return new SigmoidDoubleComparator(tolerance, tolerance);
        } else {
            System.out.println("Instanciating Discrete comparator");
            return new UniformDoubleComparator(min, max, tolerance * 2, tolerance * 2);
        }
    }

    @Override
    public int size() {
        return ddrive.size() + laser.size();
    }

    @Override
    public int getDimensionIndex(StateDimension<Double> dimension) {
        if (dimension instanceof SpeedDimension) {
            return ddrive.getDimensionIndex(dimension);
        } else {
            return laser.getDimensionIndex(dimension) + 2;
        }
    }

    @Override
    public StateDimension<Double> getDimension(int index) {
        if (index < 2) {
            return ddrive.getDimension(index);
        } else {
            return laser.getDimension(index - 2);
        }
    }

    @Override
    public SpaceDistribution<Double> newDistribution() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public SpaceDistribution<Double> newDistribution(int... stateIndex) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public SpaceDistribution<Double> newDistribution(ProbabilityDistribution<Double>... dimensions) {
        throw new UnsupportedOperationException("Not supported");
    }

    public SensoryMotorDistribution newDistribution(DifferentialDriveCommand command) {
        ProbabilityDistribution[] motorDistributions = getMotorDistributions(command);
        return new SensoryMotorDistribution(this, motorDistributions, null);
    }

    public SensoryMotorDistribution newDistribution(LaserArray laserArray) {
        ProbabilityDistribution[] sensoryDistributions = getSensoryDistributions(laserArray);
        return new SensoryMotorDistribution(this, null, sensoryDistributions);
    }

    public SensoryMotorDistribution newDistribution(DifferentialDriveCommand command, LaserArray laserArray) {
        ProbabilityDistribution[] motorDistributions = getMotorDistributions(command);
        ProbabilityDistribution[] sensoryDistributions = getSensoryDistributions(laserArray);
        return new SensoryMotorDistribution(this, motorDistributions, sensoryDistributions);
    }

    /**
     * Generates a proboability distribution representing the linear integration between command1 and command2 with repsective weights, and correspondingly laser1 and laser2 with respective weights.
     *
     * Intended for conversion of sensor and motor data into descrete time.
     *
     * @param command1
     * @param driveWeight1
     * @param command2
     * @param driveWeight2
     * @param laser1
     * @param laserWeight1
     * @param laser2
     * @param laserWeight2
     * @return a space distribution over DifferentialDrive and Laser scanner based on DualStateDistributions
     */
    public SensoryMotorDistribution newInterpolatedDistribution(DifferentialDriveCommand command1, double driveWeight1, DifferentialDriveCommand command2, double driveWeight2, LaserArray laser1, double laserWeight1, LaserArray laser2, double laserWeight2) {
        ProbabilityDistribution[] motorDistributions = getMotorDistributions(command1, driveWeight1, command2, driveWeight2);
        ProbabilityDistribution[] sensoryDistributions = getSensoryDistributions(laser1, laserWeight1, laser2, laserWeight2);
        return new SensoryMotorDistribution(this, motorDistributions, sensoryDistributions);
    }

    public SensoryMotorDistribution newInterpolatedDistribution(DifferentialDriveCommand command1, double driveWeight1, DifferentialDriveCommand command2, double driveWeight2) {
        ProbabilityDistribution[] motorDistributions = getMotorDistributions(command1, driveWeight1, command2, driveWeight2);
        return new SensoryMotorDistribution(this, motorDistributions, null);
    }

    public SensoryMotorDistribution newInterpolatedDistribution(LaserArray laser1, double laserWeight1, LaserArray laser2, double laserWeight2) {
        ProbabilityDistribution[] sensoryDistributions = getSensoryDistributions(laser1, laserWeight1, laser2, laserWeight2);
        return new SensoryMotorDistribution(this, null, sensoryDistributions);
    }

    private ProbabilityDistribution[] getSensoryDistributions(LaserArray laserArray) {
        ProbabilityDistribution[] dists = new ProbabilityDistribution[laser.size()];

        SingleStateSpaceDistribution<Double> laserState = (SingleStateSpaceDistribution) laser.newDistribution(laserArray);

        for (int i = 0; i < dists.length; i++) {
            dists[i] = laserState.getDimension(i);
        }
        return dists;
    }

    private ProbabilityDistribution[] getSensoryDistributions(LaserArray laser1, double laserWeight1, LaserArray laser2, double laserWeight2) {
        ProbabilityDistribution[] dists = new ProbabilityDistribution[laser.size()];

        SingleStateSpaceDistribution<Double> lastLaserState = (SingleStateSpaceDistribution) laser.newDistribution(laser1);
        SingleStateSpaceDistribution<Double> nextLaserState = (SingleStateSpaceDistribution) laser.newDistribution(laser2);

        for (int i = 0; i < dists.length; i++) {
            final Double lastState = lastLaserState.getState(i);
            final Double nextState = nextLaserState.getState(i);
            if (lastState.equals(nextState)) {
                dists[i] = lastLaserState.getDimension(i);
            } else {
                dists[i] = new DualStateDistribution(laser.getDimension(i), lastState, laserWeight1, nextState, laserWeight2);
            }
        }
        return dists;
    }

    private ProbabilityDistribution[] getMotorDistributions(DifferentialDriveCommand command) {
        ProbabilityDistribution[] dists = new ProbabilityDistribution[2];
        SpeedDimension angularDim = (SpeedDimension) ddrive.getDimension(0);
        SpeedDimension linearDim = (SpeedDimension) ddrive.getDimension(1);

        final Double angularState = angularDim.getState(command);
        final Double linearState = linearDim.getState(command);

        dists[0] = new SingleStateDistribution(angularDim, angularState);
        dists[1] = new SingleStateDistribution(linearDim, linearState);
        return dists;
    }

    private ProbabilityDistribution[] getMotorDistributions(DifferentialDriveCommand command1, double driveWeight1, DifferentialDriveCommand command2, double driveWeight2) {
        ProbabilityDistribution[] dists = new ProbabilityDistribution[2];
        SpeedDimension angularDim = (SpeedDimension) ddrive.getDimension(0);
        SpeedDimension linearDim = (SpeedDimension) ddrive.getDimension(1);

        final Double lastAngularState = angularDim.getState(command1);
        final Double nextAngularState = angularDim.getState(command2);

        if (lastAngularState.equals(nextAngularState)) {
            dists[0] = new SingleStateDistribution(angularDim, lastAngularState);
        } else {
            dists[0] = new DualStateDistribution(angularDim, lastAngularState, driveWeight1, nextAngularState, driveWeight2);
        }

        final Double lastLinearState = linearDim.getState(command1);
        final Double nextLinearState = linearDim.getState(command2);

        if (lastLinearState.equals(nextLinearState)) {
            dists[1] = new SingleStateDistribution(linearDim, lastLinearState);
        } else {
            dists[1] = new DualStateDistribution(linearDim, lastLinearState, driveWeight1, nextLinearState, driveWeight2);
        }
        return dists;
    }

    public SpaceDistribution<Double> getMotorDistribution(SpaceDistribution<Double> sensoryMotorDistribution) {
        ProbabilityDistribution[] dimensions = new ProbabilityDistribution[2];
        dimensions[0] = sensoryMotorDistribution.getDimension(0);
        dimensions[1] = sensoryMotorDistribution.getDimension(1);
        return new GenericSpaceDistribution<Double>(ddrive, dimensions);
    }

    public SpaceDistribution<Double> getSensoryDistribution(SpaceDistribution<Double> sensoryMotorDistribution) {
        ProbabilityDistribution[] dimensions = new ProbabilityDistribution[laser.size()];
        for (int d = 0; d < dimensions.length; d++) {
            dimensions[d] = sensoryMotorDistribution.getDimension(d + 2);
        }
        return new GenericSpaceDistribution<Double>(laser, dimensions);
    }

    public Iterator<StateDimension<Double>> iterator() {
        final StateSpace[] spaces = {ddrive, laser};

        return new Iterator<StateDimension<Double>>() {

            private final Iterator<StateSpace> i = new ArrayIterator<StateSpace>(spaces);
            private Iterator<StateDimension<Double>> iDim;

            public boolean hasNext() {
                if (iDim != null && iDim.hasNext()) {
                    return true;
                } else {
                    return i.hasNext();
                }
            }

            public StateDimension<Double> next() {
                if (iDim == null || !iDim.hasNext()) {
                    iDim = i.next().iterator();
                }
                return iDim.next();
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported");
            }
        };
    }

    public DifferentialDriveSpace getMotorSpace() {
        return ddrive;
    }

    public LaserSpace getSensorSpace() {
        return laser;
    }

    @Override
    public DimensionComparator comparator() {
        return comparator;
    }
}
