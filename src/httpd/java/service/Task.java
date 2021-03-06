package service;

import handlers.AsyncRequestReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by max on 20.02.15.
 */
public class Task implements Runnable {

    private final AsynchronousSocketChannel connection;
    private final String rootDir;
    private static final Logger log = LoggerFactory.getLogger("service.Task");
    private static final int READ_BUFFER_SIZE = 500;

    public Task(AsynchronousSocketChannel connection, String rootDir) {
        this.connection = connection;
        this.rootDir = rootDir;
    }

    public void run() {
        try {
            log.debug("Was started request handling");
            ByteBuffer buffer = ByteBuffer.allocateDirect(READ_BUFFER_SIZE);
            connection.read(buffer, buffer, new AsyncRequestReceiver(connection, rootDir));

        } catch (Exception e) {
            log.debug("Error processing the request: " + e.getMessage());
        }
    }

}
