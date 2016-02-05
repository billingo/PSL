/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package se.umu.cs.robotics.collections.fuzzy;

/**
 *
 * @author billing
 */
public interface FuzzySet<E> extends Iterable<FuzzyItem<E>> {

    public double get(E element);

    public FuzzyItem<E> get(int i);

    public void put(E element, double v);

    public void put(FuzzyItem<E> item);

    public void putAll(FuzzySet<E> collection);

    public int size();

    public boolean isEmpty();

    /**
     * @return the item with the highest value
     */
    public FuzzyItem<E> max();

    /**
     * @return the item with the lowest value
     */
    public FuzzyItem<E> min();

    /**
     * @return the total sum of all items in the set
     */
    public double sum();
}
