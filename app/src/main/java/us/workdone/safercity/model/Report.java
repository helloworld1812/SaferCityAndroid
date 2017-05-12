package us.workdone.safercity.model;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Report {

    public final String title;
    public final String details;

    public final String location;
    public final Date time;

    public final boolean isDangerous;

    private static final String TITLE = "name";
    private static final String DETAILS = "eventDescription";

    private static final String LOCATION = "place";
    private static final String TIME = "time";

    private static final String IS_DANGEROUS = "dangerous";

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
                    .put(TITLE, title)
                    .put(DETAILS, details)
                    .put(LOCATION, location)
                    .put(IS_DANGEROUS, isDangerous)
                    .put(TIME, ISODateTimeFormat.dateTimeNoMillis().print(new DateTime(time)));
        } catch (JSONException e) {
            throw new IllegalStateException(e); // should not happen
        }
    }

    public static Report fromJSON(JSONObject data) {
        try {
            System.out.println(data.getString("time"));
            return new Report(
                    data.getString(TITLE),
                    data.getString(LOCATION),
                    ISODateTimeFormat.dateTimeParser().parseDateTime(data.getString(TIME)).toDate(),
                    data.getString(DETAILS)
            );
        } catch (JSONException                                                                                                                                                                                 e) {
            throw new RuntimeException(e);
        }
    }

}
