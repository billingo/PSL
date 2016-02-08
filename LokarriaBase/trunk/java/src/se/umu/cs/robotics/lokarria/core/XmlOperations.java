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

package se.umu.cs.robotics.lokarria.core;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlOperations {

    private static Document createDocument(String uri) {
    	Document document = null;
        try {
            // Obtain factory instance
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // Obtain builder instance
            DocumentBuilder db = dbf.newDocumentBuilder();
            // Parse document
            document = db.parse(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return document;
    }
    
    public static ArrayList<Double> getDataByTagName(String uri, String nodeName, String parentNodeName){
    	ArrayList<Double> list = new ArrayList<Double>();
    	Document document = createDocument(uri);
    	NodeList listOfDouble = document.getElementsByTagName(nodeName);
    	
    	for(int i=0; i<listOfDouble.getLength(); i++){
    		Node doubleNode = listOfDouble.item(i);
    		if (doubleNode.getParentNode().getNodeName().equals(parentNodeName)){
    			try {
					list.add(NumberFormat.getNumberInstance().parse(doubleNode.getChildNodes().item(0).getNodeValue()).doubleValue());
				} catch (DOMException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
    		}
    	}
    	
    	return list;
    }
}

