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
package se.umu.cs.robotics.lokarria.telemeter;

import java.util.ArrayList;

import se.umu.cs.robotics.lokarria.core.LokarriaPropertyLoader;
import se.umu.cs.robotics.lokarria.core.XmlOperations;

public class UltrasonicOperations {

	private final String ULTRASONIC_URI = "lokarria.ultrasonic"; 
	
    public UltrasonicOperations() {
    }

    public ArrayList<Ultrasonic> getAllUltrasonicsData(){
    	ArrayList<Ultrasonic> ultrasonics = new ArrayList<Ultrasonic>();
    	ArrayList<Double> distanceList = XmlOperations.getDataByTagName(LokarriaPropertyLoader.getValue(ULTRASONIC_URI), "double", "Distance");
        ArrayList<Double> maxDistanceList = XmlOperations.getDataByTagName(LokarriaPropertyLoader.getValue(ULTRASONIC_URI), "double", "MaxDistance");
        ArrayList<Double> minDistanceList = XmlOperations.getDataByTagName(LokarriaPropertyLoader.getValue(ULTRASONIC_URI), "double", "MinDistance");
        
        for (int i=0; i<distanceList.size(); i++){
        	Ultrasonic ultrasonic = new Ultrasonic();
        	ultrasonic.setDistance(distanceList.get(i));
        	ultrasonic.setMaxDistance(maxDistanceList.get(i));
        	ultrasonic.setMinDistance(minDistanceList.get(i));
        	ultrasonics.add(ultrasonic);
        }
        
        return ultrasonics;
    }

    public Ultrasonic getUltrasonicData(int sensorNumber){
    	Ultrasonic ultrasonic = new Ultrasonic();
    	ArrayList<Double> distanceList = XmlOperations.getDataByTagName(LokarriaPropertyLoader.getValue(ULTRASONIC_URI), "double", "Distance");
        ArrayList<Double> maxDistanceList = XmlOperations.getDataByTagName(LokarriaPropertyLoader.getValue(ULTRASONIC_URI), "double", "MaxDistance");
        ArrayList<Double> minDistanceList = XmlOperations.getDataByTagName(LokarriaPropertyLoader.getValue(ULTRASONIC_URI), "double", "MinDistance");
        
    	ultrasonic.setDistance(distanceList.get(sensorNumber));
    	ultrasonic.setMaxDistance(maxDistanceList.get(sensorNumber));
    	ultrasonic.setMinDistance(minDistanceList.get(sensorNumber));
        
        return ultrasonic;
    }
}
