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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Passes all events that pass at least one of underlying filter
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class PassAnyFilter<M> implements LogEventFilter<M> {

    private LogEventFilter<M>[] filters;

    public PassAnyFilter(LogEventFilter<M>... filters) {
        this.filters = filters;
    }

    public boolean passLogger(Logger logger) {
        for (LogEventFilter<M> filter: filters) {
            if (filter.passLogger(logger)) return true;
        }
        return false;
    }

    public boolean passLevel(Level level) {
        for (LogEventFilter<M> filter: filters) {
            if (filter.passLevel(level)) return true;
        }
        return false;
    }

    public boolean passTimeStamp(long timeStamp) {
        for (LogEventFilter<M> filter: filters) {
            if (filter.passTimeStamp(timeStamp)) return true;
        }
        return false;
    }

    public boolean passMessage(M message) {
        for (LogEventFilter<M> filter: filters) {
            if (filter.passMessage(message)) return true;
        }
        return false;
    }

}
