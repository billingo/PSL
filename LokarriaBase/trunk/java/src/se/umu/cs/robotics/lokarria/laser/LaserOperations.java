/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright 2011 Benjamin Fonooni and Erik Billing
 billing@cs.umu.se, fonooni@cs.umu.se
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
package se.umu.cs.robotics.lokarria.laser;

import java.io.IOException;
import java.util.List;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

import se.umu.cs.robotics.lokarria.core.Connection;
import se.umu.cs.robotics.lokarria.core.JsonOperations;
import se.umu.cs.robotics.lokarria.core.JsonOperations.JsonException;
import se.umu.cs.robotics.lokarria.core.LokarriaPropertyLoader;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage.MessageType;

import static se.umu.cs.robotics.lokarria.log.LogUtils.logJSON;

public class LaserOperations {

    public static final Logger logger = Logger.getLogger(LaserOperations.class);
    private static double[] defaultAngles;
    private Connection connection;
    private Connection propertiesConnection;
    private boolean postRequest = false; // Will fall back on get if post does not work

    public LaserOperations() {
        if (LokarriaPropertyLoader.getVersion() == LokarriaPropertyLoader.Version.V4) {
            connection = new Connection(LokarriaPropertyLoader.getValue("lokarria.laser.echoes"));
            propertiesConnection = new Connection(LokarriaPropertyLoader.getValue("lokarria.laser.properties"));
        } else {
            connection = new Connection(LokarriaPropertyLoader.getValue("lokarria.laser"));
        }
    }

    /**
     * works only with old style laser service
     * 
     * @return
     * @deprecated
     */
    @Deprecated
    public ArrayList<Double> getLaserDistances() {
        String data = requestLaserData();
        return JsonOperations.getItemsWithKey(data, "Distance");
    }

    /**
     * works only with old style laser service
     * 
     * @param index
     * @return
     * @deprecated
     */
    @Deprecated
    public HashMap getHashedEchoDataByIndex(int index) {
        String data = requestLaserData();
        return JsonOperations.getItem(data, "Echoes", index);
    }

    public LaserEcho getEchoDataByIndex(int index) {
        String data = requestLaserData();
        HashMap json = (HashMap) JSONValue.parse(data);
        JSONArray echoes = (JSONArray) json.get("Echoes");
        if (echoes == null) {
            JSONArray angles = (JSONArray) json.get("Angles");
            if (!hasDefaultAngles() && angles != null) {
                initDefaultAngles(angles);
            }
            JSONArray distances = (JSONArray) json.get("Distances");
            return new ConcreteLaserEcho(defaultAngles[index], JsonOperations.value(distances.get(index)));
        } else {
            HashMap echo = (HashMap) echoes.get(index);
            return new ConcreteLaserEcho(JsonOperations.value(echo.get("Angle")), JsonOperations.value(echo.get("Distance")));
        }
    }

    public LaserArray getEchoesByRange(int min, int max) {
        String data = requestLaserData();
        try {
            return LaserArrayList.fromJSON(data, min, max);
        } catch (JsonException ex) {
            logger.warn(ex);
            return null;
        }
    }

    public LaserArray getEchoes() {
        try {
            String data = requestLaserData();
            return LaserArrayList.fromJSON(data);
        } catch (JsonException ex) {
            logger.warn(ex);
            return null;
        }
    }

    private String requestLaserData() {
        String response = null;
        if (!hasDefaultAngles() && LokarriaPropertyLoader.getVersion() == LokarriaPropertyLoader.Version.V4) {
            initDefaultAngles();
        }
        try {
            if (postRequest) {
                PostMethod post = connection.newPost();
                post.addParameter("distances", "true");
                if (!hasDefaultAngles()) {
                    post.addParameter("angles", "true");
                }
                response = connection.post(post);

                if (response == null) {
                    logger.info("No response when posting laser request, using GET instead.");
                    postRequest = false;
                    return requestLaserData();
                }
            } else {
                response = connection.get();
            }
            if (response == null) {
                logger.warn("No response when requesting laster data!");
            } else {
                logJSON(logger, response, postRequest ? MessageType.POST : MessageType.GET);
            }
        } catch (IOException ex) {
            logger.warn(ex);
        }
        return response;
    }

    public static boolean hasDefaultAngles() {
        return defaultAngles != null;
    }

    static void initDefaultAngles(List angles) {
        synchronized (LaserOperations.class) {
            if (!hasDefaultAngles()) {
                defaultAngles = new double[angles.size()];
                int i = 0;
                for (Object item : angles) {
                    if (item instanceof HashMap) {
                        defaultAngles[i++] = JsonOperations.value(((HashMap) item).get("Angle"));
                    } else if (item instanceof LaserEcho) {
                        defaultAngles[i++] = ((LaserEcho) item).getAngle();
                    } else {
                        defaultAngles[i++] = JsonOperations.value(item);
                    }
                }
            }
        }
    }
    
    private void initDefaultAngles() {
        String response;
        try {
            response = propertiesConnection.get();
            HashMap json = (HashMap)JSONValue.parse(response);
            initDefaultAngles(json);
            logJSON(logger, response, MessageType.GET);
        } catch (IOException ex) {
            logger.warn("Unable to init default angles. Unexpected response from server:");
            logger.warn(ex);
        }
        
    }
    
    static void initDefaultAngles(HashMap laserProperties) {
        try {
            
            double startAngle = (Double)laserProperties.get("StartAngle");
            double endAngle = (Double)laserProperties.get("EndAngle");
            double angleIncrement = (Double)laserProperties.get("AngleIncrement");
            double beemCount = (endAngle - startAngle) / angleIncrement;
            defaultAngles = new double[(int)Math.round(beemCount)];
            defaultAngles[0] = startAngle;
            defaultAngles[defaultAngles.length-1] = endAngle;
            for (int i=1; i<defaultAngles.length -1; i++) {
                defaultAngles[i]=defaultAngles[i-1]+angleIncrement;
            }            
        } catch (ClassCastException e) {
            logger.warn("Unable to init default angles. Unexpected response from server:");
            logger.warn(e);
        }
        
        
    }

    public static double getDefaultAngle(int index) {
        if (defaultAngles == null) {
            logger.warn("Default angles requested, but not available!");
            return 0;
        } else {
            return defaultAngles[index];
        }
    }

    
}
