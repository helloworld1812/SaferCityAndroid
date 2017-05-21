package us.workdone.safercity.model;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Comment {

    public final Date timestamp;
    public final String body;

    private static final String BODY = "body";
    private static final String TIMESTAMP = "timestamp";

    public Comment(String body, Date timestamp) {
        this.body = body;
        this.timestamp = timestamp;
    }

    public JSONObject toJSON() {
        try {
            return new JSONObject()
                    .put(BODY, body)
                    .put(TIMESTAMP, ISODateTimeFormat.dateTimeNoMillis().print(new DateTime(timestamp)));
        } catch (JSONException e) {
            throw new IllegalStateException(e); // should not happen
        }
    }

    public static Comment fromJSON(JSONObject data) {
        try {
            return new Comment(
                    data.getString(BODY),
                    ISODateTimeFormat.dateTimeParser().parseDateTime(data.getString(TIMESTAMP)).toDate()
            );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
