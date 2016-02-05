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
package se.umu.cs.robotics.lokarria.differentialdrive;

import java.io.IOException;
import java.util.Locale;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import se.umu.cs.robotics.lokarria.core.Connection;
import se.umu.cs.robotics.lokarria.core.LokarriaPropertyLoader;
import static se.umu.cs.robotics.lokarria.log.LogUtils.logJSON;
import se.umu.cs.robotics.lokarria.log.LokarriaLogMessage.MessageType;

public class DifferentialDriveOperations {

    public static final Logger logger = Logger.getLogger(DifferentialDriveOperations.class);
    private Connection connection;

    public DifferentialDriveOperations() {
        connection = new Connection(LokarriaPropertyLoader.getValue("lokarria.differential.drive"));
    }

    public DifferentialDriveOperations(String url) {
        connection = new Connection(url);
    }

    public void postCommand(DifferentialDriveCommand command) {
        PostMethod post = connection.newPost();

        switch (LokarriaPropertyLoader.getVersion()) {
            case V4:
                post.setRequestEntity(new StringRequestEntity(String.format(Locale.US,"{\"TargetAngularSpeed\":%.3f, \"TargetLinearSpeed\":%.3f}", command.getAngularSpeed(), command.getLinearSpeed())));
                break;
            default:
                post.addParameter("AngularSpeed", new Double(command.getAngularSpeed()).toString());
                post.addParameter("LinearSpeed", new Double(command.getLinearSpeed()).toString());
        }
        String response = null;
        try {
            response = connection.post(post);
        } catch (IOException ex) {
            logger.warn(ex);
        }
        if (response != null) {
            logger.warn("Unexpected response while posting command: " + response);
        }
        logJSON(logger, command.toJSON(), MessageType.POST);
    }

    public DifferentialDriveCommand getCommand() {
        try {
            String data = connection.get();
            logJSON(logger, data, MessageType.GET);
            return ConcreteDifferentialDriveCommand.fromJSON(data);
        } catch (ParseException ex) {
            logger.warn(ex);
        } catch (IOException ex) {
            logger.warn(ex);
        }
        return ConcreteDifferentialDriveCommand.stop();
    }
}
