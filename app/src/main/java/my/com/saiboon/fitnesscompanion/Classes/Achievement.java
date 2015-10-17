package my.com.saiboon.fitnesscompanion.Classes;

/**
 * Created by saiboon on 11/6/2015.
 */
public class Achievement {
    String AchievementID, MilestoneName, MilestoneResult;
    String UserID;

    public Achievement(){

    }

    public Achievement(String AchievementID, String UserID, String MilestoneName, String MilestoneResult){
        this.AchievementID = AchievementID;
        this.UserID = UserID;
        this.MilestoneName = MilestoneName;
        this.MilestoneResult = MilestoneResult;

    }
    public String getAchievementID() {return AchievementID;}
    public void setAchievementID(String AchievementID) {this.AchievementID = AchievementID;}

    public String getUserID() {return UserID;}
    public void setUserID(String UserID) {this.UserID = UserID;}

    public String getMilestoneName() {return MilestoneName;}
    public void setMilestoneName(String MilestoneName) {this.MilestoneName = MilestoneName;}

    public String getMilestoneResult() {
        return MilestoneResult;
    }
    public void setMilestoneResult(String MilestoneResult) {
        this.MilestoneResult = MilestoneResult;
    }
}
