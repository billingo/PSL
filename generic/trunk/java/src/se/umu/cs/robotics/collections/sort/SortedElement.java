/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.collections.sort;

/**
 *
 * @author billing
 */
public interface SortedElement extends Comparable<SortedElement> {

    public int originalPosition();
    
}
