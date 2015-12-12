package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by saiboon on 11/12/2015.
 */
public class ActivityPlan {

    private String ActivityPlanID, UserID, Type, ActivityName, Description;
    private double EstimateCalories;
    private int Duration;

    public ActivityPlan() {}

    public ActivityPlan(String activityPlanID, String userID, String type, String activityName, String description, double estimateCalories, int duration) {
        ActivityPlanID = activityPlanID;
        UserID = userID;
        Type = type;
        ActivityName = activityName;
        Description = description;
        EstimateCalories = estimateCalories;
        Duration = duration;
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
}
