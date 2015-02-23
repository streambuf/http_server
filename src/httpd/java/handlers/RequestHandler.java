package handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by max on 22.02.15.
 */
public class RequestHandler {

    public static DataForResponse parsingHeadersHTTP(InputStream in) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder headers = new StringBuilder();
        String header;
        while ((header = reader.readLine()) != null && header.trim().length() != 0) {
            headers.append(header + "\r\n");
        }

        DataForResponse dataForResponse = new DataForResponse();
        // parrse method
        String method = headers.substring(0, headers.indexOf(" "));
        dataForResponse.setMethod(method);
        // parse path to file
        String pathToFile = headers.substring(headers.indexOf(" ") + 1);
        pathToFile = pathToFile.substring(0, pathToFile.indexOf(" "));
        dataForResponse.setPathToFile(pathToFile);

        return dataForResponse;
    }



}
