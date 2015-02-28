package handlers.helpers;

import java.nio.ByteBuffer;

/**
 * Created by max on 28.02.15.
 */
public class DataForClient {

    ByteBuffer response;
    String code;
    String pathToFile;


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

    public void setCode(String code) {
        this.code = code;
    }

}
