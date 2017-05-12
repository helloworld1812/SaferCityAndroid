package us.workdone.safercity.model;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import us.workdone.safercity.GlobalUtils;

public class Report {

    public final String title;
    public final String details;

    public final String location;
    public final Date time;

    public final boolean isDangerous;

    public Report(String title, String location, Date time, String details) {
        this.location = location;
        this.time = time;
        this.title = title;
        this.details = details;
        this.isDangerous = false;
    }

    public JSONObject toJSON() {
        try {
            return new JSONObject()
                    .put("title", title)
                    .put("details", details)
                    .put("location", location)
                    .put("is_dangerous", isDangerous)
                    .put("time", ISODateTimeFormat.dateTimeNoMillis().print(new DateTime(time)));
        } catch (JSONException e) {
            throw new IllegalStateException(e); // should not happen
        }
    }

    public static Report fromJSON(JSONObject data) {
        try {
            System.out.println(data.getString("time"));
            return new Report(
                    data.getString("title"),
                    data.getString("location"),
                    ISODateTimeFormat.dateTime().parseDateTime(data.getString("time")).toDate(),
                    data.getString("details")
            );
        } catch (JSONException                                                                                                                                                                                 e) {
            throw new RuntimeException(e);
        }
    }

}
