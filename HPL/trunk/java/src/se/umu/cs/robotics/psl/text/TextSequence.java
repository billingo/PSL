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


package se.umu.cs.robotics.psl.text;

import se.umu.cs.robotics.psl.PslNode;
import se.umu.cs.robotics.psl.exceptions.HypothesisLengthLimit;

public class TextSequence extends PslNode<Character> {

	public TextSequence(final PslNode<Character> parent, final Character c) throws HypothesisLengthLimit {
		super(parent, c);
	}

	public TextSequence(final Character c) {
		super(c);
	}

	// public SLearningSequence createFromDataMatch(Object[][] data, int
	// beginAt) throws SLearningError {
	// Object[][] newData = new
	// Object[sequenceData.length+1][sequenceData[0].length];
	// if (sequenceData.length+beginAt+1>data.length) throw new
	// SLearningError("Source data too short");
	//		
	// int i = beginAt;
	// int si = 0;
	// while(si<sequenceData.length) {
	// int rowLen = sequenceData[si].length;
	// if (rowLen!=data[i].length) throw new
	// SLearningError("Missmatching row length");
	// newData[si][0]=sequenceData[si][0];
	// i++;
	// si++;
	// }
	// newData[newData.length-1]=data[i];
	// for (Object[] row: newData) if (row[0]==null) {
	// System.out.print("Hmm.. null");
	// }
	// SLearningSequence newSequence = new TextSequence(this,newData);
	// childSequences.add(newSequence);
	// return newSequence;
	// }
	//	
	// public double compare(Object[][] data, double errorTolerance, int
	// beginAt) {
	// if (sequenceData.length==0) throw new SLearningError("No sequence data");
	// if (sequenceData.length+beginAt>data.length) return 0;
	// double comparisonScore = 0;
	//		
	// int i=beginAt;
	// int si=0;
	// while (si<sequenceData.length) {
	//			
	// if (sequenceData[si][0]==null || data[i][0]==null) {
	// //Skip
	// } else if (sequenceData[si][0].equals(data[i][0])) {
	// comparisonScore+=scoreHitt(si);
	// } else {
	// return 0;
	// }
	// si++;
	// i++;
	// }
	//		
	// return comparisonScore;
	// }
	//	
	// public double match(Object[][] data, int compareAt, int skipLast) {
	// if (sequenceData.length==0) throw new SLearningError("No sequence data");
	// double comparisonScore = 0;
	// int beginAt = compareAt-sequenceData.length+skipLast+1;
	// int tail = data.length;
	// if (beginAt<0) {
	// tail += beginAt;
	// beginAt = 0;
	// if (tail<=compareAt) throw new
	// SLearningError("Comparison data too short");
	// }
	//		
	// int si = 0;
	// for (int i=tail;i<data.length;i++) {
	//			
	// if (sequenceData[si][0]==null || data[i][0]==null) {
	// //Skip
	// } else if (sequenceData[si][0].equals(data[i][0])) {
	// comparisonScore+=scoreHitt(si);
	// } else {
	// // comparisonScore+=scoreMiss(si,errorTolerance);
	// return 0;
	// }
	//			
	// si++;
	// }
	//		
	// for (int i=beginAt;i<=compareAt;i++) {
	// if (sequenceData[si][0]==null || data[i][0]==null) {
	// //Skip
	// } else if (sequenceData[si][0].equals(data[i][0])) {
	// comparisonScore+=scoreHitt(si);
	// } else {
	// // comparisonScore+=scoreMiss(si,errorTolerance);
	// return 0;
	// }
	//		
	// si++;
	// }
	//		
	// return comparisonScore;
	// }
	//	
	// public String toString() {
	// StringBuffer s = new StringBuffer();
	// s.append("[");
	// for(Object[] row: sequenceData) {
	// s.append(row[0]);
	// }
	// s.append("]");
	// s.append(getScore());
	// return s.toString();
	// }
}
