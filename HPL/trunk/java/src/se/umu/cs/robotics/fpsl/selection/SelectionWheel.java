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

package se.umu.cs.robotics.fpsl.selection;

import java.util.ArrayList;
import java.util.Random;
import se.umu.cs.robotics.probabilitydistribution.SpaceDistribution;

/**
 *
 * @author Erik Billing <erik.billing@his.se>
 */
public class SelectionWheel<E> extends ArrayList<FHypothesisMatch<E>> {
    
    private static final Random rnd = new Random();
    private double powerSum = 0;
    
    @Override
    public boolean add(FHypothesisMatch<E> item) {
        if (item instanceof MaxProductMatch) {
            powerSum += ((MaxProductMatch)item).getPower();
        }
        return super.add(item);
    }
    
    public SpaceDistribution<E> getTarget() {
        double select = rnd.nextDouble()*powerSum;
        double cursum = 0;
        for (int i=0; i<size(); i++) {
            FHypothesisMatch<E> e = get(i);
            cursum += e instanceof MaxProductMatch ? ((MaxProductMatch)e).getPower() : 0;
            if (select < cursum) return e.getHypothesis().getTarget();
        }
        return null;
    }
}
