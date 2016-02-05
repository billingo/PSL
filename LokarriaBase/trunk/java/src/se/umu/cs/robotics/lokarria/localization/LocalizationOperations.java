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
package se.umu.cs.robotics.lokarria.localization;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import se.umu.cs.robotics.lokarria.core.Connection;
import se.umu.cs.robotics.lokarria.core.JsonOperations;
import se.umu.cs.robotics.lokarria.core.LokarriaPropertyLoader;
import static se.umu.cs.robotics.lokarria.log.LogUtils.logJSON;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;

/**
 * @author Benjamin Fonooni
 */
public class LocalizationOperations {

    public static final Logger logger = Logger.getLogger(LocalizationOperations.class);
    private final String LOCALIZATION_URI = "lokarria.localization";
    private Connection connection;

    public LocalizationOperations() {
        connection = new Connection(LokarriaPropertyLoader.getValue(LOCALIZATION_URI));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Localization getRobotLocalization() {
        Localization localization = new Localization();
        try {
            String localizationJson = connection.get();
            logJSON(logger, localizationJson, LokarriaLogMessage.MessageType.GET);
            ArrayList x = JsonOperations.getItemsWithKey(localizationJson, "X");
            ArrayList y = JsonOperations.getItemsWithKey(localizationJson, "Y");
            ArrayList z = JsonOperations.getItemsWithKey(localizationJson, "Z");
            ArrayList w = JsonOperations.getItemsWithKey(localizationJson, "W");
            ArrayList<Long> t = JsonOperations.getItemsWithKey(localizationJson, "Timestamp");

            double xd, yd, zd, wd;
            xd = yd = zd = wd = 0;

            if (x.get(0) instanceof Double) {
                xd = ((Double) x.get(0));
            } else if (x.get(0) instanceof Integer) {
                xd = ((Integer) x.get(0)).doubleValue();
            } else if (x.get(0) instanceof Long) {
                xd = ((Long) x.get(0)).doubleValue();
            }

            if (y.get(0) instanceof Double) {
                yd = ((Double) y.get(0));
            } else if (y.get(0) instanceof Integer) {
                yd = ((Integer) y.get(0)).doubleValue();
            } else if (y.get(0) instanceof Long) {
                yd = ((Long) y.get(0)).doubleValue();
            }

            if (z.get(0) instanceof Double) {
                zd = ((Double) z.get(0));
            } else if (z.get(0) instanceof Integer) {
                zd = ((Integer) z.get(0)).doubleValue();
            } else if (z.get(0) instanceof Long) {
                zd = ((Long) z.get(0)).doubleValue();
            }

            if (w.get(0) instanceof Double) {
                wd = ((Double) w.get(0));
            } else if (w.get(0) instanceof Integer) {
                wd = ((Integer) w.get(0)).doubleValue();
            } else if (w.get(0) instanceof Long) {
                wd = ((Long) w.get(0)).doubleValue();
            }

            localization.setRobotOrientation(new Quaternion(xd, yd, zd, wd));
            localization.setRobotPosition(new Pose((Double) x.get(1), (Double) y.get(1), (Double) z.get(1)));
            localization.setTimestamp(t.get(0));
        } catch (IOException ex) {
            logger.warn(ex);
        }

        return localization;
    }
}
