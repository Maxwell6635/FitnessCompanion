package my.com.saiboon.fitnesscompanion.Database;

/**
 * Created by saiboon on 5/6/2015.
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FitnessDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FitnessDataBase";
    private static final int DATABASE_VERSION = 1;
    private static final String queryCreateUserProfile = "CREATE TABLE User_Profile(\n" +
            "User_ID  VARCHAR(255) PRIMARY KEY NOT NULL,\n" +
            "User_Email VARCHAR(255),\n" +
            "Password VARCHAR(255),\n" +
            "Name VARCHAR(255),\n" +
            "Date_Of_Birth VARCHAR(30),\n" +
            "Age INTEGER,\n" +
            "Gender VARCHAR(10),\n" +
            "Initial_Weight Double,\n" +
            "Height  Double,\n" +
            "Date_Of_Join VARCHAR(30),\n" +
            "Reward_Point INTEGER\n" +
            ");";
    private static final String queryCreateHealthProfile = "CREATE TABLE Health_Profile(\n" +
            "Health_Profile_ID VARCHAR(30),\n" +
            "User_ID   VARCHAR(255),\n" +
            "Weight Double,\n" +
            "Blood_Pressure INTEGER,\n" +
            "Resting_Heart_Rate INTEGER,\n" +
            "Arm_Girth DECIMAL(6,2),\n" +
            "Chest_Girth DECIMAL(6,2),\n" +
            "Calf_Girth DECIMAL(6,2),\n" +
            "Thigh_Girth DECIMAL(6,2),\n" +
            "Waist DECIMAL(6,2),\n" +
            "HIP DECIMAL(6,2),\n" +
            "Record_DateTime DATETIME,\n" +
            "PRIMARY KEY (Health_Profile_ID, User_ID),\n" +
            "FOREIGN KEY (User_ID) REFERENCES User_Profile(User_ID)\n" +
            ");";
    private static final String queryCreateGoal = "CREATE TABLE Goal(\n" +
            "Goal_ID VARCHAR(30),\n" +
            "User_ID   VARCHAR(255),\n" +
            "Goal_Description VARCHAR(255),\n" +
            "Goal_Target Integer,\n" +
            "PRIMARY KEY (Goal_ID, User_ID),\n" +
            "FOREIGN KEY (User_ID) REFERENCES User_Profile(User_ID)\n" +
            ");";
    private static final String queryCreateRecord = "CREATE TABLE Fitness_Record(\n" +
            "Fitness_Record_ID VARCHAR(30),\n" +
            "User_ID   VARCHAR(255),\n" +
            "Fitness_Activity VARCHAR(255),\n" +
            "Record_Duration INTEGER,\n" +
            "Record_Distance DECIMAL(6,2),\n" + //CHANGE AT 24/7/2015 , in meter
            "Record_Calories DECIMAL(6,2),\n" +
            "Record_Step INTEGER,\n" +
            "Average_Heart_Rate DECIMAL(6,2),\n" +
            "Fitness_Record_DateTime DATETIME,\n" +
            "PRIMARY KEY (Fitness_Record_ID, User_ID),\n" +
            "FOREIGN KEY (User_ID) REFERENCES User_Profile(User_ID)\n" +
            ");";
    private static final String queryCreateReminder = "CREATE TABLE Reminder(\n" +
            "Reminder_ID VARCHAR(30),\n" +
            "User_ID   VARCHAR(255),\n" +
            "Remind_Availability BOOLEAN,\n" +
            "Remind_Activites VARCHAR(255),\n" +
            "Remind_Repeat VARCHAR(255),\n" +
            "Remind_Time VARCHAR(30),\n" + //change at 25/7/2015 from int to string
            "Remind_Day VARCHAR(30),\n" +
            "Remind_Date INTEGER,\n" +
            "Remind_Month INTEGER,\n" +
            "Remind_Year INTEGER,\n" +
            "PRIMARY KEY (Reminder_ID, User_ID),\n" +
            "FOREIGN KEY (User_ID) REFERENCES User_Profile(User_ID)\n" +
            ");";
    private static final String queryCreateAchievement = "CREATE TABLE Achievement(\n" +
            "Achievement_ID VARCHAR(30),\n" +
            "User_ID   VARCHAR(255),\n" +
            "Milestone_Name VARCHAR(255),\n" +
            "Milestone_Result VARCHAR(255),\n" +
            "PRIMARY KEY (Achievement_ID, User_ID),\n" +
            "FOREIGN KEY (User_ID) REFERENCES User_Profile(User_ID)\n" +
            ");";

    private static final String dropTableUserProfile = "DROP TABLE User_Profile IF EXISTS";
    private static final String dropTableHealthProfile = "DROP TABLE Health_Profile IF EXISTS";
    private static final String dropTableGoal = "DROP TABLE Goal IF EXISTS";
    private static final String dropTableRecord = "DROP TABLE Fitness_Record IF EXISTS";
    private static final String dropTableReminder = "DROP TABLE Reminder IF EXISTS";
    private static final String dropTableAchievement = "DROP TABLE Achievement IF EXISTS";
    private Context context;
    private Boolean result;
    ArrayList<String> mylist = new ArrayList<String>();


    public FitnessDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(queryCreateUserProfile);
            db.execSQL(queryCreateHealthProfile);
            db.execSQL(queryCreateGoal);
            db.execSQL(queryCreateRecord);
            db.execSQL(queryCreateReminder);
            db.execSQL(queryCreateAchievement);
            result = doesDatabaseExist(context, DATABASE_NAME);
            if (result == true) {
                //Toast.makeText(context, "Database Exist", Toast.LENGTH_LONG).show();
                //InitialData();
            }
            for (int i = 0; i < mylist.size(); i++) {
                db.execSQL(mylist.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(context, "Database Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(dropTableUserProfile);
            db.execSQL(dropTableHealthProfile);
            db.execSQL(dropTableGoal);
            db.execSQL(dropTableRecord);
            db.execSQL(dropTableReminder);
            db.execSQL(dropTableAchievement);
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        Toast.makeText(context, "Database Updated", Toast.LENGTH_LONG).show();
    }

    public void InitialData() {
        InputStream in;
        BufferedReader reader;
        String line;
        Integer count = 0;
        try {
            AssetManager am = context.getAssets();
            InputStream inputStream = am.open("test.txt");
            reader = new BufferedReader(new InputStreamReader(inputStream));
            line = reader.readLine();
            while (line != null) {
                mylist.add(line);
                line = reader.readLine();
            }
            //Toast.makeText(context, mylist.get(2), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}





