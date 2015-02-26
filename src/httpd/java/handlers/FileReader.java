package handlers;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by max on 24.02.15.
 */

public class FileReader {

    public static ByteBuffer read(String pathToFile) throws Exception {
        File file = new File(pathToFile);

        if (!file.exists()) {
            return null;
        }
        RandomAccessFile aFile = new RandomAccessFile(pathToFile, "r");
        FileChannel inChannel = aFile.getChannel();
        MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        buffer.load();
        ByteBuffer result = ByteBuffer.allocate(buffer.capacity());
        result.put(buffer);
        buffer.clear();
        inChannel.close();
        aFile.close();
        return result;
    }

    public static Boolean isDirectory(String pathToDirectory) {
        File file = new File(pathToDirectory);
        return file.isDirectory();
    }

}
