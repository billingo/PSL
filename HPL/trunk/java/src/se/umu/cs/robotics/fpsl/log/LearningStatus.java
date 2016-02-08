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

package se.umu.cs.robotics.fpsl.log;

import java.io.File;
import se.umu.cs.robotics.hpl.log.AbstractHplLogMessage;
import se.umu.cs.robotics.fpsl.FLibrary;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class LearningStatus<E> extends AbstractHplLogMessage<E> {

    private final Status status;
    private final FLibrary<E> library;
    private final Object msg;

    protected LearningStatus(Status status, FLibrary<E> library, Object msg) {
        this.status = status;
        this.library = library;
        this.msg = msg;
    }

    public static <E> LearningStatus<E> started(FLibrary<E> library) {
        return new LearningStatus<E>(Status.STARTED, library, null);
    }

    public static <E> LearningStatus<E> finished(FLibrary<E> library) {
        return new LearningStatus<E>(Status.FINISHED, library, null);
    }

    public static <E> LearningStatus<E> paused(FLibrary<E> library) {
        return new LearningStatus<E>(Status.PAUSED, library, null);
    }

    public static <E> LearningStatus<E> continued(FLibrary<E> library) {
        return new LearningStatus<E>(Status.CONTINUED, library, null);
    }

    public static <E> LearningStatus<E> canceled(FLibrary<E> library) {
        return new LearningStatus<E>(Status.CANCELED, library, null);
    }

    public static <E> LearningStatus<E> aborted(FLibrary<E> library) {
        return new LearningStatus<E>(Status.ABORTED, library, null);
    }

    public static <E> LearningStatus<E> started(FLibrary<E> library, Object message) {
        return new LearningStatus<E>(Status.STARTED, library, message);
    }

    public static <E> LearningStatus<E> finished(FLibrary<E> library, Object message) {
        return new LearningStatus<E>(Status.FINISHED, library, message);
    }

    public static <E> LearningStatus<E> paused(FLibrary<E> library, Object message) {
        return new LearningStatus<E>(Status.PAUSED, library, message);
    }

    public static <E> LearningStatus<E> continued(FLibrary<E> library, Object message) {
        return new LearningStatus<E>(Status.CONTINUED, library, message);
    }

    public static <E> LearningStatus<E> canceled(FLibrary<E> library, Object message) {
        return new LearningStatus<E>(Status.CANCELED, library, message);
    }

    public static <E> LearningStatus<E> aborted(FLibrary<E> library, Object message) {
        return new LearningStatus<E>(Status.ABORTED, library, message);
    }

    public String toXML() {
        StringBuilder s = new StringBuilder();
        s.append(messageStartTag("LearningMessage"));
        s.append(String.format("<hpl:LearningStatus status=\"%s\">", status.toString()));
        if (getMsg() instanceof File) {
            s.append(String.format("<hpl:File>%s</hpl:File>", getMsg().toString()));
        }
        s.append(String.format("<hpl:Library size=\"%d\"/>", library.size()));
        s.append("</hpl:LearningStatus>");
        s.append(messageEndTag());
        return s.toString();
    }

    public FLibrary<E> getLibrary() {
        return library;
    }

    public Status getStatus() {
        return status;
    }

    public Object getMsg() {
        return msg;
    }

    public static enum Status {

        STARTED, FINISHED, PAUSED, CONTINUED, CANCELED, ABORTED
    }
}
