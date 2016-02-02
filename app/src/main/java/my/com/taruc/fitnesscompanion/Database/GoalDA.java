package my.com.taruc.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.Goal;

/**
 * Created by saiboon on 9/6/2015.
 */
public class GoalDA {

    private Context context;
    FitnessDB fitnessDB;

    private String databaseName = "Goal";
    private String columnID = "id";
    private String columnUserID = "user_id";
    private String columnDesc = "goal_desc";
    private String columnTarget = "goal_target";
    private String columnDuration = "goal_duration";
    private String columnDone = "goal_done";
    private String columnCreatedAt = "created_at";
    private String columnUpdatedAt = "updated_at";
    private String allColumn = columnID + "," + columnUserID + "," + columnDesc +","+
            columnTarget + ","+ columnDuration+ ","+ columnDone + "," +columnCreatedAt+ ","+columnUpdatedAt;

    public GoalDA(Context context){
        this.context = context;
    }

    public ArrayList<Goal> getAllGoal() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<Goal> datalist = new ArrayList<Goal>();
        Goal myGoal;
        String getquery = "SELECT "+allColumn+" FROM "+databaseName;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    boolean done = false;
                    if(c.getString(5)=="TRUE"){
                        done = true;
                    }
                    myGoal = new Goal(c.getString(0), c.getString(1),c.getString(2),Integer.parseInt(c.getString(3)), Integer.parseInt(c.getString(4)), done, new DateTime(c.getString(6)), new DateTime(c.getString(7)));
                    datalist.add(myGoal);
                } while (c.moveToNext());
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ArrayList<Goal> getAllNotDoneGoal() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<Goal> datalist = new ArrayList<Goal>();
        Goal myGoal;
        String getquery = "SELECT "+allColumn+" FROM "+databaseName +" WHERE "+columnDone+" = 'FALSE'";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    boolean done = false;
                    if(c.getString(5)=="TRUE"){
                        done = true;
                    }
                    myGoal = new Goal(c.getString(0), c.getString(1),c.getString(2),Integer.parseInt(c.getString(3)), Integer.parseInt(c.getString(4)), done, new DateTime(c.getString(6)), new DateTime(c.getString(7)));
                    datalist.add(myGoal);
                } while (c.moveToNext());
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public Goal getGoal(String GoalID) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        Goal myGoal= new Goal();
        String getquery = "SELECT "+ allColumn+" FROM "+databaseName+" WHERE "+columnID+" = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{GoalID});
            if (c.moveToFirst()) {
                do {
                    boolean done = false;
                    if(c.getString(5)=="TRUE"){
                        done = true;
                    }
                    myGoal = new Goal(c.getString(0),c.getString(1),c.getString(2),Integer.parseInt(c.getString(3)), Integer.parseInt(c.getString(4)), done, new DateTime(c.getString(6)), new DateTime(c.getString(7)));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myGoal;
    }

    public Goal getLastGoal() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        Goal myGoal= new Goal();
        String getquery = "SELECT "+ allColumn+" FROM "+ databaseName+" ORDER BY "+columnID+" DESC";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                boolean done = false;
                if(c.getString(5)=="TRUE"){
                    done = true;
                }
                myGoal = new Goal(c.getString(0),c.getString(1),c.getString(2),Integer.parseInt(c.getString(3)), Integer.parseInt(c.getString(4)), done, new DateTime(c.getString(6)), new DateTime(c.getString(7)));
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myGoal;
    }

    public boolean addGoal(Goal myGoal) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success=false;
        try {
            values.put(columnID, myGoal.getGoalId());
            values.put(columnUserID, myGoal.getUserID());
            values.put(columnDesc, myGoal.getGoalDescription());
            values.put(columnTarget, myGoal.getGoalTarget());
            values.put(columnDuration, myGoal.getGoalDuration());
            values.put(columnDone, "FALSE");
            values.put(columnCreatedAt, myGoal.getCreateAt().getDateTimeString());
            if(myGoal.getCreateAt()!=null){
                values.put(columnUpdatedAt, myGoal.getUpdateAt().getDateTimeString());
            }
            db.insert(databaseName, null, values);
            success = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean updateGoal(Goal myGoal) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        String updatequery = "UPDATE "+databaseName+" SET "+columnUserID+" = ?, "+columnDesc+" = ?, "+columnTarget+" = ?, "+columnDuration+" = ?, "+ columnDone+" = ?, "+columnCreatedAt+" = ?, "+ columnUpdatedAt +" =?  WHERE "+ columnID+" = ?";
        boolean success=false;
        try {
            db.execSQL(updatequery, new String[]{myGoal.getUserID()+"", myGoal.getGoalDescription(), myGoal.getGoalTarget() + "", myGoal.getGoalDuration()+"", myGoal.isGoalDone()+"" ,myGoal.getCreateAt().getDateTimeString(), myGoal.getUpdateAt().getDateTimeString(), myGoal.getGoalId()});
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteGoal(String goalId) {
        boolean result = false;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete(databaseName, columnID+" = ?", new String[]{goalId});
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }

    public String generateNewGoalID(){
        String newGoalID="";
        Goal lastGoal;
        try {
            lastGoal = getLastGoal();
            if (lastGoal==null){
                newGoalID = "G01";
            }else{
                String lastGoalIDNum = lastGoal.getGoalId().replace("G","") ;
                int newGoalIDNum = Integer.parseInt(lastGoalIDNum) + 1;
                if (newGoalIDNum>9){
                    newGoalID = "G"+ newGoalIDNum;
                }else{
                    newGoalID = "G0" + newGoalIDNum;
                }
            }
        }catch (Exception ex){
            newGoalID = "G01";
        }
        return newGoalID;
    }
}
