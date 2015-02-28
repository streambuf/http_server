package handlers;


import handlers.helpers.DataForClient;
import handlers.helpers.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;

/**
 * Created by max on 28.02.15.
 */
public class AsyncResponseSender implements CompletionHandler<Integer, ByteBuffer> {

    private static final Logger log = LoggerFactory.getLogger("handlers.AsynchronousResponseSender");
    AsynchronousSocketChannel connection;
    DataForClient data;
    ByteBuffer buffer = ByteBuffer.allocate(256 * 1024);
    boolean fileExists = false;
    FileChannel ch;
    RandomAccessFile aFile;

    public AsyncResponseSender(AsynchronousSocketChannel connection, DataForClient data)
            throws IOException{

        this.connection = connection;
        this.data = data;

        if (data.getCode().equals(StatusCode.OK)) {
            fileExists = true;
            File file = new File(data.getPathToFile());
            aFile = new RandomAccessFile(file, "r");
            ch = aFile.getChannel();
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        try {
            if (attachment.hasRemaining()) {
                connection.write(buffer, buffer, this);
            }
            buffer.clear();
            if (fileExists && ch.read(buffer) > 0) {
                buffer.flip();
                connection.write(buffer, buffer, this);
            } else try {
                log.debug("Was ended request handling");
                connection.close();
                buffer.clear();
                ch.close();
                aFile.close();
            } catch (IOException e) {
                log.debug("Error closing the socket");
            }
        } catch (IOException e) {
            log.debug(e.getMessage());
        }

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        log.debug(exc.getMessage());
    }

}
