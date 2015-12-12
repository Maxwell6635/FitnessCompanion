package my.com.taruc.fitnesscompanion.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.Goal;
import my.com.taruc.fitnesscompanion.Database.FitnessDB;
import my.com.taruc.fitnesscompanion.Database.GoalDA;
import my.com.taruc.fitnesscompanion.Database.HealthProfileDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.UserLocalStore;

public class GoalPage extends ActionBarActivity {

    TextView myGoal;
    TextView targetStatus;
    TextView currentStatus;
    Button editGoalBtn;
    Button addGoalBtn;
    Button deleteGoalBtn;
    Button nextGoalBtn;
    Button previousGoalBtn;

    //dialog item
    TextView goalTarget;
    TextView goalDuration;
    Spinner spinnerGoalTitle;

    Timer timer = new Timer();
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
        currentStatus = (TextView) findViewById(R.id.txtCurrentAmount);
        editGoalBtn = (Button) findViewById(R.id.buttonEditGoal);
        addGoalBtn = (Button) findViewById(R.id.buttonAddGoal);
        deleteGoalBtn = (Button) findViewById(R.id.buttonDeleteGoal);
        nextGoalBtn = (Button) findViewById(R.id.buttonPrevious);
        previousGoalBtn = (Button) findViewById(R.id.buttonNext);

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
            //my goal progress
            /*donutProgress.setPrefixText("Goal done ");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (donutProgress.getProgress() < targetAmount) {
                                donutProgress.setProgress(donutProgress.getProgress() + 1);
                            }
                        }
                    });
                }
            }, 10, 10);*/
        }
    }

    public void editGoal(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View yourCustomView = inflater.inflate(R.layout.activity_add_goal_fragment, null);
        //add item to spinner
        String[] goalTitle = new String[] {"Reduce Weight", "Step Walk", "Run Duration", "Exercise Duration", "Calories Burn"};
        final Spinner spinnerGoalTitle = (Spinner) yourCustomView.findViewById(R.id.spinnerGoal);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, goalTitle);
        spinnerGoalTitle.setAdapter(spinnerAdapter);

        final TextView goalTarget = (EditText) yourCustomView.findViewById(R.id.inputGoalTarget);
        final TextView goalDuration = (EditText) yourCustomView.findViewById(R.id.inputGoalDuration);
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
                        final boolean success = myGoalDA.updateGoal(new Goal(currentDisplayGoal.getGoalId(), currentDisplayGoal.getUserID(),
                                currentDisplayGoal.getGoalDescription(), Integer.parseInt(goalTarget.getText().toString()),
                                Integer.parseInt(goalDuration.getText().toString()), currentDisplayGoal.getCreateAt()));
                        if (success) {
                            showMyGoal(myGoalDA.getGoal(currentDisplayGoal.getGoalId()));
                        } else {
                            Toast.makeText(GoalPage.this, "Edit goal fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
    }

    public void addGoal(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View yourCustomView = inflater.inflate(R.layout.activity_add_goal_fragment, null);
        //add item to spinner
        String[] goalTitle = new String[] {"Reduce Weight", "Step Walk", "Run Duration", "Exercise Duration", "Calories Burn"};
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
            UserLocalStore userLocalStore = new UserLocalStore(this);
            Goal newGoal = new Goal(myGoalDA.generateNewGoalID(), userLocalStore.returnUserID()+"",
                    spinnerGoalTitle.getSelectedItem().toString(), Integer.parseInt(goalTarget.getText().toString()),
                    Integer.parseInt(goalDuration.getText().toString()), new DateTime().getCurrentDateTime().getDateTime());
            success = myGoalDA.addGoal(newGoal);
            if (success) {
                currentDisplayGoal = myGoalDA.getLastGoal();
                showMyGoal(currentDisplayGoal);
            } else {
                Toast.makeText(this, "Add goal fail", Toast.LENGTH_SHORT).show();
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
            targetStatus.setText(displayGoal.getGoalTarget() + "");
            if (myGoal.getText().toString().trim().equals("Reduce Weight")){
                //currentStatus.setText(myHealthProfileDA.getLastHealthProfile().getWeight());
                currentStatus.setText("?");
            }else if (myGoal.getText().toString().trim().equals("Step Walk")){
                //searching
                currentStatus.setText("?");
            }else if (myGoal.getText().toString().trim().equals("Run Duration")){
                //get from DB
                currentStatus.setText("?");
            }else if (myGoal.getText().toString().trim().equals("Exercise Duration")){
                //get from DB
                currentStatus.setText("?");
            }else if (myGoal.getText().toString().trim().equals("Calories Burn")){
                //get from DB
                currentStatus.setText("?");
            }
            else{
                currentStatus.setText("No set");
            }
            updateDonutProgress();
            editGoalBtn.setEnabled(true);
            deleteGoalBtn.setEnabled(true);
        }else{
            myGoal.setText("No set");
            targetStatus.setText("No set");
            currentStatus.setText("No set");
            editGoalBtn.setEnabled(false);
            deleteGoalBtn.setEnabled(false);
            updateDonutProgress();
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

}
