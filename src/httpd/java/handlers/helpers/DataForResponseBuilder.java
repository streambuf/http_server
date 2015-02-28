package handlers.helpers;

/**
 * Created by max on 23.02.15.
 */

public class DataForResponseBuilder {

    private String pathToFile;
    private String method;

    public String getPathToFile() {
        return pathToFile;
    }

    public String getMethod() {
        return method;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
