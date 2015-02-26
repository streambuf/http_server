package service;

import handlers.helpers.DataForResponse;
import handlers.RequestHandler;
import handlers.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by max on 20.02.15.
 */
public class Task implements Runnable {
    private final AsynchronousSocketChannel connection;
    private final String rootDir;

    private static final Logger log = LoggerFactory.getLogger("service.Task");

    public Task(AsynchronousSocketChannel connection, String rootDir) throws Exception {
        this.connection = connection;
        this.rootDir = rootDir;
    }

    public void run() {
        try {
            log.debug("Was started request handling");
            ByteBuffer buffer = ByteBuffer.allocateDirect(500);
            connection.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    try {
                        DataForResponse data = RequestHandler.parsingHeadersHTTP(attachment);
                        ResponseBuilder sender = new ResponseBuilder(rootDir);
                        ByteBuffer response = sender.sendResponseToClient(data);

                        connection.write(response, response, new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer attachment) {
                                if (attachment.hasRemaining()) {
                                    connection.write(response, response, this);
                                } else try {
                                    connection.close();
                                } catch (IOException e) {
                                    log.debug("Error closing the socket");
                                }
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer attachment) {
                                log.debug(exc.getMessage());
                            }
                        });

                    } catch (Exception e) {
                        log.debug("parsingHeadersHTTP exception");
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    log.debug(exc.getMessage());
                }
            });
            log.debug("Was ended request handling");
        } catch (Exception e) {
            log.debug("Error processing the request: " + e.getMessage());
        }
    }

}
