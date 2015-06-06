package my.com.saiboon.fitnesscompanion;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JACKSON on 5/25/2015.
 */
public class UserLocalStore {
    public static final String SP_NAME = "userDetails";

    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(UserProfile user) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("email", user.email);
        userLocalDatabaseEditor.putString("name", user.name);
        userLocalDatabaseEditor.putString("dob", user.DOB);
        userLocalDatabaseEditor.putInt("age", user.age);
        userLocalDatabaseEditor.putString("gender", user.gender);
        userLocalDatabaseEditor.putString("height", user.height.toString());
        userLocalDatabaseEditor.putString("weight", user.weight.toString());
        userLocalDatabaseEditor.putString("password", user.password);
        userLocalDatabaseEditor.putString("DOJ", user.DOJ);
        userLocalDatabaseEditor.putInt("reward", user.reward);
        userLocalDatabaseEditor.commit();
    }


    public void storeFacebookUserData(String id, String name) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("id", id);
        userLocalDatabaseEditor.putString("name", name);
        userLocalDatabaseEditor.commit();
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.commit();
    }

    public void clearUserData() {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.commit();
    }

    public UserProfile getLoggedInUser() {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        } else {
            String email = userLocalDatabase.getString("email", "");
            String name = userLocalDatabase.getString("name", "");
            String DOB = userLocalDatabase.getString("dob", "");
            String gender = userLocalDatabase.getString("gender", "");
            Double height = Double.parseDouble(userLocalDatabase.getString("height", ""));
            Double weight = Double.parseDouble(userLocalDatabase.getString("weight", ""));
            String password = userLocalDatabase.getString("password", "");
            int age = userLocalDatabase.getInt("age", 0);
            String DOJ = userLocalDatabase.getString("doj", "");
            int reward = userLocalDatabase.getInt("reward", 0);
            UserProfile user = new UserProfile(email, name, DOB, age, gender, height, weight, password, DOJ, reward);
            return user;
        }
    }

    public String getFacebookLoggedInUser() {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        } else {
            String id = userLocalDatabase.getString("id", "");
            String facebkname = userLocalDatabase.getString("name", "");
            return facebkname;
        }
    }

}