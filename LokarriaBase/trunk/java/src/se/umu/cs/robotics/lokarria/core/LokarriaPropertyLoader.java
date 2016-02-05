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
package se.umu.cs.robotics.lokarria.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import se.umu.cs.robotics.utils.ConfigTools;

/**
 * Utility class for loading properties from config files.
 *
 * @author Erik Billing <billing@cs.umu.se> and Benjamin Fonooni
 * <fonooni@cs.umu.se>
 */
public class LokarriaPropertyLoader {

    static Logger logger = Logger.getLogger(LokarriaPropertyLoader.class);
    private static final String PROPERTY_FILE_NAME = "lokarria.resources.properties";
    private static Version mrdsVersion;

    public static String getValue(String key) {
        Properties properties = new Properties();
        String configDirectory = ConfigTools.getConfigDirectory();
        try {
            InputStream resourceAsStream = new BufferedInputStream(new FileInputStream(new File(configDirectory + "/" + PROPERTY_FILE_NAME)));
            properties.load(resourceAsStream);
            resourceAsStream.close();
            return properties.getProperty(key);
        } catch (FileNotFoundException e) {
            logger.fatal(e);
        } catch (IOException e) {
            logger.fatal(e);
        }
        return "";
    }

    public static Version getVersion() {
        if (mrdsVersion == null) {
            String version = getValue("lokarria.version");
            if (version != null && "4".equals(version.trim())) {
                mrdsVersion = Version.V4;
            } else {
                mrdsVersion = Version.V2008R3;
            }
        }
        return mrdsVersion;
    }

    public static enum Version {

        V4, V2008R3;
    }
}
