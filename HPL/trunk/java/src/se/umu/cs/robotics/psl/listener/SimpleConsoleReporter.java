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


package se.umu.cs.robotics.psl.listener;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.Locale;

import se.umu.cs.robotics.psl.Library;
import se.umu.cs.robotics.psl.Hypothesis;
import se.umu.cs.robotics.psl.Psl.TrainingReference;

public class SimpleConsoleReporter<E> implements LearningListener<E> {

	private static NumberFormat format = NumberFormat.getInstance(Locale.US);

	PrintStream stream;

	public SimpleConsoleReporter() {
		this(System.out);
	}

	public SimpleConsoleReporter(final PrintStream stream) {
		this.stream = stream;
	}

	@Override
	public void hypothesisCreated(final TrainingReference<E> ref, final Hypothesis<E> newSequence) {
		stream.println("Sequence created: " + ref.getLearner().getLibrary().size() + " sequences in library");
	}

	@Override
	public void trainingComplete(final TrainingReference<E> ref) {
		Library<E> lib = ref.getLearner().getLibrary();
		stream.println("Training complete: " + lib.size() + " sequences in library, with average sequence length: " + format.format(lib.averageSequenceLength()));
	}

	@Override
	public void trainingStarted(final TrainingReference<E> ref) {
		stream.println("Training started.");

	}

	@Override
	public void hypothesisUpdated(final TrainingReference<E> ref, final Hypothesis<E> h) {}

}
