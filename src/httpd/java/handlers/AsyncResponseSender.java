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
class AsyncResponseSender implements CompletionHandler<Integer, ByteBuffer> {

    private static final Logger log = LoggerFactory.getLogger("handlers.AsyncResponseSender");
    private static final int BUF_SIZE = 256 * 1024;
    private static final int SMALL_BUF_SIZE = 100;
    private static final int BIG_FILE = 1024 * 1024;
    private ByteBuffer buffer;

    private final AsynchronousSocketChannel connection;
    private boolean fileExists = false;
    private FileChannel ch;
    private RandomAccessFile aFile;

    public AsyncResponseSender(AsynchronousSocketChannel connection, DataForClient data)
            throws IOException{

        this.connection = connection;

        if (data.getCode().equals(StatusCode.OK) && data.getMethod().equals("GET")) {
            fileExists = true;
            File file = new File(data.getPathToFile());
            aFile = new RandomAccessFile(file, "r");
            ch = aFile.getChannel();
            int bufferSize = data.getFileSize() < BIG_FILE ? BUF_SIZE : SMALL_BUF_SIZE;
            buffer = ByteBuffer.allocate(bufferSize);
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        try {
            if (attachment.hasRemaining()) {
                connection.write(buffer, buffer, this);
            }
            if (fileExists) {
                buffer.clear();
                if (ch.read(buffer) > 0) {
                    buffer.flip();
                    connection.write(buffer, buffer, this);
                }
            }
            try {
                log.debug("Was ended request handling");
                connection.close();
                if (fileExists) {
                    buffer.clear();
                    ch.close();
                    aFile.close();
                }
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
