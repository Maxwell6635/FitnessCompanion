package my.com.taruc.fitnesscompanion.Classes;

import android.graphics.Bitmap;

import java.sql.Blob;

/**
 * Created by saiboon on 10/6/2015.
 */
public class Event {

    private String EventID;
    private Bitmap banner;
    private String url;
    private DateTime created_at, updated_at;

    public Event(){}

    public Event(String eventID, Bitmap banner, String url, DateTime created_at, DateTime updated_at) {
        EventID = eventID;
        this.banner = banner;
        this.url = url;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getEventID() {
        return EventID;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public Bitmap getBanner() {
        return banner;
    }

    public void setBanner(Bitmap banner) {
        this.banner = banner;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(DateTime created_at) {
        this.created_at = created_at;
    }

    public DateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(DateTime updated_at) {
        this.updated_at = updated_at;
    }
}
