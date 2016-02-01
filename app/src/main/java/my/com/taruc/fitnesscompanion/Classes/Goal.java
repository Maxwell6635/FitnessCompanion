package my.com.taruc.fitnesscompanion.Classes;

import android.content.Context;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.BackgroundSensor.StepManager;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.Database.HealthProfileDA;

/**
 * Created by saiboon on 9/6/2015.
 */
public class Goal {

    private String GoalId, GoalDescription;
    private int GoalTarget, GoalDuration;
    private String UserID;
    private DateTime CreateAt = new DateTime().getCurrentDateTime();
    private DateTime UpdateAt = new DateTime().getCurrentDateTime();

    private String[] goalTitle = new String[] {"Reduce Weight (KG)", "Step Walk (steps)", "Run Duration (min)", "Exercise Duration (min)", "Calories Burn (joules)"};

    public Goal(){
    }

    public Goal(String GoalId, String UserID, String GoalDescription, int GoalTarget, int GoalDuration, DateTime CreateAt, DateTime updateAt){
        this.GoalId = GoalId;
        this.UserID = UserID;
        this.GoalDescription = GoalDescription;
        this.GoalTarget = GoalTarget;
        this.GoalDuration = GoalDuration;
        this.CreateAt = CreateAt;
        this.UpdateAt = updateAt;
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

    public DateTime startDate(){
        return CreateAt;
    }

    public DateTime endDate(){
        DateTime tempEndDate = startDate();
        tempEndDate.getDate().addDateNumber( GoalDuration - 1 );
        return tempEndDate;
    }

    public String[] getGoalTitle() {
        return goalTitle;
    }

    public String getReduceWeightTitle(){
        return goalTitle[0];
    }

    public String getStepWalkTitle(){
        return goalTitle[1];
    }

    public String getRunDuration(){
        return goalTitle[2];
    }

    public String getExerciseDuration(){
        return goalTitle[3];
    }

    public String getCaloriesBurn(){
        return goalTitle[4];
    }

    public String getCurrentWeight(Context context){
        HealthProfileDA myHealthProfileDA = new HealthProfileDA(context);
        //get Weight
        HealthProfile getLastHealthProfile = myHealthProfileDA.getLastHealthProfile();
        return String.valueOf(getLastHealthProfile.getWeight()) + "";
    }

    public int getCurrentStepCount(Context context) {
        //get step count
        StepManager stepManager = new StepManager(context);
        int stepNumber = 0;
        if(goalTitle!=null) {
            stepNumber = stepManager.GetStepNumber(startDate(), endDate());
        }
        return stepNumber;
    }

    public int totalAllFitnessRecord(Context context, String goalType) {
        int totalRunDuration = 0;
        int totalExerciseDuration = 0;
        int caloriesBurn = 0;
        FitnessRecordDA fitnessRecordDA = new FitnessRecordDA(context);
        ArrayList<FitnessRecord> fitnessRecordArrayList = fitnessRecordDA.getAllFitnessRecordBetweenDateTime(startDate(), endDate());
        for (int i = 0; i < fitnessRecordArrayList.size(); i++) {
            if (fitnessRecordArrayList.get(i).getActivityPlanID().equals("P0001")) {
                totalRunDuration += fitnessRecordArrayList.get(i).getRecordDuration();
            }
            totalExerciseDuration += fitnessRecordArrayList.get(i).getRecordDuration();
            caloriesBurn += fitnessRecordArrayList.get(i).getRecordCalories();
        }
        if (goalType.equals(getRunDuration())) {
            return totalRunDuration;
        } else if (goalType.equals(getExerciseDuration())) {
            return totalExerciseDuration;
        } else if (goalType.equals(getCaloriesBurn())) {
            return caloriesBurn;
        }
        return 0;
    }

}
