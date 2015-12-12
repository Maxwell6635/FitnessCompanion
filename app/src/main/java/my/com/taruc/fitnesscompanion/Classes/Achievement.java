package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by saiboon on 11/6/2015.
 */
public class Achievement {

    private String AchievementID, MilestoneName, UserID;
    private Boolean MilestoneResult;

    public Achievement(){

    }

    public Achievement(String achievementID, String userID, String milestoneName, Boolean milestoneResult) {
        AchievementID = achievementID;
        MilestoneName = milestoneName;
        UserID = userID;
        MilestoneResult = milestoneResult;
    }

    public String getAchievementID() {
        return AchievementID;
    }

    public void setAchievementID(String achievementID) {
        AchievementID = achievementID;
    }

    public String getMilestoneName() {
        return MilestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        MilestoneName = milestoneName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public Boolean getMilestoneResult() {
        return MilestoneResult;
    }

    public void setMilestoneResult(Boolean milestoneResult) {
        MilestoneResult = milestoneResult;
    }
}
