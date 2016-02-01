package my.com.taruc.fitnesscompanion.Classes;

import android.graphics.Bitmap;

import java.sql.Blob;

/**
 * Created by saiboon on 10/6/2015.
 */
public class Event {

    private String EventID, url , title, location, eventDate;
    private Bitmap banner;
    private DateTime created_at, updated_at;

    public Event(){}

    public Event(String eventID, Bitmap banner, String url, String title, String location, String eventDate,
                 DateTime created_at, DateTime updated_at) {
        EventID = eventID;
        this.banner = banner;
        this.url = url;
        this.title = title;
        this.location = location;
        this.eventDate = eventDate;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
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
