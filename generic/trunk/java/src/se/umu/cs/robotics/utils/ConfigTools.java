/*
 *  Copyright (C) 2011 Erik Billing <billing@cs.umu.se>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.umu.cs.robotics.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for loading configurations.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ConfigTools {

    public static String PROJECT_PROPERTIES = "project.properties";
    public static String DEFAULT_CONFIG_DIRECTORY = "config";
    public static String CONFIG_DIRECTORY_KEY = "config.directory";
    private static String configDirectory = null;

    public synchronized static String getConfigDirectory() {
        if (configDirectory == null) {
            File properties = new File(PROJECT_PROPERTIES);
            Properties p = new Properties();
            if (properties.isFile()) {
                try {
                    p.load(new FileReader(properties));
                } catch (FileNotFoundException ex) {
                    System.err.println("Unexpected error when loading project properties:");
                    ex.printStackTrace(System.err);
                } catch (IOException ex) {
                    System.err.println("Unexpected error when loading project properties:");
                    ex.printStackTrace(System.err);
                }
            }
            configDirectory = p.getProperty(CONFIG_DIRECTORY_KEY, DEFAULT_CONFIG_DIRECTORY);
        }
        return configDirectory;
    }
    
    public static class PropertyFormatException extends NumberFormatException {

        public PropertyFormatException(String propertyName, String message) {
            super(String.format("Property %s returns illegal value: %s", propertyName, message));
        }
        
    }
}
