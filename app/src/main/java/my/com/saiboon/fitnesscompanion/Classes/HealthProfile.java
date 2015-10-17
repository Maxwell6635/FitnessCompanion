package my.com.saiboon.fitnesscompanion.Classes;

/**
 * Created by saiboon on 11/6/2015.
 */
public class HealthProfile {

    String HealthProfileID;
    int BloodPressure, RestingHeartRate;
    double ArmGirth, ChestGirth, CalfGirth, ThighGirth, Weight,Waist,HIP;
    String RecordDateTime;
    String UserID;

    public HealthProfile(){

    }

    public HealthProfile(String HealthProfileID, String UserID, double Weight, int BloodPressure, int RestingHeartRate,
                         double ArmGirth, double ChestGirth, double CalfGirth, double ThighGirth,double Waist,double HIP ,String RecordDateTime){
        this.HealthProfileID = HealthProfileID;
        this.UserID = UserID;
        this.Weight = Weight;
        this.BloodPressure = BloodPressure;
        this.RestingHeartRate = RestingHeartRate;
        this.ArmGirth = ArmGirth;
        this.ChestGirth = ChestGirth;
        this.CalfGirth = CalfGirth;
        this.ThighGirth = ThighGirth;
        this.Waist = Waist;
        this.HIP = HIP;
        this.RecordDateTime = RecordDateTime;
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

    public String getRecordDateTime() {return RecordDateTime;}
    public void setRecordDateTime(String RecordDateTime) {this.RecordDateTime = RecordDateTime;}


}
