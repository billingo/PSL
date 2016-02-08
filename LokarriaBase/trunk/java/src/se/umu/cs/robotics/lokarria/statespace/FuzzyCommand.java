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

import se.umu.cs.robotics.lokarria.differentialdrive.AbstractDifferentialDriveCommand;
import se.umu.cs.robotics.probabilitydistribution.FuzzyDistribution;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 * A DifferentialDriveCommand created form a Fuzzy distribution. Default difuzzyfication methods will be used to compute the specific motor speeds. 
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class FuzzyCommand extends AbstractDifferentialDriveCommand {

    private final long timeStamp = System.currentTimeMillis();
    private final double angularSpeed;
    private final double linearSpeed;

    public FuzzyCommand(SpaceDistribution<Double> pd) {
        ProbabilityDistribution<Double> angularDistribution = pd.getDimension(0);
        ProbabilityDistribution<Double> linearDistribution = pd.getDimension(1);
        if (angularDistribution instanceof FuzzyDistribution) {
            angularSpeed = ((FuzzyDistribution) angularDistribution).defuzzify();
        } else {
            angularSpeed = angularDistribution.max().next().element();
        }
        if (linearDistribution instanceof FuzzyDistribution) {
            linearSpeed = ((FuzzyDistribution) linearDistribution).defuzzify();
        } else {
            linearSpeed = linearDistribution.max().next().element();
        }
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
