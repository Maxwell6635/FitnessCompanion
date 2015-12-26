package my.com.taruc.fitnesscompanion.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import my.com.taruc.fitnesscompanion.BackgroundSensor.StepManager;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Classes.Goal;
import my.com.taruc.fitnesscompanion.Database.FitnessDB;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.Database.GoalDA;
import my.com.taruc.fitnesscompanion.Database.HealthProfileDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.UserLocalStore;

public class GoalPage extends ActionBarActivity {

    TextView myGoal;
    TextView goalDurationDate;
    TextView targetStatus;
    TextView targetStatusUnit;
    TextView currentStatus;
    TextView currentStatusUnit;
    Button editGoalBtn;
    Button addGoalBtn;
    Button deleteGoalBtn;
    Button nextGoalBtn;
    Button previousGoalBtn;
    TextView NoGoal;

    //donut circle progress bar
    Timer timer = new Timer();
    DonutProgress donutProgress;

    //step count
    SharedPreferences sharedPreferences;

    //dialog item
    TextView goalTarget;
    TextView goalDuration;
    Spinner spinnerGoalTitle;

    FitnessDB myFitnessDB;
    GoalDA myGoalDA;
    HealthProfileDA myHealthProfileDA;
    Goal currentDisplayGoal = new Goal();
    ArrayList<Goal> myGoalList = new ArrayList<Goal>();
    boolean success= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_page);

        myGoal = (TextView) findViewById(R.id.textViewMyGoal);
        targetStatus = (TextView) findViewById(R.id.txtTargetAmount);
        targetStatusUnit = (TextView) findViewById(R.id.txtTargetUnit);
        currentStatus = (TextView) findViewById(R.id.txtCurrentAmount);
        currentStatusUnit = (TextView) findViewById(R.id.txtCurrentUnit);
        editGoalBtn = (Button) findViewById(R.id.buttonEditGoal);
        addGoalBtn = (Button) findViewById(R.id.buttonAddGoal);
        deleteGoalBtn = (Button) findViewById(R.id.buttonDeleteGoal);
        nextGoalBtn = (Button) findViewById(R.id.buttonPrevious);
        previousGoalBtn = (Button) findViewById(R.id.buttonNext);
        goalDurationDate = (TextView) findViewById(R.id.textViewDurationDate);
        NoGoal = (TextView) findViewById(R.id.textViewNoGoal);

        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        donutProgress.setPrefixText("Goal done ");
        donutProgress.setUnfinishedStrokeColor(Color.WHITE);
        donutProgress.setFinishedStrokeColor(Color.GREEN);

        //create DB
        myFitnessDB = new FitnessDB(this);
        SQLiteDatabase sqLiteDatabase = myFitnessDB.getWritableDatabase();
        myFitnessDB.onCreate(sqLiteDatabase);
        myGoalDA = new GoalDA(this);

        startDisplayInitialInfo();
        updateButton();
    }

    public void startDisplayInitialInfo(){
        try {
            myGoalList = myGoalDA.getAllGoal();
            if (!myGoalList.isEmpty()){
                currentDisplayGoal = myGoalList.get(0);
                showMyGoal(currentDisplayGoal);
            }else{
                showMyGoal(null);
            }
        }catch(Exception ex){
            //Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Get all record fail", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateDonutProgress(){
        if(targetStatus.getText()!=""||targetStatus.getText()!=null){
            final int targetAmount = Integer.parseInt(targetStatus.getText().toString());
            final int currentAmount = Integer.parseInt(currentStatus.getText().toString());

            timer.cancel();
            timer = new Timer();
            donutProgress.setProgress(0);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(donutProgress.getProgress() < 100) {
                                if ( donutProgress.getProgress() <  (int)(currentAmount / (double)targetAmount * 100) ) {
                                    donutProgress.setProgress(donutProgress.getProgress() + 1);
                                }
                            }
                        }
                    });
                }
            }, 500, 10);
        }
    }

    public void editGoal(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View yourCustomView = inflater.inflate(R.layout.activity_add_goal, null);
        //add item to spinner
        String[] goalTitle = currentDisplayGoal.getGoalTitle();
        final Spinner spinnerGoalTitle = (Spinner) yourCustomView.findViewById(R.id.spinnerGoal);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, goalTitle);
        spinnerGoalTitle.setAdapter(spinnerAdapter);

        final TextView goalTarget = (EditText) yourCustomView.findViewById(R.id.inputGoalTarget);
        goalTarget.setText(currentDisplayGoal.getGoalTarget()+"");
        final TextView goalDuration = (EditText) yourCustomView.findViewById(R.id.inputGoalDuration);
        goalDuration.setText(currentDisplayGoal.getGoalDuration()+"");
        spinnerGoalTitle.setClickable(false);
        //set selected item
        for (int i=0; i<spinnerGoalTitle.getCount(); i++) {
            if (currentDisplayGoal.getGoalDescription().trim().equals(spinnerGoalTitle.getItemAtPosition(i))) {
                spinnerGoalTitle.setSelection(i);
            }
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Goal")
                .setView(yourCustomView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (goalTarget.getText() == "" || goalDuration.getText() == "") {
                            Toast.makeText(GoalPage.this, "Please fill in goal target and goal duration.", Toast.LENGTH_SHORT).show();
                        } else if (goalTarget.getText() == "0" || goalDuration.getText() == "0") {
                            Toast.makeText(GoalPage.this, "Goal target and goal duration cannot be zero.", Toast.LENGTH_SHORT).show();
                        } else {
                            final boolean success = myGoalDA.updateGoal(new Goal(currentDisplayGoal.getGoalId(), currentDisplayGoal.getUserID(),
                                    currentDisplayGoal.getGoalDescription(), Integer.parseInt(goalTarget.getText().toString()),
                                    Integer.parseInt(goalDuration.getText().toString()), currentDisplayGoal.getCreateAt()));
                            if (success) {
                                showMyGoal(myGoalDA.getGoal(currentDisplayGoal.getGoalId()));
                            } else {
                                Toast.makeText(GoalPage.this, "Edit goal fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
    }

    public void addGoal(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View yourCustomView = inflater.inflate(R.layout.activity_add_goal, null);
        //add item to spinner
        String[] goalTitle = currentDisplayGoal.getGoalTitle();
        spinnerGoalTitle = (Spinner) yourCustomView.findViewById(R.id.spinnerGoal);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, goalTitle);
        spinnerGoalTitle.setAdapter(spinnerAdapter);

        goalTarget = (EditText) yourCustomView.findViewById(R.id.inputGoalTarget);
        goalDuration = (EditText) yourCustomView.findViewById(R.id.inputGoalDuration);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Goal")
                .setView(yourCustomView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        addNewGoal();
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
        updateButton();
    }

    public void addNewGoal(){
        try {
            if(goalTarget.getText()==""||goalDuration.getText()==""){
                Toast.makeText(this, "Please fill in goal target and goal duration.", Toast.LENGTH_SHORT).show();
            }else if (goalTarget.getText()=="0"||goalDuration.getText()=="0"){
                Toast.makeText(this, "Goal target and goal duration cannot be zero.", Toast.LENGTH_SHORT).show();
            }else {
                UserLocalStore userLocalStore = new UserLocalStore(this);
                Goal newGoal = new Goal(myGoalDA.generateNewGoalID(), userLocalStore.returnUserID() + "",
                        spinnerGoalTitle.getSelectedItem().toString(), Integer.parseInt(goalTarget.getText().toString()),
                        Integer.parseInt(goalDuration.getText().toString()), new DateTime().getCurrentDateTime().getDateTime());
                success = myGoalDA.addGoal(newGoal);
                if (success) {
                    currentDisplayGoal = myGoalDA.getLastGoal();
                    showMyGoal(currentDisplayGoal);
                } else {
                    Toast.makeText(this, "Add goal fail", Toast.LENGTH_SHORT).show();
                }
            }
        }catch(Exception ex){
            Toast.makeText(GoalPage.this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void deleteGoal(View view){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete Goal")
                .setMessage("Confirm delete this goal?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final boolean success = myGoalDA.deleteGoal(currentDisplayGoal.getGoalId());
                        if (success) {
                            Toast.makeText(GoalPage.this, "Delete goal success", Toast.LENGTH_SHORT).show();
                            startDisplayInitialInfo();
                        } else {
                            Toast.makeText(GoalPage.this, "Delete goal fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
        updateButton();
    }

    public void nextGoal(View view){
        myGoalList = myGoalDA.getAllGoal();
        for (int i=0; i<myGoalList.size(); i++){
            if (myGoalList.get(i).getGoalId().equals(currentDisplayGoal.getGoalId())){
                if ( i+1 != myGoalList.size() ){
                    currentDisplayGoal = myGoalList.get(i+1);
                    showMyGoal(currentDisplayGoal);
                }else{
                    Toast.makeText(this, "This is last Goal.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void previousGoal(View view){
        myGoalList = myGoalDA.getAllGoal();
        for (int i=0; i<myGoalList.size(); i++){
            if (myGoalList.get(i).getGoalId().equals(currentDisplayGoal.getGoalId())){
                if (i!=0){
                    currentDisplayGoal=myGoalList.get(i-1);
                    showMyGoal(currentDisplayGoal);
                }else{
                    Toast.makeText(this, "This is first Goal.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void showMyGoal(Goal displayGoal){
        if (displayGoal!=null){
            myGoal.setText(displayGoal.getGoalDescription().toString());
            goalDurationDate.setText(displayGoal.startDate().getDate().getFullDate() + " - " + displayGoal.endDate().getDate().getFullDate());
            targetStatus.setText(displayGoal.getGoalTarget() + "");
            if (myGoal.getText().toString().trim().equals(displayGoal.getReduceWeightTitle())){
                //get Weight
                currentStatus.setText(myHealthProfileDA.getLastHealthProfile().getWeight()+"");
                targetStatusUnit.setText(" KG");
                currentStatusUnit.setText(" KG");
            }else if (myGoal.getText().toString().trim().equals(displayGoal.getStepWalkTitle())){
                //get step count
                StepManager stepManager = new StepManager(this);
                int stepNumber = stepManager.GetStepNumber(displayGoal.startDate(),displayGoal.endDate());
                currentStatus.setText(stepNumber+"");
                targetStatusUnit.setText(" steps");
                currentStatusUnit.setText(" steps");
            }
            else{
                int currentValue = totalAllFitnessRecord(displayGoal,myGoal.getText().toString().trim());
                if(myGoal.getText().toString().trim().equals(displayGoal.getRunDuration()) || myGoal.getText().toString().trim().equals(displayGoal.getExerciseDuration()) ){
                    //"Run Duration", "Exercise Duration"
                    currentStatus.setText((currentValue / 60)+"");
                    targetStatusUnit.setText(" minutes");
                    currentStatusUnit.setText(" minutes");
                }else if(myGoal.getText().toString().trim().equals(displayGoal.getCaloriesBurn())){
                    //"Calories Burn"
                    currentStatus.setText(currentValue + "");
                    targetStatusUnit.setText(" Joules");
                    currentStatusUnit.setText(" Joules");
                }
            }
            updateDonutProgress();
            visibleView(true);
        }else{
            visibleView(false);
        }
    }

    public void updateButton(){
        if(myGoalList==null){
            previousGoalBtn.setEnabled(false);
            nextGoalBtn.setEnabled(false);
        }else{
            previousGoalBtn.setEnabled(true);
            nextGoalBtn.setEnabled(true);
        }
    }

    public int totalAllFitnessRecord(Goal myGoal, String goalType){
        int totalRunDuration =0;
        int totalExerciseDuration =0;
        int caloriesBurn =0;
        FitnessRecordDA fitnessRecordDA = new FitnessRecordDA(this);
        ArrayList<FitnessRecord> fitnessRecordArrayList = fitnessRecordDA.getAllFitnessRecordBetweenDateTime(myGoal.startDate(), myGoal.endDate());
        for(int i=0; i<fitnessRecordArrayList.size(); i++){
            if (fitnessRecordArrayList.get(i).getActivityPlanID().equals("P0001")){
                totalRunDuration += fitnessRecordArrayList.get(i).getRecordDuration();
            }
            totalExerciseDuration += fitnessRecordArrayList.get(i).getRecordDuration();
            caloriesBurn += fitnessRecordArrayList.get(i).getRecordCalories();
        }
        if(goalType.equals(myGoal.getRunDuration())){
            return totalRunDuration;
        }else if(goalType.equals(myGoal.getExerciseDuration())){
            return totalExerciseDuration;
        }else if(goalType.equals(myGoal.getCaloriesBurn())){
            return caloriesBurn;
        }
        return 0;
    }

    public void visibleView(boolean visible){
        if(visible){
            myGoal.setVisibility(View.VISIBLE);
            targetStatus.setVisibility(View.VISIBLE);
            currentStatus.setVisibility(View.VISIBLE);
            editGoalBtn.setEnabled(true);
            deleteGoalBtn.setEnabled(true);
            donutProgress.setVisibility(View.VISIBLE);
            nextGoalBtn.setVisibility(View.VISIBLE);
            previousGoalBtn.setVisibility(View.VISIBLE);
            NoGoal.setVisibility(View.INVISIBLE);
        }else{
            myGoal.setVisibility(View.INVISIBLE);
            targetStatus.setVisibility(View.INVISIBLE);
            currentStatus.setVisibility(View.INVISIBLE);
            editGoalBtn.setEnabled(false);
            deleteGoalBtn.setEnabled(false);
            donutProgress.setVisibility(View.INVISIBLE);
            nextGoalBtn.setVisibility(View.INVISIBLE);
            previousGoalBtn.setVisibility(View.INVISIBLE);
            NoGoal.setVisibility(View.VISIBLE);
        }
    }

}
