/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright 2007 - 2009 Erik Billing
Interaction Lab, School of Informatics, University of Skovde, Sweden,
(http://www.his.se/erikb). 

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
package se.umu.cs.robotics.lokarria.fpsl.log;

import java.util.Iterator;
import java.util.Locale;
import org.apache.log4j.spi.LoggingEvent;
import org.json.simple.JSONObject;
import se.umu.cs.robotics.fpsl.FHypothesis;
import se.umu.cs.robotics.fpsl.FPrediction;
import se.umu.cs.robotics.fpsl.responsibility.ContextPredictionErrors;
import se.umu.cs.robotics.fpsl.selection.FLhsMatch;
import se.umu.cs.robotics.fpsl.selection.MatchList;
import se.umu.cs.robotics.hpl.log.HplLogMessage;
import se.umu.cs.robotics.log.AbstractMessageFormat;
import se.umu.cs.robotics.lokarria.statespace.AngularSpeedDimension;
import se.umu.cs.robotics.lokarria.statespace.LinearSpeedDimension;
import se.umu.cs.robotics.lokarria.statespace.SensoryMotorDistribution;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.probabilitydistribution.SingleStateDistribution;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 * @author Erik Billing <erik.billing@his.se>
 */
public class PredictionMessageFormat extends AbstractMessageFormat {

    @Override
    public boolean renderMessage(StringBuffer buffer, LoggingEvent event) {
        Object message = event.getMessage();
        if (message instanceof FPrediction) {
            return renderPrediction(buffer, (FPrediction)message);    
        } else if (message instanceof SensoryMotorDistribution) {
            return renderSensoryMotorMessage(buffer, (SensoryMotorDistribution)message);
        } else if (message instanceof ContextPredictionErrors) {
            return renderPredictionErrors(buffer, (ContextPredictionErrors)message);
        } else {
            return false;
        }
    }

    private boolean renderPrediction(StringBuffer buffer, FPrediction message) {
        SpaceDistribution element = message.element();
        buffer.append("<").append(getNameSpaceID()).append(":Prediction>{");
        if (element instanceof SensoryMotorDistribution) {
            renderSensoryMotorDistribution(buffer, (SensoryMotorDistribution)element);
            buffer.append(",");
        }
        
        MatchList<?> matches = ((FPrediction)message).getSelection().getMatches();
        buffer.append("\"Selection\":{");
        for (int length=1; length<matches.size();length++) {
            if (length>1) buffer.append(",");
            buffer.append("\"").append(length).append("\":[");
            boolean first = true;
            for (FLhsMatch<?> match: matches.get(length)) {
                first = renderMatch(buffer, match,first);
            }
            buffer.append("]");
        }
        
        buffer.append("}}</").append(getNameSpaceID()).append(":Prediction>\n");
        return true;
    }
    
    private boolean renderSensoryMotorMessage(StringBuffer buffer, SensoryMotorDistribution message) {
        buffer.append("<").append(getNameSpaceID()).append(":Distribution type=\"SensoryMotorDistribution\">{");
        renderSensoryMotorDistribution(buffer, message);
        buffer.append("}</").append(getNameSpaceID()).append(":Distribution>");
        return true;
    }

    private void renderSensoryMotorDistribution(StringBuffer buffer, SensoryMotorDistribution element) {
        Iterator<? extends ProbabilityDistribution<Double>> mDims = element.getMotorDistribution().dimensions();
        while (mDims.hasNext()) {
            ProbabilityDistribution<Double> dist = mDims.next();
            if (dist.getDimension() instanceof LinearSpeedDimension) {
                buffer.append("\"LinearSpeed\":");
            } else if (dist.getDimension() instanceof AngularSpeedDimension) {
                buffer.append("\"AngularSpeed\":");
            }
            renderDistribution(buffer, dist);
            buffer.append(",");
        }
        Iterator<? extends ProbabilityDistribution<Double>> sDims = ((SensoryMotorDistribution)element).getSensoryDistribution().dimensions();
        buffer.append("\"Echoes\":[");
        renderDistribution(buffer, sDims.next());
        while (sDims.hasNext()) {
            buffer.append(",");
            renderDistribution(buffer, sDims.next());
        }
        buffer.append("]");
    }
    
    private boolean renderMatch(StringBuffer buffer, FLhsMatch<?> match, boolean first) {
//        buffer.append("[");
//        boolean first = true;
        for (FHypothesis<?> h: match.getLhs().getHypotheses()) {
            if (first) {
                first = false;
            } else {
                buffer.append(",");
            }
            buffer.append(h.getId());
        }
//        buffer.append("]");
        return first;
    }

    private void renderDistribution(StringBuffer buffer, ProbabilityDistribution<Double> dist) {
        if (dist instanceof SingleStateDistribution) {
            Double state = ((SingleStateDistribution<Double>)dist).getState();
            buffer.append(String.format(Locale.US, "%.3f", state));
        } else {
            buffer.append(dist.getClass().getCanonicalName());
        }
    }

    @Override
    public String getNameSpace() {
        return HplLogMessage.NAME_SPACE;
    }

    @Override
    public String getPreferredNameSpaceID() {
        return "hpl";
    }

    private boolean renderPredictionErrors(StringBuffer buffer, ContextPredictionErrors contextPredictionErrors) {
        buffer.append("<").append(getNameSpaceID()).append(":ContextPrediction>");
        JSONObject json = new JSONObject();
        JSONObject errors = new JSONObject();
        JSONObject confidences = new JSONObject();
        for (int c=0; c<contextPredictionErrors.size(); c++) {
            final String name = contextPredictionErrors.getContext(c).getName();
            errors.put(name, contextPredictionErrors.getError(c));
            confidences.put(name,contextPredictionErrors.getConfidence(c));
        }
        json.put("Errors", errors);
        json.put("Confidences", confidences);
        buffer.append(json);
        buffer.append("</").append(getNameSpaceID()).append(":ContextPrediction>");
        return true;
    }
    
}