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

package se.umu.cs.robotics.lokarria.localization;

/**
 * @author Benjamin Fonooni
 */

public class Localization {
	private Quaternion robotOrientation;
	private Pose robotPosition;
	private Long timestamp;

	public Quaternion getRobotOrientation() {
		return robotOrientation;
	}
	
	public void setRobotOrientation(Quaternion robotOrientation) {
		this.robotOrientation = robotOrientation;
	}
	
	public Pose getRobotPosition() {
		return robotPosition;
	}
	
	public void setRobotPosition(Pose robotPosition) {
		this.robotPosition = robotPosition;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	public Localization copy() {
		Localization nl = new Localization();
		nl.setRobotOrientation(this.robotOrientation.copy());
		nl.setRobotPosition(this.robotPosition.copy());
		nl.setTimestamp(this.timestamp);
		return nl;
	}
}
