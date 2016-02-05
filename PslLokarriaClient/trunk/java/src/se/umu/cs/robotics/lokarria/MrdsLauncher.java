
package se.umu.cs.robotics.lokarria;

import java.io.IOException;
import org.apache.log4j.Logger;
import se.umu.cs.robotics.lokarria.core.LokarriaPropertyLoader;
import se.umu.cs.robotics.utils.StringTools;

/**
 *
 * @author Erik Billing <erik.billing@his.se>
 */
public class MrdsLauncher {

    static Logger logger = Logger.getLogger(MrdsLauncher.class);
    private final ProcessBuilder pb;
    private Process process;
    
    public MrdsLauncher() {
        pb = new ProcessBuilder(LokarriaPropertyLoader.getValue("mrds.exe").split(" "));
    }
    
    public MrdsLauncher(String configName, int configId) {
        pb = new ProcessBuilder("c:\\MRDS4\\bin\\DssHost32.exe", "-p:50000", "-t:50001", 
                "-m:c:\\mrds4\\store\\environments\\Apartment-topview.Manifest.xml", 
                String.format("-m:c:\\mrds4\\store\\applications\\robulab\\simulated\\simulation\\Application.manifest.%s%d.xml",configName,configId), 
                "-m:c:\\mrds4\\store\\applications\\robulab\\simulated\\monitoring\\Application.manifest.xml", 
                "-m:c:\\mrds4\\store\\applications\\robulab\\simulated\\lokarria\\Application.cs.manifest.xml");
    }
    
    public Process start() throws IOException, InterruptedException {
        if (process != null && process.isAlive()) return process;
        System.out.println("Starting mrds! " + StringTools.join(pb.command(), " "));
        process = pb.start();
        return process;
    }

    public void stop() {
        if (process != null && process.isAlive()) process.destroy();
    }
    
}
