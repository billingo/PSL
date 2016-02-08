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

package se.umu.cs.robotics.lokarria.statespace;

import org.apache.log4j.Logger;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.lokarria.differentialdrive.AbstractDifferentialDriveCommand;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 * A Differential Drive Command based on a Probability Distribution, using the
 * states with highest probability. 
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class MaxProbabilityCommand extends AbstractDifferentialDriveCommand {

    static Logger logger = Logger.getLogger(MaxProbabilityCommand.class);

    private final long timeStamp = System.currentTimeMillis();
    private final double angularSpeed;
    private final double linearSpeed;

    public MaxProbabilityCommand(SpaceDistribution<Double> pd) {
        final ProbabilityDistribution<Double> angularDist = pd.getDimension(0);
        final ProbabilityDistribution<Double> linearDist = pd.getDimension(1);
        FuzzyItem<Double> maxAngular = angularDist.max().next();
        FuzzyItem<Double> maxLinear = linearDist.max().next();
        angularSpeed = maxAngular.element();
        linearSpeed = maxLinear.element();
    }

    public long timeStamp() {
        return timeStamp;
    }

    public double getAngularSpeed() {
        return angularSpeed;
    }

    public double getLinearSpeed() {
        return linearSpeed;
    }

}
