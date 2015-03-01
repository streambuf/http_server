package handlers.helpers;

import java.nio.ByteBuffer;

/**
 * Created by max on 28.02.15.
 */
public class DataForClient {

    private final ByteBuffer response;
    private final String code;
    private final String pathToFile;
    private final long fileSize;
    private final String method;


    public DataForClient(ByteBuffer response, String code,
                         String pathToFile, long fileSize, String method) {
        this.response = response;
        this.code = code;
        this.pathToFile = pathToFile;
        this.fileSize = fileSize;
        this.method = method;
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

    public long getFileSize() {
        return fileSize;
    }

    public String getMethod() {
        return method;
    }

}
