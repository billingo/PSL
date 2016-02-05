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
