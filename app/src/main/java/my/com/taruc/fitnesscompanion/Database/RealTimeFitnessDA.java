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
import my.com.taruc.fitnesscompanion.Classes.RealTimeFitness;

/**
 * Created by saiboon on 10/6/2015.
 */
public class RealTimeFitnessDA {

    private Context context;
    private FitnessDB mFitnessDB;

    private String databaseName = "RealTime_Fitness";
    private String columnID = "id";
    private String columnUserID = "user_id";
    private String columnCapture = "capture_datetime";
    private String columnStep = "step_number";
    private String allColumn = columnID + ", " + columnUserID +", " + columnCapture + ", " + columnStep ;

    public RealTimeFitnessDA(Context context){
        this.context = context;
    }

    public ArrayList<RealTimeFitness> getAllRealTimeFitness() {
        mFitnessDB = new FitnessDB(context);
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        ArrayList<RealTimeFitness> datalist = new ArrayList<RealTimeFitness>();
        RealTimeFitness myRealTimeFitness;
        String getquery = "SELECT " + allColumn + " FROM " + databaseName;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), new DateTime(c.getString(2)), Integer.parseInt(c.getString(3)));
                    datalist.add(myRealTimeFitness);
                } while (c.moveToNext());
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ArrayList<RealTimeFitness> getAllRealTimeFitnessPerDay(DateTime date) {
        mFitnessDB = new FitnessDB(context);
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        ArrayList<RealTimeFitness> datalist = new ArrayList<RealTimeFitness>();
        RealTimeFitness myRealTimeFitness;
        String getquery = "SELECT " + allColumn +
                " FROM " + databaseName +
                " WHERE "+ columnCapture +" > date('" + date.getDate().getFullDateString() +"') " +
                " AND "+ columnCapture + " <  datetime('" + date.getDate().getFullDateString() +" 01:00:00', '+1 day')";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), new DateTime(c.getString(2)), Integer.parseInt(c.getString(3)));
                    datalist.add(myRealTimeFitness);
                } while (c.moveToNext());
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ArrayList<RealTimeFitness> getAllRealTimeFitnessAfter(DateTime date) {
        mFitnessDB = new FitnessDB(context);
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        ArrayList<RealTimeFitness> datalist = new ArrayList<RealTimeFitness>();
        RealTimeFitness myRealTimeFitness;
        String getquery = "SELECT " + allColumn +
                " FROM " + databaseName +
                " WHERE " + columnCapture + " > datetime('" + date.getDateTimeString() + "') ";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), new DateTime(c.getString(2)), Integer.parseInt(c.getString(3)));
                    datalist.add(myRealTimeFitness);
                } while (c.moveToNext());
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ArrayList<RealTimeFitness> getAllRealTimeFitnessAfterLimit(DateTime date) {
        mFitnessDB = new FitnessDB(context);
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        ArrayList<RealTimeFitness> datalist = new ArrayList<RealTimeFitness>();
        RealTimeFitness myRealTimeFitness;
        String getquery = "SELECT " + allColumn +
                " FROM " + databaseName +
                " WHERE " + columnCapture + " > datetime('" + date.getDateTimeString() + "') "+
                " AND "+ columnCapture + " <  datetime('" + date.getDate().getFullDateString() +" 01:00:00', '+1 day')";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), new DateTime(c.getString(2)), Integer.parseInt(c.getString(3)));
                    datalist.add(myRealTimeFitness);
                } while (c.moveToNext());
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ArrayList<RealTimeFitness> getAllRealTimeFitnessBeforeLimit(DateTime date) {
        mFitnessDB = new FitnessDB(context);
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        ArrayList<RealTimeFitness> datalist = new ArrayList<RealTimeFitness>();
        RealTimeFitness myRealTimeFitness;
        String getquery = "SELECT " + allColumn +
                " FROM " + databaseName +
                " WHERE " + columnCapture + " < datetime('" + date.getDateTimeString() + "') "+
                " AND "+ columnCapture + " >  datetime('" + date.getDate().getFullDateString() +" 01:00:00', '-1 day')";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), new DateTime(c.getString(2)), Integer.parseInt(c.getString(3)));
                    datalist.add(myRealTimeFitness);
                } while (c.moveToNext());
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ArrayList<RealTimeFitness> getAllRealTimeFitnessBetweenDate(DateTime startDateTime, DateTime endDateTime) {
        mFitnessDB = new FitnessDB(context);
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        ArrayList<RealTimeFitness> datalist = new ArrayList<RealTimeFitness>();
        RealTimeFitness myRealTimeFitness;
        String getquery = "SELECT " + allColumn +
                " FROM " + databaseName +
                " WHERE "+ columnCapture +" > date('"+ startDateTime.getDate().getFullDateString() +"') " +
                " AND " + columnCapture +" < date('"+ endDateTime.getDate().getFullDateString() +" 01:00:00', '+1 day')";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), new DateTime(c.getString(2)), Integer.parseInt(c.getString(3)));
                    datalist.add(myRealTimeFitness);
                } while (c.moveToNext());
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ArrayList<RealTimeFitness> getAllRealTimeFitnessBetweenDateTime(DateTime startDateTime, DateTime endDateTime) {
        mFitnessDB = new FitnessDB(context);
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        ArrayList<RealTimeFitness> datalist = new ArrayList<RealTimeFitness>();
        RealTimeFitness myRealTimeFitness;
        String getquery = "SELECT " + allColumn +
                " FROM " + databaseName +
                " WHERE "+ columnCapture +" > datetime('"+ startDateTime.getDateTimeString() +"') " +
                " AND " + columnCapture +" < datetime('"+ endDateTime.getDateTimeString() +"')";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), new DateTime(c.getString(2)), Integer.parseInt(c.getString(3)));
                    datalist.add(myRealTimeFitness);
                } while (c.moveToNext());
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public RealTimeFitness getRealTimeFitness(String id) {
        mFitnessDB = new FitnessDB(context);
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        RealTimeFitness myRealTimeFitness = new RealTimeFitness();
        String getquery = "SELECT " + allColumn +
                "FROM "+ databaseName +" WHERE " +columnID+ " = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{id});
            if (c.moveToFirst()) {
                do {
                    myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), new DateTime(c.getString(2)), Integer.parseInt(c.getString(3)));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myRealTimeFitness;
    }

    public RealTimeFitness getRealTimeFitnessByDateTime(DateTime datetime) {
        mFitnessDB = new FitnessDB(context);
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
//        RealTimeFitness myRealTimeFitness = new RealTimeFitness();
        RealTimeFitness myRealTimeFitness = null;
        String getquery = "SELECT " + allColumn +
                " FROM "+ databaseName +" WHERE "+ columnCapture +" = ? ";
        //String query = "Select * From RealTime_Fitness Where capture_datetime = datetime('2016-01-01 02:00:00')";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{datetime.getDateTimeString()});
            //Cursor c = db.rawQuery(query,null);
            if (c.moveToFirst()) {
                do {
                    myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), new DateTime(c.getString(2)), Integer.parseInt(c.getString(3)));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myRealTimeFitness;
    }

    public boolean addRealTimeFitness(RealTimeFitness myRealTimeFitness) {
        boolean result = true;
        mFitnessDB = new FitnessDB(context);
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(columnID, myRealTimeFitness.getRealTimeFitnessID());
            values.put(columnUserID, myRealTimeFitness.getUserID());
            values.put(columnCapture, myRealTimeFitness.getCaptureDateTime().getDateTimeString());
            values.put(columnStep, myRealTimeFitness.getStepNumber());
            db.insert(databaseName, null, values);
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            result = false;
        }
        db.close();
        return result;
    }

    public void updateRealTimeFitness(RealTimeFitness myRealTimeFitness) {
        mFitnessDB = new FitnessDB(context);
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        String updatequery = "UPDATE "+databaseName+" SET "+columnCapture+" = ?, "+columnStep+" = ? " +
                "WHERE "+columnID+" = '" + myRealTimeFitness.getRealTimeFitnessID() + "' " ;
        try {
            db.execSQL(updatequery, new String[]{myRealTimeFitness.getCaptureDateTime().getDateTimeString(), myRealTimeFitness.getStepNumber() + ""});
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public boolean deleteRealTimeFitness(String id) {
        boolean result = false;
        mFitnessDB = new FitnessDB(context);
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        try {
            db.delete(databaseName, columnID+" = ?", new String[] {id});
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }

    public RealTimeFitness getLastRealTimeFitness() {
        mFitnessDB = new FitnessDB(context);
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        RealTimeFitness myRealTimeFitness = new RealTimeFitness();
        String getquery = "SELECT " + allColumn +
                " FROM "+databaseName+" ORDER BY "+columnCapture+" DESC";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), new DateTime(c.getString(2)), Integer.parseInt(c.getString(3)));
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myRealTimeFitness;
    }

    public String generateNewRealTimeFitnessID(){
        String newRealTimeFitnessRecordID="";
        RealTimeFitness lastRealTimeFitnessRecord;
        DateTime currentDateTime = new DateTime().getCurrentDateTime();
        String formattedDate = currentDateTime.getDate().getTrimCurrentDateString(); //current date
        try {
            lastRealTimeFitnessRecord = getLastRealTimeFitness();
            String[] lastFitnessID = lastRealTimeFitnessRecord.getRealTimeFitnessID().split("RTF");
            if (lastRealTimeFitnessRecord==null||lastRealTimeFitnessRecord.getRealTimeFitnessID().equals("")){
                newRealTimeFitnessRecordID = formattedDate+"RTF001";
            }
            else if (!lastFitnessID[0].equals(formattedDate)){
                newRealTimeFitnessRecordID = formattedDate+"RTF001" ;
                //Toast.makeText(context,"New day for new real time fitness record id",Toast.LENGTH_SHORT).show();
            }
            else{
                String lastFitnessRecordIDNum = lastFitnessID[1];
                int newFitnessRecordIDNum = Integer.parseInt(lastFitnessRecordIDNum) + 1;
                if (newFitnessRecordIDNum>99){
                    newRealTimeFitnessRecordID = formattedDate + "RTF" + newFitnessRecordIDNum ;
                }
                else if(newFitnessRecordIDNum>9){
                    newRealTimeFitnessRecordID =  formattedDate+ "RTF"+  "0"+ newFitnessRecordIDNum;
                }else{
                    newRealTimeFitnessRecordID = formattedDate +"RTF"+ "00" + newFitnessRecordIDNum ;
                }
            }
        }catch (Exception ex){
            newRealTimeFitnessRecordID = formattedDate + "RTF001" ;
            //Toast.makeText(context,"Generate last record fail",Toast.LENGTH_SHORT).show();
        }
        return newRealTimeFitnessRecordID;
    }
}
