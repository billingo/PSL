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

package se.umu.cs.robotics.log;

import java.net.URL;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public interface ConfigurableLogComponent {

    /**
     * Called by the log configurator directly after Log4j is configured.
     * @param configurationFile
     */
    void setup(URL configurationFile);

    /**
     * Called by the log configurator directly prior to Log4j shutdown.
     */
    void shutdown();
}
