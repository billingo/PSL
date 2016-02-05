/*-------------------------------------------------------------------*\
THIS SOURCE IS PART OF THE HPL-FRAMEWORK - www.cognitionreversed.com

Copyright 2011 Benjamin Fonooni and Erik Billing
 fonooni@cs.umu.se, billing@cs.umu.se
Department of Computing Science, Umea University, Sweden,
(http://www.cognitionreversed.com).

LICENSE:

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston,
MA 02111-1307, USA.
\*-------------------------------------------------------------------*/

package se.umu.cs.robotics.lokarria.statespace;

import java.util.Iterator;
import se.umu.cs.robotics.iteration.ArrayIterator;
import se.umu.cs.robotics.probabilitydistribution.AbstractSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.GenericSpaceDistribution;
import se.umu.cs.robotics.probabilitydistribution.ProbabilityDistribution;
import se.umu.cs.robotics.statespace.StateDimension;

/**
 *
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SensoryMotorDistribution extends AbstractSpaceDistribution<Double> {

    private final ProbabilityDistribution<Double>[] ddriveDimensions;
    private final ProbabilityDistribution<Double>[] laserDimensions;

    public SensoryMotorDistribution(SensoryMotorSpace space, ProbabilityDistribution<Double>[] ddriveDimensions, ProbabilityDistribution<Double>[] laserDimensions) {
        super(space);
        this.ddriveDimensions = ddriveDimensions;
        this.laserDimensions = laserDimensions;
    }

    @Override
    public ProbabilityDistribution<Double> getDimension(int dim) {
        if (dim < 2) {
            return ddriveDimensions == null ? null : ddriveDimensions[dim];
        } else {
            return laserDimensions == null ? null : laserDimensions[dim - 2];
        }
    }

    @Override
    public ProbabilityDistribution<Double> getDimension(StateDimension<Double> dim) {
        if (dim instanceof SpeedDimension) {
            return ddriveDimensions == null ? null : ddriveDimensions[stateSpace().getDimensionIndex(dim)];
        } else {
            return laserDimensions == null ? null : laserDimensions[stateSpace().getDimensionIndex(dim) - 2];
        }
    }

    @Override
    public Iterator<? extends ProbabilityDistribution<Double>> dimensions() {
        ProbabilityDistribution<Double>[] ddrive = ddriveDimensions == null ? new ProbabilityDistribution[2] : ddriveDimensions;
        ProbabilityDistribution<Double>[] laser = laserDimensions == null ? new ProbabilityDistribution[stateSpace().size()-2] : laserDimensions;
        return new ArrayIterator<>(ddrive, laser);
    }

    public GenericSpaceDistribution<Double> getMotorDistribution() {
        return ddriveDimensions == null ? null : new GenericSpaceDistribution<>(((SensoryMotorSpace) stateSpace()).getMotorSpace(), ddriveDimensions);
    }

    public GenericSpaceDistribution<Double> getSensoryDistribution() {
        return laserDimensions == null ? null : new GenericSpaceDistribution<>(((SensoryMotorSpace) stateSpace()).getSensorSpace(), laserDimensions);
    }

    public boolean isMotorDistribution() {
        return ddriveDimensions != null;
    }

    public boolean isSensoryDistribution() {
        return laserDimensions != null;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        ProbabilityDistribution<Double> pd = null;
        s.append('(');
        pd = getDimension(0);
        s.append(pd==null?'-':pd);
        for (int dim = 1; dim < stateSpace().size(); dim++) {
            pd = getDimension(dim);
            if (pd==null) {
                s.append('-');
            } else {
                s.append(';');
                s.append(pd);
            }
        }
        s.append(')');
        return s.toString();
    }
}
