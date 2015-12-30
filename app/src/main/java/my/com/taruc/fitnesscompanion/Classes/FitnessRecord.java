package my.com.taruc.fitnesscompanion.Classes;

import android.widget.Toast;

import my.com.taruc.fitnesscompanion.Database.ActivityPlanDA;

/**
 * Created by saiboon on 11/6/2015.
 */
public class FitnessRecord {

    private String FitnessRecordID, UserID, ActivityPlanID;
    private int RecordDuration;
    private double RecordDistance,RecordCalories;
    private int RecordStep;
    private double AverageHeartRate;
    private DateTime CreateAt, UpdateAt;


    public FitnessRecord(){

    }

    public FitnessRecord(String fitnessRecordID, String userID, String activityPlanID, int recordDuration, double recordDistance, double recordCalories, int recordStep, double averageHeartRate, DateTime createAt, DateTime updateAt) {
        FitnessRecordID = fitnessRecordID;
        UserID = userID;
        ActivityPlanID = activityPlanID;
        RecordDuration = recordDuration;
        RecordDistance = recordDistance;
        RecordCalories = recordCalories;
        RecordStep = recordStep;
        AverageHeartRate = averageHeartRate;
        CreateAt = createAt;
        UpdateAt = updateAt;
    }

    public String getFitnessRecordID() {
        return FitnessRecordID;
    }
    public void setFitnessRecordID(String FitnessRecordID) {
        this.FitnessRecordID = FitnessRecordID;
    }

    public String getUserID() {
        return UserID;
    }
    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getActivityPlanID() {
        return ActivityPlanID;
    }
    public void setActivityPlanID(String ActivityPlanID) {this.ActivityPlanID = ActivityPlanID;}

    public int getRecordDuration() {
        return RecordDuration;
    }
    public void setRecordDuration(int RecordDuration) {
        this.RecordDuration = RecordDuration;
    }

    public double getRecordDistance() {
        return RecordDistance;
    }
    public void setRecordDistance(double RecordDistance) {
        this.RecordDistance = RecordDistance;
    }

    public double getRecordCalories() {
        return RecordCalories;
    }
    public void setRecordCalories(double RecordCalories) {
        this.RecordCalories = RecordCalories;
    }

    public int getRecordStep() {
        return RecordStep;
    }
    public void setRecordStep(int RecordStep) {
        this.RecordStep = RecordStep;
    }

    public double getAverageHeartRate() {
        return AverageHeartRate;
    }
    public void setAverageHeartRate(double AverageHeartRate) {this.AverageHeartRate = AverageHeartRate;}

    public DateTime getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(DateTime createAt) {
        CreateAt = createAt;
    }

    public DateTime getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(DateTime updateAt) {
        UpdateAt = updateAt;
    }
}
