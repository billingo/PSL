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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

public class Connection {

    static Logger logger = Logger.getLogger(Connection.class);
    private final String url;
    StringBuffer buffer = new StringBuffer();
    ReentrantLock bufferLock = new ReentrantLock();

    public Connection(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    /**
     * Executes a GET and returns the response as a string.
     *
     * @return response from the server
     * @throws IOException
     */
    public String get() throws IOException {
        URL u = new URL(url);
        URLConnection connection = u.openConnection();
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(1000);
        connection.setDoOutput(false);
        InputStream stream = connection.getInputStream();
        return getData(stream);
    }

    private String getData(InputStream stream) throws IOException {
        if (stream == null) {
            return null;
        }

        bufferLock.lock();
        try {
            buffer.setLength(0);

            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } finally {
            bufferLock.unlock();
        }
    }

    public PostMethod newPost() {
        return new PostMethod(url);
    }

    /**
     * Executes the specified POST method and returns the data as a string.
     *
     * @param method POST method
     * @return response from the server.
     * @throws IOException
     */
    public String post(PostMethod method) throws IOException {
        String response = null;
        HttpClient client = new HttpClient();

        switch (LokarriaPropertyLoader.getVersion()) {
            case V4:
                method.addRequestHeader("Content-type", "application/json");
                method.addRequestHeader("Accept", "text/json");
        }
        client.getParams().setParameter("http.useragent", "Lokarria Client");

        try {
            int returnCode = client.executeMethod(method);

            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                logger.fatal("The Post method is not implemented by this URL");
                method.getResponseBodyAsString();
            }
            InputStream stream = method.getResponseBodyAsStream();
            response = getData(stream);
        } finally {
            method.releaseConnection();
        }
        return response;
    }
}
