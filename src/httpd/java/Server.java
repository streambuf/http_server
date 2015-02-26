import ch.qos.logback.classic.Level;
import com.beust.jcommander.JCommander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ExecutorTaskService;
import service.Task;
import utils.commandLine.CommandLineParser;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by max on 19.02.15.
 */
@SuppressWarnings("WeakerAccess")
public class Server {

    private static final int PORT = 8080;
    private static final int NUM_THREADS = 30;
    private static String rootDir;
    private static String debug;
    private static final Logger log = LoggerFactory.getLogger("Server");

    public static void main(String[] args) throws Exception {
        configureServer(args);
        ExecutorTaskService threadPool = new ExecutorTaskService(NUM_THREADS);

        final AsynchronousServerSocketChannel listener =
                AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(PORT));

        listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel ch, Void att) {
                try {
                    listener.accept(null, this);
                    ch.setOption(StandardSocketOptions.TCP_NODELAY, true);
                    threadPool.execute(new Task(ch, rootDir));
                } catch (Exception e) {
                    log.debug(e.getMessage());
                    try {
                        ch.close();
                    } catch (IOException ee) {
                        log.debug(ee.getMessage());
                    }
                }
            }

            @Override
            public void failed(Throwable exc, Void att) {
                log.debug("failed: " + exc.getMessage());
            }
        });

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

    private static void setLoggingLevel(ch.qos.logback.classic.Level level) {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
    }

}
