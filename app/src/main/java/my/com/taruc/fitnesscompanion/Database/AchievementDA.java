package my.com.taruc.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.Achievement;

/**
 * Created by saiboon on 11/6/2015.
 */
public class AchievementDA {
    private Context context;
    FitnessDB fitnessDB;

    public AchievementDA(Context context){
        this.context = context;
    }

    public ArrayList<Achievement> getAllAchievement() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<Achievement> datalist = new ArrayList<Achievement>();
        Achievement myAchievement;
        String getquery = "SELECT id, user_id, milestone_name, milestone_result FROM Achievement";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myAchievement = new Achievement( c.getString(0), c.getString(1), c.getString(2), Boolean.parseBoolean(c.getString(3)) );
                    datalist.add(myAchievement);
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public Achievement getAchievement(String AchievementID) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        Achievement myAchievement= new Achievement();
        String getquery = "SELECT id, user_id, milestone_name, milestone_result FROM Achievement WHERE id = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{AchievementID});
            if (c.moveToFirst()) {
                do {
                    myAchievement = new Achievement( c.getString(0), c.getString(1), c.getString(2), Boolean.parseBoolean(c.getString(3)) );
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myAchievement;
    }

    public boolean addAchievement(Achievement myAchievement) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success = false;
        try {
            values.put("id", myAchievement.getAchievementID());
            values.put("user_id", myAchievement.getUserID());
            values.put("milestone_name", myAchievement.getMilestoneName());
            values.put("milestone_result", myAchievement.getMilestoneResult());
            db.insert("Achievement", null, values);
            success = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean updateAchievement(Achievement myAchievement) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        String updatequery = "UPDATE Achievement SET user_id = ?, milestone_name = ?, milestone_result = ?  WHERE id = ?";
        boolean success= false;
        try {
            db.execSQL(updatequery, new String[]{myAchievement.getUserID()+"", myAchievement.getMilestoneName(), myAchievement.getMilestoneResult().toString(), myAchievement.getAchievementID()});
            success= true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteAchievement(String AchievementId) {
        boolean result = false;
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete("Achievement", "id = ?", new String[] {AchievementId});
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }
}
