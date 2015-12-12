package my.com.taruc.fitnesscompanion.Database;

/**
 * Created by saiboon on 5/6/2015.
 */

import android.content.Context;
import android.content.res.AssetManager;
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
    private static final String queryCreateUserProfile = "CREATE TABLE Users(" +
            "id  VARCHAR(255) PRIMARY KEY NOT NULL," +
            "email VARCHAR(255)," +
            "password VARCHAR(255)," +
            "name VARCHAR(255)," +
            "dob VARCHAR(30)," + //date format: yyyy-mm-dd
            "gender VARCHAR(10)," +
            "initial_weight Double," +
            "height  Double," +
            "reward_point INTEGER," + //defaut zero
            "created_at DateTime," +
            "image BLOB" +
            ");";
    private static final String queryCreateHealthProfile = "CREATE TABLE Health_Profiles(\n" +
            "id VARCHAR(30)," +
            "user_id VARCHAR(255)," +
            "weight Double," +
            "blood_pressure INTEGER," +
            "resting_heart_rate INTEGER," +
            "arm_girth DECIMAL(6,2)," +
            "chest_girth DECIMAL(6,2)," +
            "calf_girth DECIMAL(6,2)," +
            "thigh_girth DECIMAL(6,2)," +
            "waist DECIMAL(6,2)," +
            "hip DECIMAL(6,2)," +
            "created_at DATETIME," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES Users(id)" +
            ");";

    private static final String queryCreateActivityPlans = "CREATE TABLE Activity_Plans(" +
            "id VARCHAR(30)," +
            "user_id VARCHAR(255)," +
            "type VARCHAR(255)," +
            "name VARCHAR(255)," +
            "desc VARCHAR(255)," +
            "estimate_calories DOUBLE," +
            "duration Integer," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES Users(id)" +
            ");";

    private static final String queryCreateRecord = "CREATE TABLE Fitness_Records(" +
            "id VARCHAR(30)," +
            "user_id VARCHAR(255)," +
            "activities_id VARCHAR(30)," +
            "record_duration INTEGER," +  // in sec
            "record_distance DECIMAL(6,2)," + //CHANGE AT 24/7/2015 , in meter
            "record_calories DECIMAL(6,2)," +
            "record_step INTEGER," +
            "average_heart_rate DECIMAL(6,2)," +
            "created_at DATETIME," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES Users(id)," +
            "FOREIGN KEY (activities_id) REFERENCES Activity_Plans(id)" +
            ");";

    private static final String queryCreateGoal = "CREATE TABLE Goals(" +
            "id VARCHAR(30)," +
            "user_id   VARCHAR(255)," +
            "goal_desc VARCHAR(255)," +
            "goal_target Integer," +
            "goal_duration Integer," + //day
            "created_at DATETIME," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES Users(id)" +
            ");";

    private static final String queryCreateReminder = "CREATE TABLE Reminders(" +
            "id VARCHAR(30)," +
            "user_id   VARCHAR(255)," +
            "availability BOOLEAN," +
            "activities_id VARCHAR(255)," +
            "repeat VARCHAR(255)," +
            "time VARCHAR(30)," + //change at 25/7/2015 from int to string
            "day VARCHAR(30)," +
            "date INTEGER," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES Users(id)," +
            "FOREIGN KEY (activities_id) REFERENCES Activity_Plans(id)" +
            ");";

    private static final String queryCreateRanking = "CREATE TABLE Rankings(" +
            "id VARCHAR(30)," +
            "user_id   VARCHAR(255)," +
            "type   VARCHAR(255), " +
            "points  INTEGER," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES Users(id)" +
            ");";

    private static final String queryCreateAchievement = "CREATE TABLE Achievements(" +
            "id VARCHAR(30)," +
            "user_id   VARCHAR(255)," +
            "milestone_name VARCHAR(255)," +
            "milestone_result BOOLEAN," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES Users(id)" +
            ");";
    private static final String queryCreateRealTimeFitness = "CREATE TABLE RealTime_Fitness(" +
            "id VARCHAR(30)," +
            "user_id   VARCHAR(255)," +
            "capture_datetime DATETIME, " +
            "step_number Integer," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES Users(id)" +
            ");"; // keep track real time fitness into graph -- walking running secondary

    private static final String dropTableUserProfile = "DROP TABLE Users IF EXISTS";
    private static final String dropTableHealthProfile = "DROP TABLE Health_Profiles IF EXISTS";
    private static final String dropTableGoal = "DROP TABLE Goals IF EXISTS";
    private static final String dropTableRecord = "DROP TABLE Fitness_Records IF EXISTS";
    private static final String dropTableReminder = "DROP TABLE Reminders IF EXISTS";
    private static final String dropTableAchievement = "DROP TABLE Achievements IF EXISTS";
    private static final String dropTableRealTimeFitness = "DROP TABLE RealTime_Fitness IF EXISTS";
    private static final String dropTableActivityPlans = "DROP TABLE Activity_Plans IF EXISTS";
    private static final String dropTableRanking = "DROP TABLE Rankings IF EXISTS";

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
            db.execSQL(queryCreateActivityPlans);
            db.execSQL(queryCreateRecord);
            db.execSQL(queryCreateGoal);
            db.execSQL(queryCreateReminder);
            db.execSQL(queryCreateRanking);
            db.execSQL(queryCreateAchievement);
            db.execSQL(queryCreateRealTimeFitness);
            result = doesDatabaseExist(context, DATABASE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(dropTableUserProfile);
            db.execSQL(dropTableHealthProfile);
            db.execSQL(dropTableActivityPlans);
            db.execSQL(dropTableRecord);
            db.execSQL(dropTableGoal);
            db.execSQL(dropTableReminder);
            db.execSQL(dropTableRanking);
            db.execSQL(dropTableAchievement);
            db.execSQL(dropTableRealTimeFitness);
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





