package my.com.taruc.fitnesscompanion;

import android.content.Context;
import android.content.SharedPreferences;

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;

/**
 * Created by JACKSON on 5/25/2015.
 */
public class UserLocalStore {
    public static final String SP_NAME = "userDetails";

    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void storeUserData(UserProfile user) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("id", user.getUserID());
        userLocalDatabaseEditor.putString("email", user.getEmail());
        userLocalDatabaseEditor.putString("name", user.getName());
        userLocalDatabaseEditor.putString("dob", user.getDOB().getDateTime());
        //userLocalDatabaseEditor.putInt("age", user.age);
        userLocalDatabaseEditor.putString("gender", user.getGender());
        userLocalDatabaseEditor.putString("height", user.getHeight()+"");
        userLocalDatabaseEditor.putString("weight", user.getInitial_Weight()+"");
        userLocalDatabaseEditor.putString("password", user.getPassword());
        userLocalDatabaseEditor.putString("DOJ", user.getDOB().getDateTime());
        userLocalDatabaseEditor.putInt("reward", user.getReward_Point());
        userLocalDatabaseEditor.commit();
    }


    public void storeFacebookUserData(String id, String email,String name,String gender,String DOB,String DOJ,int age,Double height) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("id", id);
        userLocalDatabaseEditor.putString("email", email);
        userLocalDatabaseEditor.putString("name", name);
        userLocalDatabaseEditor.putString("dob", DOB);
        userLocalDatabaseEditor.putInt("age", age);
        userLocalDatabaseEditor.putString("gender", gender);
        userLocalDatabaseEditor.putString("height", height.toString());
        userLocalDatabaseEditor.putString("doj", DOJ);
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

    public void setFirstTime(boolean first) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("first", first);
        userLocalDatabaseEditor.commit();
    }



    public boolean checkFirstUser(){
        if (userLocalDatabase.getBoolean("first", false) == false) {
            return false;
        }else {
            return true;
        }
    }

    public void setNormalUser(boolean firstUser) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("firstUser", firstUser);
        userLocalDatabaseEditor.commit();
    }



    public boolean checkNormalUser(){
        if (userLocalDatabase.getBoolean("firstUser", false) == false) {
            return false;
        }else {
            return true;
        }
    }


    public void setUserID(Integer id) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putInt("userID", id);
        userLocalDatabaseEditor.commit();
    }

    public Integer returnUserID(){
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        } else {
            Integer userID = userLocalDatabase.getInt("userID",0);
            return userID;
        }
    }



    public UserProfile getLoggedInUser() {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        }else {
            String id = userLocalDatabase.getString("id", "");
            String email = userLocalDatabase.getString("email", "");
            String name = userLocalDatabase.getString("name", "");
            String DOB = userLocalDatabase.getString("dob", "");
            String gender = userLocalDatabase.getString("gender", "");
            Double height = Double.parseDouble(userLocalDatabase.getString("height", ""));
            Double weight = Double.parseDouble(userLocalDatabase.getString("weight", ""));
            String password = userLocalDatabase.getString("password", "");
            //int age = userLocalDatabase.getInt("age", 0);
            String DOJ = userLocalDatabase.getString("doj", "");
            int reward = userLocalDatabase.getInt("reward", 0);
            //UserProfile user = new UserProfile(id,email, name, DOB, age, gender, height, weight, password, DOJ, reward);
            UserProfile user = new UserProfile(id, email, password, name, new DateTime(DOB), gender, weight, height, reward, new DateTime(DOJ), null);
            return user;
        }
    }



    public UserProfile getFacebookLoggedInUser() {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        } else {
            String id = userLocalDatabase.getString("id", "");
            String email = userLocalDatabase.getString("email", "");
            String name = userLocalDatabase.getString("name", "");
            String DOB = userLocalDatabase.getString("dob", "");
            String gender = userLocalDatabase.getString("gender", "");
            //int age = userLocalDatabase.getInt("age", 0);
            String DOJ = userLocalDatabase.getString("doj", "");
            //UserProfile profile = new UserProfile(id,email,name,DOB,age,gender,0.0,0.0,"",DOJ,0);
            UserProfile profile = new UserProfile(id, email, null, name, new DateTime(DOB), gender, 0, 0, 0, new DateTime(DOJ), null);
            return profile;
        }
    }

}