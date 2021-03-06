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


package se.umu.cs.robotics.lokarria.localization;

import java.io.Serializable;

/**
 * @author Benjamin Fonooni
 */

public class Pose implements Serializable {
	
	private static final long serialVersionUID = 1723472734493624595L;

	double x,y,z ;
	
	public Pose(){
		x = 0;
		y = 0;
		x = 0;
	}
	
	public Pose(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Pose(double x, double y){
		this.x = x;
		this.y = y;
		this.z = 0;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	public void setPose(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Pose copy() { 
		return new Pose(this.x, this.y, this.z);
	} 
        
        public String toString() {
            return String.format("Pose{x:%.3f,y:%.3f,z:%.3f}", x,y,z);
        }
}
