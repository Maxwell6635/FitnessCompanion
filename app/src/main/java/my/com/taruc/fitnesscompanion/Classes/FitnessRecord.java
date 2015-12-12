package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by saiboon on 11/6/2015.
 */
public class FitnessRecord {

    private String FitnessRecordID, ActivityPlanID, UserID, CreateAt;
    private int RecordDuration, RecordStep;
    private double RecordDistance,RecordCalories,AverageHeartRate;

    public FitnessRecord(){

    }

    public FitnessRecord(String FitnessRecordID, String UserID, String ActivityPlanID, int RecordDuration, double RecordDistance, double RecordCalories,
                         int RecordStep, double AverageHeartRate, String CreateAt){
        this.FitnessRecordID = FitnessRecordID;
        this.UserID = UserID;
        this.ActivityPlanID = ActivityPlanID;
        this.RecordDuration = RecordDuration;
        this.RecordDistance = RecordDistance;
        this.RecordCalories = RecordCalories;
        this.RecordStep = RecordStep;
        this.AverageHeartRate = AverageHeartRate;
        this.CreateAt = CreateAt;

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

    public String getCreateAt() {
        return CreateAt;
    }
    public void setCreateAt(String CreateAt) {this.CreateAt = CreateAt;}
}
