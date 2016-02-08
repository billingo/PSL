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

package se.umu.cs.robotics.logreader.filter;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SourceFilter<M> implements LogEventFilter<M> {

    private final Logger[] acceptedLoggers;

    public SourceFilter(Logger... acceptedLoggers) {
        this.acceptedLoggers = acceptedLoggers;
    }

    public boolean passLogger(final Logger logger) {
        Category c = logger;
        while (c != null) {
            for (Logger l : acceptedLoggers) {
                if (l == c) {
                    return true;
                }
            }
            c = c.getParent();
        }
        return false;
    }

    public boolean passLevel(Level level) {
        return true;
    }

    public boolean passTimeStamp(long timeStamp) {
        return true;
    }

    public boolean passMessage(M message) {
        return true;
    }
}
