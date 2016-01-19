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
import my.com.taruc.fitnesscompanion.Classes.Ranking;

/**
 * Created by saiboon on 28/12/2015.
 */
public class RankingDA {
    private Context context;
    FitnessDB fitnessDB;

    private String databaseName = "Ranking";
    private String columnID = "id";
    private String columnUserID = "user_id";
    private String columnPoint = "points";
    private String columnType = "type";
    private String columnFitnessRecord = "fitness_record_id";
    private String columnCreatedAt = "created_at";
    private String columnUpdatedAt = "updated_at";
    private String allColumn = columnID + "," + columnUserID + "," + columnType +","+
            columnPoint +","+columnFitnessRecord+","+columnCreatedAt+","+columnUpdatedAt;

    public RankingDA(Context context){
        this.context = context;
    }

    public ArrayList<Ranking> getAllRanking() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<Ranking> datalist = new ArrayList<Ranking>();
        Ranking myRanking;
        String getquery = "SELECT "+allColumn+" FROM "+databaseName;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myRanking = new Ranking(c.getString(0),c.getString(1),c.getString(2),Integer.parseInt(c.getString(3)),c.getString(4), new DateTime(c.getString(5)), new DateTime(c.getString(6)));
                    datalist.add(myRanking);
                } while (c.moveToNext());
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ArrayList<String> getAllRankingType() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<String> datalist = new ArrayList<String>();
        Ranking myRanking;
        String getquery = "SELECT DISTINCT "+ columnType +" FROM "+databaseName;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    datalist.add(c.getString(0));
                } while (c.moveToNext());
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public ArrayList<Ranking> getAllRankingByType(String type) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<Ranking> datalist = new ArrayList<Ranking>();
        Ranking myRanking;
        String getquery = "SELECT "+allColumn+" FROM "+databaseName + " WHERE "+ columnType + " =? " +
                " ORDER BY " + columnPoint + " DESC";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{type});
            if (c.moveToFirst()) {
                do {
                    myRanking = new Ranking(c.getString(0), c.getString(1), c.getString(2), Integer.parseInt(c.getString(3)), c.getString(4), new DateTime(c.getString(5)), new DateTime(c.getString(6)));
                    datalist.add(myRanking);
                } while (c.moveToNext());
                c.close();
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public Ranking getRanking(String RankingID) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        Ranking myRanking= new Ranking();
        String getquery = "SELECT "+ allColumn+" FROM "+databaseName+" WHERE "+columnID+" = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{RankingID});
            if (c.moveToFirst()) {
                do {
                    myRanking = new Ranking(c.getString(0),c.getString(1),c.getString(2),Integer.parseInt(c.getString(3)), c.getString(4), new DateTime(c.getString(5)), new DateTime(c.getString(6)));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myRanking;
    }

    public boolean addRanking(Ranking myRanking) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success=false;
        try {
            values.put(columnID, myRanking.getRankingID());
            values.put(columnUserID, myRanking.getUserID());
            values.put(columnType, myRanking.getType());
            values.put(columnPoint, myRanking.getPoints());
            values.put(columnCreatedAt, myRanking.getCreatedAt().getDateTimeString());
            if(myRanking.getUpdatedAt()!=null) {
                values.put(columnUpdatedAt, myRanking.getUpdatedAt().getDateTimeString());
            }
            db.insert(databaseName, null, values);
            success = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean updateRanking(Ranking myRanking) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        String updatequery = "UPDATE "+databaseName+" SET "+columnUserID+" = ?, "+columnType+" = ?, "+columnPoint+" = ?, "+ columnFitnessRecord + " = ?, " +columnCreatedAt+" = ?, "+ columnUpdatedAt +" =?  WHERE "+ columnID+" = ?";
        boolean success=false;
        try {
            db.execSQL(updatequery, new String[]{myRanking.getUserID() + "", myRanking.getType(), myRanking.getPoints() + "", myRanking.getFitnessRecordID(), myRanking.getCreatedAt().getDateTimeString() + "", myRanking.getUpdatedAt().getDateTimeString(), myRanking.getRankingID()});
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteRanking(String RankingId) {
        boolean result = false;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete(databaseName, columnID + " = ?", new String[]{RankingId});
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }

    public boolean deleteAllRanking(){
        boolean result = false;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.execSQL("delete from "+ databaseName);
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }
}
