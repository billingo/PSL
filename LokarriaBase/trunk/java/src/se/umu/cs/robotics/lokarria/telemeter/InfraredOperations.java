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

public class InfraredOperations {
	private final String INFRARED_URI = "lokarria.infrared"; 
	
    public InfraredOperations() {
    }

    public ArrayList<Infrared> getAllInfraredsData(){
    	ArrayList<Infrared> infrareds = new ArrayList<Infrared>();
    	ArrayList<Double> distanceList = XmlOperations.getDataByTagName(LokarriaPropertyLoader.getValue(INFRARED_URI), "double", "Distance");
        ArrayList<Double> maxDistanceList = XmlOperations.getDataByTagName(LokarriaPropertyLoader.getValue(INFRARED_URI), "double", "MaxDistance");
        ArrayList<Double> minDistanceList = XmlOperations.getDataByTagName(LokarriaPropertyLoader.getValue(INFRARED_URI), "double", "MinDistance");
        
        for (int i=0; i<distanceList.size(); i++){
        	Infrared infrared = new Infrared();
        	infrared.setDistance(distanceList.get(i));
        	infrared.setMaxDistance(maxDistanceList.get(i));
        	infrared.setMinDistance(minDistanceList.get(i));
        	infrareds.add(infrared);
        }
        
        return infrareds;
    }

    public Infrared getInfraredData(int sensorNumber){
    	Infrared infrared = new Infrared();
    	ArrayList<Double> distanceList = XmlOperations.getDataByTagName(LokarriaPropertyLoader.getValue(INFRARED_URI), "double", "Distance");
        ArrayList<Double> maxDistanceList = XmlOperations.getDataByTagName(LokarriaPropertyLoader.getValue(INFRARED_URI), "double", "MaxDistance");
        ArrayList<Double> minDistanceList = XmlOperations.getDataByTagName(LokarriaPropertyLoader.getValue(INFRARED_URI), "double", "MinDistance");
        
        infrared.setDistance(distanceList.get(sensorNumber));
        infrared.setMaxDistance(maxDistanceList.get(sensorNumber));
        infrared.setMinDistance(minDistanceList.get(sensorNumber));
        
        return infrared;
    }
}
