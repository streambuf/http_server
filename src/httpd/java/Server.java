import ch.qos.logback.classic.Level;
import com.beust.jcommander.JCommander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.commandLine.CommandLineParser;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by max on 19.02.15.
 */
public class Server {

    private static String rootDir;
    private static String debug;
    private static Logger log = LoggerFactory.getLogger("Server");

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
            debug = jct.getDebug();

            Level logLevel = (debug.equals("true")) ? Level.DEBUG : Level.ERROR;
            setLoggingLevel(logLevel);

            log.debug("Debug mode: " + debug);
            log.debug("NCPU: " + jct.getNumCPU());
            log.debug("ROOTDIR: " + rootDir);

        } catch (Exception e) {
            log.error(e.getMessage());
            System.exit(-1);
        }
    }

    public static void setLoggingLevel(ch.qos.logback.classic.Level level) {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
    }

}
