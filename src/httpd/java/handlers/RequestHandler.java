package handlers;

import handlers.helpers.DataForResponseBuilder;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;

/**
 * Created by max on 22.02.15.
 */
class RequestHandler {

    public static DataForResponseBuilder parsingHeadersHTTP(ByteBuffer attachment) throws IOException{

        attachment.flip();
        byte[] buffer = new byte[attachment.limit()];
        attachment.get(buffer).clear();
        String headers = new String(buffer);
        DataForResponseBuilder dataForResponse = new DataForResponseBuilder();

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
