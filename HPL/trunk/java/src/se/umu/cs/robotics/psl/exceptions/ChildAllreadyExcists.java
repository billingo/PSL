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
package se.umu.cs.robotics.psl.exceptions;

import se.umu.cs.robotics.psl.Hypothesis;

public class ChildAllreadyExcists extends PslError {
	private static final long serialVersionUID = 1L;
	private final Hypothesis<?> hypothesis;
	private final Object element;

	public ChildAllreadyExcists(final Hypothesis<?> h, final Object element) {
		super("Hypothesis " + (h == null ? "null" : h.toString()) + "allready has a child with element " + (element == null ? "null" : element.toString()));
		this.hypothesis = h;
		this.element = element;
	}

	public Hypothesis<?> getHypothesis() {
		return hypothesis;
	}

	public Object getElement() {
		return element;
	}
}
