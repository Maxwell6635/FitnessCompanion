package my.com.taruc.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.UserProfile;

/**
 * Created by saiboon on 9/6/2015.
 */
public class UserProfileDA {
    private Context context;
    FitnessDB fitnessDB;

    public UserProfileDA(Context context){
        this.context = context;
    }

    public ArrayList<UserProfile> getAllUserProfile() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<UserProfile> datalist = new ArrayList<UserProfile>();
        UserProfile myUserProfile;
        String getquery = "SELECT User_Email, Name, Date_Of_Birth, Age, Gender," +
                "Height, Initial_Weight, Password, Date_Of_Join, Reward_Point FROM User_Profile";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myUserProfile = new UserProfile(c.getString(0),c.getString(1),c.getString(2),Integer.parseInt(c.getString(3)),c.getString(4), Double.parseDouble(c.getString(5)),
                            Double.parseDouble(c.getString(6)), c.getString(7), c.getString(8), Integer.parseInt(c.getString(9)));
                    datalist.add(myUserProfile);
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public UserProfile getUserProfile(String UserProfileID) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        UserProfile myUserProfile= new UserProfile();
        String getquery = "SELECT User_Email, Name, Date_Of_Birth, Age, Gender," +
                "Height, Initial_Weight, Password, Date_Of_Join, Reward_Point FROM User_Profile WHERE User_ID =?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{UserProfileID});
            if (c.moveToFirst()) {
                do {
                    myUserProfile = new UserProfile(c.getString(0),c.getString(1),c.getString(2),Integer.parseInt(c.getString(3)),c.getString(4), Double.parseDouble(c.getString(5)),
                            Double.parseDouble(c.getString(6)), c.getString(7), c.getString(8), Integer.parseInt(c.getString(9)));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myUserProfile;
    }


    public UserProfile getUserProfile2() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        UserProfile myUserProfile= new UserProfile();
        String getquery = "SELECT User_ID,User_Email, Name, Date_Of_Birth, Age, Gender," +
                "Height, Initial_Weight, Password, Date_Of_Join, Reward_Point FROM User_Profile";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myUserProfile = new UserProfile(c.getString(0),c.getString(1),c.getString(2),c.getString(3),Integer.parseInt(c.getString(4)),c.getString(5), Double.parseDouble(c.getString(6)),
                            Double.parseDouble(c.getString(7)), c.getString(8), c.getString(9), Integer.parseInt(c.getString(10)));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myUserProfile;
    }

    public boolean addUserProfile(UserProfile myUserProfile) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success=false;
        try {
            values.put("User_ID", myUserProfile.getId());
            values.put("User_Email", myUserProfile.getEmail());
            values.put("Name", myUserProfile.getName());
            values.put("Date_Of_Birth", myUserProfile.getDOB());
            values.put("Age", myUserProfile.getAge());
            values.put("Gender", myUserProfile.getGender());
            values.put("Height", myUserProfile.getHeight());
            values.put("Initial_Weight", myUserProfile.getWeight());
            values.put("Password", myUserProfile.getPassword());
            values.put("Date_Of_Join", myUserProfile.getDOJ());
            values.put("Reward_Point", myUserProfile.getReward());
            db.insert("User_Profile", null, values);
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean updateUserProfile(UserProfile myUserProfile) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        String updatequery = "UPDATE User_Profile SET User_Email = ?, Name = ?, Date_Of_Birth = ?, Age=?, Gender=?," +
                "Height=?, Initial_Weight=?, Password=?, Date_Of_Join=?, Reward_Point=?  WHERE User_ID = ?";
        ContentValues values = new ContentValues();
        boolean success=false;
        try {
            Toast.makeText(context,"DB = "+myUserProfile.getId(),Toast.LENGTH_SHORT).show();
            db.execSQL(updatequery, new String[]{myUserProfile.getEmail(), myUserProfile.getName(), myUserProfile.getDOB(),
                    myUserProfile.getAge()+"", myUserProfile.getGender(), myUserProfile.getHeight()+"", myUserProfile.getWeight()+"",
                    myUserProfile.getPassword(), myUserProfile.getDOJ(), myUserProfile.getReward()+"", myUserProfile.getId()+""});
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteUserProfile(String UserProfileId) {
        boolean result = false;
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete("User_Profile", "User_ID = ?", new String[] {UserProfileId});
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }

}
