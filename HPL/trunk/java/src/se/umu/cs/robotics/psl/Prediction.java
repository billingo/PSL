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

import se.umu.cs.robotics.iteration.position.GenericPositionIterator;
import se.umu.cs.robotics.iteration.position.GenericReverseIterator;
import se.umu.cs.robotics.iteration.position.IteratorPosition;
import se.umu.cs.robotics.iteration.position.ElementIterator;
import se.umu.cs.robotics.iteration.position.LinkedPositionIterator;
import se.umu.cs.robotics.iteration.position.PositionIterator;
import se.umu.cs.robotics.psl.comparison.HypothesisSelector;

/**
 *
 * @author billing
 */
public class Prediction<E> implements IteratorPosition<E> {

    private IteratorPosition<E> previous;
    private final Library library;
    private HypothesisList<E> match;

    /**
     *
     * @param library
     * @param history must be a
     */
    public Prediction(Library<E> library, PositionIterator<E> source) {
        this(library,source.getPosition());
    }

    public Prediction(Library<E> library, IteratorPosition<E> previous) {
        this.library = library;
        this.previous = previous;
        this.match = library.match(new LinkedPositionIterator<E>(new ElementIterator<E>(new GenericReverseIterator<E>(previous,true))));
    }

    public IteratorPosition<E> getPrevious() {
        return previous;
    }

    public IteratorPosition<E> getNext() {
        return new Prediction<E>(library,this);
    }

    public E element() {
        Hypothesis<E> h = hypothesis();
        return h==null?null:h.getTarget();
    }

    public E element(HypothesisSelector selector) {
        Hypothesis<E> h = hypothesis(selector);
        return h==null?null:h.getElement();
    }

    public Hypothesis<E> hypothesis() {
        return match.getBest();
    }

    public Hypothesis<E> hypothesis(HypothesisSelector<E> selector) {
        return match.getBest(selector);
    }

    public HypothesisList<E> getMatch() {
        return match;
    }

    public boolean hasNext() {
        return true;
    }

    public boolean hasPrevious() {
        return true;
    }

    public PositionIterator<E> iterator() {
        return new GenericPositionIterator<E>(this);
    }

}
