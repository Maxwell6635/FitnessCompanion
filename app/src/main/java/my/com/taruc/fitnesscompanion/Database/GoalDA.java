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
import my.com.taruc.fitnesscompanion.ServerAPI.UpdateRequest;

/**
 * Created by saiboon on 9/6/2015.
 */
public class GoalDA {

    private Context context;
    private FitnessDB mFitnessDB;

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

    private UpdateRequest updateRequest;

    public GoalDA(Context context){
        this.context = context;
        this.mFitnessDB = new FitnessDB(context);
        updateRequest = new UpdateRequest(context);
    }

    public ArrayList<Goal> getAllGoal() {
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        ArrayList<Goal> datalist = new ArrayList<Goal>();
        Goal myGoal;
        String getquery = "SELECT "+allColumn+" FROM "+databaseName;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    boolean done = false;
                    if(c.getString(5).equalsIgnoreCase("1")){
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
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        ArrayList<Goal> datalist = new ArrayList<Goal>();
        Goal myGoal;
        String getquery = "SELECT "+allColumn+" FROM "+databaseName +" WHERE "+columnDone+" = '0'";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    boolean done = false;
                    if(c.getString(5)=="1"){
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
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        Goal myGoal= new Goal();
        String getquery = "SELECT "+ allColumn+" FROM "+databaseName+" WHERE "+columnID+" = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{GoalID});
            if (c.moveToFirst()) {
                do {
                    boolean done = false;
                    if(c.getString(5)=="1"){
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
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        Goal myGoal= new Goal();
        String getquery = "SELECT "+ allColumn+" FROM "+ databaseName+" ORDER BY "+columnID+" DESC";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                boolean done = false;
                if(c.getString(5)=="1"){
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
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success=false;
        int done = 0;
        try {
            values.put(columnID, myGoal.getGoalId());
            values.put(columnUserID, myGoal.getUserID());
            values.put(columnDesc, myGoal.getGoalDescription());
            values.put(columnTarget, myGoal.getGoalTarget());
            values.put(columnDuration, myGoal.getGoalDuration());
            if(myGoal.isGoalDone()){
                done = 1;
            }
            values.put(columnDone, done);
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


    public int addListGoal(ArrayList<Goal> goalArrayList) {
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        int count = 0;
        ContentValues values = new ContentValues();
        int done = 0;
        try {
            for(int i=0; i < goalArrayList.size(); i++) {
                values.put(columnID, goalArrayList.get(i).getGoalId());
                values.put(columnUserID, goalArrayList.get(i).getUserID());
                values.put(columnDesc, goalArrayList.get(i).getGoalDescription());
                values.put(columnTarget, goalArrayList.get(i).getGoalTarget());
                values.put(columnDuration, goalArrayList.get(i).getGoalDuration());
                if(goalArrayList.get(i).isGoalDone()) {
                    done = 1;
                }
                values.put(columnDone, done);
                values.put(columnCreatedAt, goalArrayList.get(i).getCreateAt().getDateTimeString());
                if(goalArrayList.get(i).getCreateAt()!=null){
                    values.put(columnUpdatedAt, goalArrayList.get(i).getUpdateAt().getDateTimeString());
                }
                db.insert(databaseName, null, values);
                count++;
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return count;
    }

    public boolean updateGoal(Goal myGoal) {
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        int done = 0;
        String updatequery = "UPDATE "+databaseName+" SET "+columnUserID+" = ?, "+columnDesc+" = ?, "+columnTarget+" = ?, "+columnDuration+" = ?, "+ columnDone+" = ?, "+columnCreatedAt+" = ?, "+ columnUpdatedAt +" =?  WHERE "+ columnID+" = ?";
        boolean success=false;
        try {
            if(myGoal.isGoalDone()){
                done = 1;
            }
            db.execSQL(updatequery, new String[]{myGoal.getUserID()+"", myGoal.getGoalDescription(), myGoal.getGoalTarget() + "", myGoal.getGoalDuration()+"", done+"" ,myGoal.getCreateAt().getDateTimeString(), myGoal.getUpdateAt().getDateTimeString(), myGoal.getGoalId()});
            success=true;
            updateRequest.updateGoalDataInBackground(myGoal);
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteGoal(String goalId) {
        SQLiteDatabase db = mFitnessDB.getWritableDatabase();
        boolean result = false;
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
