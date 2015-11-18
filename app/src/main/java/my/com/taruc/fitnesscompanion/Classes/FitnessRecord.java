package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by saiboon on 11/6/2015.
 */
public class FitnessRecord {
    String FitnessRecordID, FitnessActivity;
    int RecordDuration, RecordStep;
    String UserID;
    double RecordDistance,RecordCalories,AverageHeartRate;
    String FitnessRecordDateTime;

    public FitnessRecord(){

    }

    public FitnessRecord(String FitnessRecordID, String UserID, String FitnessActivity, int RecordDuration, double RecordDistance, double RecordCalories,
                         int RecordStep, double AverageHeartRate, String FitnessRecordDateTime){
        this.FitnessRecordID = FitnessRecordID;
        this.UserID = UserID;
        this.FitnessActivity = FitnessActivity;
        this.RecordDuration = RecordDuration;
        this.RecordDistance = RecordDistance;
        this.RecordCalories = RecordCalories;
        this.RecordStep = RecordStep;
        this.AverageHeartRate = AverageHeartRate;
        this.FitnessRecordDateTime = FitnessRecordDateTime;

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

    public String getFitnessActivity() {
        return FitnessActivity;
    }
    public void setFitnessActivity(String FitnessActivity) {this.FitnessActivity = FitnessActivity;}

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

    public String getFitnessRecordDateTime() {
        return FitnessRecordDateTime;
    }
    public void setFitnessRecordDateTime(String FitnessRecordDateTime) {this.FitnessRecordDateTime = FitnessRecordDateTime;}
}
