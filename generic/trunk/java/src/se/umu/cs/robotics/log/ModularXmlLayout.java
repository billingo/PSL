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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map.Entry;
import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.log4j.xml.XMLLayout;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class ModularXmlLayout extends XMLLayout {

    private final int DEFAULT_SIZE = 8192;
    private final int UPPER_LIMIT = 16384;
    private StringBuffer buf = new StringBuffer(DEFAULT_SIZE);
    private ArrayList<LogMessageFormat> formats = new ArrayList<LogMessageFormat>();
    private HashMap<String, String> nameSpaces = new HashMap<String, String>();
    private ArrayList<String> styleSheets = new ArrayList<String>();

    public ModularXmlLayout() {
        nameSpaces.put("log4j", "http://jakarta.apache.org/log4j");
        nameSpaces.put("robotics", "http://www.cs.umu.se/robotics");
    }

    @Override
    public void activateOptions() {
        super.activateOptions();
    }

    @Override
    public String getHeader() {
        StringBuilder s = new StringBuilder();
        s.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n");
        for (String url : styleSheets) {
            s.append("<?xml-stylesheet type=\"text/xsl\" href=\"");
            s.append(url);
            s.append("\"?>\r\n");
        }
        s.append("<robotics:log");
        for (Entry<String, String> entry : nameSpaces.entrySet()) {
            s.append(" xmlns:");
            s.append(entry.getKey());
            s.append("=\"");
            s.append(entry.getValue());
            s.append("\"");
        }
        s.append(">\r\n");
//                "<?xml-stylesheet href='log.lokarria.css' type='text/css' ?>\r\n";
        return s.toString();
    }

    @Override
    public String getFooter() {
        return "</robotics:log>\r\n";
    }

    /**
     * Formats a {@link org.apache.log4j.spi.LoggingEvent} in conformance with the log4j.dtd.
     * */
    @Override
    public String format(final LoggingEvent event) {
        // Modified source from XMLLayout

        // Reset working buffer. If the buffer is too large, then we need a new
        // one in order to avoid the penalty of creating a large array.
        if (buf.capacity() > UPPER_LIMIT) {
            buf = new StringBuffer(DEFAULT_SIZE);
        } else {
            buf.setLength(0);
        }

        // We yield to the \r\n heresy.

        buf.append("<log4j:event logger=\"");
        buf.append(Transform.escapeTags(event.getLoggerName()));
        buf.append("\" timestamp=\"");
        buf.append(event.timeStamp);
        buf.append("\" level=\"");
        buf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
        buf.append("\" thread=\"");
        buf.append(Transform.escapeTags(event.getThreadName()));
        buf.append("\">\r\n");

        renderMessage(event);

        String ndc = event.getNDC();
        if (ndc != null) {
            buf.append("<log4j:NDC><![CDATA[");
            Transform.appendEscapingCDATA(buf, ndc);
            buf.append("]]></log4j:NDC>\r\n");
        }

        String[] s = event.getThrowableStrRep();
        if (s != null) {
            buf.append("<log4j:throwable><![CDATA[");
            for (int i = 0; i < s.length; i++) {
                Transform.appendEscapingCDATA(buf, s[i]);
                buf.append("\r\n");
            }
            buf.append("]]></log4j:throwable>\r\n");
        }

        if (getLocationInfo()) {
            LocationInfo locationInfo = event.getLocationInformation();
            buf.append("<log4j:locationInfo class=\"");
            buf.append(Transform.escapeTags(locationInfo.getClassName()));
            buf.append("\" method=\"");
            buf.append(Transform.escapeTags(locationInfo.getMethodName()));
            buf.append("\" file=\"");
            buf.append(Transform.escapeTags(locationInfo.getFileName()));
            buf.append("\" line=\"");
            buf.append(locationInfo.getLineNumber());
            buf.append("\"/>\r\n");
        }

        if (getProperties()) {
            Set keySet = event.getPropertyKeySet();
            if (keySet.size() > 0) {
                buf.append("<log4j:properties>\r\n");
                Object[] keys = keySet.toArray();
                Arrays.sort(keys);
                for (int i = 0; i < keys.length; i++) {
                    String key = keys[i].toString();
                    Object val = event.getMDC(key);
                    if (val != null) {
                        buf.append("<log4j:data name=\"");
                        buf.append(Transform.escapeTags(key));
                        buf.append("\" value=\"");
                        buf.append(Transform.escapeTags(String.valueOf(val)));
                        buf.append("\"/>\r\n");
                    }
                }
                buf.append("</log4j:properties>\r\n");
            }
        }

        buf.append("</log4j:event>\r\n\r\n");

        return buf.toString();
    }

    /**
     * Renders the event message using message formats in formats. Will use the first matching format in the list.
     * @param event
     */
    private void renderMessage(LoggingEvent event) {
        for (LogMessageFormat format : formats) {
            if (format.renderMessage(buf, event)) {
                break;
            }
        }
    }

    /**
     * Adds a new message format to the layout
     * @param format
     */
    public void addFormat(LogMessageFormat format) {
        addFormatNameSpace(format, format.getPreferredNameSpaceID());
        formats.add(format);
    }

    private void addFormatNameSpace(LogMessageFormat format, String nsId) {
        if (nameSpaces.containsKey(nsId)) {
            if (!nameSpaces.get(nsId).equals(format.getNameSpace())) {
                addFormatNameSpace(format, nsId, 1);
            }
        } else {
            nameSpaces.put(nsId, format.getNameSpace());
        }
        format.setNameSpaceID(nsId);
    }

    private void addFormatNameSpace(LogMessageFormat format, String nsId, int index) {
        final String myId = nsId + index;
        if (nameSpaces.containsKey(myId)) {
            if (!nameSpaces.get(myId).equals(format.getNameSpace())) {
                addFormatNameSpace(format, nsId, index + 1);
            }
        } else {
            nameSpaces.put(myId, format.getNameSpace());
        }
        format.setNameSpaceID(myId);
    }

    /**
     * Adds a new message format to the layout
     * @param cls full name of the desired format class
     */
    public void setFormat(String cls) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        String[] split = cls.split(":");
        Class<?> formatClass = Class.forName(split[0]);
        if (split.length == 1) {
            addFormat((LogMessageFormat) formatClass.newInstance());
        } else {
            Object[] params = split[1].split(",");
            Class<?>[] paramTypes = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++) {
                paramTypes[i] = String.class;
            }
            Constructor<?> constructor = formatClass.getConstructor(paramTypes);
            addFormat((LogMessageFormat) constructor.newInstance(params));
        }
    }

    public ArrayList<LogMessageFormat> getFormats() {
        return formats;
    }

    public void setStyleSheet(String url) {
        styleSheets.add(url);
    }
}
