package handlers;

import handlers.helpers.ContentType;
import handlers.helpers.DataForClient;
import handlers.helpers.DataForResponseBuilder;
import handlers.helpers.StatusCode;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by max on 22.02.15.
 */
class ResponseBuilder {

    private final String rootDir;

    private static final String DEFAULT_FILE = "index.html";
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final String TIMEZONE = "GMT";

    public ResponseBuilder(String rootDir) {
        this.rootDir = rootDir;
    }

    public DataForClient getResponseForClient(DataForResponseBuilder data) {
        String method = data.getMethod();
        StringBuilder pathToFile = new StringBuilder(rootDir + data.getPathToFile());

        String status = StatusCode.OK;
        long fileSize = 0L;
        ByteBuffer file = ByteBuffer.allocate(0);
        String contentType = ContentType.getContentType(".html");

        if (method.equals("GET") || method.equals("HEAD")) {
            if (pathToFile.toString().contains("../")) {
                status = StatusCode.FORBIDDEN;
                file = buildFile(status);
                fileSize = file.limit();
            } else {
                boolean isDirectory = checkDirectory(pathToFile);
                fileSize = FileInformation.read(pathToFile.toString());
                if (fileSize != 0L) {
                    String extension = pathToFile.substring(pathToFile.toString().lastIndexOf('.'));
                    contentType = ContentType.getContentType(extension);
                } else {
                    status = isDirectory ? StatusCode.FORBIDDEN : StatusCode.NOT_FOUND;
                    file = buildFile(status);
                    fileSize = file.limit();
                }
            }

        } else  {
            status = StatusCode.METHOD_NOT_ALLOWED;
            file = buildFile(status);
        }

        String headers = buildHeader(status, fileSize, contentType);
        ByteBuffer response = mergeBuffers(ByteBuffer.wrap(headers.getBytes()), file);
        return new DataForClient(response, status, pathToFile.toString(), fileSize, method);
    }

    private String buildHeader(String status, long contentLength, String contentType) {
        String rn = "\r\n";
        return
            "HTTP/1.1 "             + status + rn +
            "Server: http_server"   + rn +
            "Date:"                 + getServerDateTime() +rn +
            "Content-Type: "        + contentType + rn +
            "Content-Length: "      + contentLength + rn +
            "Connection: close"     + rn + rn;

    }

    private ByteBuffer buildFile(String status) {
        ByteBuffer file = ByteBuffer.allocate(status.length());
        file.put(status.getBytes());
        return file;
    }

    private String getServerDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                DATE_FORMAT, Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE));

        return dateFormat.format(calendar.getTime());
    }

    private boolean checkDirectory(StringBuilder pathToFile) {
        if (FileInformation.isDirectory(pathToFile.toString())) {
            if (!pathToFile.substring(pathToFile.length() - 1).equals("/")) {
                pathToFile.append("/");
            }
            pathToFile.append(DEFAULT_FILE);
            return true;
        }
        return false;
    }

    private ByteBuffer mergeBuffers(ByteBuffer b1, ByteBuffer b2) {
        ByteBuffer result = ByteBuffer.allocate(b1.limit() + b2.limit());
        result.put(b1);
        byte[] bf = b2.array();
        result.put(bf);
        result.flip();
        return result;
    }


}
