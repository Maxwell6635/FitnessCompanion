package my.com.taruc.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.Reminder;

/**
 * Created by saiboon on 11/6/2015.
 */
public class ReminderDA {
    private Context context;
    FitnessDB fitnessDB;

    private String databaseName = "Reminder";
    private String columnID = "id";
    private String columnUserID = "user_id";
    private String columnAvailability = "availability";
    private String columnActivitiesID = "activities_id";
    private String columnRepeat = "repeat";
    private String columnTime = "time";
    private String columnDay = "day";
    private String columnDate = "date";
    private String columnCreatedAt = "created_at";
    private String columnUpdatedAt = "updated_at";
    private String allColumn = columnID +","+columnUserID+","+columnAvailability+","+columnActivitiesID+","+
            columnRepeat+","+columnTime+","+columnDay+","+columnDate+","+columnCreatedAt+","+columnUpdatedAt ;

    public ReminderDA(Context context){
        this.context = context;
    }

    public ArrayList<Reminder> getAllReminder() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<Reminder> datalist = new ArrayList<Reminder>();
        Reminder myReminder;
        String getquery = "SELECT "+allColumn+" FROM "+ databaseName;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myReminder = new Reminder(c.getString(0),c.getString(1),Boolean.parseBoolean(c.getString(2)),c.getString(3),c.getString(4), c.getString(5),
                            c.getString(6),Integer.parseInt(c.getString(7)), new DateTime(c.getString(8)), new DateTime(c.getString(9)));
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
        String getquery = "SELECT "+ allColumn+" FROM "+databaseName+" WHERE "+columnID+" = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{ReminderID});
            if (c.moveToFirst()) {
                do {
                    myReminder = new Reminder(c.getString(0),c.getString(1),Boolean.parseBoolean(c.getString(2)),c.getString(3),c.getString(4), c.getString(5),
                            c.getString(6),Integer.parseInt(c.getString(7)), new DateTime(c.getString(8)), new DateTime(c.getString(9)));
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
        String getquery = "SELECT "+allColumn+" FROM "+databaseName+" WHERE "+columnTime+" = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{time});
            if (c.moveToFirst()) {
                do {
                    myReminder = new Reminder(c.getString(0),c.getString(1),Boolean.parseBoolean(c.getString(2)),c.getString(3),c.getString(4), c.getString(5),
                            c.getString(6),Integer.parseInt(c.getString(7)), new DateTime(c.getString(8)), new DateTime(c.getString(9)));
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
            values.put(columnID, myReminder.getReminderID());
            values.put(columnUserID, myReminder.getUserID());
            if (myReminder.isAvailability()){
                values.put(columnAvailability,"TRUE");
            }else{
                values.put(columnAvailability,"FALSE");
            }
            values.put(columnActivitiesID, myReminder.getActivitesPlanID());
            values.put(columnRepeat, myReminder.getRemindRepeat());
            values.put(columnTime, myReminder.getRemindTime());
            values.put(columnDay, myReminder.getRemindDay());
            values.put(columnDate, myReminder.getRemindDate());
            values.put(columnCreatedAt, myReminder.getCreatedAt().getDateTimeString());
            if(myReminder.getUpdatedAt()!=null){
                values.put(columnUpdatedAt, myReminder.getUpdatedAt().getDateTimeString());
            }
            db.insert(databaseName, null, values);
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return  success;
    }

    public int addListReminder(ArrayList<Reminder> reminderArrayList) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        int count = 0;
        int done = 0;
        try {
            for(int i=0; i < reminderArrayList.size(); i++) {
                values.put(columnID, reminderArrayList.get(i).getReminderID());
                values.put(columnUserID, reminderArrayList.get(i).getUserID());
                if(reminderArrayList.get(i).isAvailability()) {
                    done = 1;
                }
                values.put(columnAvailability, done);
                values.put(columnActivitiesID, reminderArrayList.get(i).getActivitesPlanID());
                values.put(columnRepeat, reminderArrayList.get(i).getRemindRepeat());
                values.put(columnTime, reminderArrayList.get(i).getRemindTime());
                values.put(columnDay, reminderArrayList.get(i).getRemindDay());
                values.put(columnDate, reminderArrayList.get(i).getRemindDate());
                values.put(columnCreatedAt, reminderArrayList.get(i).getCreatedAt().getDateTimeString());
                values.put(columnUpdatedAt, reminderArrayList.get(i).getUpdatedAt().getDateTimeString());
                db.insert(databaseName, null, values);
                count++;
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return count;
    }

    public boolean updateReminder(Reminder myReminder) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        String updatequery = "UPDATE "+databaseName+" SET "+columnUserID+" = ?, "+columnAvailability+" = ?, "+columnActivitiesID+" = ?, "+columnRepeat+" = ?, "+columnTime+"=?," +
                columnDay+"=?, "+columnDate+"=?, "+ columnCreatedAt+ "=?, "+ columnUpdatedAt +"=? WHERE "+columnID+" = ?";
        boolean success=false;
        try {
            db.execSQL(updatequery, new String[]{myReminder.getUserID() + "", myReminder.isAvailability()+"", myReminder.getActivitesPlanID(), myReminder.getRemindRepeat(), myReminder.getRemindTime() + "",
                    myReminder.getRemindDay(), myReminder.getRemindDate() + "", myReminder.getCreatedAt().getDateTimeString(), myReminder.getUpdatedAt().getDateTimeString(), myReminder.getReminderID()});
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteReminder(String ReminderId) {
        boolean result = false;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete(databaseName, columnID+" = ?", new String[]{ReminderId});
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
        String getquery = "SELECT "+allColumn+" FROM "+databaseName+" ORDER BY "+columnID+" DESC";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                myReminder = new Reminder(c.getString(0),c.getString(1),Boolean.parseBoolean(c.getString(2)),c.getString(3),c.getString(4), c.getString(5),
                        c.getString(6),Integer.parseInt(c.getString(7)), new DateTime(c.getString(8)), new DateTime(c.getString(9)));
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
