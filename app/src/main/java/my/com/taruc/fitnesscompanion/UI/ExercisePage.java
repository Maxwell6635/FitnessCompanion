package my.com.taruc.fitnesscompanion.UI;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.Spinner;
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
import my.com.taruc.fitnesscompanion.Classes.DateTime;
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
import my.com.taruc.fitnesscompanion.ServerAPI.ServerRequests;
import my.com.taruc.fitnesscompanion.UserLocalStore;

public class ExercisePage extends ActionBarActivity {

    Chronometer myChronometer;
    TextView txtHeartRate;
    TextView txtDistance;
    Spinner spinnerExerciseName;
    TextView viewStart;
    Context context;

    FitnessRecordDA myFitnessRecordDA;
    ActivityPlanDA myActivityPlanDA;

    double totalHR = 0.0;
    static int HRno = 0;

    UserLocalStore userLocalStore;
    ServerRequests serverRequests;
    FitnessRecord fitnessRecord;
    ArrayList<ActivityPlan> activityPlanArrayList;

    AlarmSound alarmSound = new AlarmSound();
    boolean timerRunning = false;

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
    //@Bind(R.id.SpinnerBackground)
    //TextView SpinnerBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_menu);
        ButterKnife.bind(this);
        context = this;
        viewStart = (TextView) findViewById(R.id.ViewStart);
        myFitnessRecordDA = new FitnessRecordDA(this);
        // Initialize Accelerometer sensor
        distanceSensor = new DistanceSensor(this);
        // Initialize HR Strip
        heartRateSensor = new HeartRateSensor(this);
        // get activity plan
        myActivityPlanDA = new ActivityPlanDA(this);
        activityPlanArrayList = myActivityPlanDA.getAllActivityPlan();
        String[] exerciseName = new String[activityPlanArrayList.size()];
        for (int i = 0; i < activityPlanArrayList.size(); i++) {
            exerciseName[i] = activityPlanArrayList.get(i).getActivityName();
        }
        spinnerExerciseName = (Spinner) findViewById(R.id.spinnerExerciseName);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.exercise_spiiner_item, exerciseName);
        spinnerExerciseName.setAdapter(spinnerAdapter);
        try {
            spinnerExerciseName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    showPlanDetail(activityPlanArrayList.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        myChronometer = (Chronometer) findViewById(R.id.chronometerTimer);
        myChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                String chronoText = myChronometer.getText().toString();
                String array[] = chronoText.split(":");
                int seconds = Integer.parseInt(array[array.length - 1]);
                if (seconds == 0 && timerRunning) {
                    timerRunning = true;
                    alarmSound.play(context, 1);
                    Toast.makeText(context, "This is notification every minutes.", Toast.LENGTH_LONG).show();
                } else {
                    alarmSound.stop();
                }
            }
        });
        txtHeartRate = (TextView) findViewById(R.id.textViewHeartRate);
        txtHeartRate.setText(" -- bpm");
        txtDistance = (TextView) findViewById(R.id.textViewDistance);
        txtDistance.setText("-- m");

        userLocalStore = new UserLocalStore(this);
        serverRequests = new ServerRequests(this);

        showPlanDetail(activityPlanArrayList.get(0));
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
        this.finish();
    }

    public void buttonStart(View view) {
        String txt = viewStart.getText().toString();
        if (txt.equals("Start")) {
            StartTimer();
            viewStart.setText("Stop");
            //spinnerExerciseName.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            //SpinnerBackground.setBackgroundColor(Color.GRAY);
        } else {
            StopTimer();
            viewStart.setText("Start");
            //spinnerExerciseName.getBackground().setColorFilter(Color.parseColor("#ff6600aa"), PorterDuff.Mode.MULTIPLY);
            //SpinnerBackground.setBackgroundColor(Color.parseColor("#ff6600aa"));
        }
    }

    public void StartTimer() {
        //start count time
        int stoppedMilliseconds = 0;
        String chronoText = myChronometer.getText().toString();
        String array[] = chronoText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000 + Integer.parseInt(array[2]) * 1000;
        }
        myChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
        myChronometer.start();

        //start count step
        distanceSensor.startSensor();
        spinnerExerciseName.setEnabled(false);
    }

    public void PauseTimer() {
        myChronometer.stop();
    }

    public void StopTimer() {
        myChronometer.stop();
        String message = "Confirm stop fitness activity now?";
        if (getDuration() < 120) {
            message = "More effective if we can done fitness for at least 2 minutes. Are you sure you want to stop here?";
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Stop activity")
                .setMessage(message)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //reset Timer
                        addFitnessRecord();
                        int stoppedMilliseconds = 0;
                        myChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                        distanceSensor.stopSensor();
                        txtDistance.setText("-- m");
                        spinnerExerciseName.setEnabled(true);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        myChronometer.start();
                    }
                }).create();
        dialog.show();
    }

    public void addFitnessRecord() {
        //add in fitness record
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
        //get duration in second
        int stoppedMilliseconds = 0;
        String chronoText = myChronometer.getText().toString();
        String array[] = chronoText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000 + Integer.parseInt(array[2]) * 1000;
        }
        int timerSecond = stoppedMilliseconds / 1000;
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
        String activityName = spinnerExerciseName.getSelectedItem().toString();
        ActivityPlan activityPlan = myActivityPlanDA.getActivityPlanByName(activityName);
        if (activityPlan != null) {
            return activityPlan.getActivityPlanID();
        } else {
            Toast.makeText(this, "Fail to get activity plan.", Toast.LENGTH_SHORT);
            return "";
        }
    }

    //setting button
    public void showPlanDetail(ActivityPlan activityPlan) {
        textViewType.setText(activityPlan.getType());
        textViewCaloriesBurn.setText(activityPlan.getEstimateCalories() + " joules");
        textViewDuration.setText(activityPlan.getDuration() + " minutes");
    }

    //**************************************************** HR *****************************************************

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
        UserProfileDA userProfileDA = new UserProfileDA(this);
        UserProfile userProfile = userProfileDA.getUserProfile(userLocalStore.returnUserID() + "");
        int age = userProfile.calAge();
        return 220 - age;
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

    //******************************* Distance sensor **********************************************8

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
