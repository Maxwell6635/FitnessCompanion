package my.com.taruc.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.Goal;

/**
 * Created by saiboon on 9/6/2015.
 */
public class GoalDA {

    private Context context;
    FitnessDB fitnessDB;

    public GoalDA(Context context){
        this.context = context;
    }

    public ArrayList<Goal> getAllGoal() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<Goal> datalist = new ArrayList<Goal>();
        Goal myGoal;
        String getquery = "SELECT id, user_id, goal_desc, goal_target, goal_duration, created_at FROM Goal";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myGoal = new Goal(c.getString(0),c.getString(1),c.getString(2),Integer.parseInt(c.getString(3)), Integer.parseInt(c.getString(4)), c.getString(5));
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
        String getquery = "SELECT id, user_id, goal_desc, goal_target, goal_duration, created_at FROM Goal WHERE id = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{GoalID});
            if (c.moveToFirst()) {
                do {
                    myGoal = new Goal(c.getString(0),c.getString(1),c.getString(2),Integer.parseInt(c.getString(3)), Integer.parseInt(c.getString(4)), c.getString(5));
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
        String getquery = "SELECT id, user_id, goal_desc, goal_target, goal_duration, created_at FROM Goal ORDER BY id DESC";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                myGoal = new Goal(c.getString(0),c.getString(1),c.getString(2),Integer.parseInt(c.getString(3)), Integer.parseInt(c.getString(4)), c.getString(5));
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
            values.put("id", myGoal.getGoalId());
            values.put("user_id", myGoal.getUserID());
            values.put("goal_desc", myGoal.getGoalDescription());
            values.put("goal_target", myGoal.getGoalTarget());
            values.put("goal_duration", myGoal.getGoalDuration());
            values.put("created_at", myGoal.getCreateAt());
            db.insert("Goal", null, values);
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
        String updatequery = "UPDATE Goal SET user_id = ?, goal_desc = ?, goal_target = ?, goal_duration = ?, created_at = ?  WHERE id = ?";
        boolean success=false;
        try {
            db.execSQL(updatequery, new String[]{myGoal.getUserID()+"", myGoal.getGoalDescription(), myGoal.getGoalTarget() + "", myGoal.getGoalDuration()+"", myGoal.getCreateAt(), myGoal.getGoalId()});
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteGoal(String goalId) {
        boolean result = false;
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete("Goal", "id = ?", new String[]{goalId});
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
