package my.com.taruc.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Classes.DateTime;

/**
 * Created by saiboon on 24/12/2015.
 */
public class ActivityPlanDA {
    private Context context;
    private FitnessDB fitnessDB;

    private String DatabaseTable = "Activity_Plan";
    private String columnID = "id";
    private String columnUserID = "user_id";
    private String columnType = "type";
    private String columnName = "name";
    private String columnDesc = "desc";
    private String columnEstimateCalories = "estimate_calories";
    private String columnDuration = "duration";
    private String columnString = columnID + ", " + columnUserID + ", " + columnType + ", " + columnName + ", " +
            columnDesc + ", " + columnEstimateCalories + ", " + columnDuration;

    public ActivityPlanDA(Context context){
        this.context = context;
    }

    public ArrayList<ActivityPlan> getAllActivityPlan() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<ActivityPlan> datalist = new ArrayList<ActivityPlan>();
        ActivityPlan myActivityPlan;
        String getquery = "SELECT " + columnString + " FROM " + DatabaseTable;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myActivityPlan = new ActivityPlan(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), Double.parseDouble(c.getString(5)),
                            Integer.parseInt(c.getString(6)));
                    datalist.add(myActivityPlan);
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ActivityPlan getActivityPlan(String ActivityPlanID) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ActivityPlan myActivityPlan = new ActivityPlan();
        String getquery = "SELECT " + columnString + " FROM " + DatabaseTable + " WHERE " + columnID + " = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{ActivityPlanID});
            if (c.moveToFirst()) {
                do {
                    myActivityPlan = new ActivityPlan(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), Double.parseDouble(c.getString(5)),
                            Integer.parseInt(c.getString(6)));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myActivityPlan;
    }

    public ActivityPlan getActivityPlanByName(String ActivityPlanName) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ActivityPlan myActivityPlan = new ActivityPlan();
        String getquery = "SELECT " + columnString + " FROM " + DatabaseTable + " WHERE " + columnName + " = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{ActivityPlanName});
            if (c.moveToFirst()) {
                do {
                    myActivityPlan = new ActivityPlan(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), Double.parseDouble(c.getString(5)),
                            Integer.parseInt(c.getString(6)));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myActivityPlan;
    }

    public boolean addActivityPlan(ActivityPlan myActivityPlan) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success=false;
        try {
            values.put(columnID, myActivityPlan.getActivityPlanID());
            values.put(columnUserID, myActivityPlan.getUserID());
            values.put(columnType, myActivityPlan.getType());
            values.put(columnName, myActivityPlan.getActivityName());
            values.put(columnDesc, myActivityPlan.getDescription());
            values.put(columnEstimateCalories, myActivityPlan.getEstimateCalories());
            values.put(columnDuration, myActivityPlan.getDuration());
            db.insert(DatabaseTable, null, values);
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean updateActivityPlan(ActivityPlan myActivityPlan) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        String updatequery = "UPDATE " + DatabaseTable + " SET " +
                columnUserID + " = ?, " +
                columnType + " = ?, " +
                columnName + " = ?, " +
                columnDesc + " = ?, " +
                columnEstimateCalories + " = ?, " +
                columnDuration + " = ?  WHERE " + columnID + " = ?";
        ContentValues values = new ContentValues();
        boolean success=false;
        try {
            //Toast.makeText(context,"DB = "+myActivityPlan.getUserID(),Toast.LENGTH_SHORT).show();
            db.execSQL(updatequery, new String[]{myActivityPlan.getUserID(), myActivityPlan.getType(), myActivityPlan.getActivityName(),
                    myActivityPlan.getDescription(), myActivityPlan.getEstimateCalories()+"", myActivityPlan.getDuration()+"", myActivityPlan.getActivityPlanID()});
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteActivityPlan(String ActivityPlanId) {
        boolean result = false;
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete(DatabaseTable, columnID + " = ?", new String[] {ActivityPlanId});
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }

}
