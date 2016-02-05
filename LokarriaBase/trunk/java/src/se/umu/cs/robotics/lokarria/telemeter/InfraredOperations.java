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
