package handlers;

import handlers.helpers.ContentType;
import handlers.helpers.DataForResponse;
import handlers.helpers.StatusCode;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by max on 22.02.15.
 */
public class ResponseBuilder {

    private final String rootDir;

    private static final String DEFAULT_FILE = "index.html";
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final String TIMEZONE = "GMT";

    public ResponseBuilder(String rootDir) {
        this.rootDir = rootDir;
    }

    public ByteBuffer sendResponseToClient(DataForResponse data) throws Exception {
        String method = data.getMethod();
        String pathToFile = data.getPathToFile();

        boolean isDirectory = false;
        String status;
        ByteBuffer file;
        String contentType = ContentType.getContentType(".html");

        if (method.equals("GET") || method.equals("HEAD")) {
            if (pathToFile.contains("../")) {
                status = StatusCode.FORBIDDEN;
                file = buildFile(status);
            } else {
                if (FileReader.isDirectory(rootDir + pathToFile)) {
                    isDirectory = true;
                    if (!pathToFile.substring(pathToFile.length() - 1).equals("/")) {
                        pathToFile +="/";
                    }
                    pathToFile += DEFAULT_FILE;
                }
                file = FileReader.read(rootDir + pathToFile);
                if (file != null) {
                    status = StatusCode.OK;
                    String extension = pathToFile.substring(pathToFile.lastIndexOf('.'));
                    contentType = ContentType.getContentType(extension);
                } else {
                    status = isDirectory ? StatusCode.FORBIDDEN : StatusCode.NOT_FOUND;
                    file = buildFile(status);
                }
            }

        } else  {
            status = StatusCode.METHOD_NOT_ALLOWED;
            file = buildFile(status);
        }

        String headers = buildHeader(status, file.limit(), contentType);
        if (method.equals("HEAD")) {
            file = ByteBuffer.allocate(0);
        }

        ByteBuffer bufHeaders = ByteBuffer.wrap(headers.getBytes());
        ByteBuffer response = ByteBuffer.allocate(bufHeaders.limit() + file.limit());
        response.put(bufHeaders);
        byte[] bf = file.array();
        response.put(bf);
        response.flip();

        return response;
    }

    private String buildHeader(String status, int contentLength, String contentType) {
        String rn = "\r\n";
        StringBuilder headers = new StringBuilder();
        headers.append("HTTP/1.1 " + status + rn);
        headers.append("Server: http_server" + rn);
        headers.append("Date:" + getServerDateTime() + rn);
        headers.append("Content-Type: " + contentType + rn);
        headers.append("Content-Length: " + contentLength + rn);
        headers.append("Connection: close" + rn + rn);
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
