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

import java.util.concurrent.CopyOnWriteArraySet;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Abstrasct class intended to act as baseclass for a particular application 
 * that should listned to some log events. The extended class should be put as
 * an appender in the Log4j config file and provide an interface such that listeners 
 * can be added.
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public abstract class LogReporter extends AppenderSkeleton {

    private final CopyOnWriteArraySet<LogListener> listeners;

    public LogReporter(CopyOnWriteArraySet<LogListener> listeners) {
        super();
        this.listeners = listeners;
    }

    public LogReporter(boolean isActive, CopyOnWriteArraySet<LogListener> listeners) {
        super(isActive);
        this.listeners = listeners;
    }

    @Override
    protected void append(LoggingEvent le) {
        for (LogListener listener: listeners) listener.event(le);
    }

    @Override
    public void activateOptions() {
        super.activateOptions();
        for (LogListener listener: listeners) listener.loggingConfigured();
    }

    @Override
    public void close() {
        for (LogListener listener: listeners) listener.loggingClosed();
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

}