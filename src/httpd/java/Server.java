import com.beust.jcommander.JCommander;
import utils.CommandLineParser;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by max on 19.02.15.
 */
public class Server {
    private static String rootDir;

    public static void main(String[] args) throws Throwable {

        configureServer(args);
        ExecutorTaskService threadPool = new ExecutorTaskService(20);
        ServerSocket ss = new ServerSocket(8080);

        while (true) {
            Socket s = ss.accept();
            threadPool.execute(new HandlerSocket(s, rootDir));
        }

    }

    private static void configureServer(String[] args) {
        CommandLineParser jct = new CommandLineParser();
        try {
            new JCommander(jct, args);
            rootDir = jct.getRootDir();
            System.out.println("NCPU: " + jct.getNumCPU());
            System.out.println("ROOTDIR: " + rootDir);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
