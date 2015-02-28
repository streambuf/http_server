package handlers;

import handlers.helpers.DataForClient;
import handlers.helpers.DataForResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by max on 28.02.15.
 */
public class AsyncRequestReceiver implements CompletionHandler<Integer, ByteBuffer> {

    private static final Logger log = LoggerFactory.getLogger("handlers.AsynchronousRequestReceiver");
    AsynchronousSocketChannel connection;
    String rootDir;

    public AsyncRequestReceiver(AsynchronousSocketChannel connection, String rootDir) {
        this.connection = connection;
        this.rootDir = rootDir;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        try {
            DataForResponseBuilder data = RequestHandler.parsingHeadersHTTP(attachment);
            ResponseBuilder sender = new ResponseBuilder(rootDir);
            DataForClient dataForClient = sender.getResponseToClient(data);
            ByteBuffer buffer = (dataForClient.getResponse());
            connection.write(buffer, buffer,
                    new AsyncResponseSender(connection, dataForClient));

        } catch (Exception e) {
            log.debug("parsingHeadersHTTP exception");
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        log.debug(exc.getMessage());
    }
}
