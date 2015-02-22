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

    public static void sendResponseToClient(OutputStream out, String resp) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: http_server\r\n" +
                "Date:" + getServerDateTime() + "\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + resp.length() + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + resp;
        out.write(result.getBytes());
        out.flush();
    }

    private static String getServerDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.format(calendar.getTime());
    }
}
