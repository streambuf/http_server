package handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by max on 22.02.15.
 */
public class ResponseSender {

    private final OutputStream out;
    private final String rootDir;

    public ResponseSender(OutputStream out, String rootDir) {
        this.out = out;
        this.rootDir = rootDir;
    }

    public void sendResponseToClient(DataForResponse data) throws Exception {
        String method = data.getMethod();
        String pathToFile = data.getPathToFile();
        String headers;
        String status;
        ByteBuffer file = FileReader.read(rootDir + pathToFile);
        switch(method) {
            case "GET":
                if (file == null) {
                    status = ResponseStatusCode.NOT_FOUND;
                    file = ByteBuffer.allocate(status.length());
                    file.put(status.getBytes());
                } else {
                    status = ResponseStatusCode.OK;
                }
                break;
            case "HEAD":

                break;
            default:
                status = ResponseStatusCode.METHOD_NOT_ALLOWED;
                file = ByteBuffer.allocate(status.length());
                file.put(status.getBytes());
        }

        headers = buildHeader(ResponseStatusCode.OK, file.limit());
        writeResponse(headers, file);
    }

    private String buildHeader(String status, int contentLength) {
        String rn = "\r\n";
        StringBuilder headers = new StringBuilder();
        headers.append("HTTP/1.1 " + status + rn);
        headers.append("Server: http_server" + rn);
        headers.append("Date:" + getServerDateTime() + rn);
        headers.append("Content-Type: text/html" + rn);
        headers.append("Content-Length: " + contentLength + rn);
        headers.append("Connection: close" + rn + rn);
        return headers.toString();
    }

    private void writeResponse(String response, ByteBuffer file) throws IOException {
        out.write(response.getBytes());
        out.write(file.array());
        out.flush();
    }

    private String getServerDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.format(calendar.getTime());
    }


}
