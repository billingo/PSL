/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.cs.robotics.probabilitydistribution.junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.statespace.CharDimension;
import static org.junit.Assert.*;

/**
 *
 * @author billing
 */
public class CharDistributionTest {

    public CharDistributionTest() {
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
    public void testChars() {
        CharDimension d = new CharDimension();
        System.out.print("Testing alphabet: ");
        String a = CharDimension.ALPHABET.toLowerCase();
        for (int i = 0; i < a.length(); i++) {
            System.out.print(a.charAt(i));
            assertEquals(i, d.getIndex(a.charAt(i)));
        }
        System.out.println("");
    }
}
