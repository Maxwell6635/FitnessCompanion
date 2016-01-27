package my.com.taruc.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;

/**
 * Created by saiboon on 13/6/2015.
 */
public class FitnessRecordDA {
    private Context context;
    FitnessDB fitnessDB;

    private  String databaseName = "Fitness_Record";
    private String columnID = "id";
    private String columnUserID = "user_id";
    private String columnActivitiesID = "activities_id";
    private String columnDuration = "record_duration";
    private String columnDistance = "record_distance";
    private String columnCalories = "record_calories";
    private String columnStep = "record_step";
    private String columnHeartRate = "average_heart_rate";
    private String columnCreatedAt = "created_at";
    private String columnUpdatedAt = "updated_at";
    private String allColumn = columnID + ", " + columnUserID + ", " + columnActivitiesID + ", " +
            columnDuration+ ", "+ columnDistance+", " +columnCalories +", " + columnStep +", " +
            columnHeartRate + ", " + columnCreatedAt + ", " + columnUpdatedAt;

    public FitnessRecordDA(Context context){
        this.context = context;
    }

    public ArrayList<FitnessRecord> getAllFitnessRecord() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<FitnessRecord> datalist = new ArrayList<FitnessRecord>();
        FitnessRecord myFitnessRecord;
        String getquery = "SELECT " + allColumn + " FROM " + databaseName;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myFitnessRecord = new FitnessRecord(c.getString(0), c.getString(1), c.getString(2), Integer.parseInt(c.getString(3)), Double.parseDouble(c.getString(4)),
                            Double.parseDouble(c.getString(5)), Integer.parseInt(c.getString(6)), Double.parseDouble(c.getString(7)), new DateTime(c.getString(8)), new DateTime(c.getString(9)));
                    datalist.add(myFitnessRecord);
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ArrayList<FitnessRecord> getAllFitnessRecordBetweenDateTime(DateTime startDateTime, DateTime endDateTime) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<FitnessRecord> datalist = new ArrayList<FitnessRecord>();
        FitnessRecord myFitnessRecord;
        DateTime currentDateTime = new DateTime();
        currentDateTime = currentDateTime.getCurrentDateTime();
        String getquery = "SELECT "+ allColumn + " FROM "+ databaseName +
                " WHERE " + columnCreatedAt + " >= datetime('"+ startDateTime.getDate().getFullDateString()+"') " +
                " AND " + columnCreatedAt + " <= datetime('"+ endDateTime.getDate().getFullDateString()+"')";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myFitnessRecord = new FitnessRecord(c.getString(0), c.getString(1), c.getString(2), Integer.parseInt(c.getString(3)), Double.parseDouble(c.getString(4)),
                            Double.parseDouble(c.getString(5)), Integer.parseInt(c.getString(6)), Double.parseDouble(c.getString(7)), new DateTime(c.getString(8)), new DateTime(c.getString(9)));
                    datalist.add(myFitnessRecord);
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ArrayList<FitnessRecord> getAllFitnessRecordPerDay(DateTime date) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<FitnessRecord> datalist = new ArrayList<FitnessRecord>();
        FitnessRecord myFitnessRecord;
        String getquery = "SELECT " + allColumn + " FROM " + databaseName +
                " WHERE " + columnCreatedAt + " > date('"+date.getDate().getFullDateString()+"') " +
                " AND " + columnCreatedAt + " <  date('"+date.getDate().getFullDateString()+"', '+1 day')";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myFitnessRecord = new FitnessRecord(c.getString(0), c.getString(1), c.getString(2), Integer.parseInt(c.getString(3)), Double.parseDouble(c.getString(4)),
                            Double.parseDouble(c.getString(5)), Integer.parseInt(c.getString(6)), Double.parseDouble(c.getString(7)), new DateTime(c.getString(8)), new DateTime(c.getString(9)));
                    datalist.add(myFitnessRecord);
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public FitnessRecord getFitnessRecord(String FitnessRecordID) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        FitnessRecord myFitnessRecord= new FitnessRecord();
        String getquery = "SELECT "+ allColumn + " FROM "+ databaseName+" WHERE "+ columnID+" = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{FitnessRecordID});
            if (c.moveToFirst()) {
                do {
                    myFitnessRecord = new FitnessRecord(c.getString(0), c.getString(1), c.getString(2), Integer.parseInt(c.getString(3)), Double.parseDouble(c.getString(4)),
                            Double.parseDouble(c.getString(5)), Integer.parseInt(c.getString(6)), Double.parseDouble(c.getString(7)), new DateTime(c.getString(8)), new DateTime(c.getString(9)));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myFitnessRecord;
    }

    public boolean addFitnessRecord(FitnessRecord myFitnessRecord) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success=false;
        try {
            values.put(columnID, myFitnessRecord.getFitnessRecordID());
            values.put(columnUserID, myFitnessRecord.getUserID());
            values.put(columnActivitiesID, myFitnessRecord.getActivityPlanID());
            values.put(columnDuration, myFitnessRecord.getRecordDuration());
            values.put(columnDistance, myFitnessRecord.getRecordDistance());
            values.put(columnCalories, myFitnessRecord.getRecordCalories());
            values.put(columnStep, myFitnessRecord.getRecordStep());
            values.put(columnHeartRate, myFitnessRecord.getAverageHeartRate());
            values.put(columnCreatedAt, myFitnessRecord.getCreateAt().getDateTimeString());
            if(myFitnessRecord.getUpdateAt()!=null) {
                values.put(columnUpdatedAt, myFitnessRecord.getUpdateAt().getDateTimeString());
            }
            db.insert(databaseName, null, values);
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean updateFitnessRecord(FitnessRecord myFitnessRecord) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        String updatequery = "UPDATE "+ databaseName+" SET "+columnUserID+" = ?, "+columnActivitiesID+" = ?, "+ columnDuration+" = ?, "+columnDistance+" = ?," +
                columnCalories+"=?, "+columnStep+"=?, "+columnHeartRate+"=?, "+columnCreatedAt+"=?  WHERE "+ columnID+" = ?";
        boolean success=false;
        try {
            db.execSQL(updatequery, new String[]{myFitnessRecord.getUserID() + "", myFitnessRecord.getActivityPlanID(), myFitnessRecord.getRecordDuration() + "",
                    myFitnessRecord.getRecordDistance() + "", myFitnessRecord.getRecordCalories() + "", myFitnessRecord.getRecordStep() + "", myFitnessRecord.getAverageHeartRate() + "",
                    myFitnessRecord.getCreateAt().getDateTimeString(), myFitnessRecord.getUpdateAt().getDateTimeString(), myFitnessRecord.getFitnessRecordID()});
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteFitnessRecord(String FitnessRecordId) {
        boolean result = false;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete(databaseName, columnID+" = ?", new String[]{FitnessRecordId});
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }

    public FitnessRecord getLastFitnessRecord() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        FitnessRecord myFitnessRecord= new FitnessRecord();
        String getquery = "SELECT "+ allColumn+" FROM "+ databaseName+" ORDER BY "+ columnID+" DESC";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                myFitnessRecord = new FitnessRecord(c.getString(0), c.getString(1), c.getString(2), Integer.parseInt(c.getString(3)), Double.parseDouble(c.getString(4)),
                        Double.parseDouble(c.getString(5)), Integer.parseInt(c.getString(6)), Double.parseDouble(c.getString(7)), new DateTime(c.getString(8)), new DateTime(c.getString(9)));
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myFitnessRecord;
    }

    public String generateNewFitnessRecordID(){
        String newFitnessRecordID="";
        FitnessRecord lastFitnessRecord;
        Calendar c = Calendar.getInstance();
        String myDate = c.get(Calendar.DATE) + "/"+ (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR);
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObj = new Date();
        try {
            dateObj = curFormater.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
        String formattedDate = df.format(dateObj); //current date

        try {
            lastFitnessRecord = getLastFitnessRecord();
            String[] lastFitnessID = lastFitnessRecord.getFitnessRecordID().split("FR");
            if (lastFitnessRecord==null||lastFitnessRecord.getFitnessRecordID().equals("")){
                newFitnessRecordID = formattedDate+"FR001";
            }
            else if (!lastFitnessID[0].equals(formattedDate)){
                newFitnessRecordID = formattedDate+"FR001" ;
                //Toast.makeText(context,"New day for new fitness record id",Toast.LENGTH_SHORT).show();
            }
            else{
                String lastFitnessRecordIDNum = lastFitnessID[1];
                int newFitnessRecordIDNum = Integer.parseInt(lastFitnessRecordIDNum) + 1;
                if (newFitnessRecordIDNum>99){
                    newFitnessRecordID = formattedDate + "FR" + newFitnessRecordIDNum ;
                }
                else if(newFitnessRecordIDNum>9){
                    newFitnessRecordID =  formattedDate+ "FR"+  "0"+ newFitnessRecordIDNum;
                }else{
                    newFitnessRecordID = formattedDate +"FR"+ "00" + newFitnessRecordIDNum ;
                }
            }
        }catch (Exception ex){
            newFitnessRecordID = formattedDate + "FR001" ;
            //Toast.makeText(context,"Generate last record fail",Toast.LENGTH_SHORT).show();
        }
        return newFitnessRecordID;
    }

    public String getActivityPlanName(String activityPlanID) {
        ActivityPlanDA activityPlanDA = new ActivityPlanDA(context);
        ActivityPlan activityPlan = activityPlanDA.getActivityPlan(activityPlanID);
        if (activityPlan != null) {
            return activityPlan.getActivityName();
        } else {
            Toast.makeText(context, "Fail to get activity plan.", Toast.LENGTH_SHORT);
            return "";
        }
    }

}
