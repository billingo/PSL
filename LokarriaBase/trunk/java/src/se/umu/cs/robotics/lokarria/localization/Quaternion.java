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

/**
 * @author Benjamin Fonooni
 */

public class Quaternion {
	
	private double x, y, z, w;
	
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

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	public Quaternion(Double x, Double y, Double z, Double w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Quaternion() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.w = 1;
	}
	
	public void setQuaternion(double x, double y, double z, double w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public void setQuaternion(Quaternion quater){
		this.x = quater.x;
		this.y = quater.y;
		this.z = quater.z;
		this.w = quater.w;
	}
	
	public double getEulerAngle(){
		return Math.atan2(2 * (w * z + x * y), 1 - 2 * (y * y + z * z));
	}
	
	public double translateTo360(){
		double angle = 0;
		angle = getEulerAngle();
		
		if (angle < 0){
			angle = 2 * Math.PI + angle;
		}

		return angle;
	}
	
	public double getRadianAngle(){
		return translateTo360() * 57.2957795;
	}
	
	public Quaternion copy(){
		return new Quaternion(this.x, this.y, this.z, this.w ); 
	}
	
	public double getHeadingAngle()
	{
		double angle = 2 * Math.atan2(z, w);
		return angle * 180 / Math.PI;
	}
}
