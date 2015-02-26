package handlers;

import handlers.helpers.DataForResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

/**
 * Created by max on 22.02.15.
 */
public class RequestHandler {

    private static final Logger log = LoggerFactory.getLogger("handlers.RequestHandler");
    private static String headers;

    public static DataForResponse parsingHeadersHTTP(ByteBuffer attachment) throws IOException{
        attachment.flip();
        byte[] buffer = new byte[attachment.limit()];
        attachment.get(buffer).clear();
        String headers = new String(buffer);
        DataForResponse dataForResponse = new DataForResponse();

        // parrse method
        String method = headers.substring(0, headers.indexOf(" "));
        dataForResponse.setMethod(method);
        // parse path to file
        String pathToFile = headers.substring(headers.indexOf(" ") + 1);
        pathToFile = pathToFile.substring(0, pathToFile.indexOf(" "));
        int pos;
        if ((pos = pathToFile.indexOf("?")) != -1) {
            pathToFile = pathToFile.substring(0, pos);
        }
        pathToFile = URLDecoder.decode(pathToFile, "UTF-8");
        dataForResponse.setPathToFile(pathToFile);

        return dataForResponse;
    }



}
