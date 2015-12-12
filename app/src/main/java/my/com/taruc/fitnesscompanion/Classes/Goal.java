package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by saiboon on 9/6/2015.
 */
public class Goal {

    private String GoalId, GoalDescription;
    private int GoalTarget, GoalDuration;
    private String UserID, CreateAt;

    public Goal(){

    }

    public Goal(String GoalId, String UserID, String GoalDescription, int GoalTarget, int GoalDuration, String CreateAt){
        this.GoalId = GoalId;
        this.UserID = UserID;
        this.GoalDescription = GoalDescription;
        this.GoalTarget = GoalTarget;
        this.GoalDuration = GoalDuration;
        this.CreateAt = CreateAt;

    }

    public String getGoalId() {
        return GoalId;
    }

    public String getGoalDescription() {
        return GoalDescription;
    }

    public int getGoalTarget() {
        return GoalTarget;
    }

    public int getGoalDuration() {
        return GoalDuration;
    }

    public String getUserID() {
        return UserID;
    }

    public String getCreateAt() {
        return CreateAt;
    }

    public void setGoalId(String goalId) {
        GoalId = goalId;
    }

    public void setGoalDescription(String goalDescription) {
        GoalDescription = goalDescription;
    }

    public void setGoalTarget(int goalTarget) {
        GoalTarget = goalTarget;
    }

    public void setGoalDuration(int goalDuration) {
        GoalDuration = goalDuration;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setCreateAt(String createAt) {
        CreateAt = createAt;
    }
}
