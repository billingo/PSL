/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright 2011 Erik Billing and Benjamin Fonooni
 billing@cs.umu.se, fonooni@cs.umu.se
Department of Computing Science, Umea University, Sweden,
(http://www.cognitionreversed.com).

LICENSE:

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston,
MA 02111-1307, USA.
\*-------------------------------------------------------------------*/
package se.umu.cs.robotics.lokarria.core;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

public class JsonOperations {

    static Logger logger = Logger.getLogger(JsonOperations.class);

    public static HashMap getItem(String json, String item, int index) {
        HashMap results = (HashMap) JSONValue.parse(json);
        JSONArray array = (JSONArray) results.get(item);
        HashMap oneEcho = (HashMap) array.get(index);
        return oneEcho;
    }

    public static JSONArray getAllItems(String json, String item) {
        HashMap results = (HashMap) JSONValue.parse(json);
        JSONArray array = (JSONArray) results.get(item);
        return array;
    }

    public static ArrayList getItemsWithKey(String json, String key) {
        ArrayList items = new ArrayList();
        JSONParser parser = new JSONParser();
        KeyFinder finder = new KeyFinder();
        finder.setMatchKey(key);
        while (!finder.isEnd()) {
            try {
                parser.parse(json, finder, true);
                if (finder.isFound()) {
                    finder.setFound(false);
                    items.add(finder.getValue());
                }
            } catch (ParseException ex) {
                logger.warn(ex);
            }
        }
        return items;
    }

    /**
     * The JSON parser converts values to either Double or Long depending on how they are written in the JSON src. This method converts both types to double.
     * @param v
     * @return double value of v
     */
    public static double value(Object v) {
        if (v instanceof Double) {
            return (Double) v;
        } else if (v instanceof Long) {
            return (Long) v;
        } else if (v instanceof Integer) {
            return (double) ((Integer) v).intValue();
        } else {
            throw new IllegalArgumentException(String.format("Unexpected value type: %s", v.getClass().toString()));
        }
    }

    public static class JsonException extends ParseException {

        private final String msg;

        public JsonException(String message) {
            super(ERROR_UNEXPECTED_EXCEPTION);
            this.msg = message;
        }

        @Override
        public String toString() {
            return "JsonException: " + msg;
        }
    }
}
