package my.com.taruc.fitnesscompanion.UI;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.BackgroundSensor.DistanceSensor;
import my.com.taruc.fitnesscompanion.BackgroundSensor.HeartRateSensor;
import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Classes.CheckAchievement;
import my.com.taruc.fitnesscompanion.Classes.CountDownTimerWithPause;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.Duration;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Classes.HealthProfile;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.Database.ActivityPlanDA;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.Database.HealthProfileDA;
import my.com.taruc.fitnesscompanion.Database.UserProfileDA;
import my.com.taruc.fitnesscompanion.HRStripBLE.BluetoothLeService;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.AlarmSound;
import my.com.taruc.fitnesscompanion.ServerAPI.RetrieveRequest;
import my.com.taruc.fitnesscompanion.ServerAPI.ServerRequests;
import my.com.taruc.fitnesscompanion.UserLocalStore;

public class ExercisePage extends ActionBarActivity {

    Context context;
    private FitnessRecordDA myFitnessRecordDA;
    private ActivityPlanDA myActivityPlanDA;
    private ArrayList<ActivityPlan> activityPlanArrayList = new ArrayList<>();
    private UserLocalStore userLocalStore;
    private ServerRequests serverRequests;
    private RetrieveRequest mRetreiveRequests;
    private FitnessRecord fitnessRecord;
    private ActivityPlan activityPlan;
    double totalHR = 0.0;
    static int HRno = 0;
    private CheckAchievement checkAchievement;

    //Timer
    AlarmSound alarmSound = new AlarmSound();
    boolean timerRunning = false;

    //Challenge
    boolean isChallenge = false;
    FitnessRecord fitnessRecordFromServer;
    CountDownTimerWithPause countDownTimer;

    //HR sensor
    HeartRateSensor heartRateSensor;
    boolean HRAlertDialogExist = false;

    //Distance sensor
    DistanceSensor distanceSensor;

    @Bind(R.id.textViewType)
    TextView textViewType;
    @Bind(R.id.textViewCaloriesBurn)
    TextView textViewCaloriesBurn;
    @Bind(R.id.textViewDuration)
    TextView textViewDuration;
    @Bind(R.id.textViewDistanceTarget)
    TextView textViewDistanceTarget;
    @Bind(R.id.CountDownTimer)
    TextView CountDownTimerText;
    @Bind(R.id.textViewDistanceTargetCaption)
    TextView textViewDistanceTargetCaption;
    @Bind(R.id.textViewTitle)
    TextView textViewTitle;
    @Bind(R.id.TextViewStage)
    TextView TextViewStage;
    @Bind(R.id.chronometerTimer)
    Chronometer myChronometer;
    @Bind(R.id.ViewStart)
    TextView ViewStart;
    @Bind(R.id.textViewHeartRate)
    TextView txtHeartRate;
    @Bind(R.id.textViewDistance)
    TextView txtDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_menu);
        ButterKnife.bind(this);
        context = this;
        myFitnessRecordDA = new FitnessRecordDA(this);
        myActivityPlanDA = new ActivityPlanDA(this);
        mRetreiveRequests = new RetrieveRequest(this);
        userLocalStore = new UserLocalStore(this);
        serverRequests = new ServerRequests(this);
        activityPlanArrayList = myActivityPlanDA.getAllActivityPlan();
        checkAchievement = new CheckAchievement(this, this);

        String activityPlanID = getIntent().getStringExtra("ActivityPlanID");
        String fitnessRecordID = getIntent().getStringExtra("FitnessRecordID");
        if (activityPlanID != null && !activityPlanID.isEmpty() && activityPlanID != "") {
            ExerciseSetup(activityPlanID);
        } else if (fitnessRecordID != null && !fitnessRecordID.isEmpty() && fitnessRecordID != "") {
            Challenge();
        } else {
            Toast.makeText(this, "No activity plan selected.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initialize Accelerometer sensor
        distanceSensor = new DistanceSensor(this);
        txtDistance.setText("-- m");
        txtDistance.setTextColor(Color.GRAY);
        // Initialize HR Strip
        heartRateSensor = new HeartRateSensor(this);
        txtHeartRate.setText(" -- bpm");
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, heartRateSensor.makeGattUpdateIntentFilter());
        if (heartRateSensor.getmBluetoothLeService() != null) {
            final boolean result = heartRateSensor.getmBluetoothLeService().connect(heartRateSensor.mDeviceAddress);
            Log.d(heartRateSensor.getTAG(), "Connect request result=" + result);
        }
        registerReceiver(DistanceBroadcastReceiver, new IntentFilter(DistanceSensor.BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
        unregisterReceiver(DistanceBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (heartRateSensor.getmBluetoothLeService() != null) {
            unbindService(heartRateSensor.getmServiceConnection());
            heartRateSensor.setmBluetoothLeService(null);
        }
    }

    @Override
    public void onBackPressed() {
        warningMessage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void BackAction(View view) {
        warningMessage();
    }

    public void buttonStart(View view) {
        String txt = ViewStart.getText().toString();
        if (isChallenge) {
            if (txt.equals("Start")) {
                startCountDownTimer();
                ViewStart.setText("Stop");
            } else {
                stopCountDownTimer();
                ViewStart.setText("Start");
            }
        } else {
            if (txt.equals("Start")) {
                /*************Start Warm Up*************/
                TextViewStage.setText("Warming up...");
                resetChronometer();
                myChronometer.setOnChronometerTickListener(new TickListener(0, 5));
                startTimer();
                ViewStart.setText("Next");
            } else if (txt.equals("Next")) {
                /*************Start Exercise*************/
                TextViewStage.setText("Start " + activityPlan.getActivityName());
                resetChronometer();
                distanceSensor.startSensor();
                Duration myDuration = new Duration();
                myDuration.addMinutes(activityPlan.getDuration());
                myChronometer.setOnChronometerTickListener(new TickListener(myDuration.getHours(), myDuration.getMinutes()));
                startTimer();
                ViewStart.setText("Stop");
            } else if (txt.equals("Stop")) {
                /*************Start Cool Down*************/
                TextViewStage.setText("Cooling down...");
                stopExerciseTimer();
                resetChronometer();
                myChronometer.setOnChronometerTickListener(new TickListener(0, 5));
                startTimer();
                ViewStart.setText("End");
            } else {
                /*************End*************/
                TextViewStage.setText("End " + activityPlan.getActivityName());
                resetChronometer();
                ViewStart.setText("Start");
            }
        }
    }

    public void showPlanDetail(ActivityPlan activityPlan) {
        textViewType.setText(activityPlan.getType());
        textViewCaloriesBurn.setText(activityPlan.getEstimateCalories() + " joules");
        textViewDuration.setText(activityPlan.getDuration() + " minutes");
    }

    public void warningMessage() {
        String message = "Are you sure you wan exit without stop timer? Fitness record will lose after exit.";
        if (isChallenge) {
            message = "Are you sure you wan exit without stop timer? Your challenge will lose after exit.";
        }
        if (timerRunning) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit without stop")
                    .setMessage(message)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        } else {
            finish();
        }
    }

    /*********************************************************************************************
     * Insert Fitness Record
     ********************************************************************************************/

    public void addFitnessRecord() {
        try {
            String activityPlanID = getActivityPlanID();
            String currentDateTime = getCurrentDateTime();
            Double myDistance = getDistance();
            int myDuration = getDuration();
            double myCalories = getCalories(myDuration);
            double averageHeartRate = getAverageHeartRate();

            fitnessRecord = new FitnessRecord(myFitnessRecordDA.generateNewFitnessRecordID(),
                    userLocalStore.returnUserID() + "",
                    activityPlanID,
                    myDuration,
                    myDistance,
                    myCalories, 0,
                    averageHeartRate,
                    new DateTime(currentDateTime),
                    new DateTime().getCurrentDateTime());
            boolean success = myFitnessRecordDA.addFitnessRecord(fitnessRecord);
            if (success) {
                serverRequests.storeFitnessRecordInBackground(fitnessRecord);
                Toast.makeText(ExercisePage.this, "Insert fitness record success", Toast.LENGTH_SHORT).show();
                checkAchievement.checkGoal();
            } else {
                Toast.makeText(ExercisePage.this, "Insert fitness record fail", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(ExercisePage.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String getCurrentDateTime() {
        //get current Datetime
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.sss");
        String formattedTime = df.format(c.getTime());
        String formattedDate = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE);
        return formattedDate + " " + formattedTime;
    }

    public double getDistance() {
        //get Distance
        String[] distanceString = txtDistance.getText().toString().split("m");
        double distanceAmount = 0.0;
        if (!distanceString[0].trim().equals("--")) {
            distanceAmount = Double.parseDouble(distanceString[0].trim());
        }
        return distanceAmount;
    }

    public int getDuration() {
        int timerSecond;
        if (isChallenge) {
            DateTime RemainningTime = new DateTime();
            RemainningTime.setTime(CountDownTimerText.getText().toString());
            timerSecond = fitnessRecordFromServer.getRecordDuration() - (fitnessRecordFromServer.getRecordDuration() - RemainningTime.getTime().getTotalSeconds());

        } else {
            //get duration in second
            int stoppedMilliseconds = 0;
            String chronoText = myChronometer.getText().toString();
            String array[] = chronoText.split(":");
            if (array.length == 2) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
            } else if (array.length == 3) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000 + Integer.parseInt(array[2]) * 1000;
            }
            timerSecond = stoppedMilliseconds / 1000;
        }
        return timerSecond;
    }

    public double getCalories(int timerSecond) {
        //get calories
        // jogging general MET 7.0
        //url https://en.wikipedia.org/wiki/Metabolic_equivalent
        // Calories = METS x weight (kg) x time (hours) 
        //url http://www.mhhe.com/hper/physed/clw/webreview/web07/tsld007.htm
        HealthProfileDA healthProfileDA = new HealthProfileDA(this);
        HealthProfile healthProfile = healthProfileDA.getLastHealthProfile();
        double calories = 7.0 * healthProfile.getWeight() * (timerSecond / 60.0 / 60.0);
        return calories;
    }

    public double getAverageHeartRate() {
        //get HR
        double averageHR = 0.0;
        if (HRno != 0) {
            averageHR = totalHR / HRno;
        }
        return averageHR;
    }

    public String getActivityPlanID() {
        String activityName = textViewTitle.getText().toString();
        ActivityPlan activityPlan = myActivityPlanDA.getActivityPlanByName(activityName);
        if (activityPlan != null) {
            return activityPlan.getActivityPlanID();
        } else {
            Toast.makeText(this, "Fail to get activity plan.", Toast.LENGTH_SHORT);
            return "";
        }
    }

    /**********************************************************************************************************
     * Exercise
     **********************************************************************************************************/

    public void ExerciseSetup(String activityPlanID) {
        activityPlan = myActivityPlanDA.getActivityPlan(activityPlanID);
        textViewTitle.setText(activityPlan.getActivityName());
        showPlanDetail(activityPlan);
        myChronometer.setVisibility(View.VISIBLE);
        textViewDistanceTarget.setVisibility(View.INVISIBLE);
        textViewDistanceTargetCaption.setVisibility(View.INVISIBLE);
        CountDownTimerText.setVisibility(View.INVISIBLE);
    }

    public void startTimer() {
        txtDistance.setTextColor(Color.WHITE);
        //start count time
        int stoppedMilliseconds = 0;
        timerRunning = true;
        String chronoText = myChronometer.getText().toString();
        String array[] = chronoText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000 + Integer.parseInt(array[2]) * 1000;
        }
        myChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
        myChronometer.start();
    }

    public void pauseExerciseTimer() {
        myChronometer.stop();
        timerRunning = false;
    }

    public void stopExerciseTimer() {
        myChronometer.stop();
        timerRunning = false;
        String message = "Confirm stop fitness activity now?";
        if (getDuration() < 120) {
            message = "More effective if we can done fitness for at least 2 minutes. Are you sure you want to stop here?";
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Stop activity")
                .setMessage(message)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        addFitnessRecord();
                        distanceSensor.stopSensor();
                        txtDistance.setTextColor(Color.GRAY);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        timerRunning = true;
                        myChronometer.start();
                    }
                }).create();
        dialog.show();
    }

    public void resetChronometer() {
        timerRunning = false;
        myChronometer.stop();
        int stoppedMilliseconds = 0;
        myChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
    }

    private class TickListener implements Chronometer.OnChronometerTickListener {
        private int hour;
        private int min;

        public TickListener(int hour, int min) {
            this.hour = hour;
            this.min = min;
        }

        @Override
        public void onChronometerTick(Chronometer chronometer) {
            int tickHours = 0;
            int tickMinutes = 0;
            int tickSeconds = 0;
            String chronoText = myChronometer.getText().toString();
            String array[] = chronoText.split(":");
            if (array.length == 3) {
                tickHours = Integer.parseInt(array[0]);
                tickMinutes = Integer.parseInt(array[1]);
                tickSeconds = Integer.parseInt(array[2]);
            } else {
                tickMinutes = Integer.parseInt(array[0]);
                tickSeconds = Integer.parseInt(array[1]);
            }
            if (tickHours == hour && tickMinutes == min && tickSeconds == 00 && timerRunning) {
                timerRunning = true;
                alarmSound.play(context, 1);
            } else {
                if (alarmSound.isPlay()) {
                    alarmSound.stop();
                }
            }
        }
    }

    /**********************************************************************************************************
     * Challenge
     **********************************************************************************************************/

    public void Challenge() {
        isChallenge = true;
        myChronometer.setVisibility(View.INVISIBLE);
        CountDownTimerText.setVisibility(View.VISIBLE);
        textViewDistanceTarget.setVisibility(View.VISIBLE);
        textViewDistanceTargetCaption.setVisibility(View.VISIBLE);

        //get fitness record from server by sending fitness record id and user id
        String challengeFitnessRecordID = getIntent().getStringExtra("FitnessRecordID");
        String challengeUserID = getIntent().getStringExtra("UserID");
        fitnessRecordFromServer = mRetreiveRequests.fetchFitnessRecord(challengeFitnessRecordID, challengeUserID);

        boolean notFoundActivityPlan = true;
        int positionIndex = 0;
        do {
            if (activityPlanArrayList.get(positionIndex).getActivityPlanID().equalsIgnoreCase(fitnessRecordFromServer.getActivityPlanID())) {
                activityPlan = activityPlanArrayList.get(positionIndex);
                textViewTitle.setText(activityPlanArrayList.get(positionIndex).getActivityName());
                showPlanDetail(activityPlanArrayList.get(positionIndex));
                DateTime startingTime = new DateTime();
                startingTime.getTime().addSecond(fitnessRecordFromServer.getRecordDuration());
                CountDownTimerText.setText(startingTime.getTime().getFullTimeString()); //set count down timer
                textViewDistanceTarget.setText(fitnessRecordFromServer.getRecordDistance() + " m"); // use meter as unit
                notFoundActivityPlan = false;
                positionIndex++;
            }
        } while (notFoundActivityPlan && positionIndex < activityPlanArrayList.size());
    }

    public void startCountDownTimer() {
        countDownTimer = new CountDownTimerWithPause(fitnessRecordFromServer.getRecordDuration() * 1000, 1000, true) {

            public void onTick(long millisUntilFinished) {
                timerRunning = true;
                DateTime countingDown = new DateTime();
                countingDown.getTime().addSecond(millisUntilFinished / 1000);
                CountDownTimerText.setText(countingDown.getTime().getFullTimeString());
            }

            public void onFinish() {
                Log.i("CountDown timer", "Time up!");
                timerRunning = false;
                CountDownEnd();
            }
        }.create();

        //start count step
        distanceSensor.startSensor();
        txtDistance.setTextColor(Color.WHITE);
    }

    public void stopCountDownTimer() {
        countDownTimer.pause();
        String message;
        if (challengeSuccess()) {
            message = "Your challenge is success! Are you sure wan to stop now?";
        } else {
            message = "Your challenge is almost done! Are you sure wan to stop now?";
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Stop Challenge")
                .setMessage(message)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        countDownTimer.cancel();
                        CountDownEnd();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        countDownTimer.resume();
                    }
                }).create();
        dialog.show();
    }

    public void CountDownEnd() {
        String message;
        if (challengeSuccess()) {
            message = "You challenge successfully!";
        } else {
            message = "You challenge is fail!";
        }
        alarmSound.play(context, 1);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Time up!")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //reset Timer
                        addFitnessRecord();
                        distanceSensor.stopSensor();
                    }
                }).create();
        dialog.show();
        alarmSound.stop();
        distanceSensor.stopSensor();
        txtDistance.setTextColor(Color.GRAY);
    }

    public boolean challengeSuccess() {
        String[] distanceString = txtDistance.getText().toString().split("m");
        int userDistance = 0;
        if (!distanceString[0].trim().equals("--")) {
            userDistance = Integer.parseInt(distanceString[0]);
        }
        if (userDistance > fitnessRecordFromServer.getRecordDistance()) {
            return true;
        } else {
            return false;
        }
    }

    /************************************************************************************************************
     * Heart Rate
     ************************************************************************************************************/

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                heartRateSensor.setmConnected(true);
                Toast.makeText(context, "Heart Rate Sensor connected", Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                heartRateSensor.setmConnected(false);
                Toast.makeText(context, "Heart Rate Sensor disconnected", Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();
                heartRateSensor.getmBluetoothLeService().connect(heartRateSensor.mDeviceAddress);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                heartRateSensor.displayGattServices(heartRateSensor.getmBluetoothLeService().getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayHRData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    public void displayHRData(String data) {
        if (data != null) {
            double currentHR = Double.parseDouble(data);
            txtHeartRate.setText(" " + currentHR + " bpm");
            //record HR
            if (!txtHeartRate.getText().toString().contains("--")) {
                HRno++;
                //double currentHR = Double.parseDouble(txtHeartRate.getText().toString().replace("bpm", "").trim());
                totalHR += currentHR;
            }
            // monitor heart rate
            if (currentHR >= getMaximumHR()) {
                if (!HRAlertDialogExist) {
                    HRAlertDialog();
                }
                txtHeartRate.setTextColor(Color.RED);
            } else if (currentHR < getMaximumHR() / 2) {
                txtHeartRate.setTextColor(Color.YELLOW);
            } else {
                txtHeartRate.setTextColor(Color.GREEN);
            }
        } else {
            txtHeartRate.setText(" -- bpm");
            txtHeartRate.setTextColor(Color.WHITE);
        }
    }

    public int getMaximumHR() {
        //http://www.heart.org/HEARTORG/GettingHealthy/PhysicalActivity/FitnessBasics/Target-Heart-Rates_UCM_434341_Article.jsp#
        //Alert user when heart rate reach maximum
        /*UserProfileDA userProfileDA = new UserProfileDA(this);
        UserProfile userProfile = userProfileDA.getUserProfile(userLocalStore.returnUserID() + "");
        int age = userProfile.calAge();
        return 220 - age;*/
        return (int)activityPlan.getMaxHR();
    }

    public void HRAlertDialog() {
        HRAlertDialogExist = true;
        AlertDialog alarmDialog = new AlertDialog.Builder(this)
                .setTitle("Heart Rate Alert")
                .setMessage("Your heart rate is over maximum heart rate. You may hurt if continue exercise. Please STOP your activity and take a rest.")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 30000ms (0.5 min)
                                HRAlertDialogExist = false;
                            }
                        }, 30000);
                    }
                })
                .create();
        alarmDialog.show();
    }

    /*********************************************************************************************
     * Distance sensor
     *********************************************************************************************/

    private BroadcastReceiver DistanceBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            displayDistance(intent);
        }
    };

    public void displayDistance(Intent intent) {
        int stepsCount = Integer.parseInt(intent.getStringExtra("stepCounter"));
        if (stepsCount > 0) {
            //step = 0.45 * Height
            //url http://stackoverflow.com/questions/22292617/how-to-calculate-distance-while-walking-in-android
            UserProfileDA userProfileDA = new UserProfileDA(this);
            UserProfile userProfile = userProfileDA.getUserProfile2();
            txtDistance.setText(String.format("%.2f m", (stepsCount * (0.414 * userProfile.getHeight())) / 100));
        }
    }

}
