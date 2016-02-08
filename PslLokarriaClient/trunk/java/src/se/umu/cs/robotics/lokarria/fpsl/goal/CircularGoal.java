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
