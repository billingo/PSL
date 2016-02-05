
package se.umu.cs.robotics.collections;

import java.util.ArrayList;

/**
 *
 * @author Erik Billing <erik.billing@his.se>
 */
public class IntegerRange extends ArrayList<Integer> {

    public IntegerRange(int max) {
        this(0,max);
    }
    
    public IntegerRange(int min, int max) {
        super(max-min);
        for (int i=min; i<max; i++) {
            this.add(i);
        }
    }
    
    
}
