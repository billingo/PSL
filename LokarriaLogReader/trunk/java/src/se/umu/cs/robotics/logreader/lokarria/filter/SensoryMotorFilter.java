/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package se.umu.cs.robotics.logreader.lokarria.filter;

import se.umu.cs.robotics.logreader.filter.LogEventFilter;
import se.umu.cs.robotics.logreader.filter.PassAnyFilter;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SensoryMotorFilter extends PassAnyFilter<LokarriaLogMessage> {

    public SensoryMotorFilter() {
        super(new DifferentialDriveFilter(), new LaserFilter());
    }

}
