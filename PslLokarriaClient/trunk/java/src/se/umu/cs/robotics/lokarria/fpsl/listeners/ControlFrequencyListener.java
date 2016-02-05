/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright 2011 Erik Billing
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
package se.umu.cs.robotics.lokarria.fpsl.listeners;

import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.fpsl.PslController;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ControlFrequencyListener implements PslControllerListener {

    private long startTime;
    private long stopTime;
    private long predictionCount;

    public void controllerStarted(PslController controller) {
        startTime = System.currentTimeMillis();
        stopTime = 0;
        predictionCount = 0;
    }

    public void controllerStopped(PslController controller) {
        stopTime = System.currentTimeMillis();
    }

    /**
     * @return the number of predictions per second executed by the reporter controller
     */
    public double getFrequency() {
        long t = stopTime == 0 ? System.currentTimeMillis() : stopTime;
        double d = (double) (t - startTime);
        return (predictionCount * 1000d) / d;
    }

    public void command(PslController controller, DifferentialDriveCommand command, FPrediction<Double> prediction) {
        predictionCount++;
    }
}
