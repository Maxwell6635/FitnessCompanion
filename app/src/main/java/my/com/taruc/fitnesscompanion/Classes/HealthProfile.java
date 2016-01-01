package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by saiboon on 11/6/2015.
 */
public class HealthProfile {

    private String HealthProfileID, UserID;
    private double Weight;
    private int BloodPressure, RestingHeartRate;
    private double ArmGirth, ChestGirth, CalfGirth, ThighGirth,Waist,HIP;
    private DateTime RecordDateTime, UpdatedAt;

    public HealthProfile(){

    }

    public HealthProfile(String healthProfileID, String userID, double weight, int bloodPressure, int restingHeartRate, double armGirth, double chestGirth, double calfGirth, double thighGirth, double waist, double HIP, DateTime recordDateTime, DateTime updatedAt) {
        HealthProfileID = healthProfileID;
        UserID = userID;
        Weight = weight;
        BloodPressure = bloodPressure;
        RestingHeartRate = restingHeartRate;
        ArmGirth = armGirth;
        ChestGirth = chestGirth;
        CalfGirth = calfGirth;
        ThighGirth = thighGirth;
        Waist = waist;
        this.HIP = HIP;
        RecordDateTime = recordDateTime;
        UpdatedAt = updatedAt;
    }

    public double getWaist() {
        return Waist;
    }

    public void setWaist(double waist) {
        Waist = waist;
    }

    public double getHIP() {
        return HIP;
    }

    public void setHIP(double HIP) {
        this.HIP = HIP;
    }

    public String getHealthProfileID() {return HealthProfileID;}
    public void setHealthProfileID(String HealthProfileID) {this.HealthProfileID = HealthProfileID;}

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public double getWeight() {return Weight;}
    public void setWeight(double Weight) {this.Weight = Weight;}

    public int getBloodPressure() {return BloodPressure;}
    public void setBloodPressure(int BloodPressure) {this.BloodPressure = BloodPressure;}

    public int getRestingHeartRate() {return RestingHeartRate;}
    public void setRestingHeartRate(int RestingHeartRate) {this.RestingHeartRate = RestingHeartRate;}

    public double getArmGirth() {return ArmGirth;}
    public void setArmGirth(double ArmGirth) {this.ArmGirth = ArmGirth;}

    public double getChestGirth() {return ChestGirth;}
    public void setChestGirth(double ChestGirth) {this.ChestGirth = ChestGirth;}

    public double getCalfGirth() {return CalfGirth;}
    public void setCalfGirth(double CalfGirth) {this.CalfGirth = CalfGirth;}

    public double getThighGirth() {return ThighGirth;}
    public void setThighGirth(double ThighGirth) {this.ThighGirth = ThighGirth;}

    public DateTime getRecordDateTime() {
        return RecordDateTime;
    }

    public void setRecordDateTime(DateTime recordDateTime) {
        RecordDateTime = recordDateTime;
    }

    public DateTime getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        UpdatedAt = updatedAt;
    }
}
