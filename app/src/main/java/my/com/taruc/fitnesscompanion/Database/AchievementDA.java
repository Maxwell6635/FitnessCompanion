package my.com.taruc.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.Achievement;
import my.com.taruc.fitnesscompanion.Classes.DateTime;

/**
 * Created by saiboon on 11/6/2015.
 */
public class AchievementDA {
    private Context context;
    FitnessDB fitnessDB;

    private String databaseTable = "Achievement";
    private String columnID = "id";
    private String columnUserID = "user_id";
    private String columnMilestoneName = "milestone_name";
    private String columnMilestoneResult = "milestone_result";
    private String columnCreatedAt = "created_at";
    private String columnUpdatedAt = "updated_at";
    private String allColumn = columnID + "," +  columnUserID+"," + columnMilestoneName+" ," + columnMilestoneResult+","+
            columnCreatedAt+","+ columnUpdatedAt;


    public AchievementDA(Context context){
        this.context = context;
    }

    public ArrayList<Achievement> getAllAchievement() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<Achievement> datalist = new ArrayList<Achievement>();
        Achievement myAchievement;
        String getquery = "SELECT "+ allColumn +" FROM "+ databaseTable;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myAchievement = new Achievement( c.getString(0), c.getString(1), c.getString(2), Boolean.parseBoolean(c.getString(3)) , new DateTime(c.getString(4)), new DateTime(c.getString(5)));
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
        String getquery = "SELECT "+ allColumn+" FROM "+databaseTable+" WHERE "+columnID+" = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{AchievementID});
            if (c.moveToFirst()) {
                do {
                    myAchievement = new Achievement( c.getString(0), c.getString(1), c.getString(2), Boolean.parseBoolean(c.getString(3)) , new DateTime(c.getString(4)), new DateTime(c.getString(5)));
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
            values.put(columnID, myAchievement.getAchievementID());
            values.put(columnUserID, myAchievement.getUserID());
            values.put(columnMilestoneName, myAchievement.getMilestoneName());
            values.put(columnMilestoneResult, myAchievement.getMilestoneResult());
            values.put(columnCreatedAt, myAchievement.getCreate_at().getDateTime());
            if(myAchievement.getUpdate_at()!=null) {
                values.put(columnUpdatedAt, myAchievement.getUpdate_at().getDateTime());
            }
            db.insert(databaseTable, null, values);
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
        String updatequery = "UPDATE "+ databaseTable+" SET "+ columnUserID+" = ?, "+columnMilestoneName+" = ?, "+columnMilestoneResult+" = ?, "+
                columnCreatedAt+" =?, "+ columnUpdatedAt +"=?  WHERE "+ columnID+" = ?";
        boolean success= false;
        try {
            db.execSQL(updatequery, new String[]{myAchievement.getUserID()+"", myAchievement.getMilestoneName(), myAchievement.getMilestoneResult().toString(),
                    myAchievement.getCreate_at().getDateTime(), myAchievement.getUpdate_at().getDateTime(), myAchievement.getAchievementID()});
            success= true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteAchievement(String AchievementId) {
        boolean result = false;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete(databaseTable, columnID+" = ?", new String[] {AchievementId});
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }
}
