package barqsoft.footballscores.util;

import com.google.gson.JsonElement;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Iterator;

public class JsonUtil {
// ------------------------------ FIELDS ------------------------------

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm");

// -------------------------- STATIC METHODS --------------------------

    public static String toDate(JsonElement element, String path) {
        String date = toString(element, path);
        if (!"".equals(date)) {
            date = DATE_FORMATTER.print(DateTime.parse(date).withZone(DateTimeZone.getDefault()));
        }
        return date;
    }

    public static Integer toInteger(JsonElement element, String path) {
        return Integer.parseInt(toString(element, path));
    }

    public static Iterator<JsonElement> toIterator(JsonElement element, String path) {
        return element.getAsJsonObject().get(path).getAsJsonArray().iterator();
    }

    public static String toString(JsonElement element, String path) {
        JsonElement e = element;
        for (String key : path.split("/")) {
            if (e != null) {
                e = e.getAsJsonObject().get(key);
            }
        }
        return e == null ? "" : e.getAsString().trim();
    }

    public static String toTime(JsonElement element, String path) {
        String time = toString(element, path);
        if (!"".equals(time)) {
            time = TIME_FORMATTER.print(DateTime.parse(time).withZone(DateTimeZone.getDefault()));
        }
        return time;
    }
}
