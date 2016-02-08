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
package se.umu.cs.robotics.psl;

public class RootNode<E> extends AbstractHypothesis<E> {

	private final Library<E> library;

	public RootNode(final Library<E> library, final E element) {
		super(element);
		this.library = library;
	}

	@Override
	public double getConfidence() {
		return 1;
	}

	@Override
	public Library<E> getLibrary() {
		return library;
	}

	@Override
	public Hypothesis<E> getParent() {
		return null;
	}

	@Override
	public Hypothesis<E> getRoot() {
		return this;
	}

	@Override
	public E getTarget() {
		return getElement();
	}

	@Override
	public long getUpdateId() {
		return 0;
	}

	@Override
	public void hit() {}

	@Override
	public int length() {
		return 0;
	}

	@Override
	public boolean lhsEquals(final Hypothesis<E> h) {
		return false;
	}

	@Override
	public void miss() {}

}
