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
