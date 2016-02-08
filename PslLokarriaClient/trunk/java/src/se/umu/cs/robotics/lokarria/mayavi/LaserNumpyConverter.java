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

package se.umu.cs.robotics.lokarria.mayavi;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import se.umu.cs.robotics.lokarria.laser.LaserArray;
import se.umu.cs.robotics.lokarria.laser.LaserEcho;

/**
 * Converts laser data from specified source to a Numpy array
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LaserNumpyConverter {

    private final String name;
    private final String className;

    /**
     * @param name Name of the generated array
     */
    public LaserNumpyConverter(String name) {
        this.name = name.toLowerCase();
        this.className = name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public void writePointArray(Writer out, Iterator<LaserArray> source) throws IOException {
        out.write("\n'''");
        out.write("Laser data converted to relative positions.");
        out.write("'''\n");
        ArrayList<LaserArray> echoes = new ArrayList<LaserArray>();
        while (source.hasNext()) {
            echoes.add(source.next());
        }

        /*
         * Initiating variables
         */
        out.append(String.format("class %s: pass\n", className));
        out.append(String.format("%s = %s()\n", name, className));
        out.append(String.format("%s.x = zeros([%d])\n", name, echoes.size() * 271));
        out.append(String.format("%s.y = zeros([%d])\n", name, echoes.size() * 271));
        out.append(String.format("%s.t = zeros([%d])\n", name, echoes.size() * 271));
        out.append(String.format("%s.connections = list()\n", name));

        for (int i = 0; i < echoes.size(); i++) {
            LaserArray echo = echoes.get(i);
            int startIndex = i*271;
            int endIndex = startIndex+271;

            /*
             * Declaring angles
             */
            out.append(String.format("%s.x[%d:%d] = %s\n", name, startIndex, endIndex, echoesToXCoordinates(echo)));

            /*
             * Declaring time
             */
            out.append(String.format("%s.t[%d:%d] = %s\n", name, startIndex, endIndex, echo.timeStamp()/100d));

            /*
             * Declaring surface
             */
            out.append(String.format("%s.y[%d:%d] = %s\n", name, startIndex, endIndex, echoesToYCoordinates(echo)));
            out.write('\n');
        }

    }

    public void writeTemporalLines(Writer out) throws IOException {
        out.write("\n'''");
        out.write("Generates temporal lines between laser data points.");
        out.write("'''\n");
        out.append(String.format("for i in range(%s.x.size-271): %s.connections.append(hstack([i,i+271]))\n", name, name));
        out.append(String.format("%s.connections = vstack(%s.connections)\n", name, name));
    }

    public void writeAngularLines(Writer out) throws IOException {
        out.write("\n'''");
        out.write("Generates lines between laser data points within the same scan.");
        out.write("'''\n");
        out.append(String.format("for i in range(%s.x.size-271):\n", name));
        out.append(String.format("\tif i%%271>0: %s.connections.append(hstack([i-1,i]))\n", name));
        out.append(String.format("%s.connections = vstack(%s.connections)\n", name, name));
    }

    public static String distancesToPythonList(LaserArray echoes) {
        StringBuilder s = new StringBuilder();
        Iterator<LaserEcho> i = echoes.iterator();
        s.append('[');
        s.append(i.next().getDistance());
        while (i.hasNext()) {
            s.append(',');
            s.append(i.next().getDistance());
        }
        s.append(']');
        return s.toString();
    }

    public static String anglesToPythonList(LaserArray echoes) {
        StringBuilder s = new StringBuilder();
        Iterator<LaserEcho> i = echoes.iterator();
        s.append('[');
        s.append(i.next().getAngle());
        while (i.hasNext()) {
            s.append(',');
            s.append(i.next().getAngle());
        }
        s.append(']');
        return s.toString();
    }

    public static String echoesToXCoordinates(LaserArray echoes) {
        StringBuilder s = new StringBuilder();
        Iterator<LaserEcho> i = echoes.iterator();
        s.append('[');
        s.append(echoeToXCoordinate(i.next()));
        while (i.hasNext()) {
            s.append(',');
            s.append(echoeToXCoordinate(i.next()));
        }
        s.append(']');
        return s.toString();
    }

    public static String echoesToYCoordinates(LaserArray echoes) {
        StringBuilder s = new StringBuilder();
        Iterator<LaserEcho> i = echoes.iterator();
        s.append('[');
        s.append(echoeToYCoordinate(i.next()));
        while (i.hasNext()) {
            s.append(',');
            s.append(echoeToYCoordinate(i.next()));
        }
        s.append(']');
        return s.toString();
    }

    public static double echoeToXCoordinate(LaserEcho echo) {
        return Math.sin(echo.getAngle()) * echo.getDistance();
    }

    public static double echoeToYCoordinate(LaserEcho echo) {
        return Math.cos(echo.getAngle()) * echo.getDistance();
    }

    public static String timeStampsToPythonList(Iterator<LaserArray> echoes) {
        StringBuilder s = new StringBuilder();
        s.append('[');
        s.append(echoes.next().timeStamp());
        while (echoes.hasNext()) {
            s.append(',');
            s.append(echoes.next().timeStamp());
        }
        s.append(']');
        return s.toString();
    }

    public static String timeStampsToPythonList(LaserArray echoes) {
        StringBuilder s = new StringBuilder();
        Iterator<LaserEcho> i = echoes.iterator();
        s.append('[');
        i.next();
        s.append(((double)echoes.timeStamp())/1000);
        while (i.hasNext()) {
            s.append(',');
            i.next();
            s.append(((double)echoes.timeStamp())/1000);
        }
        s.append(']');
        return s.toString();
    }
}
