/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.lokarria.fpsl.goal;

import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import se.umu.cs.robotics.lokarria.localization.Pose;

/**
 *
 * @author Erik Billing <erik.billing@his.se>
 */
public class CircularGoal implements PslGoal {
    private final double x;
    private final double y;
    private final double r;
    private final String name;

    public CircularGoal(String name, double x, double y, double r) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.r = r;
    }
    
    public CircularGoal(String name, HashMap<String,Double> goal) {
        this(name, goal.getOrDefault("x", 0d), goal.getOrDefault("y", 0d), goal.getOrDefault("r", 1d));
    }
    
    public double distance(Pose pose) {
        double dx = pose.getX()-x;
        double dy = pose.getY()-y;
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    @Override
    public boolean goalReached(Pose pose) {
        return distance(pose) <= r;
    }

    @Override
    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }
    
}
