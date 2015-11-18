package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by saiboon on 9/6/2015.
 */
public class Goal {

    String GoalId, GoalDescription;
    int GoalTarget;
    String UserID;

    public Goal(){

    }

    public Goal(String GoalId, String UserID, String GoalDescription, int GoalTarget){
        this.GoalId = GoalId;
        this.UserID = UserID;
        this.GoalDescription = GoalDescription;
        this.GoalTarget = GoalTarget;

    }
    public String getGoalId() {
        return GoalId;
    }

    public void setGoalId(String GoalId) {
        this.GoalId = GoalId;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public int getGoalTarget() {
        return GoalTarget;
    }

    public void setGoalTarget(int GoalTarget) {
        this.GoalTarget = GoalTarget;
    }

    public String getGoalDescription() {
        return GoalDescription;
    }

    public void setGoalDescription(String GoalDescription) {
        this.GoalDescription = GoalDescription;
    }

}
