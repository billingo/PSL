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
