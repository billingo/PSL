/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.collections.sort;

/**
 *
 * @author billing
 */
public class SortedDouble implements SortedElement {

    private int originalPosition;
    private double v;
    private SorterOrder order;

    public SortedDouble(int originalPosition, double value, SorterOrder order) {
        this.originalPosition = originalPosition;
        this.v = value;
        this.order = order;
    }

    public int originalPosition() {
        return originalPosition;
    }

    public double value() {
        return v;
    }

    public int compareTo(SortedElement t) {
        if (t instanceof SortedDouble) {
            double diff = v - ((SortedDouble) t).v;
            switch (order) {
                case REVERSED_NATURAL:
                    return diff > 0 ? -1 : diff < 0 ? 1 : 0;
                default: //NATURAL
                    return diff > 0 ? 1 : diff < 0 ? -1 : 0;
            }
        } else {
            return 0;
        }
    }
}
