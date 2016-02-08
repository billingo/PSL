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

package se.umu.cs.robotics.lokarria.laser;

/**
 * A single measurement from the lasar scanner.
 * 
 * @author Erik Billing <billing@cs.umu.se>
 * @author Benjamin Fonooni <fonooni@cs.umu.se>
 */
public interface LaserEcho {

    public double getAngle() ;

    public double getDistance() ;

    public double getX();

    public double getY();
    
    public void setOverflow(boolean overflow);
    
    public boolean getOverflow();
}
