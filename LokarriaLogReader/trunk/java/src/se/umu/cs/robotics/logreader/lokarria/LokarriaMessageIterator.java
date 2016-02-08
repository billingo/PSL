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

package se.umu.cs.robotics.logreader.lokarria;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.logreader.LogEvent;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LokarriaMessageIterator implements IterableIterator<LokarriaLogMessage> {

    private final Iterator<LogEvent<LokarriaLogMessage>> source;
    private LogEvent<LokarriaLogMessage> currentEvent;
    private Iterator<LokarriaLogMessage> currentMessageIterator;

    public LokarriaMessageIterator(Iterator<LogEvent<LokarriaLogMessage>> source) {
        this.source = source;
    }

    public Iterator<LokarriaLogMessage> iterator() {
        return this;
    }

    private void feed(boolean force) {
        while ((currentMessageIterator == null || !currentMessageIterator.hasNext()) && (force || source.hasNext())) {
            currentEvent = source.next();
            currentMessageIterator = currentEvent.messages().iterator();
        }
    }

    public boolean hasNext() {
        feed(false);
        return currentMessageIterator != null && currentMessageIterator.hasNext();
    }

    public LokarriaLogMessage next() {
        feed(true);
        return currentMessageIterator.next();
    }

    public LogEvent<LokarriaLogMessage> currentEvent() {
        return currentEvent;
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }
}
