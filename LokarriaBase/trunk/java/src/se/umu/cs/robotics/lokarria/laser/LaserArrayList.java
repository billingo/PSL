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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONValue;

import se.umu.cs.robotics.lokarria.core.JsonOperations;
import se.umu.cs.robotics.lokarria.core.JsonOperations.JsonException;
import se.umu.cs.robotics.lokarria.core.LokarriaPropertyLoader;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LaserArrayList extends ArrayList<LaserEcho> implements LaserArray {

    private static final long serialVersionUID = -2830938821767707928L;
    private final long timeStamp;

    public LaserArrayList(long timeStamp, List echoes, int minIndex, int maxIndex) {
        super();
        this.timeStamp = timeStamp;
        if (!LaserOperations.hasDefaultAngles()) {
            LaserOperations.initDefaultAngles(echoes);
        }
        for (int i = minIndex; i < maxIndex; i++) {
            final Object echo = echoes.get(i);
            if (echo instanceof HashMap) {
                HashMap echoMap = (HashMap) echo;
                add(new ConcreteLaserEcho(JsonOperations.value(echoMap.get("Angle")), JsonOperations.value(echoMap.get("Distance"))));
            } else if (echo instanceof LaserEcho) {
                add((LaserEcho) echo);
            } else if (echo instanceof Double) {
                add(new ConcreteLaserEcho(i, (Double) echo));
            }
        }
    }

    LaserArrayList(long timeStamp, List angles, List distances, int minIndex, int maxIndex) {
        super(maxIndex - minIndex);
        this.timeStamp = timeStamp;
        if (angles != null && !LaserOperations.hasDefaultAngles()) {
            LaserOperations.initDefaultAngles(angles);
        }
        for (int i = minIndex; i < maxIndex; i++) {
            add(new ConcreteLaserEcho(LaserOperations.getDefaultAngle(i), JsonOperations.value(distances.get(i))));
        }
    }

    public LaserArrayList(long timeStamp, List echoes) {
        this(timeStamp, echoes, 0, echoes.size());
    }

    LaserArrayList(long timeStamp, List angles, List distances) {
        this(timeStamp, angles, distances, 0, distances.size());
    }

    public static LaserArrayList newAverageArray(List<LaserArray> arrays) {
        final double n = arrays.size();
        ArrayList<Double> average = new ArrayList<Double>();
        long t = 0;
        for (LaserArray array : arrays) {
            t += array.timeStamp();
            if (average.isEmpty()) {
                for (LaserEcho echo : array) {
                    average.add(echo.getDistance() / n);
                }
            } else {
                int e = 0;
                for (LaserEcho echo : array) {
                    Double v = average.get(e) + echo.getDistance() / n;
                    average.set(e, v);
                    e++;
                }
            }
        }
        return new LaserArrayList((long) (t / n), null, average);
    }

    public static LaserArrayList fromJSON(String json) throws JsonException {
        if (json == null) {
            return new LaserArrayList(0, new ArrayList());
        } else {
            final Object jsonValue = JSONValue.parse(json);
            if (jsonValue != null && jsonValue instanceof HashMap) {
                return fromJSON((HashMap) jsonValue);
            } else {
                throw new JsonException("Specified JSON source does not evaluate to a key-value pair");
            }
        }
    }

    public static LaserArrayList fromJSON(String json, int minIndex, int maxIndex) throws JsonException {
        final Object jsonValue = JSONValue.parse(json);
        if (jsonValue != null && jsonValue instanceof HashMap) {
            return fromJSON((HashMap) jsonValue, minIndex, maxIndex);
        } else {
            throw new JsonException("Specified JSON source does not evaluate to a key-value pair");
        }
    }

    public static LaserArrayList fromJSON(HashMap json) {
        if (json.get("AngleIncrement")==null) {
            Long timeStamp = (Long) json.get("Timestamp");
            final List echoes = (List) json.get("Echoes");
            if (echoes == null) {
                // New style customizedlaser service
                List angles = (List) json.get("Angles");
                List distances = (List) json.get("Distances");
                /*
                 * Not used
                 * List overflows = (List) json.get("Overflows");
                 */
                return new LaserArrayList(timeStamp, angles, distances);
            } else if (LokarriaPropertyLoader.getVersion() == LokarriaPropertyLoader.Version.V4) {
                return new LaserArrayList(timeStamp, null, echoes);
            } else {
                // Old style laser 2008R3 service
                return new LaserArrayList(timeStamp, echoes);
            }
        } else {
            LaserOperations.initDefaultAngles(json);
            return null;
        }
    }

    public static LaserArrayList fromJSON(HashMap json, int minIndex, int maxIndex) {
        //return new LaserArrayList((Long) json.get("Timestamp"), (List) json.get("Echoes"), minIndex, maxIndex);

        // New style customizedlaser service
        return new LaserArrayList((Long) json.get("Timestamp"), (List) json.get("Distances"), minIndex, maxIndex);
    }

    /**
     * @return the time stamp in miliseconds
     */
    public long timeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Timestamp:%d", timeStamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return timeStamp == ((LaserArrayList) obj).timeStamp;
//        Iterator<LaserEcho> other = ((LaserArrayList) obj).iterator();
//        Iterator<LaserEcho> me = iterator();
//        while (me.hasNext()) {
//            if (!me.next().equals(other.next())) {
//                return false;
//            }
//        }
//        return !other.hasNext();
    }
}
