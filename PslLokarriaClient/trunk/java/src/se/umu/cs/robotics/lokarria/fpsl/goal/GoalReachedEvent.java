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

package se.umu.cs.robotics.lokarria.fpsl.goal;

import se.umu.cs.robotics.hpl.log.AbstractHplLogMessage;

/**
 *
 * @author Erik Billing <erik.billing@his.se>
 */
public class GoalReachedEvent extends AbstractHplLogMessage {

    private final PslGoal goal;
    private final long stepCount;

    public GoalReachedEvent(PslGoal goal, long stepCount) {
        this.goal = goal;
        this.stepCount = stepCount;
    }
        
    @Override
    public String toXML() {
        StringBuilder s = new StringBuilder();
        s.append("<hpl:GoalReached name=\"").append(goal.getName()).append("\" ");
        if (goal instanceof CircularGoal) {
            s.append("x=\"").append(((CircularGoal)goal).getX()).append("\" ");
            s.append("y=\"").append(((CircularGoal)goal).getY()).append("\" ");
            s.append("r=\"").append(((CircularGoal)goal).getR()).append("\" ");
        }
        s.append("stepCount=\"").append(stepCount).append("\" ");
        s.append("/>");
        return s.toString();
    }
    
}
