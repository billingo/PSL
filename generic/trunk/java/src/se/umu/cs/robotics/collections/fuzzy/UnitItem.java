/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package se.umu.cs.robotics.collections.fuzzy;

/**
 *
 * @author billing
 */
public class UnitItem<E> implements FuzzyItem<E> {

    private E element;

    public UnitItem(E element) {
        this.element = element;
    }

    public E element() {
        return element;
    }

    public double value() {
        return 1d;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}
