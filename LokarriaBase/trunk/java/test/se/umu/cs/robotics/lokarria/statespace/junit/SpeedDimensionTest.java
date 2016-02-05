
package se.umu.cs.robotics.lokarria.statespace.junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.lokarria.statespace.AngularSpeedDimension;
import se.umu.cs.robotics.lokarria.statespace.DifferentialDriveSpace;
import se.umu.cs.robotics.lokarria.statespace.LinearSpeedDimension;
import static org.junit.Assert.*;

/**
 *
 * @author Erik Billing <billing@cs.umu.se>
 */
public class SpeedDimensionTest {

    final DifferentialDriveSpace space = new DifferentialDriveSpace(30, 20);
    final AngularSpeedDimension angularDim = space.getAngularDimension();
    final LinearSpeedDimension linearDim = space.getLinearDimension();

    public SpeedDimensionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void canReturnIndex() {
        assertEquals(0, angularDim.getIndex(-1.5));
        assertEquals(15, angularDim.getIndex(0d));
        assertEquals(29,angularDim.getIndex(1.5));

        assertEquals(0, linearDim.getIndex(-1d));
        assertEquals(10, linearDim.getIndex(0d));
        assertEquals(19,linearDim.getIndex(1d));
    }
}
