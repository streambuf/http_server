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
        String pathToFile = data.getPathToFile();

        boolean isDirectory = false;
        String status;
        long fileSize = 0L;
        ByteBuffer file = ByteBuffer.allocate(0);
        String contentType = ContentType.getContentType(".html");

        if (method.equals("GET") || method.equals("HEAD")) {
            if (pathToFile.contains("../")) {
                status = StatusCode.FORBIDDEN;
                file = buildFile(status);
                fileSize = file.limit();
            } else {
                if (FileInformation.isDirectory(rootDir + pathToFile)) {
                    isDirectory = true;
                    if (!pathToFile.substring(pathToFile.length() - 1).equals("/")) {
                        pathToFile +="/";
                    }
                    pathToFile += DEFAULT_FILE;
                }
                fileSize = FileInformation.read(rootDir + pathToFile);
                if (fileSize != 0L) {
                    status = StatusCode.OK;
                    String extension = pathToFile.substring(pathToFile.lastIndexOf('.'));
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
        if (method.equals("HEAD")) {
            file = ByteBuffer.allocate(0);
        }

        ByteBuffer bufHeaders = ByteBuffer.wrap(headers.getBytes());
        ByteBuffer response = ByteBuffer.allocate(bufHeaders.limit() + file.limit());
        response.put(bufHeaders);
        byte[] bf = file.array();
        response.put(bf);
        response.flip();

        return new DataForClient(response, status, rootDir + pathToFile);
    }

    private String buildHeader(String status, long contentLength, String contentType) {
        String rn = "\r\n";
        @SuppressWarnings("StringBufferReplaceableByString")
        StringBuilder headers = new StringBuilder();
        headers.append("HTTP/1.1 ").append(status).append(rn);
        headers.append("Server: http_server").append(rn);
        headers.append("Date:").append(getServerDateTime()).append(rn);
        headers.append("Content-Type: ").append(contentType).append(rn);
        headers.append("Content-Length: ").append(contentLength).append(rn);
        headers.append("Connection: close").append(rn).append(rn);
        return headers.toString();
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


}
