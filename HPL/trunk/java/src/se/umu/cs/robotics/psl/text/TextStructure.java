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

import java.io.IOException;
import java.text.Format;
import java.text.NumberFormat;

import se.umu.cs.robotics.psl.Library;
import se.umu.cs.robotics.psl.Predictor;
import se.umu.cs.robotics.psl.Psl;
import se.umu.cs.robotics.utils.FileTools;

public class TextStructure {
	String text;
	Library<Character> library;
	Psl<Character> slearner;
	Predictor<Character> spredictor;

	Format format = NumberFormat.getInstance();

	public TextStructure(final String text) {

		this.library = new TextLibrary();
		this.slearner = new Psl<Character>(library);
		this.spredictor = new Predictor<Character>(library);
		this.text = text;

		// slearner.train(text);
		int hits = 0;
		int misses = 0;

		// for (int i = 0; i < text.length(); i++) {
		// Character c = text.charAt(i);
		// if (c.equals('\n')) continue;
		// System.out.print(c.toString()+",");
		// SLearningSequence s = spredictor.bestSequence();
		// if (s == null) {
		// System.out.print("-  MISS  ");
		// misses++;
		// } else {
		// Character pc = (Character)s.predictNext();
		// if (pc == null) {
		// System.out.print("-  MISS  ");
		// misses++;
		// } else if (pc.equals(c)) {
		// System.out.print(pc.toString()+"  HIT   ");
		// hits++;
		// } else {
		// System.out.print(pc.toString()+"  MISS  ");
		// misses++;
		// }
		// }
		//			
		// System.out.print(s);
		//			
		// System.out.println("   "+format.format(((double)hits)/(hits+misses)*100)+"% correct");
		//			
		//			
		//			
		// spredictor.teach(c);
		// spredictor.input(c);
		// }

		System.out.print(library);
	}

	public static void main(final String[] args) {
		String srcData = "";
		try {
			srcData = FileTools.readFile(args[0]);
			TextStructure ts = new TextStructure(srcData);
		} catch (IOException e) {
			System.out.println("Can't open source file: " + args[0]);
			return;
		}

	}

}
