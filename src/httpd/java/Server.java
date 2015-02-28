import ch.qos.logback.classic.Level;
import com.beust.jcommander.JCommander;
import handlers.AsyncSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ExecutorTaskService;
import utils.commandLine.CommandLineParser;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

/**
 * Created by max on 19.02.15.
 */
@SuppressWarnings("WeakerAccess")
public class Server {

    private static final int PORT = 8080;
    private static int poolSize;
    private static String rootDir;
    private static AsynchronousServerSocketChannel listener;
    private static final Logger log = LoggerFactory.getLogger("Server");

    public static void main(String[] args) {

        configureServer(args);

        ExecutorTaskService threadPool = new ExecutorTaskService(poolSize);

        listener.accept(null, new AsyncSocketListener(listener, rootDir, threadPool));

    }

    private static void configureServer(String[] args) {
        CommandLineParser jct = new CommandLineParser();
        try {
            new JCommander(jct, args);
            rootDir = jct.getRootDir();
            poolSize = jct.getNumCPU();
            String debug = jct.getDebug();

            Level logLevel = (debug.equals("true")) ? Level.DEBUG : Level.ERROR;
            setLoggingLevel(logLevel);

            log.debug("Debug mode: " + debug);
            log.debug("NCPU: " + poolSize);
            log.debug("ROOTDIR: " + rootDir);

            listener = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(PORT));

        } catch (Exception e) {
            log.error(e.getMessage());
            System.exit(-1);
        }
    }

    private static void setLoggingLevel(ch.qos.logback.classic.Level level) {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
    }

}
