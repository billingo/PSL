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

package se.umu.cs.robotics.logreader;

import javax.xml.stream.XMLStreamReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SimpleStaxTest {

    public SimpleStaxTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testStax() throws XMLStreamException, FileNotFoundException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
//        XMLEventReader eventReader = factory.createXMLEventReader(new FileReader("log.test.xml"));
        XMLStreamReader streamReader = factory.createXMLStreamReader(new FileReader("log.test.xml"));

        while (streamReader.hasNext()) {
            int event = streamReader.next();
            if (event == streamReader.START_DOCUMENT) {
                System.out.println("Doc start");
            } else if (event == streamReader.START_ELEMENT) {
                System.out.println("Element start: "+streamReader.getLocalName());
                
            } else if (event == streamReader.END_ELEMENT) {
                System.out.println("Element end");
            } else if (event == streamReader.END_DOCUMENT) {
                System.out.println("Doc end");
            }
//            System.out.println(next+": "+streamReader.getEventType());
//            if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT) {
//                String elementName = streamReader.getLocalName();
//                if ("driver".equals(elementName)) {
//                    parseDriverAndAllChildren(streamReader);
//                } else if ("vehicle".equals(elementName)) {
//                    parseVehicleAndAllChildren(streamReader);
//                }
//            }
        }
    }
}
