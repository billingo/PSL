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

package se.umu.cs.robotics.log;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import javax.xml.parsers.FactoryConfigurationError;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import se.umu.cs.robotics.utils.ConfigTools;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LogConfigurator {

    static Logger logger = Logger.getLogger(LogConfigurator.class);
    public static String DEFAULT_CONFIG_FILE = "log.config.xml";
    private static URL activeConfiguration = null;
    private final static HashSet<ConfigurableLogComponent> logComponents = new HashSet<ConfigurableLogComponent>();

    public static void configure() {
        configure(DEFAULT_CONFIG_FILE);
    }

    public static void configure(String configFileName) {
        if (activeConfiguration == null) {
            String configDirectory = ConfigTools.getConfigDirectory();
            File configFile = new File(configDirectory, configFileName);
            configure(configFile);
        } else {
            throw new FactoryConfigurationError("Log system already configured using: " + activeConfiguration);
        }
    }

    public static void configure(File configFile) {
        try {
            configure(configFile.toURI().toURL());
        } catch (MalformedURLException ex) {
            BasicConfigurator.configure();
            logger.warn("Illegal path to log configuration file: " + configFile.toString());
        }
    }

    public static void configure(URL configFile) {
        if (activeConfiguration == null) {
            DOMConfigurator.configure(configFile);
            for (ConfigurableLogComponent c : logComponents) {
                c.setup(configFile);
            }
            activeConfiguration = configFile;
        } else {
            logger.warn("Log system already configured using: " + activeConfiguration.toString());
        }
    }

    public static void shutdown() {
        for (ConfigurableLogComponent c : logComponents) {
            c.shutdown();
        }
        LogManager.shutdown();
        activeConfiguration = null;
    }

    public static URL getActiveConfiguration() {
        return activeConfiguration;
    }

    public static boolean isConfigured() {
        return activeConfiguration != null;
    }

    public static void registerLogComponent(ConfigurableLogComponent component) {
        logComponents.add(component);
    }
}
