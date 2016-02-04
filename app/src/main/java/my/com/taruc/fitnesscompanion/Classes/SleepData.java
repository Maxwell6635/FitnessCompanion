package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by saiboon on 26/1/2016.
 */
public class SleepData {
    private String SleepDataID, UserID;
    private int movement;
    private DateTime created_at, updated_at;

    public SleepData() {
    }

    public SleepData(String sleepDataID, String userID, int movement, DateTime created_at, DateTime updated_at) {
        SleepDataID = sleepDataID;
        UserID = userID;
        this.movement = movement;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getSleepDataID() {
        return SleepDataID;
    }

    public void setSleepDataID(String sleepDataID) {
        SleepDataID = sleepDataID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
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
