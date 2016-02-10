package my.com.taruc.fitnesscompanion.Classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by saiboon on 11/12/2015.
 */
public class ActivityPlan {

    private String ActivityPlanID, UserID, Type, ActivityName, Description;
    private double EstimateCalories;
    private int Duration;
    private double maxHR;
    private DateTime created_at = new DateTime();
    private DateTime updated_at = new DateTime();
    private int trainer_id;

    public ActivityPlan() {}

    public ActivityPlan(String activityPlanID, String userID, String type, String activityName, String description, double estimateCalories, int duration, double maxHR, DateTime created_at, DateTime updated_at, int trainer_id) {
        ActivityPlanID = activityPlanID;
        UserID = userID;
        Type = type;
        ActivityName = activityName;
        Description = description;
        EstimateCalories = estimateCalories;
        Duration = duration;
        this.maxHR = maxHR;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.trainer_id = trainer_id;
    }

    public String getActivityPlanID() {
        return ActivityPlanID;
    }

    public String getUserID() {
        return UserID;
    }

    public String getType() {
        return Type;
    }

    public String getActivityName() {
        return ActivityName;
    }

    public String getDescription() {
        return Description;
    }

    public double getEstimateCalories() {
        return EstimateCalories;
    }

    public int getDuration() {
        return Duration;
    }

    public void setActivityPlanID(String activityPlanID) {
        ActivityPlanID = activityPlanID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setActivityName(String activityName) {
        ActivityName = activityName;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setEstimateCalories(double estimateCalories) {
        EstimateCalories = estimateCalories;
    }

    public void setDuration(int duration) {
        Duration = duration;
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

    public int getTrainer_id() {
        return trainer_id;
    }

    public void setTrainer_id(int trainer_id) {
        this.trainer_id = trainer_id;
    }

    public double getMaxHR() {
        return maxHR;
    }

    public void setMaxHR(double maxHR) {
        this.maxHR = maxHR;
    }

}
