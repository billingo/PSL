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

package se.umu.cs.robotics.fpsl;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import se.umu.cs.robotics.fpsl.log.ResponsibilityChanged;

/**
 * A Fuzzy Hypothesis Set representing confidence values for each hypothesis in the set. 
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class FContext {

    final static Logger logger = Logger.getLogger(FContext.class);
    
    private final ArrayList<Confidence> confidences = new ArrayList<Confidence>();
    private final String name;
    private double responsibility;

    FContext(String name, double responsibility) {
        this.name = name;
        setResponsibility(responsibility);
    }

    public String getName() {
        return name;
    }

    public double getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(double responsibility) {
        this.responsibility = responsibility;
        logger.info(new ResponsibilityChanged(this,responsibility));
    }

    public Confidence getConfidence(FHypothesis hypothesis) {
        return getConfidence(hypothesis, false);
    }

    public Confidence getConfidence(FHypothesis hypothesis, boolean useParentIfMissing) {
        final int id = hypothesis.getId();
        Confidence c = null;
        if (id < confidences.size()) {
            c = confidences.get(id);
        }
        if (c == null && useParentIfMissing && hypothesis.length() > 1) {
            return getConfidence(hypothesis.getParent());
        } else {
            return c;
        }
    }

    public double getConfidenceValue(FHypothesis hypothesis) {
        return getConfidenceValue(hypothesis, false);
    }

    public double getConfidenceValue(FHypothesis hypothesis, boolean useParentIfMissing) {
        Confidence confidence = getConfidence(hypothesis, useParentIfMissing);
        if (confidence == null) {
            return 0d;
        } else {
            return confidence.getValue();
        }
    }

    private Confidence createConfidence(FHypothesis hypothesis) {
        int id = hypothesis.getId();
        while (id >= confidences.size()) {
            confidences.add(null);
        }
        Confidence confidence = new Confidence();
        confidences.set(id, confidence);
        return confidence;
    }

    public void hit(FHypothesis hypothesis, double value) {
        if (value > 0) {
            Confidence confidence = getConfidence(hypothesis);
            if (confidence == null) {
                confidence = createConfidence(hypothesis);
            }
            confidence.hits += value;
        }
    }

    public void miss(FHypothesis hypothesis, double value) {
        if (value > 0) {
            Confidence confidence = getConfidence(hypothesis);
            if (confidence == null) {
                confidence = createConfidence(hypothesis);
            }
            confidence.misses += value;
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public static class Confidence {

        private double hits;
        private double misses;

        private Confidence() {
            hits = 0d;
            misses = 0d;
        }

        public double getValue() {
            return getValue(hits, misses);
        }

        public static double getValue(double hits, double misses) {
            final double sum = hits + misses;
            if (sum > 0) {
                return hits * hits / (sum * sum);
            } else {
                return 0d;
            }
        }

        public double getHits() {
            return hits;
        }

        public double getMisses() {
            return misses;
        }
    }

	public void clear() {
		confidences.clear();
	}
}
