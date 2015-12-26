package my.com.taruc.fitnesscompanion.UI;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import my.com.taruc.fitnesscompanion.BackgroundSensor.AccelerometerSensor;
import my.com.taruc.fitnesscompanion.BackgroundSensor.TheService;
import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Classes.HealthProfile;
import my.com.taruc.fitnesscompanion.Classes.Reminder;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.ConnectionDetector;
import my.com.taruc.fitnesscompanion.Database.ActivityPlanDA;
import my.com.taruc.fitnesscompanion.Database.FitnessDB;
import my.com.taruc.fitnesscompanion.Database.HealthProfileDA;
import my.com.taruc.fitnesscompanion.Database.ReminderDA;
import my.com.taruc.fitnesscompanion.Database.UserProfileDA;
import my.com.taruc.fitnesscompanion.LoginPage;
import my.com.taruc.fitnesscompanion.NavigationDrawerFragment;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.AlarmServiceController;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.MyReceiver;
import my.com.taruc.fitnesscompanion.ServerAPI.ServerRequests;
import my.com.taruc.fitnesscompanion.ShowAlert;
import my.com.taruc.fitnesscompanion.UserLocalStore;


public class MainMenu extends ActionBarActivity implements View.OnClickListener  {

    Button btnLogout;
    UserLocalStore userLocalStore;
    FitnessDB fitnessDB;
    UserProfile saveUserProfile , checkUserProfile , userProfile;
    UserProfileDA userProfileDA;
    HealthProfile healthProfile;
    HealthProfileDA healthProfileDA;
    ServerRequests serverRequests;
    private Toolbar toolBar;

    public static final String TAG = MainMenu.class.getName();
    private Intent intent;
    private boolean checkSensor = false;

    // Connection detector class
    private ConnectionDetector cd;
    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Alert Dialog Manager
    ShowAlert alert = new ShowAlert();

    //Reminder
    ReminderDA myReminderDA;
    ArrayList<Reminder> myReminderList;
    AlarmServiceController alarmServiceController;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu_appbar_2);
        toolBar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userLocalStore = new UserLocalStore(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        serverRequests = new ServerRequests(getApplicationContext());
        userProfileDA = new UserProfileDA(this);
        healthProfileDA = new HealthProfileDA(this);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout),toolBar);
        cd = new ConnectionDetector(getApplicationContext());

        //background sensor
        PackageManager pm = getPackageManager();
        checkSensor = IsKitKatWithStepCounter(pm);
        if (!checkSensor) {
            intent = new Intent(this, AccelerometerSensor.class);
        } else {
            intent = new Intent(this, TheService.class);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        int i = preferences.getInt("numberoflaunches", 1);

        //Activate reminder alarm
        activateReminder();
        //HR reminder
        alarmMethod();

        //Test dump data of activity plan
        ActivityPlanDA activityPlanDA = new ActivityPlanDA(this);
        ArrayList<ActivityPlan> activityPlanArrayList = activityPlanDA.getAllActivityPlan();
        //for(int j=0; j<activityPlanArrayList.size(); j++){
        //    activityPlanDA.deleteActivityPlan(activityPlanArrayList.get(j).getActivityPlanID());
        //}
        //activityPlanArrayList = activityPlanDA.getAllActivityPlan();
        if(activityPlanArrayList.isEmpty()){
            ActivityPlan activityPlan1 = new ActivityPlan("P0001", null, "common", "Running", "Run", 3.0, 20);
            ActivityPlan activityPlan2 = new ActivityPlan("P0002", null, "common", "Cycling", "Cycle", 5.0, 20);
            ActivityPlan activityPlan3 = new ActivityPlan("P0003", null, "common", "Hiking", "Hike", 4.0, 20);
            ActivityPlan activityPlan4 = new ActivityPlan("P0004", null, "common", "Workout", "Workout", 12.0, 20);
            ActivityPlan activityPlan5 = new ActivityPlan("P0005", null, "common", "Sport", "Sport", 30.0, 20);
            activityPlanDA.addActivityPlan(activityPlan1);
            activityPlanDA.addActivityPlan(activityPlan2);
            activityPlanDA.addActivityPlan(activityPlan3);
            activityPlanDA.addActivityPlan(activityPlan4);
            activityPlanDA.addActivityPlan(activityPlan5);
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
            alert.showAlertDialog(MainMenu.this, "Fail", "Internet Connection is NOT Available", false);
            Intent intent = new Intent(MainMenu.this, LoginPage.class);
            startActivityForResult(intent, 1);
        } else {
           if (authenticate()) {
                System.out.print("onStart");
                if (userLocalStore.checkNormalUser()) {
                    Calendar c2 = Calendar.getInstance();
                    System.out.println("Current time => "+c2.getTime());
                    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate2 = df2.format(c2.getTime());
                    userProfile = userLocalStore.getLoggedInUser();
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_profile);
                    saveUserProfile = new UserProfile(userProfile.getUserID(), userProfile.getEmail(), userProfile.getPassword(), userProfile.getName(), userProfile.getDOB(), userProfile.getGender(), userProfile.getInitial_Weight(), userProfile.getHeight(), userProfile.getReward_Point(), userProfile.getCreated_At(), bitmap);
                    List<HealthProfile> result = serverRequests.fetchHealthProfileDataInBackground(userProfile.getUserID());
                    if(result.size()!= 0){
                        List<HealthProfile> dbResult = healthProfileDA.getAllHealthProfile();
                        if(dbResult.size()==0) {
                            int count = healthProfileDA.addListHealthProfile(result);
                            if (count == result.size()) {
                                Toast.makeText(this, "Insert Success", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        healthProfile = new HealthProfile(healthProfileDA.generateNewHealthProfileID(), userProfile.getUserID(), userProfile.getInitial_Weight(), 0, 0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0,formattedDate2);
                        boolean success2 = healthProfileDA.addHealthProfile(healthProfile);
                        if(success2 == true) {
                            serverRequests.storeHealthProfileDataInBackground(healthProfile);
                        }
                    }
                    if(userLocalStore.checkFirstUser()!=false){
                        userLocalStore.setUserID(Integer.parseInt(userProfile.getUserID()));
                        userLocalStore.setNormalUser(false);
                        Toast.makeText(this,"Not Insert",Toast.LENGTH_SHORT).show();
                    }else{
                        boolean success = userProfileDA.addUserProfile(saveUserProfile);
                        if (success) {
                            userLocalStore.setUserID(Integer.parseInt(userProfile.getUserID()));
                            userLocalStore.setNormalUser(false);
                            userLocalStore.setFirstTime(false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainMenu.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private boolean authenticate() {
        boolean status;
            if (userLocalStore.getLoggedInUser() == null) {
                Intent intent = new Intent(MainMenu.this, LoginPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                status = false;
            }else{
                status = true;
            }
        return status;
    }

    public static boolean haveNetworkConnection(final Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            final NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (final NetworkInfo netInfoCheck : netInfo) {
                if (netInfoCheck.getTypeName().equalsIgnoreCase("WIFI")) {
                    if (netInfoCheck.isConnected()) {
                        haveConnectedWifi = true;
                    }
                }
                if (netInfoCheck.getTypeName().equalsIgnoreCase("MOBILE")) {
                    if (netInfoCheck.isConnected()) {
                        haveConnectedMobile = true;
                    }
                }
            }
        }

        return haveConnectedWifi || haveConnectedMobile;
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
            case R.id.btnLogout:
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
        Intent intent = new Intent(this, ExercisePage.class);
        startActivity(intent);
    }

    public void GoProfileMenu(View view){
        Intent intent = new Intent(this, ProfilePage.class);
        startActivity(intent);
    }

    public void GoGoal(View view){
        Intent intent = new Intent(this, GoalPage.class);
        startActivity(intent);
    }

    public void GoSchedule(View view){
        Intent intent = new Intent(this, SchedulePage.class);
        startActivity(intent);
    }

    public void GoAchievementMenu(View view){
        Intent intent = new Intent(this, AchievementMenu.class);
        startActivity(intent);
    }

    public void GoFriends(View view){
        //Intent intent = new Intent(this, FriendsPage.class);
        //startActivity(intent);
        Toast.makeText(this,"Coming Soon, In Phase 2",Toast.LENGTH_SHORT).show();
    }

    public void GoReward(View view){
        Toast.makeText(this,"Coming Soon, In Phase 2",Toast.LENGTH_SHORT).show();
    }

    //background sensor
    @Override
    public void onResume() {
        super.onResume();
        startService(intent);
        if(!checkSensor) {
            registerReceiver(broadcastReceiver, new IntentFilter(AccelerometerSensor.BROADCAST_ACTION));
        }else{
            registerReceiver(broadcastReceiver, new IntentFilter(TheService.BROADCAST_ACTION));
        }
    }

    @Override
    public void onPause() {
        unregisterReceiver(broadcastReceiver);
        super.onPause();
        //Try and test when back will close the service anot
        //stopService(intent);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };


    public static boolean IsKitKatWithStepCounter(PackageManager pm) {
        // Require at least Android KitKat
        //int currentApiVersion = (int) Build.VERSION.SdkInt;
        // Check that the device supports the step counter and detector sensors
        //return currentApiVersion >= 19
        boolean kitKatwithStepCount = false;
        int currentApiVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
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
        String counter = intent.getStringExtra("counter");
        //String time = intent.getStringExtra("time");
      // Caused by: java.lang.NullPointerException: println needs a message , 2015/11/29
       /* if(counter != "" && time != "") {
            Log.d(TAG, counter);
            Log.d(TAG, time);
        }*/
        TextView txtCounter = (TextView) findViewById(R.id.StepNumber);
        txtCounter.setText(counter);
    }

    //HR alarm
    private void alarmMethod(){

        Calendar calendar = Calendar.getInstance();

        //set notification for date --> 8th January 2015 at 9:06:00 PM
//        calendar.set(Calendar.MONTH, 10);
//        calendar.set(Calendar.YEAR, 2015);
//        calendar.set(Calendar.DAY_OF_MONTH, 18);

        calendar.set(Calendar.DAY_OF_WEEK, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 13);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);

        Intent myIntent = new Intent(MainMenu.this, MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainMenu.this, 0, myIntent,0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }

    public void activateReminder(){
        myReminderDA = new ReminderDA(this);
        myReminderList = myReminderDA.getAllReminder();
        alarmServiceController = new AlarmServiceController(this);
        for (int i=0; i<myReminderList.size(); i++){
            if(myReminderList.get(i).isAvailability()){
                alarmServiceController.startAlarm(myReminderList.get(i));
            }
        }
    }

}