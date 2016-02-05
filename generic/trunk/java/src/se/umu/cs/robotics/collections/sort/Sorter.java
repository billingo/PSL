/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.collections.sort;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author billing
 */
public class Sorter<E extends SortedElement> extends ArrayList<E> {

    protected Sorter() {
        super();
    }

    public static Sorter<SortedDouble> sort(double[] array) {
        return sort(array, SorterOrder.NATURAL);
    }

    public static Sorter<SortedDouble> sort(double[] array, SorterOrder order) {
        Sorter<SortedDouble> sorter = new Sorter<SortedDouble>();
        for (int i = 0; i < array.length; i++) {
            sorter.add(new SortedDouble(i, array[i], order));
        }
        Collections.sort(sorter);
        return sorter;
    }
}
