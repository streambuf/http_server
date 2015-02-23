package handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by max on 22.02.15.
 */
public class ResponseSender {

    private final OutputStream out;

    public ResponseSender(OutputStream out) {
        this.out = out;
    }

    public void sendResponseToClient(DataForResponse data) throws IOException {
        String method = data.getMethod();
        String pathToFile = data.getPathToFile();
        String headers;

        switch(method) {
            case "GET":
                headers = buildHeader(ResponseStatusCode.OK);
                writeResponse(headers + ResponseStatusCode.OK);
                break;
            case "HEAD":
                break;
            default:
                headers = buildHeader(ResponseStatusCode.METHOD_NOT_ALLOWED);
                writeResponse(headers + ResponseStatusCode.METHOD_NOT_ALLOWED);
        }

    }

    private String buildHeader(String status) {
        ResponseStatusCode statusCode = new ResponseStatusCode();
        String rn = "\r\n";
        StringBuilder headers = new StringBuilder();
        headers.append("HTTP/1.1 " + status + rn);
        headers.append("Server: http_server" + rn);
        headers.append("Date:" + getServerDateTime() + rn);
        headers.append("Content-Type: text/html" + rn);
        headers.append("Content-Length: " + status.length() + rn);
        headers.append("Connection: close" + rn + rn);
        return headers.toString() + status;
    }

    private void writeResponse(String response) throws IOException {
        out.write(response.getBytes());
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