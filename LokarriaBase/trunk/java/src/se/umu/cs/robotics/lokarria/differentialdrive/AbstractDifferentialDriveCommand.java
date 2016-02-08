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

package se.umu.cs.robotics.lokarria.differentialdrive;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public abstract class AbstractDifferentialDriveCommand implements DifferentialDriveCommand {

    public String toJSON() {
        return "{\"Command\":{\"TargetLinearSpeed\":" + getLinearSpeed() + ", \"TargetAngularSpeed\":" + getAngularSpeed() + "}}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConcreteDifferentialDriveCommand other = (ConcreteDifferentialDriveCommand) obj;
        if (Double.doubleToLongBits(getAngularSpeed()) != Double.doubleToLongBits(other.getAngularSpeed())) {
            return false;
        }
        if (Double.doubleToLongBits(getLinearSpeed()) != Double.doubleToLongBits(other.getLinearSpeed())) {
            return false;
        }
        if (timeStamp() != other.timeStamp()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        double angularSpeed = getAngularSpeed();
        double linearSpeed = getLinearSpeed();
        long timeStamp = timeStamp();
        hash = 89 * hash + (int) (Double.doubleToLongBits(angularSpeed) ^ (Double.doubleToLongBits(angularSpeed) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(linearSpeed) ^ (Double.doubleToLongBits(linearSpeed) >>> 32));
        hash = hash ^ (int) (timeStamp ^ timeStamp);
        return hash;
    }

    public boolean equals(DifferentialDriveCommand other, boolean compareTimeStamp) {
        if (Double.doubleToLongBits(getAngularSpeed()) != Double.doubleToLongBits(other.getAngularSpeed())) {
            return false;
        }
        if (Double.doubleToLongBits(getLinearSpeed()) != Double.doubleToLongBits(other.getLinearSpeed())) {
            return false;
        }
        if (compareTimeStamp && timeStamp() != other.timeStamp()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Command{A%.3f,L%.3f}", getAngularSpeed(),getLinearSpeed());
    }

}
