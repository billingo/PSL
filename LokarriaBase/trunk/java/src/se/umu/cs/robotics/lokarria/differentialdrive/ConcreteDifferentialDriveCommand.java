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

import java.util.HashMap;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import se.umu.cs.robotics.lokarria.core.JsonOperations;
import se.umu.cs.robotics.lokarria.core.JsonOperations.JsonException;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ConcreteDifferentialDriveCommand extends AbstractDifferentialDriveCommand {

    private final double angularSpeed;
    private final double linearSpeed;
    private final long timeStamp;

    public ConcreteDifferentialDriveCommand(double angularSpeed, double linearSpeed) {
        this(angularSpeed, linearSpeed, 0);
    }

    private ConcreteDifferentialDriveCommand(double angularSpeed, double linearSpeed, long timeStamp) {
        this.angularSpeed = angularSpeed;
        this.linearSpeed = linearSpeed;
        this.timeStamp = timeStamp;
    }

    public double getAngularSpeed() {
        return angularSpeed;
    }

    public double getLinearSpeed() {
        return linearSpeed;
    }

    public long timeStamp() {
        return timeStamp;
    }

    /**
     * Instantiates a DifferentialDriveCommand from a JSON string.
     *
     * @param json source string
     * @return a new DifferentialDriveCommand
     * @throws ParseException
     */
    public static DifferentialDriveCommand fromJSON(String json) throws ParseException {
        return fromJSON(json,0);
    }

    /**
     * Instantiates a DifferentialDriveCommand from a JSON string. If the JSON does not contain a time stamp, the provided timeStamp is used.
     *
     * @param json source string
     * @param defaultTimeStamp Time stamp of the new command (is overrided by timeStamp in JSON source, if present)
     * @return a new DifferentialDriveCommand
     * @throws ParseException
     */
    public static DifferentialDriveCommand fromJSON(String json, long defaultTimeStamp) throws ParseException {
        final Object value = JSONValue.parse(json);
        if (value != null && value instanceof HashMap) {
            return fromJSON((HashMap) value, defaultTimeStamp);
        } else {
            throw new JsonException("Specified JSON source does not evaluate to a key-value pair");
        }
    }

    public static DifferentialDriveCommand fromJSON(HashMap json) throws ParseException {
        return fromJSON(json,0);
    }

    public static DifferentialDriveCommand fromJSON(HashMap json, long defaultTimeStamp) throws ParseException {
        HashMap command = (HashMap) json.get("Command");
        Long ts = (Long) json.get("Timestamp");
        if (ts != null) {
            defaultTimeStamp = ts;
        }
        if (command == null) {
            throw new JsonException("Specified JSON source does not contain a 'Command'");
        } else {
            return new ConcreteDifferentialDriveCommand(JsonOperations.value(command.get("TargetAngularSpeed")), JsonOperations.value(command.get("TargetLinearSpeed")), defaultTimeStamp);
        }
    }

    public static DifferentialDriveCommand stop() {
        return new ConcreteDifferentialDriveCommand(0, 0);
    }

}
