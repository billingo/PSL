package se.umu.cs.robotics.collections.sort.junit;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.collections.sort.SortedDouble;
import se.umu.cs.robotics.collections.sort.Sorter;
import static org.junit.Assert.*;

/**
 *
 * @author billing
 */
public class SorterTest {

    public static final double[] doubles = {1, 0, 3, 5, 9, 3, 2.5, 2, 4};

    public SorterTest() {
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
    public void testSorter() {
        Sorter<SortedDouble> sorted = Sorter.sort(doubles);
        Iterator<SortedDouble> i = sorted.iterator();

        SortedDouble e = i.next();
        assertEquals(0d,e.value(),0.0001);
        assertEquals(1, e.originalPosition());
        e = i.next();
        assertEquals(1d,e.value(),0.0001);
        assertEquals(0, e.originalPosition());
        e = i.next();
        assertEquals(2d,e.value(),0.0001);
        assertEquals(7, e.originalPosition());
    }
}
