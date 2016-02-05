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

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.lokarria.laser.LaserEcho;
import se.umu.cs.robotics.probabilitydistribution.SingleStateSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;
import se.umu.cs.robotics.statespace.FloatSpace;
import se.umu.cs.robotics.statespace.StateDimension;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LaserSpace extends FloatSpace {

    static Logger logger = Logger.getLogger(LaserSpace.class);

    public LaserSpace(int dimensionCount) {
        this(dimensionCount, (int)LaserDimension.MAX_DISTANCE);
    }

    public LaserSpace(int dimensionCount, int discretizationLevels) {
        super(SensoryMotorSpace.newDimensionComparator(),initDimensions(dimensionCount, discretizationLevels));
    }

    private static LaserDimension[] initDimensions(int dimensionCount, int discretizationLevels) {
        LaserDimension[] dimensions = new LaserDimension[dimensionCount];
        for (int i = 0; i < dimensionCount; i++) {
            dimensions[i] = new LaserDimension(discretizationLevels);
        }
        return dimensions;
    }

    public SpaceDistribution<Double> newDistribution(LaserArray laserArray) {
        if (laserArray==null) {
            logger.warn("Creation of Laser Distribution from null array!");
        }
        Double[] values = new Double[size()];
        Iterator<StateDimension<Double>> dimensions = iterator();
        final double laserArraySize = (double) laserArray.size();
        double samplingFactor = (laserArraySize) / ((double) values.length);
        ArrayList<LaserEcho> currentEchoes = new ArrayList<LaserEcho>();
        LaserDimension currentDimension = (LaserDimension) dimensions.next();
        int currentDimensionIndex = 0;
        for (int i = 0; i < laserArray.size(); i++) {
            if (i > currentDimensionIndex * samplingFactor + samplingFactor) {
                values[currentDimensionIndex] = currentDimension.getState(currentEchoes);
                currentEchoes.clear();
                currentDimension = (LaserDimension) dimensions.next();
                currentDimensionIndex++;
            }
            currentEchoes.add(laserArray.get(i));
        }
        values[currentDimensionIndex] = currentDimension.getState(currentEchoes);
        return new SingleStateSpaceDistribution<Double>(this, values);
    }
}
