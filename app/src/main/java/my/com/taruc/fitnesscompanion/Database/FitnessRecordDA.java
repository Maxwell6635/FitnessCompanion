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

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;

/**
 * Created by saiboon on 13/6/2015.
 */
public class FitnessRecordDA {
    private Context context;
    FitnessDB fitnessDB;

    public FitnessRecordDA(Context context){
        this.context = context;
    }

    public ArrayList<FitnessRecord> getAllFitnessRecord() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<FitnessRecord> datalist = new ArrayList<FitnessRecord>();
        FitnessRecord myFitnessRecord;
        String getquery = "SELECT id, user_id, activities_id, record_duration, record_distance, record_calories," +
                "record_step, average_heart_rate, created_at FROM Fitness_Record";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myFitnessRecord = new FitnessRecord(c.getString(0), c.getString(1), c.getString(2), Integer.parseInt(c.getString(3)), Double.parseDouble(c.getString(4)),
                            Double.parseDouble(c.getString(5)), Integer.parseInt(c.getString(6)), Double.parseDouble(c.getString(7)), c.getString(8));
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
        String getquery = "SELECT id, user_id, activities_id, record_duration, record_distance, record_calories," +
                "record_step, average_heart_rate, created_at FROM Fitness_Record "+
                "WHERE created_at >= datetime('"+ startDateTime.getDate().getFullDate()+"') " +
                "AND created_at <= datetime('"+ endDateTime.getDate().getFullDate()+"')";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myFitnessRecord = new FitnessRecord(c.getString(0), c.getString(1), c.getString(2), Integer.parseInt(c.getString(3)), Double.parseDouble(c.getString(4)),
                            Double.parseDouble(c.getString(5)), Integer.parseInt(c.getString(6)), Double.parseDouble(c.getString(7)), c.getString(8));
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
        String getquery = "SELECT id, user_id, activities_id, record_duration, record_distance, record_calories," +
                "record_step, average_heart_rate, created_at FROM Fitness_Record "+
                "WHERE created_at > datetime('"+date.getDate().getFullDate()+"') " +
                "AND created_at <  datetime('"+date.getDate().getFullDate()+"', '+1 day')";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myFitnessRecord = new FitnessRecord(c.getString(0), c.getString(1), c.getString(2), Integer.parseInt(c.getString(3)), Double.parseDouble(c.getString(4)),
                            Double.parseDouble(c.getString(5)), Integer.parseInt(c.getString(6)), Double.parseDouble(c.getString(7)), c.getString(8));
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
        String getquery = "SELECT id, user_id, activities_id, record_duration, record_distance, record_calories," +
                "record_step, average_heart_rate, created_at FROM Fitness_Record WHERE id = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{FitnessRecordID});
            if (c.moveToFirst()) {
                do {
                    myFitnessRecord = new FitnessRecord(c.getString(0), c.getString(1), c.getString(2), Integer.parseInt(c.getString(3)), Double.parseDouble(c.getString(4)),
                            Double.parseDouble(c.getString(5)), Integer.parseInt(c.getString(6)), Double.parseDouble(c.getString(7)), c.getString(8));
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
            values.put("id", myFitnessRecord.getFitnessRecordID());
            values.put("user_id", myFitnessRecord.getUserID());
            values.put("activities_id", myFitnessRecord.getActivityPlanID());
            values.put("record_duration", myFitnessRecord.getRecordDuration());
            values.put("record_distance", myFitnessRecord.getRecordDistance());
            values.put("record_calories", myFitnessRecord.getRecordCalories());
            values.put("record_step", myFitnessRecord.getRecordStep());
            values.put("average_heart_rate", myFitnessRecord.getAverageHeartRate());
            values.put("created_at", myFitnessRecord.getCreateAt());
            db.insert("Fitness_Record", null, values);
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
        String updatequery = "UPDATE Fitness_Record SET user_id = ?, activities_id = ?, record_duration = ?, record_distance = ?," +
                "record_calories=?, record_step=?, average_heart_rate=?, created_at=?  WHERE id = ?";
        boolean success=false;
        try {
            db.execSQL(updatequery, new String[]{myFitnessRecord.getUserID()+"", myFitnessRecord.getActivityPlanID(), myFitnessRecord.getRecordDuration() + "",
                    myFitnessRecord.getRecordDistance() + "", myFitnessRecord.getRecordCalories() + "", myFitnessRecord.getRecordStep() + "", myFitnessRecord.getAverageHeartRate() + "",
                    myFitnessRecord.getCreateAt(), myFitnessRecord.getFitnessRecordID()});
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteFitnessRecord(String FitnessRecordId) {
        boolean result = false;
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete("Fitness_Record", "id = ?", new String[]{FitnessRecordId});
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
        String getquery = "SELECT id, user_id, activities_id, record_duration, record_distance, record_calories," +
                " record_step, average_heart_rate, created_at FROM Fitness_Record ORDER BY id DESC";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                myFitnessRecord = new FitnessRecord(c.getString(0), c.getString(1), c.getString(2), Integer.parseInt(c.getString(3)), Double.parseDouble(c.getString(4)),
                        Double.parseDouble(c.getString(5)), Integer.parseInt(c.getString(6)), Double.parseDouble(c.getString(7)), c.getString(8));
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
                Toast.makeText(context,"New day for new fitness record id",Toast.LENGTH_SHORT).show();
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

}
