package us.workdone.safercity.model;

import java.util.Date;

public class Report {

    public final String title;
    public final String details;

    public final String location;
    public final Date time;

    public final boolean isDangerous;

    public Report(String location, Date time, String title, String details) {
        this.location = location;
        this.time = time;
        this.title = title;
        this.details = details;
        this.isDangerous = true;
    }

}
