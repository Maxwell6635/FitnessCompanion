package my.com.saiboon.fitnesscompanion.Classes;

/**
 * Created by saiboon on 11/6/2015.
 */
public class Reminder {
    String ReminderID, RemindActivites, RemindRepeat, RemindDay, RemindTime;
    int RemindDate, RemindMonth, RemindYear;
    String UserID;
    boolean availability;

    public Reminder(){

    }

    public Reminder(String ReminderID, String UserID,boolean availability, String RemindActivites, String RemindRepeat,String RemindTime, String RemindDay,
                    int RemindDate, int RemindMonth, int RemindYear){
        this.ReminderID = ReminderID;
        this.UserID = UserID;
        this.availability=availability;
        this.RemindActivites = RemindActivites;
        this.RemindRepeat = RemindRepeat;
        this.RemindTime = RemindTime;
        this.RemindDay = RemindDay;
        this.RemindDate = RemindDate;
        this.RemindMonth = RemindMonth;
        this.RemindYear = RemindYear;
    }

    public String getReminderID() {return ReminderID;}
    public void setReminderID(String ReminderID) {this.ReminderID = ReminderID;}

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getRemindActivites() {return RemindActivites;}
    public void setRemindActivites(String RemindActivites) {this.RemindActivites = RemindActivites;}

    public String getRemindRepeat() { return RemindRepeat;}
    public void setRemindRepeat(String RemindRepeat) { this.RemindRepeat = RemindRepeat;}

    public String getRemindTime() {return RemindTime;}
    public void setRemindTime(String RemindTime) {this.RemindTime = RemindTime;}

    public String getRemindDay() { return RemindDay;}
    public void setRemindDay(String RemindDay) { this.RemindDay = RemindDay;}

    public int getRemindDate() { return RemindDate;}
    public void setRemindDate(int RemindDate) { this.RemindDate = RemindDate;}

    public int getRemindMonth() {return RemindMonth;}
    public void setRemindMonth(int RemindMonth) {this.RemindMonth = RemindMonth;}

    public int getRemindYear() { return RemindYear;}
    public void setRemindYear(int RemindYear) { this.RemindYear = RemindYear;}


}
