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


package se.umu.cs.robotics.hpl;

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
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class HplPropertyLoader {

    static Logger logger = Logger.getLogger(HplPropertyLoader.class);
    private static final String PROPERTY_FILE_NAME = "hpl.properties";

    public static String loadProperty(String propertyName) {
        Properties properties = new Properties();
        String configDirectory = ConfigTools.getConfigDirectory();
        try {
            InputStream resourceAsStream = new BufferedInputStream(new FileInputStream(new File(configDirectory + "/" + PROPERTY_FILE_NAME)));
            properties.load(resourceAsStream);
            resourceAsStream.close();
            return properties.getProperty(propertyName);
        } catch (FileNotFoundException e) {
            logger.fatal(e);
        } catch (IOException e) {
            logger.fatal(e);
        }
        return "";
    }

    public static Double loadDoubleProperty(String propertyName) {
        try {
            return new Double(loadProperty(propertyName));
        } catch (NumberFormatException ex) {
            throw new ConfigTools.PropertyFormatException(propertyName, ex.getMessage());
        }
    }

    public static Integer loadIntegerProperty(String propertyName) {
        try {
            return new Integer(loadProperty(propertyName));
        } catch (NumberFormatException ex) {
            throw new ConfigTools.PropertyFormatException(propertyName, ex.getMessage());
        }
    }

    public static Boolean loadBooleanProperty(String propertyName) {
        return new Boolean(loadProperty(propertyName));
    }
    
}
