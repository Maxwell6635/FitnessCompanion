package my.com.taruc.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.RealTimeFitness;

/**
 * Created by saiboon on 10/6/2015.
 */
public class RealTimeFitnessDA {

    private Context context;
    FitnessDB testDB;

    private String databaseName = "RealTime_Fitness";
    private String columnID = "id";
    private String columnUserID = "user_id";
    private String columnCapture = "capture_datetime";
    private String columnStep = "step_number";
    private String allColumn = columnID + ", " + columnUserID +", " + columnCapture + ", " + columnStep;

    public RealTimeFitnessDA(Context context){
        this.context = context;
    }

    public ArrayList<RealTimeFitness> getAllRealTimeFitness() {
        testDB = new FitnessDB(context);
        SQLiteDatabase db = testDB.getWritableDatabase();
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
        testDB = new FitnessDB(context);
        SQLiteDatabase db = testDB.getWritableDatabase();
        ArrayList<RealTimeFitness> datalist = new ArrayList<RealTimeFitness>();
        RealTimeFitness myRealTimeFitness;
        /*String getquery = "SELECT " + allColumn +
                " FROM " + databaseName +
                " WHERE "+ columnCapture +" > datetime('" + date.getDate().getFullDate() +"') " +
                " AND "+ columnCapture + " <  datetime('" + date.getDate().getFullDate() +"', '+1 day')";*/
        String getquery = "SELECT " + allColumn +
                " FROM " + databaseName +
                " WHERE "/*+ columnCapture +" > datetime('2016-01-01') AND "*/+ columnCapture +" < datetime('2016-01-01')" ;
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
        testDB = new FitnessDB(context);
        SQLiteDatabase db = testDB.getWritableDatabase();
        ArrayList<RealTimeFitness> datalist = new ArrayList<RealTimeFitness>();
        RealTimeFitness myRealTimeFitness;
        String getquery = "SELECT " + allColumn +
                " FROM " + databaseName +
                " WHERE " + columnCapture + " > datetime('"+date.getDateTime()+"') ";
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
        testDB = new FitnessDB(context);
        SQLiteDatabase db = testDB.getWritableDatabase();
        ArrayList<RealTimeFitness> datalist = new ArrayList<RealTimeFitness>();
        RealTimeFitness myRealTimeFitness;
        String getquery = "SELECT " + allColumn +
                " FROM " + databaseName +
                " WHERE "+ columnCapture +" > datetime('"+ startDateTime.getDate().getFullDate() +"') " +
                " AND " + columnCapture +" < datetime('"+ endDateTime.getDate().getFullDate() +"')";
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
        testDB = new FitnessDB(context);
        SQLiteDatabase db = testDB.getWritableDatabase();
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

    public RealTimeFitness getRealTimeFitnessByDateTime(String datetime) {
        testDB = new FitnessDB(context);
        SQLiteDatabase db = testDB.getWritableDatabase();
        //RealTimeFitness myRealTimeFitness = new RealTimeFitness();
        RealTimeFitness myRealTimeFitness = null;
        String getquery = "SELECT " + allColumn +
                "FROM "+databaseName +" WHERE "+columnCapture+" = ? ";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{datetime});
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
        testDB = new FitnessDB(context);
        SQLiteDatabase db = testDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(columnID, myRealTimeFitness.getRealTimeFitnessID());
            values.put(columnUserID, myRealTimeFitness.getUserID());
            values.put(columnCapture, myRealTimeFitness.getCaptureDateTime().getDateTime());
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
        testDB = new FitnessDB(context);
        SQLiteDatabase db = testDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        String updatequery = "UPDATE "+databaseName+" SET "+columnCapture+" = ?, "+columnStep+" = ? " +
                "WHERE "+columnID+" = '" + myRealTimeFitness.getRealTimeFitnessID() + "' " ;
        try {
            db.execSQL(updatequery, new String[]{myRealTimeFitness.getCaptureDateTime().getDateTime(), myRealTimeFitness.getStepNumber() + ""});
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public boolean deleteRealTimeFitness(String id) {
        boolean result = false;
        SQLiteDatabase db = testDB.getWritableDatabase();
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
        testDB = new FitnessDB(context);
        SQLiteDatabase db = testDB.getWritableDatabase();
        RealTimeFitness myRealTimeFitness = new RealTimeFitness();
        String getquery = "SELECT " + allColumn +
                " FROM "+databaseName+" ORDER BY "+columnID+" DESC";
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
        String newRealTimeFitnessID="";
        RealTimeFitness lastRealTimeFitness;
        try {
            lastRealTimeFitness = getLastRealTimeFitness();
            if (lastRealTimeFitness==null){
                newRealTimeFitnessID = "RTF0001";
            }else{
                String lastRealTimeFitnessIDNum = lastRealTimeFitness.getRealTimeFitnessID().replace("RTF","") ;
                int newRealTimeFitnessIDNum = Integer.parseInt(lastRealTimeFitnessIDNum) + 1;
                if (newRealTimeFitnessIDNum > 999){
                    newRealTimeFitnessID = "RTF"+ newRealTimeFitnessIDNum;
                }
                else if (newRealTimeFitnessIDNum > 99){
                    newRealTimeFitnessID = "RTF0"+ newRealTimeFitnessIDNum;
                }
                else if(newRealTimeFitnessIDNum > 9){
                    newRealTimeFitnessID = "RTF00"+ newRealTimeFitnessIDNum;
                }else{
                    newRealTimeFitnessID = "RTF000" + newRealTimeFitnessIDNum;
                }
            }
        }catch (Exception ex){
            newRealTimeFitnessID = "RTF0001";
        }
        return newRealTimeFitnessID;
    }
}
