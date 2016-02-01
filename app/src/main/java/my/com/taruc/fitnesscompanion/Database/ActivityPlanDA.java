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
    private String columnCreatedAt = "created_at";
    private String columnUpdatedAt = "updated_at";
    private String columnTrainerID = "trainer_id";
    private String columnString = columnID + ", " + columnUserID + ", " + columnType + ", " + columnName + ", " +
            columnDesc + ", " + columnEstimateCalories + ", " + columnDuration + ", " + columnCreatedAt + ", " +
            columnUpdatedAt + ", " + columnTrainerID;

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
                            Integer.parseInt(c.getString(6)), new DateTime(c.getString(7)), new DateTime(c.getString(8)), Integer.parseInt(c.getString(9)));
                    datalist.add(myActivityPlan);
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ArrayList<ActivityPlan> getTypeActivityPlan(String typeValue) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<ActivityPlan> datalist = new ArrayList<ActivityPlan>();
        ActivityPlan myActivityPlan;
        String getquery = "SELECT " + columnString + " FROM " + DatabaseTable + " WHERE " + columnType + " = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{typeValue});
            if (c.moveToFirst()) {
                do {
                    myActivityPlan = new ActivityPlan(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), Double.parseDouble(c.getString(5)),
                            Integer.parseInt(c.getString(6)), new DateTime(c.getString(7)), new DateTime(c.getString(8)), Integer.parseInt(c.getString(9)));
                    datalist.add(myActivityPlan);
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ArrayList<String> getAllActivityPlanType() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        String myActivityPlanType;
        ArrayList<String> dataList = new ArrayList<String>();
        String getquery = "SELECT DISTINCT " + columnType + " FROM " + DatabaseTable;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myActivityPlanType = c.getString(0);
                    dataList.add(myActivityPlanType);
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return dataList;
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
                            Integer.parseInt(c.getString(6)), new DateTime(c.getString(7)), new DateTime(c.getString(8)), Integer.parseInt(c.getString(9)));
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
                            Integer.parseInt(c.getString(6)), new DateTime(c.getString(7)), new DateTime(c.getString(8)), Integer.parseInt(c.getString(9)));
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
            values.put(columnCreatedAt, myActivityPlan.getCreated_at().getDateTimeString());
            if(myActivityPlan.getUpdated_at()!=null) {
                values.put(columnUpdatedAt, myActivityPlan.getUpdated_at().getDateTimeString());
            }
            values.put(columnTrainerID, myActivityPlan.getTrainer_id()+"");
            db.insert(DatabaseTable, null, values);
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }


    public int addListActivityPlan(ArrayList<ActivityPlan> myActivityPlan) {
        int count = 0;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success=false;
        try {
            for(int i=0; i<myActivityPlan.size(); i++) {
                values.put(columnID, myActivityPlan.get(i).getActivityPlanID());
                values.put(columnUserID, myActivityPlan.get(i).getUserID());
                values.put(columnType, myActivityPlan.get(i).getType());
                values.put(columnName, myActivityPlan.get(i).getActivityName());
                values.put(columnDesc,myActivityPlan.get(i).getDescription());
                values.put(columnEstimateCalories, myActivityPlan.get(i).getEstimateCalories());
                values.put(columnDuration, myActivityPlan.get(i).getDuration());
                values.put(columnCreatedAt, myActivityPlan.get(i).getCreated_at().getDateTimeString());
                if (myActivityPlan.get(i).getUpdated_at() != null) {
                    values.put(columnUpdatedAt, myActivityPlan.get(i).getUpdated_at().getDateTimeString());
                }
                values.put(columnTrainerID, myActivityPlan.get(i).getTrainer_id() + "");
                db.insert(DatabaseTable, null, values);
                count++;
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return count;
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
                columnDuration + " = ? " +
                columnCreatedAt + " = ? " +
                columnUpdatedAt + " = ? " +
                columnTrainerID + " = ? " +
                "WHERE " + columnID + " = ?";
        ContentValues values = new ContentValues();
        boolean success=false;
        try {
            //Toast.makeText(context,"DB = "+myActivityPlan.getUserID(),Toast.LENGTH_SHORT).show();
            db.execSQL(updatequery, new String[]{myActivityPlan.getUserID(), myActivityPlan.getType(), myActivityPlan.getActivityName(),
                    myActivityPlan.getDescription(), myActivityPlan.getEstimateCalories()+"", myActivityPlan.getDuration()+"",
                    myActivityPlan.getCreated_at().getDateTimeString(), myActivityPlan.getUpdated_at().getDateTimeString()
                    , myActivityPlan.getTrainer_id()+"", myActivityPlan.getActivityPlanID()});
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteActivityPlan(String ActivityPlanId) {
        boolean result = false;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete(DatabaseTable, columnID + " = ?", new String[]{ActivityPlanId});
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }

    public boolean deleteAll(){
        boolean result = false;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.execSQL("delete from " + DatabaseTable);
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }

}
