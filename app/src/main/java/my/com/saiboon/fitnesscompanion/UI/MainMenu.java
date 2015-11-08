package my.com.saiboon.fitnesscompanion.UI;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import my.com.saiboon.fitnesscompanion.BackgroundSensor.AccelerometerSensor;
import my.com.saiboon.fitnesscompanion.BackgroundSensor.TheService;
import my.com.saiboon.fitnesscompanion.Classes.HealthProfile;
import my.com.saiboon.fitnesscompanion.ConnectionDetector;
import my.com.saiboon.fitnesscompanion.Database.FitnessDB;
import my.com.saiboon.fitnesscompanion.Database.HealthProfileDA;
import my.com.saiboon.fitnesscompanion.Database.UserProfileDA;
import my.com.saiboon.fitnesscompanion.Graph.MyGraphView;
import my.com.saiboon.fitnesscompanion.LoginPage;
import my.com.saiboon.fitnesscompanion.NavigationDrawerFragment;
import my.com.saiboon.fitnesscompanion.R;
import my.com.saiboon.fitnesscompanion.ServerRequests;
import my.com.saiboon.fitnesscompanion.ShowAlert;
import my.com.saiboon.fitnesscompanion.UserLocalStore;
import my.com.saiboon.fitnesscompanion.UserProfile;


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
    int backButtonCount = 0;
    // Alert Dialog Manager
    ShowAlert alert = new ShowAlert();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu_appbar);
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
            // Internet Connection is not present
            /*alert.showAlertDialog(MainMenu.this,
                    "Success",
                    "Internet Connection is Available", true);*/
            if (authenticate()) {
                System.out.print("onStart");
                if (userLocalStore.checkNormalUser()) {
                    Calendar c2 = Calendar.getInstance();
                    System.out.println("Current time => "+c2.getTime());
                    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate2 = df2.format(c2.getTime());
                    userProfile = userLocalStore.getLoggedInUser();
                    saveUserProfile = new UserProfile(userProfile.getId(), userProfile.getEmail(), userProfile.getName(), userProfile.getDOB(), userProfile.getAge(), userProfile.getGender(), userProfile.getHeight(), userProfile.getWeight(), userProfile.getPassword(), userProfile.getDOJ(), userProfile.getReward());
                    healthProfile = new HealthProfile(healthProfileDA.generateNewHealthProfileID(), userProfile.getId(), userProfile.getWeight(), 0, 0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0,formattedDate2);
                    boolean success = userProfileDA.addUserProfile(saveUserProfile);
                    boolean success2 = healthProfileDA.addHealthProfile(healthProfile);
                    if (success) {
                        serverRequests.storeHealthProfileDataInBackground(healthProfile);
                        userLocalStore.setUserID(Integer.parseInt(userProfile.getId()));
                        userLocalStore.setNormalUser(false);
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
        boolean status = true;
            if (userLocalStore.getLoggedInUser() == null) {
                System.out.print("Fail");
                Intent intent = new Intent(MainMenu.this, LoginPage.class);
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
                startActivity(loginIntent);
                break;
        }
    }

    public void GoExerciseMenu(View view) {
        Intent intent = new Intent(this, ExerciseMenu.class);
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
        super.onPause();
        //Try and test when back will close the service anot
        unregisterReceiver(broadcastReceiver);
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
    private void updateUI(Intent intent) {
        String counter = intent.getStringExtra("counter");
        String time = intent.getStringExtra("time");
        Log.d(TAG, counter);
        Log.d(TAG, time);

        TextView txtCounter = (TextView) findViewById(R.id.StepNumber);
        txtCounter.setText(counter);
    }
}