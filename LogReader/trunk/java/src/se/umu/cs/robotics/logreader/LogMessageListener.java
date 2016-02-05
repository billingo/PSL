/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package se.umu.cs.robotics.logreader;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public interface LogMessageListener<M> {

    void message(M message);
}
