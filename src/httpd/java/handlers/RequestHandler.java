package handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by max on 22.02.15.
 */
public class RequestHandler {

    public static String parsingHeadersHTTP(InputStream in) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null && line.trim().length() != 0) {
            result.append(line + "\r\n");
        }
        return result.toString();
    }

}
