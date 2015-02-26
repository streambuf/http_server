package service;

import handlers.helpers.DataForResponse;
import handlers.RequestHandler;
import handlers.ResponseSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import java.net.Socket;

/**
 * Created by max on 20.02.15.
 */
public class Task implements Runnable {
    private final Socket socket;
    private final String rootDir;
    private final InputStream in;
    private final OutputStream out;
    private static final Logger log = LoggerFactory.getLogger("service.Task");

    public Task(Socket socket, String rootDir) throws Exception {
        this.socket = socket;
        this.rootDir = rootDir;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    public void run() {
        try {
            log.debug("Was started request handling");
            DataForResponse data = RequestHandler.parsingHeadersHTTP(in);
            ResponseSender sender = new ResponseSender(out, rootDir);
            sender.sendResponseToClient(data);
            log.debug("Was ended request handling");
        } catch (Exception e) {
            log.debug("Error processing the request: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                log.debug("Error closing the socket");
            }
        }
    }

}
