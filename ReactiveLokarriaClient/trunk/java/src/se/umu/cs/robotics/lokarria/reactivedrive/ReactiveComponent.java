
package se.umu.cs.robotics.lokarria.reactivedrive;

import se.umu.cs.robotics.lokarria.differentialdrive.DifferentialDriveCommand;
import se.umu.cs.robotics.lokarria.laser.LaserArray;

/**
 * A reactive controller component used together with {@link ReactiveControl}.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public interface ReactiveComponent {

    DifferentialDriveCommand get(LaserArray laser, DifferentialDriveCommand targetCommand);
}
