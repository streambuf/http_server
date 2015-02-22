package service;

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
    private Socket socket;
    private String rootDir;
    private InputStream in;
    private OutputStream out;
    private static Logger log = LoggerFactory.getLogger("service.Task");

    public Task(Socket socket, String rootDir) throws Exception {
        this.socket = socket;
        this.rootDir = rootDir;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    public void run() {
        try {
            log.debug("Was started request handling");
            String taskToResponse = RequestHandler.parsingHeadersHTTP(in);
            ResponseSender.sendResponseToClient(out, taskToResponse);
            log.debug("Was ended request handling");
        } catch (Exception e) {
            log.debug("Error processing the request");
        }
    }

}
