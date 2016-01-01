package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by saiboon on 11/6/2015.
 */
public class Achievement {

    private String AchievementID, UserID, MilestoneName;
    private Boolean MilestoneResult;
    private DateTime create_at, update_at;

    public Achievement(){

    }

    public Achievement(String achievementID, String userID, String milestoneName, Boolean milestoneResult, DateTime create_at, DateTime update_at) {
        AchievementID = achievementID;
        UserID = userID;
        MilestoneName = milestoneName;
        MilestoneResult = milestoneResult;
        this.create_at = create_at;
        this.update_at = update_at;
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

    public DateTime getCreate_at() {
        return create_at;
    }

    public void setCreate_at(DateTime create_at) {
        this.create_at = create_at;
    }

    public DateTime getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(DateTime update_at) {
        this.update_at = update_at;
    }
}
