package my.com.taruc.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.Reminder;

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
        String getquery = "SELECT id, user_id, availability, activities_id, repeat, time, day, date FROM Reminder";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myReminder = new Reminder(c.getString(0),c.getString(1),Boolean.parseBoolean(c.getString(2)),c.getString(3),c.getString(4), c.getString(5),
                            c.getString(6),Integer.parseInt(c.getString(7)));
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
        String getquery = "SELECT id, user_id, availability, activities_id, repeat, time, day, date FROM Reminder WHERE id = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{ReminderID});
            if (c.moveToFirst()) {
                do {
                    myReminder = new Reminder(c.getString(0),c.getString(1),Boolean.parseBoolean(c.getString(2)),c.getString(3),c.getString(4), c.getString(5),
                            c.getString(6),Integer.parseInt(c.getString(7)));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myReminder;
    }

    public Reminder getReminderByTime(String time) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        Reminder myReminder= new Reminder();
        String getquery = "SELECT id, user_id, availability, activities_id, repeat, time, day, date FROM Reminder WHERE time = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{time});
            if (c.moveToFirst()) {
                do {
                    myReminder = new Reminder(c.getString(0),c.getString(1),Boolean.parseBoolean(c.getString(2)),c.getString(3),c.getString(4), c.getString(5),
                            c.getString(6),Integer.parseInt(c.getString(7)));
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
            values.put("id", myReminder.getReminderID());
            values.put("user_id", myReminder.getUserID());
            if (myReminder.isAvailability()){
                values.put("availability","TRUE");
            }else{
                values.put("availability","FALSE");
            }
            values.put("activities_id", myReminder.getActivitesPlanID());
            values.put("repeat", myReminder.getRemindRepeat());
            values.put("time", myReminder.getRemindTime());
            values.put("day", myReminder.getRemindDay());
            values.put("date", myReminder.getRemindDate());
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
        String updatequery = "UPDATE Reminder SET user_id = ?, availability = ?, activities_id = ?, repeat = ?, time=?," +
                "day=?, date=? WHERE id = ?";
        boolean success=false;
        try {
            db.execSQL(updatequery, new String[]{myReminder.getUserID() + "", myReminder.isAvailability()+"", myReminder.getActivitesPlanID(), myReminder.getRemindRepeat(), myReminder.getRemindTime() + "",
                    myReminder.getRemindDay(), myReminder.getRemindDate() + "", myReminder.getReminderID()});
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
            db.delete("Reminder", "id = ?", new String[]{ReminderId});
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
        String getquery = "SELECT  id, user_id, availability, activities_id, repeat, time, day, date FROM Reminder ORDER BY id DESC";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                myReminder = new Reminder(c.getString(0),c.getString(1),Boolean.parseBoolean(c.getString(2)),c.getString(3),c.getString(4), c.getString(5),
                        c.getString(6),Integer.parseInt(c.getString(7)));
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
