package my.com.taruc.fitnesscompanion.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.BackgroundSensor.StepManager;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Classes.Goal;
import my.com.taruc.fitnesscompanion.Classes.HealthProfile;
import my.com.taruc.fitnesscompanion.Database.FitnessDB;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.Database.GoalDA;
import my.com.taruc.fitnesscompanion.Database.HealthProfileDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.ServerAPI.DeleteRequest;
import my.com.taruc.fitnesscompanion.ServerAPI.ServerRequests;
import my.com.taruc.fitnesscompanion.ServerAPI.UpdateRequest;
import my.com.taruc.fitnesscompanion.UserLocalStore;

public class GoalPage extends ActionBarActivity {

    //donut circle progress bar
    Timer timer = new Timer();

    //step count
    SharedPreferences sharedPreferences;

    //dialog item
    TextView goalTarget;
    TextView goalDuration;
    Spinner spinnerGoalTitle;
    TextView txtCurrentStatus;

    Context context;
    GoalDA myGoalDA;
    HealthProfileDA myHealthProfileDA;
    Goal currentDisplayGoal = new Goal();
    ArrayList<Goal> myGoalList = new ArrayList<Goal>();
    boolean success = false;
    ServerRequests serverRequests;
    UpdateRequest updateRequest;
    DeleteRequest deleteRequest;
    UserLocalStore userLocalStore;
    String[] goalTitle;

    @Bind(R.id.textViewMyGoal)
    TextView textViewMyGoal;
    @Bind(R.id.imageCurrentStatus)
    ImageView imageCurrentStatus;
    @Bind(R.id.txtCurrentEqual)
    TextView txtCurrentEqual;
    @Bind(R.id.txtCurrentAmount)
    TextView txtCurrentAmount;
    @Bind(R.id.txtCurrentUnit)
    TextView txtCurrentUnit;
    @Bind(R.id.imageTargetStatus)
    ImageView imageTargetStatus;
    @Bind(R.id.txtTargetEqual)
    TextView txtTargetEqual;
    @Bind(R.id.txtTargetAmount)
    TextView txtTargetAmount;
    @Bind(R.id.txtTargetUnit)
    TextView txtTargetUnit;
    @Bind(R.id.contentTable)
    TableLayout contentTable;
    @Bind(R.id.buttonAddGoal)
    ImageView buttonAddGoal;
    @Bind(R.id.buttonEditGoal)
    ImageView buttonEditGoal;
    @Bind(R.id.buttonDeleteGoal)
    ImageView buttonDeleteGoal;
    @Bind(R.id.buttonNext)
    Button buttonNext;
    @Bind(R.id.buttonPrevious)
    Button buttonPrevious;
    @Bind(R.id.donut_progress)
    DonutProgress donutProgress;
    @Bind(R.id.textViewDurationDate)
    TextView textViewDurationDate;
    @Bind(R.id.textViewNoGoal)
    TextView textViewNoGoal;
    @Bind(R.id.textViewTitle)
    TextView textViewTitle;
    @Bind(R.id.imageViewBackButton)
    ImageView imageViewBackButton;
    @Bind(R.id.ScrollView01)
    ScrollView ScrollView01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_page);
        context = this;
        ButterKnife.bind(this);

        userLocalStore = new UserLocalStore(this);
        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        donutProgress.setPrefixText("Goal done ");
        donutProgress.setUnfinishedStrokeColor(Color.WHITE);
        donutProgress.setFinishedStrokeColor(Color.GREEN);

        myGoalDA = new GoalDA(this);
        myHealthProfileDA = new HealthProfileDA(this);
        serverRequests = new ServerRequests(this);
        updateRequest = new UpdateRequest(this);
        deleteRequest = new DeleteRequest(this);

        goalTitle = currentDisplayGoal.getGoalTitles();

        startDisplayInitialInfo();
        updateButton();
    }

    public void BackAction(View view) {
        this.finish();
    }

    public void startDisplayInitialInfo() {
        try {
            myGoalList = myGoalDA.getAllGoal();
            if (!myGoalList.isEmpty()) {
                currentDisplayGoal = myGoalList.get(0);
                showMyGoal(currentDisplayGoal);
            } else {
                showMyGoal(null);
            }
        } catch (Exception ex) {
            //Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Get all record fail", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateDonutProgress() {
        if (txtTargetAmount.getText() != "" || txtTargetAmount.getText() != null) {
            final int targetAmount = Integer.parseInt(txtTargetAmount.getText().toString());
            final double currentAmount = Double.parseDouble(txtCurrentAmount.getText().toString());

            timer.cancel();
            timer = new Timer();
            donutProgress.setProgress(0);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (donutProgress.getProgress() < 100) {
                                if (textViewMyGoal.getText().equals(currentDisplayGoal.getReduceWeightTitle())) {
                                    if (donutProgress.getProgress() < (int) ((double) targetAmount / currentAmount * 100)) {
                                        donutProgress.setProgress(donutProgress.getProgress() + 1);
                                    }
                                } else {
                                    if (donutProgress.getProgress() < (int) (currentAmount / (double) targetAmount * 100)) {
                                        donutProgress.setProgress(donutProgress.getProgress() + 1);
                                    }
                                }
                            }
                        }
                    });
                }
            }, 500, 10);
        }
    }

    public void editGoal(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View yourCustomView = inflater.inflate(R.layout.activity_add_goal, null);

        //locate component
        spinnerGoalTitle = (Spinner) yourCustomView.findViewById(R.id.spinnerGoal);
        txtCurrentStatus = (TextView) yourCustomView.findViewById(R.id.txtCurrentStatus);
        goalTarget = (EditText) yourCustomView.findViewById(R.id.inputGoalTarget);
        goalDuration = (EditText) yourCustomView.findViewById(R.id.inputGoalDuration);

        goalTarget.setText(currentDisplayGoal.getGoalTarget() + "");
        goalDuration.setText(currentDisplayGoal.getGoalDuration() + "");
        spinnerGoalTitle.setClickable(false);
        dataPreset();

        //set selected item
        for (int i = 0; i < spinnerGoalTitle.getCount(); i++) {
            if (currentDisplayGoal.getGoalDescription().trim().equals(spinnerGoalTitle.getItemAtPosition(i))) {
                spinnerGoalTitle.setSelection(i);
            }
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Goal")
                .setView(yourCustomView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        editGoalRecord();
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
    }

    public void editGoalRecord(){
        if(validation("Edit")){
            Goal updateGoal = new Goal(currentDisplayGoal.getGoalId(), currentDisplayGoal.getUserID(),
                    currentDisplayGoal.getGoalDescription(), Integer.parseInt(goalTarget.getText().toString()),
                    Integer.parseInt(goalDuration.getText().toString()), false,
                    currentDisplayGoal.getCreateAt(), currentDisplayGoal.getUpdateAt());
            final boolean success = myGoalDA.updateGoal(updateGoal);
            if (success) {
                updateRequest.updateGoalDataInBackground(updateGoal);
                currentDisplayGoal = myGoalDA.getGoal(currentDisplayGoal.getGoalId());
                showMyGoal(currentDisplayGoal);
            } else {
                Toast.makeText(GoalPage.this, "Edit goal fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addGoal(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View yourCustomView = inflater.inflate(R.layout.activity_add_goal, null);

        //add item to spinner
        spinnerGoalTitle = (Spinner) yourCustomView.findViewById(R.id.spinnerGoal);
        txtCurrentStatus = (TextView) yourCustomView.findViewById(R.id.txtCurrentStatus);
        goalTarget = (EditText) yourCustomView.findViewById(R.id.inputGoalTarget);
        goalDuration = (EditText) yourCustomView.findViewById(R.id.inputGoalDuration);
        dataPreset();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Goal")
                .setView(yourCustomView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        addNewGoalRecord();
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
        updateButton();
    }

    public void addNewGoalRecord() {
        if(validation("Create")) {
            Goal newGoal = new Goal(myGoalDA.generateNewGoalID(), userLocalStore.returnUserID() + "",
                    spinnerGoalTitle.getSelectedItem().toString(), Integer.parseInt(goalTarget.getText().toString()),
                    Integer.parseInt(goalDuration.getText().toString()), false,
                    new DateTime().getCurrentDateTime(), new DateTime().getCurrentDateTime());
            success = myGoalDA.addGoal(newGoal);
            if (success) {
                serverRequests.storeGoalDataInBackground(newGoal);
                currentDisplayGoal = myGoalDA.getLastGoal();
                showMyGoal(currentDisplayGoal);
            } else {
                Toast.makeText(this, "Add goal fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean validation(String action){
        boolean valid = false;
        if (goalTarget.getText() == "" || goalTarget.getText() == null || goalDuration.getText() == "" || goalDuration.getText() == null) {
            Toast.makeText(this, action+" Goal Fail. \nPlease fill in goal target and goal duration.", Toast.LENGTH_SHORT).show();
        }else if (goalTarget.getText() == "0" || goalDuration.getText() == "0") {
            Toast.makeText(this, action+" Goal Fail. \nGoal target and goal duration cannot be zero.", Toast.LENGTH_SHORT).show();
        } else {
            double targetValue = Double.parseDouble(goalTarget.getText().toString());
            String[] currentValueString = txtCurrentStatus.getText().toString().split(":");
            double currentValue = Double.parseDouble(currentValueString[1].trim());
            boolean IsWeightTitle = spinnerGoalTitle.getSelectedItem().toString().trim().equals(currentDisplayGoal.getReduceWeightTitle().trim());
            if (IsWeightTitle && targetValue >= currentValue) {
                Toast.makeText(this, action+" Goal Fail. \nInput target value should lesser than current value.", Toast.LENGTH_SHORT).show();
            } else if (!IsWeightTitle && targetValue <= currentValue) {
                Toast.makeText(this, action+" Goal Fail. \nInput target value should bigger than current value.", Toast.LENGTH_SHORT).show();
            } else {
                valid = true;
            }
        }
        return valid;
    }

    public void dataPreset(){
        final Goal myGoal = new Goal();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, goalTitle);
        spinnerGoalTitle.setAdapter(spinnerAdapter);
        spinnerGoalTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        txtCurrentStatus.setText(String.format("Current: %.2f", myGoal.getCurrentWeight(context)));
                        break;
                    case 1:
                        txtCurrentStatus.setText("Current: " + myGoal.getCurrentStepCount(context));
                        break;
                    case 2:
                    case 3:
                    case 4:
                        txtCurrentStatus.setText("Current: " + myGoal.totalAllFitnessRecord(context, myGoal.getGoalTitles()[position]));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void deleteGoal(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete Goal")
                .setMessage("Confirm delete this goal?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final boolean success = myGoalDA.deleteGoal(currentDisplayGoal.getGoalId());
                        if (success) {
                            deleteRequest.deleteGoalDataInBackground(currentDisplayGoal);
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

    public void nextGoal(View view) {
        myGoalList = myGoalDA.getAllGoal();
        for (int i = 0; i < myGoalList.size(); i++) {
            if (myGoalList.get(i).getGoalId().equals(currentDisplayGoal.getGoalId())) {
                if (i + 1 != myGoalList.size()) {
                    currentDisplayGoal = myGoalList.get(i + 1);
                    showMyGoal(currentDisplayGoal);
                } else {
                    Toast.makeText(this, "This is last Goal.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void previousGoal(View view) {
        myGoalList = myGoalDA.getAllGoal();
        for (int i = 0; i < myGoalList.size(); i++) {
            if (myGoalList.get(i).getGoalId().equals(currentDisplayGoal.getGoalId())) {
                if (i != 0) {
                    currentDisplayGoal = myGoalList.get(i - 1);
                    showMyGoal(currentDisplayGoal);
                } else {
                    Toast.makeText(this, "This is first Goal.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void showMyGoal(Goal displayGoal) {
        if (displayGoal != null) {
            textViewMyGoal.setText(displayGoal.getGoalDescription().toString());
            textViewDurationDate.setText(displayGoal.startDate().getDate().getFullDateString() + " - " + displayGoal.endDate().getDate().getFullDateString());
            txtTargetAmount.setText(displayGoal.getGoalTarget() + "");
            if (textViewMyGoal.getText().toString().trim().equals(displayGoal.getReduceWeightTitle())) {
                //get Weight
                txtCurrentAmount.setText(String.format("%.2f",displayGoal.getCurrentWeight(this)));
                txtTargetUnit.setText(" KG");
                txtCurrentUnit.setText(" KG");
            } else if (textViewMyGoal.getText().toString().trim().equals(displayGoal.getStepWalkTitle())) {
                //get step count
                txtCurrentAmount.setText(displayGoal.getCurrentStepCount(this) + "");
                txtTargetUnit.setText(" steps");
                txtCurrentUnit.setText(" steps");
            } else {
                int currentValue = displayGoal.totalAllFitnessRecord(this, textViewMyGoal.getText().toString().trim());
                if (textViewMyGoal.getText().toString().trim().equals(displayGoal.getRunDuration()) || textViewMyGoal.getText().toString().trim().equals(displayGoal.getExerciseDuration())) {
                    //"Run Duration", "Exercise Duration"
                    txtCurrentAmount.setText((currentValue / 60) + "");
                    txtTargetUnit.setText(" minutes");
                    txtCurrentUnit.setText(" minutes");
                } else if (textViewMyGoal.getText().toString().trim().equals(displayGoal.getCaloriesBurn())) {
                    //"Calories Burn"
                    txtCurrentAmount.setText(currentValue + "");
                    txtTargetUnit.setText(" Joules");
                    txtCurrentUnit.setText(" Joules");
                }
            }
            updateDonutProgress();
            visibleView(true);
        } else {
            visibleView(false);
        }
    }

    public void updateButton() {
        if (myGoalList == null) {
            buttonPrevious.setEnabled(false);
            buttonNext.setEnabled(false);
        } else {
            buttonPrevious.setEnabled(true);
            buttonNext.setEnabled(true);
        }
    }

    public void visibleView(boolean visible) {
        if (visible) {
            textViewMyGoal.setVisibility(View.VISIBLE);
            textViewDurationDate.setVisibility(View.VISIBLE);
            contentTable.setVisibility(View.VISIBLE);
            //txtTargetAmount.setVisibility(View.VISIBLE);
            //txtCurrentAmount.setVisibility(View.VISIBLE);
            buttonEditGoal.setVisibility(View.VISIBLE);
            buttonDeleteGoal.setVisibility(View.VISIBLE);
            donutProgress.setVisibility(View.VISIBLE);
            buttonNext.setVisibility(View.VISIBLE);
            buttonPrevious.setVisibility(View.VISIBLE);
            textViewNoGoal.setVisibility(View.INVISIBLE);
        } else {
            textViewMyGoal.setVisibility(View.INVISIBLE);
            textViewDurationDate.setVisibility(View.INVISIBLE);
            contentTable.setVisibility(View.INVISIBLE);
            //txtTargetAmount.setVisibility(View.INVISIBLE);
            //txtCurrentAmount.setVisibility(View.INVISIBLE);
            buttonEditGoal.setVisibility(View.INVISIBLE);
            buttonDeleteGoal.setVisibility(View.INVISIBLE);
            donutProgress.setVisibility(View.INVISIBLE);
            buttonNext.setVisibility(View.INVISIBLE);
            buttonPrevious.setVisibility(View.INVISIBLE);
            textViewNoGoal.setVisibility(View.VISIBLE);
        }
    }

}
