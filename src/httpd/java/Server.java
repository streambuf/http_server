import com.beust.jcommander.JCommander;
import utils.CommandLineParser;

/**
 * Created by max on 19.02.15.
 */
public class Server {

    public static void main(String[] args) {
        configureServer(args);
    }

    private static void configureServer(String[] args) {
        CommandLineParser jct = new CommandLineParser();
        try {
            new JCommander(jct, args);
            System.out.println("NCPU: " + jct.getNumCPU());
            System.out.println("ROOTDIR: " + jct.getRootDir());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
