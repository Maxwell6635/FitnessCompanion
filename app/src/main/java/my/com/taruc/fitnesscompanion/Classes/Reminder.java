package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by saiboon on 11/6/2015.
 */
public class Reminder {

    private String ReminderID, UserID;
    private boolean availability;
    private String ActivitesPlanID, RemindRepeat, RemindTime, RemindDay;
    private int RemindDate;
    private DateTime CreatedAt, UpdatedAt;

    public Reminder(){}

    public Reminder(String reminderID, String userID, boolean availability, String activitesPlanID, String remindRepeat, String remindTime, String remindDay,
                    int remindDate, DateTime createdAt, DateTime updatedAt) {
        ReminderID = reminderID;
        UserID = userID;
        this.availability = availability;
        ActivitesPlanID = activitesPlanID;
        RemindRepeat = remindRepeat;
        RemindTime = remindTime;
        RemindDay = remindDay;
        RemindDate = remindDate;
        CreatedAt = createdAt;
        UpdatedAt = updatedAt;
    }

    public String getReminderID() {
        return ReminderID;
    }

    public String getActivitesPlanID() {
        return ActivitesPlanID;
    }

    public String getRemindRepeat() {
        return RemindRepeat;
    }

    public String getRemindDay() {
        return RemindDay;
    }

    public String getRemindTime() {
        return RemindTime;
    }

    public String getUserID() {
        return UserID;
    }

    public int getRemindDate() {
        return RemindDate;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setReminderID(String reminderID) {
        ReminderID = reminderID;
    }

    public void setActivitesPlanID(String activitesPlanID) {
        ActivitesPlanID = activitesPlanID;
    }

    public void setRemindRepeat(String remindRepeat) {
        RemindRepeat = remindRepeat;
    }

    public void setRemindDay(String remindDay) {
        RemindDay = remindDay;
    }

    public void setRemindTime(String remindTime) {
        RemindTime = remindTime;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setRemindDate(int remindDate) {
        RemindDate = remindDate;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public DateTime getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        CreatedAt = createdAt;
    }

    public DateTime getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        UpdatedAt = updatedAt;
    }
}
