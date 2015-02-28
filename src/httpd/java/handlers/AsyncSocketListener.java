package handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ExecutorTaskService;
import service.Task;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by max on 28.02.15.
 */
public class AsyncSocketListener implements CompletionHandler<AsynchronousSocketChannel, Void> {

    private final AsynchronousServerSocketChannel listener;
    private final String rootDir;
    private final ExecutorTaskService executorTaskService;
    private static final Logger log = LoggerFactory.getLogger("handlers.AsyncSocketListener");

    public AsyncSocketListener(AsynchronousServerSocketChannel listener,
                               String rootDir, ExecutorTaskService executorTaskService) {
        this.listener = listener;
        this.rootDir = rootDir;
        this.executorTaskService = executorTaskService;
    }

    @Override
    public void completed(AsynchronousSocketChannel ch, Void att) {
        try {
            listener.accept(null, this);
            ch.setOption(StandardSocketOptions.TCP_NODELAY, true);
            ch.setOption(StandardSocketOptions.SO_SNDBUF, 1024*1024);
            ch.setOption(StandardSocketOptions.SO_RCVBUF, 1024*1024);

            executorTaskService.execute(new Task(ch, rootDir));
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
}
