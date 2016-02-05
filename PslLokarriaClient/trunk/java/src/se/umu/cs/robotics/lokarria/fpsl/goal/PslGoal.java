/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.lokarria.fpsl.goal;

import se.umu.cs.robotics.lokarria.localization.Pose;

/**
 *
 * @author Erik Billing <erik.billing@his.se>
 */
public interface PslGoal {
    
    boolean goalReached(Pose pose);
    String getName();
}
