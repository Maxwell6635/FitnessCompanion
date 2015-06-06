package my.com.saiboon.fitnesscompanion.UI;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import my.com.saiboon.fitnesscompanion.LoginPage;
import my.com.saiboon.fitnesscompanion.R;
import my.com.saiboon.fitnesscompanion.UserLocalStore;
import my.com.saiboon.fitnesscompanion.UserProfile;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainMenu extends ActionBarActivity implements View.OnClickListener {
    Button btnLogout;
    UserLocalStore userLocalStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        FacebookSdk.sdkInitialize(getApplicationContext());

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
        if (authenticate() == true) {
            System.out.print("onStart");
            //displayUserDetails();
        }
    }

    private boolean authenticate() {
        if (userLocalStore.getFacebookLoggedInUser() != null) {
            return true;
        } else if (userLocalStore.getLoggedInUser() != null) {
            return true;
        } else {
            System.out.print("Fail");
            Intent intent = new Intent(this, LoginPage.class);
            startActivity(intent);
            return false;
        }
    }

    private void displayUserDetails() {
        UserProfile user = userLocalStore.getLoggedInUser();
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
        Intent intent = new Intent(this, FriendsPage.class);
        startActivity(intent);
    }

}