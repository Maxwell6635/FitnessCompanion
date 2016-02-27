package my.com.taruc.fitnesscompanion.UI;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import my.com.taruc.fitnesscompanion.BackgroundSensor.AccelerometerSensor2;
import my.com.taruc.fitnesscompanion.BackgroundSensor.StepManager;
import my.com.taruc.fitnesscompanion.BackgroundSensor.TheService;
import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.FitnessFormula;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Classes.Goal;
import my.com.taruc.fitnesscompanion.Classes.HealthProfile;
import my.com.taruc.fitnesscompanion.Classes.RealTimeFitness;
import my.com.taruc.fitnesscompanion.Classes.Reminder;
import my.com.taruc.fitnesscompanion.Classes.SleepData;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.ConnectionDetector;
import my.com.taruc.fitnesscompanion.Database.ActivityPlanDA;
import my.com.taruc.fitnesscompanion.Database.FitnessDB;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.Database.GoalDA;
import my.com.taruc.fitnesscompanion.Database.HealthProfileDA;
import my.com.taruc.fitnesscompanion.Database.RealTimeFitnessDA;
import my.com.taruc.fitnesscompanion.Database.ReminderDA;
import my.com.taruc.fitnesscompanion.Database.SleepDataDA;
import my.com.taruc.fitnesscompanion.Database.UserProfileDA;
import my.com.taruc.fitnesscompanion.GCM.QuickstartPreferences;
import my.com.taruc.fitnesscompanion.GCM.RegistrationIntentService;
import my.com.taruc.fitnesscompanion.LoginPage;
import my.com.taruc.fitnesscompanion.NavigationDrawerFragment;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.AlarmServiceController;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.MyReceiver;
import my.com.taruc.fitnesscompanion.ServerAPI.RetrieveRequest;
import my.com.taruc.fitnesscompanion.ServerAPI.ServerRequests;
import my.com.taruc.fitnesscompanion.ServerAPI.UpdateRequest;
import my.com.taruc.fitnesscompanion.ShowAlert;
import my.com.taruc.fitnesscompanion.UserLocalStore;
import my.com.taruc.fitnesscompanion.Util.Constant;
import my.com.taruc.fitnesscompanion.Util.ValidateUtil;

public class MainMenu extends ActionBarActivity implements View.OnClickListener {

    public static final String TAG = MainMenu.class.getName();
    private ProgressDialog progress;

    private UserLocalStore userLocalStore;
    private FitnessDB fitnessDB;
    private UserProfile saveUserProfile, userProfile;
    private UserProfileDA userProfileDA;
    private HealthProfile healthProfile;
    private HealthProfileDA healthProfileDA;
    private ServerRequests serverRequests;
    private RetrieveRequest retrieveRequest;
    private UpdateRequest updateRequest;
    private Toolbar toolBar;

    private Intent intent;
    private boolean checkSensor = false;

    // Connection detector class
    private ConnectionDetector cd;
    // flag for Internet connection status
    private Boolean isInternetPresent = false;

    // Alert Dialog Manager
    private ShowAlert alert = new ShowAlert();

    private ReminderDA myReminderDA;
    private SleepDataDA mSleepDataDA;
    private RealTimeFitnessDA mRealTimeFitnessDA;
    private GoalDA mGoalDA;
    private FitnessRecordDA mFitnessRecordDA;
    private ReminderDA mReminderDA;
    private ArrayList<Reminder> myReminderList;
    private AlarmServiceController alarmServiceController;

    //update Step
    StepManager stepManager;
    TextView txtCounter;
    TextView ichoiceRemark;

    private PendingIntent pendingIntent;
    private FitnessFormula fitnessFormula;

    private String result;
    private boolean alarmUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main_menu_appbar_3);

        txtCounter = (TextView) findViewById(R.id.StepNumber);
        ichoiceRemark = (TextView) findViewById(R.id.ichoiceRemark);
        toolBar = (Toolbar) findViewById(R.id.app_bar);

        ichoiceRemark.setVisibility(View.INVISIBLE);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        alarmServiceController = new AlarmServiceController(this);

        //create DB
        fitnessDB = new FitnessDB(this);
        SQLiteDatabase sqLiteDatabase = fitnessDB.getWritableDatabase(); //Create DB for First Time
        int version = sqLiteDatabase.getVersion();
        if (version != Constant.DB_Version) {
            sqLiteDatabase.beginTransaction();
            try {
                if (version == Constant.DB_Version) {
                    fitnessDB.onCreate(sqLiteDatabase);
                } else {
                    if (version < Constant.DB_Version) {
                        fitnessDB.onUpgrade(sqLiteDatabase, version, Constant.DB_Version);
                    }
                }
                sqLiteDatabase.setVersion(Constant.DB_Version);
                sqLiteDatabase.setTransactionSuccessful();
            } finally {
                sqLiteDatabase.endTransaction();
            }
        }

        userLocalStore = new UserLocalStore(this);
        serverRequests = new ServerRequests(this);
        retrieveRequest = new RetrieveRequest(this);
        updateRequest = new UpdateRequest(this);
        userProfileDA = new UserProfileDA(this);
        healthProfileDA = new HealthProfileDA(this);
        mRealTimeFitnessDA = new RealTimeFitnessDA(this);
        mSleepDataDA = new SleepDataDA(this);
        mGoalDA = new GoalDA(this);
        mFitnessRecordDA = new FitnessRecordDA(this);
        mReminderDA = new ReminderDA(this);
        fitnessFormula = new FitnessFormula(this);
        cd = new ConnectionDetector(this);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolBar);

        //background sensor
        PackageManager pm = getPackageManager();
        checkSensor = IsKitKatWithStepCounter(pm);

        if (!checkSensor) {
            intent = new Intent(this, AccelerometerSensor2.class);
        } else {
            intent = new Intent(this, TheService.class);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //When User Exits App , Relog again will execute this.
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            // Internet Connection is not present
            alert.showAlertDialog(MainMenu.this, "Fail", "Internet Connection is Not Available", false);
            Intent intent = new Intent(MainMenu.this, LoginPage.class);
            startActivityForResult(intent, 1);
        } else {
            Intent iChoiceIntent = getIntent();
            if (iChoiceIntent != null) {
                if (iChoiceIntent.getExtras() != null) {
                    String iChoiceTotalStep = iChoiceIntent.getStringExtra("ichoicestep");
                    if (!iChoiceTotalStep.equals("Step")) {
                        txtCounter.setText(iChoiceTotalStep);
                        userLocalStore.setCurrentDisplayStep(iChoiceTotalStep);
                        ichoiceRemark.setVisibility(View.VISIBLE);
                    } else {
                        txtCounter.setText(userLocalStore.getCurrentDisplayStep());
                    }
                }
            }

            if (ValidateUtil.checkPlayServices(this)) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }

            progress = ProgressDialog.show(this, "Fetching Data", "Connecting....Please Wait.", true);
            if (authenticate()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fetchData();
                        progress.dismiss();
                    }

                }).start();
            }
        }
    }

    //Fetch data from server
    private void fetchData(){

        ActivityPlanDA activityPlanDA = new ActivityPlanDA(this);
        ArrayList<ActivityPlan> activityPlanArrayList = activityPlanDA.getAllActivityPlan();
        ArrayList<ActivityPlan> activityPlans = retrieveRequest.fetchActivityPlanDataInBackground();
        if (activityPlanArrayList.isEmpty()) {
            activityPlanDA.addListActivityPlan(activityPlans);
        } else if (activityPlans.isEmpty()) {
            return;
        } else if (activityPlanArrayList.size() != activityPlans.size()) {
            activityPlanDA.deleteAll();
            activityPlanDA.addListActivityPlan(activityPlans);
        }

        if (!checkSensor) {
            //registerReceiver(broadcastReceiver, new IntentFilter(AccelerometerSensor.BROADCAST_ACTION));
            registerReceiver(broadcastReceiver, new IntentFilter(AccelerometerSensor2.BROADCAST_ACTION));
        } else {
            registerReceiver(broadcastReceiver, new IntentFilter(TheService.BROADCAST_ACTION));
        }

        alarmUp = (PendingIntent.getBroadcast(MainMenu.this, 0,
                new Intent(MainMenu.this, MyReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmUp) {
            //HR reminder
            alarmMethod();
        }
        //activate reminder
        alarmServiceController.activateReminders();

        userProfile = userLocalStore.getLoggedInUser();

        if (userLocalStore.checkNormalUser()) {
            if (userLocalStore.checkIChoiceMode()) {
                //return;
            } else if (ValidateUtil.isMyServiceRunning(this, TheService.class) || ValidateUtil.isMyServiceRunning(this, AccelerometerSensor2.class)) {
                //return;
            } else {
                startService(intent);
            }

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_profile_grey);
            if (userProfile.getBitmap() != null) {
                saveUserProfile = new UserProfile(userProfile.getUserID(), userProfile.getmGCMID(), userProfile.getEmail(), userProfile.getPassword(), userProfile.getName()
                        , userProfile.getDOB(), userProfile.getGender(), userProfile.getInitial_Weight(), userProfile.getHeight(), userProfile.getReward_Point(), userProfile.getCreated_At(), new DateTime().getCurrentDateTime(), userProfile.getBitmap());
            } else {
                saveUserProfile = new UserProfile(userProfile.getUserID(), userProfile.getmGCMID(), userProfile.getEmail(), userProfile.getPassword(), userProfile.getName()
                        , userProfile.getDOB(), userProfile.getGender(), userProfile.getInitial_Weight(), userProfile.getHeight(), userProfile.getReward_Point(), userProfile.getCreated_At(), new DateTime().getCurrentDateTime(), bitmap);
            }
            List<HealthProfile> result = serverRequests.fetchHealthProfileDataInBackground(userProfile.getUserID());

            if (result.size() != 0) {
                List<HealthProfile> dbResult = healthProfileDA.getAllHealthProfile();
                if (dbResult.size() == 0) {
                    int count = healthProfileDA.addListHealthProfile(result);
                    if (count == result.size()) {
//                                Toast.makeText(this, "Insert Success", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                healthProfile = new HealthProfile(healthProfileDA.generateNewHealthProfileID(), userProfile.getUserID(), userProfile.getInitial_Weight(), 0, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, new DateTime().getCurrentDateTime(), new DateTime().getCurrentDateTime());
                boolean success2 = healthProfileDA.addHealthProfile(healthProfile);
                if (success2 == true) {
                    serverRequests.storeHealthProfileDataInBackground(healthProfile);
                }
            }

            if (userLocalStore.checkFirstUser() == false) {
                userLocalStore.setUserID(Integer.parseInt(userProfile.getUserID()));
                userLocalStore.setNormalUser(false);
            } else {
                boolean success = userProfileDA.addUserProfile(saveUserProfile);
                ArrayList<SleepData> sleepDataArrayList = retrieveRequest.fetchAllSleepDataInBackground(userProfile.getUserID());
                ArrayList<RealTimeFitness> realTimeFitnessArrayList = retrieveRequest.fetchAllRealTimeFitnessInBackground(userProfile.getUserID());
                ArrayList<Goal> goalArrayList = retrieveRequest.fetchAllGoalInBackground(userProfile.getUserID());
                ArrayList<FitnessRecord> fitnessRecordArrayList = retrieveRequest.fetchAllFitnessRecordInBackground(userProfile.getUserID());
                ArrayList<Reminder> reminderArrayList = retrieveRequest.fetchAllReminderInBackground(userProfile.getUserID());
                if (success) {
                    if (realTimeFitnessArrayList.size() != 0) {
                        mRealTimeFitnessDA.addListRealTimeFitness(realTimeFitnessArrayList);
                    }
                    if (sleepDataArrayList.size() != 0) {
                        mSleepDataDA.addListSleepData(sleepDataArrayList);
                    }
                    if (goalArrayList.size() != 0) {
                        mGoalDA.addListGoal(goalArrayList);
                    }
                    if (fitnessRecordArrayList.size() != 0) {
                        mFitnessRecordDA.addListFitnessRecord(fitnessRecordArrayList);
                    }
                    if (reminderArrayList.size() != 0) {
                        mReminderDA.addListReminder(reminderArrayList);
                    }
                    fitnessFormula.updateRewardPoint();
                    userLocalStore.setUserID(Integer.parseInt(userProfile.getUserID()));
                    userLocalStore.setNormalUser(false);
                    userLocalStore.setFirstTime(false);
                }
            }
        }
    }

    //background sensor
    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

        if (!checkSensor) {
            registerReceiver(broadcastReceiver, new IntentFilter(AccelerometerSensor2.BROADCAST_ACTION));
        } else {
            registerReceiver(broadcastReceiver, new IntentFilter(TheService.BROADCAST_ACTION));
        }

        if (authenticate()) {
            //update step display when UI firstly created
            stepManager = new StepManager(this);
            stepManager.DisplayStepCountInfo();

            if (userLocalStore.checkIChoiceMode()) {
                return;
            } else if (ValidateUtil.isMyServiceRunning(this, TheService.class) || ValidateUtil.isMyServiceRunning(this, AccelerometerSensor2.class)) {
                return;
            } else {
                startService(intent);
            }
        }
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        unregisterReceiver(broadcastReceiver);
        super.onPause();
        //Try and test when back will close the service anot
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = FitnessApplication.getRefWatcher(this);
//        refWatcher.watch(this);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MainMenu.this.finish();
            }
        }).setNegativeButton("No", null).show();
    }

    private boolean authenticate() {
        boolean status;
        if (userLocalStore.getLoggedInUser() == null) {
            Intent intent = new Intent(MainMenu.this, LoginPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            status = false;
        } else {
            status = true;
        }
        return status;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignOut:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                LoginManager.getInstance().logOut();
                Intent loginIntent = new Intent(this, LoginPage.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                finish();
                break;
        }
    }

    public void GoExerciseMenu(View view) {
        Intent intent = new Intent(this, ActivityPlanPage.class);
        startActivity(intent);
    }

    public void GoProfileMenu(View view) {
        Intent intent = new Intent(this, ProfilePage.class);
        startActivity(intent);
    }

    public void GoGoal(View view) {
        Intent intent = new Intent(this, GoalPage.class);
        startActivity(intent);
    }

    public void GoSchedule(View view) {
        Intent intent = new Intent(this, SchedulePage.class);
        startActivity(intent);
    }

    public void GoAchievementMenu(View view) {
        Intent intent = new Intent(this, AchievementMenu.class);
        startActivity(intent);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };


    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            result = intent.getStringExtra("GCM");
        }
    };

    public static boolean IsKitKatWithStepCounter(PackageManager pm) {
        // Require at least Android KitKat
        //int currentApiVersion = (int) Build.VERSION.SdkInt;
        // Check that the device supports the step counter and detector sensors
        //return currentApiVersion >= 19
        boolean kitKatwithStepCount = false;
        int currentApiVersion = Integer.valueOf(Build.VERSION.SDK);
        boolean hasStepCount = pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);
        boolean hasStepDetector = pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR);
        if (currentApiVersion >= 19) {
            if (hasStepCount) {
                kitKatwithStepCount = true;
            } else if (!hasStepCount) {
                kitKatwithStepCount = false;
            }
        }
        return kitKatwithStepCount;
    }

    //update step number at main menu ui
    private void updateUI(Intent intent) {
        if (!userLocalStore.checkIChoiceMode()) {
            String counter = intent.getStringExtra("counter");
            txtCounter.setText(counter);
            userLocalStore.setCurrentDisplayStep(counter);
            ichoiceRemark.setVisibility(View.INVISIBLE);
        }else{
            txtCounter.setText(userLocalStore.getCurrentDisplayStep());
            ichoiceRemark.setVisibility(View.VISIBLE);
        }
    }

    //HR alarm
    private void alarmMethod() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 7);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);

        Intent myIntent = new Intent(MainMenu.this, MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainMenu.this, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);

    }

}