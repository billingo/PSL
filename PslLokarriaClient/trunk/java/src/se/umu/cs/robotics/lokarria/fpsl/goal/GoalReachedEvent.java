/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
