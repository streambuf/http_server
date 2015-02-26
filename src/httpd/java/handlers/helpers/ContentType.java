package handlers.helpers;

import java.util.HashMap;

/**
 * Created by max on 24.02.15.
 */
public class ContentType {

    private static final HashMap<String, String> type = new HashMap<>();

    static {
        type.put(".html", "text/html");
        type.put(".css", "text/css");
        type.put(".js", "application/javascript");
        type.put(".jpg", "image/jpeg");
        type.put(".jpeg", "image/jpeg");
        type.put(".png", "image/png");
        type.put(".gif", "image/gif");
        type.put(".swf", "application/x-shockwave-flash");
    }

    public static String getContentType(String fileExtension) {
        return type.get(fileExtension);
    }
}
