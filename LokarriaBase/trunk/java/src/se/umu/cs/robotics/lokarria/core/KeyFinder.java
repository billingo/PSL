/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright 2011 Benjamin Fonooni and Erik Billing
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

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class KeyFinder implements ContentHandler {
    private Object value;
    private boolean found = false;
    private boolean end = false;
    private String key;
    private String matchKey;

    public void setMatchKey(String matchKey){
        this.matchKey = matchKey;
    }

    public Object getValue(){
        return value;
    }

    public boolean isEnd(){
        return end;
    }

    public void setFound(boolean found){
        this.found = found;
    }

    public boolean isFound(){
        return found;
    }

    public void startJSON() throws ParseException, IOException {
        found = false;
        end = false;
    }

    public void endJSON() throws ParseException, IOException {
        end = true;
    }

    public boolean primitive(Object value) throws ParseException, IOException {
        if(key != null){
            if(key.equals(matchKey)){
                found = true;
                this.value = value;
                key = null;
                return false;
            }
        }
        return true;
    }

    public boolean startArray() throws ParseException, IOException {
        return true;
    }


    public boolean startObject() throws ParseException, IOException {
        return true;
    }

    public boolean startObjectEntry(String key) throws ParseException, IOException {
        this.key = key;
        return true;
    }

    public boolean endArray() throws ParseException, IOException {
        return false;
    }

    public boolean endObject() throws ParseException, IOException {
        return true;
    }

    public boolean endObjectEntry() throws ParseException, IOException {
        return true;
    }
}
