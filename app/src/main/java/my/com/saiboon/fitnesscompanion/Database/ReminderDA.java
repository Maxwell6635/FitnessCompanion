package my.com.saiboon.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IntegerRes;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.saiboon.fitnesscompanion.Classes.Goal;
import my.com.saiboon.fitnesscompanion.Classes.Reminder;

/**
 * Created by saiboon on 11/6/2015.
 */
public class ReminderDA {
    private Context context;
    FitnessDB fitnessDB;

    public ReminderDA(Context context){
        this.context = context;
    }

    public ArrayList<Reminder> getAllReminder() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<Reminder> datalist = new ArrayList<Reminder>();
        Reminder myReminder;
        String getquery = "SELECT Reminder_ID, User_ID, Remind_Availability, Remind_Activites, Remind_Repeat, Remind_Time," +
                "Remind_Day, Remind_Date, Remind_Month, Remind_Year FROM Reminder";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myReminder = new Reminder(c.getString(0),c.getString(1),Boolean.parseBoolean(c.getString(2)),c.getString(3),c.getString(4), c.getString(5),
                            c.getString(6),Integer.parseInt(c.getString(7)), Integer.parseInt(c.getString(8)), Integer.parseInt(c.getString(9)));
                    datalist.add(myReminder);
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public Reminder getReminder(String ReminderID) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        Reminder myReminder= new Reminder();
        String getquery = "SELECT Reminder_ID, User_ID, Remind_Availability, Remind_Activites, Remind_Repeat, Remind_Time," +
                "Remind_Day, Remind_Date, Remind_Month, Remind_Year FROM Reminder WHERE Reminder_ID = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{ReminderID});
            if (c.moveToFirst()) {
                do {
                    myReminder = new Reminder(c.getString(0),c.getString(1),Boolean.parseBoolean(c.getString(2)),c.getString(3),c.getString(4), c.getString(5),
                            c.getString(6),Integer.parseInt(c.getString(7)), Integer.parseInt(c.getString(8)), Integer.parseInt(c.getString(9)));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myReminder;
    }

    public boolean addReminder(Reminder myReminder) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success=false;
        try {
            values.put("Reminder_ID", myReminder.getReminderID());
            values.put("User_ID", myReminder.getUserID());
            if (myReminder.isAvailability()){
                values.put("Remind_Availability","TRUE");
            }else{
                values.put("Remind_Availability","FALSE");
            }
            values.put("Remind_Activites", myReminder.getRemindActivites());
            values.put("Remind_Repeat", myReminder.getRemindRepeat());
            values.put("Remind_Time", myReminder.getRemindTime());
            values.put("Remind_Day", myReminder.getRemindDay());
            values.put("Remind_Date", myReminder.getRemindDate());
            values.put("Remind_Month", myReminder.getRemindMonth());
            values.put("Remind_Year", myReminder.getRemindYear());
            db.insert("Reminder", null, values);
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return  success;
    }

    public boolean updateReminder(Reminder myReminder) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        String updatequery = "UPDATE Reminder SET User_ID = ?, Remind_Availability=?, Remind_Activites = ?, Remind_Repeat = ?, Remind_Time=?," +
                "Remind_Day=?, Remind_Date=?, Remind_Month=? ,Remind_Year=? WHERE Reminder_ID = ?";
        boolean success=false;
        try {
            db.execSQL(updatequery, new String[]{myReminder.getUserID() + "", myReminder.isAvailability()+"", myReminder.getRemindActivites(), myReminder.getRemindRepeat(), myReminder.getRemindTime() + "",
                    myReminder.getRemindDay(), myReminder.getRemindDate() + "", myReminder.getRemindMonth() + "", myReminder.getRemindYear() + "", myReminder.getReminderID()});
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteReminder(String ReminderId) {
        boolean result = false;
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete("Reminder", "Reminder_ID = ?", new String[]{ReminderId});
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }

    public Reminder getLastReminder() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        Reminder myReminder= new Reminder();
        String getquery = "SELECT Reminder_ID, User_ID, Remind_Availability, Remind_Activites, Remind_Repeat, Remind_Time,\" +\n" +
                "                \"Remind_Day, Remind_Date, Remind_Month, Remind_Year FROM Reminder ORDER BY Reminder_ID DESC";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                myReminder = new Reminder(c.getString(0),c.getString(1),Boolean.parseBoolean(c.getString(2)),c.getString(3),c.getString(4), c.getString(5),
                        c.getString(6),Integer.parseInt(c.getString(7)), Integer.parseInt(c.getString(8)), Integer.parseInt(c.getString(9)));
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myReminder;
    }

    public String generateNewReminderID(){
        String newReminderID="";
        Reminder lastReminder;
        try {
            lastReminder = getLastReminder();
            if (lastReminder==null){
                newReminderID = "RE001";
            }else{
                String lastGoalIDNum = lastReminder.getReminderID().replace("RE","");
                int newGoalIDNum = Integer.parseInt(lastGoalIDNum) + 1;
                if (newGoalIDNum>99){
                    newReminderID = "RE"+ newGoalIDNum;
                }else if (newGoalIDNum>9){
                    newReminderID = "RE0"+ newGoalIDNum;
                }else{
                    newReminderID = "RE00" + newGoalIDNum;
                }
            }
        }catch (Exception ex){
            newReminderID = "RE001";
        }
        return newReminderID;
    }

}
