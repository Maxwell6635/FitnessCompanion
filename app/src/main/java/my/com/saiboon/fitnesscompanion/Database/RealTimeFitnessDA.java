package my.com.saiboon.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.saiboon.fitnesscompanion.Classes.DateTime;
import my.com.saiboon.fitnesscompanion.Classes.RealTimeFitness;

/**
 * Created by saiboon on 10/6/2015.
 */
public class RealTimeFitnessDA {

    private Context context;
    FitnessDB testDB;

    public RealTimeFitnessDA(Context context){
        this.context = context;
    }

    public ArrayList<RealTimeFitness> getAllRealTimeFitness() {
        testDB = new FitnessDB(context);
        SQLiteDatabase db = testDB.getWritableDatabase();
        ArrayList<RealTimeFitness> datalist = new ArrayList<RealTimeFitness>();
        RealTimeFitness myRealTimeFitness;
        String getquery = "SELECT RealTime_Fitness_ID, Capture_DateTime, Step_Number " +
                "FROM RealTime_Fitness";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), Integer.parseInt(c.getString(2)));
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
        String getquery = "SELECT RealTime_Fitness_ID, Capture_DateTime, Step_Number " +
                "FROM RealTime_Fitness " +
                "WHERE Capture_DateTime > datetime('"+date.getDate().getFullDate()+"') " +
                "AND Capture_DateTime <  datetime('"+date.getDate().getFullDate()+"', '+1 day')";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), Integer.parseInt(c.getString(2)));
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
        String getquery = "SELECT RealTime_Fitness_ID, Capture_DateTime, Step_Number " +
                "FROM RealTime_Fitness " +
                "WHERE Capture_DateTime > datetime('"+date.getDateTime()+"') ";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), Integer.parseInt(c.getString(2)));
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
        String getquery = "SELECT RealTime_Fitness_ID, Capture_DateTime, Step_Number " +
                "FROM RealTime_Fitness WHERE Fitness_Detail_ID = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{id});
            if (c.moveToFirst()) {
                do {
                    myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), Integer.parseInt(c.getString(2)));
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
        String getquery = "SELECT RealTime_Fitness_ID, Capture_DateTime, Step_Number " +
                "FROM RealTime_Fitness WHERE Capture_DateTime = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{datetime});
            if (c.moveToFirst()) {
                do {
                    myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), Integer.parseInt(c.getString(2)));
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
            values.put("RealTime_Fitness_ID", myRealTimeFitness.getRealTimeFitnessID());
            values.put("Capture_DateTime", myRealTimeFitness.getCaptureDateTime().getDateTime());
            values.put("Step_Number", myRealTimeFitness.getStepNumber());
            db.insert("RealTime_Fitness", null, values);
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
        String updatequery = "UPDATE RealTime_Fitness SET Capture_DateTime = ?, Step_Number = ? " +
                "WHERE RealTime_Fitness_ID =\"" + myRealTimeFitness.getRealTimeFitnessID() + "\"" ;
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
            db.delete("RealTime_Fitness", "RealTime_Fitness_ID = ?", new String[] {id});
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
        String getquery = "SELECT RealTime_Fitness_ID, Capture_DateTime, Step_Number " +
                "FROM RealTime_Fitness ORDER BY RealTime_Fitness_ID DESC";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                myRealTimeFitness = new RealTimeFitness(c.getString(0),c.getString(1), Integer.parseInt(c.getString(2)));
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
