package my.com.taruc.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.Event;
import my.com.taruc.fitnesscompanion.Classes.RealTimeFitness;
import my.com.taruc.fitnesscompanion.Classes.SleepData;

/**
 * Created by saiboon on 26/1/2016.
 */
public class SleepDataDA {
    private Context context;
    FitnessDB fitnessDB;

    private String databaseTable = "Sleep_Data";
    private String columnID = "id";
    private String columnUserID = "user_id";
    private String columnMovement = "movement";
    private String columnCreatedAt = "created_at";
    private String columnUpdatedAt = "updated_at";
    private String allColumn = columnID +","+columnUserID+","+columnMovement+","+columnCreatedAt+","+columnUpdatedAt ;

    public SleepDataDA(Context context){
        this.context = context;
    }

    public ArrayList<SleepData> getAllSleepData() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<SleepData> datalist = new ArrayList<SleepData>();
        SleepData mySleepData;
        String getquery = "SELECT "+ allColumn +" FROM "+ databaseTable;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    mySleepData = new SleepData(c.getString(0), c.getString(1), Integer.parseInt(c.getString(2)), new DateTime(c.getString(3)), new DateTime(c.getString(4)));
                    datalist.add(mySleepData);
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ArrayList<SleepData> getAllSleepDataPerDay(DateTime date) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<SleepData> datalist = new ArrayList<SleepData>();
        SleepData mySleepData;
        String getquery = "SELECT " + allColumn +
                " FROM " + databaseTable +
                " WHERE "+ columnCreatedAt +" > datetime('" + date.getDateTimeString() +"') " +
                " AND "+ columnCreatedAt + " <  datetime('" + date.getDate().getFullDateString() +" 12:00:00', '+1 day')";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    mySleepData = new SleepData(c.getString(0), c.getString(1), Integer.parseInt(c.getString(2)), new DateTime(c.getString(3)), new DateTime(c.getString(4)));
                    datalist.add(mySleepData);
                } while (c.moveToNext());
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public SleepData getSleepData(String SleepDataID) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        SleepData mySleepData = new SleepData();
        String getquery = "SELECT "+ allColumn+" FROM "+databaseTable+" WHERE "+columnID+" = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{SleepDataID});
            if (c.moveToFirst()) {
                do {
                    mySleepData = new SleepData( c.getString(0), c.getString(1), Integer.parseInt(c.getString(2)), new DateTime(c.getString(3)), new DateTime(c.getString(4)));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return mySleepData;
    }

    public boolean addSleepData(SleepData mySleepData) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success = false;
        try {
            values.put(columnID, mySleepData.getSleepDataID());
            values.put(columnUserID, mySleepData.getUserID());
            values.put(columnMovement, mySleepData.getMovement());
            values.put(columnCreatedAt, mySleepData.getCreated_at().getDateTimeString());
            values.put(columnUpdatedAt, mySleepData.getUpdated_at().getDateTimeString());
            db.insert(databaseTable, null, values);
            success = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public int addListSleepData(ArrayList<SleepData> mySleepData) {
        int count = 0;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success=false;
        try {
            for(int i=0; i<mySleepData.size(); i++) {
                values.put(columnID, mySleepData.get(i).getSleepDataID());
                values.put(columnUserID, mySleepData.get(i).getUserID());
                values.put(columnMovement, mySleepData.get(i).getMovement());
                values.put(columnCreatedAt, mySleepData.get(i).getCreated_at().getDateTimeString());
                values.put(columnUpdatedAt, mySleepData.get(i).getUpdated_at().getDateTimeString());
                db.insert(databaseTable, null, values);
                count++;
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return count;
    }


    public boolean deleteSleepData(String SleepDataId) {
        boolean result = false;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete(databaseTable, columnID + " = ?", new String[]{SleepDataId});
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }

    public boolean deleteAllSleepData(){
        boolean result = false;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.execSQL("delete from " + databaseTable);
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }

    public SleepData getLastSleepData() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        SleepData mySleepData = new SleepData();
        String getquery = "SELECT * FROM "+ databaseTable + " ORDER BY " + columnCreatedAt + " DESC";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                mySleepData = new SleepData(c.getString(0), c.getString(1), Integer.parseInt(c.getString(2)), new DateTime(c.getString(3)), new DateTime(c.getString(4)));
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return mySleepData;
    }

    public String generateNewSleepDataID(){
        String newSleepDataID="";
        SleepData lastSleepData;
        DateTime currentDate = new DateTime().getCurrentDateTime();
        String formattedDate = currentDate.getDate().getTrimCurrentDateString(); //current date
        try {
            lastSleepData = getLastSleepData();
            String[] lastSleepID = lastSleepData.getSleepDataID().split("SD");
            if (lastSleepData==null||lastSleepData.getSleepDataID().equals("")){
                newSleepDataID = formattedDate+"SD001";
            }
            else if (!lastSleepID[0].equals(formattedDate)){
                newSleepDataID = formattedDate+"SD001" ;
                //Toast.makeText(context,"New day for new sleep data id",Toast.LENGTH_SHORT).show();
            }
            else{
                String lastFitnessRecordIDNum = lastSleepID[1];
                int newFitnessRecordIDNum = Integer.parseInt(lastFitnessRecordIDNum) + 1;
                if (newFitnessRecordIDNum>99){
                    newSleepDataID = formattedDate + "SD" + newFitnessRecordIDNum ;
                }
                else if(newFitnessRecordIDNum>9){
                    newSleepDataID =  formattedDate+ "SD"+  "0"+ newFitnessRecordIDNum;
                }else{
                    newSleepDataID = formattedDate +"SD"+ "00" + newFitnessRecordIDNum ;
                }
            }
        }catch (Exception ex){
            newSleepDataID = formattedDate + "SD001" ;
            //Toast.makeText(context,"Generate last record fail",Toast.LENGTH_SHORT).show();
        }
        return newSleepDataID;
    }
}
