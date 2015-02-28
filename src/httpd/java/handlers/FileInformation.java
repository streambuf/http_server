package handlers;
import java.io.File;

/**
 * Created by max on 24.02.15.
 */

class FileInformation {

    public static long read(String pathToFile) {
        File file = new File(pathToFile);
        if (!file.exists()) {
            return 0L;
        }
        return file.length();
    }

    public static Boolean isDirectory(String pathToDirectory) {
        File file = new File(pathToDirectory);
        return file.isDirectory();
    }

}
