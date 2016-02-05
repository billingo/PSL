
package se.umu.cs.robotics.lokarria.test;

import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.umu.cs.robotics.lokarria.MrdsLauncher;

/**
 *
 * @author Erik Billing <erik.billing@his.se>
 */
public class MrdsLauncherTest {
    
    @Test
    public void start() throws IOException, InterruptedException {
        MrdsLauncher mrds = new MrdsLauncher();
        mrds.start();
    }
}
