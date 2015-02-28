package handlers.helpers;

import java.nio.ByteBuffer;

/**
 * Created by max on 28.02.15.
 */
public class DataForClient {

    private final ByteBuffer response;
    private final String code;
    private final String pathToFile;


    public DataForClient(ByteBuffer response, String code, String pathToFile) {
        this.response = response;
        this.code = code;
        this.pathToFile = pathToFile;
    }

    public ByteBuffer getResponse() {
        return response;
    }

    public String getCode() {
        return code;
    }

    public String getPathToFile() {
        return pathToFile;
    }

}
