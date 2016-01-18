package my.com.taruc.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.Achievement;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.Event;

/**
 * Created by saiboon on 18/1/2016.
 */
public class EventDA {

    private Context context;
    FitnessDB fitnessDB;

    private String databaseTable = "Event";
    private String columnID = "id";
    private String columnBanner = "banner";
    private String columnUrl = "url";
    private String columnCreatedAt = "created_at";
    private String columnUpdatedAt = "updated_at";
    private String allColumn = columnID + "," +  columnBanner+"," + columnUrl+","+columnCreatedAt+","+columnUpdatedAt;


    public EventDA(Context context){
        this.context = context;
    }

    public ArrayList<Event> getAllEvent() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<Event> datalist = new ArrayList<Event>();
        Event myEvent;
        String getquery = "SELECT "+ allColumn +" FROM "+ databaseTable;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    byte[] image = c.getBlob(1);
                    myEvent = new Event(c.getString(0), getImage(image), c.getString(2), new DateTime(c.getString(3)), new DateTime(c.getString(4)));
                    datalist.add(myEvent);
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public Event getEvent(String EventID) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        Event myEvent= new Event();
        String getquery = "SELECT "+ allColumn+" FROM "+databaseTable+" WHERE "+columnID+" = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{EventID});
            if (c.moveToFirst()) {
                do {
                    byte[] image = c.getBlob(1);
                    myEvent = new Event( c.getString(0), getImage(image), c.getString(2), new DateTime(c.getString(3)), new DateTime(c.getString(4)));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myEvent;
    }

    public boolean addEvent(Event myEvent) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success = false;
        try {
            values.put(columnID, myEvent.getEventID());
            values.put(columnBanner, getBytes(myEvent.getBanner()));
            values.put(columnUrl, myEvent.getUrl());
            db.insert(databaseTable, null, values);
            success = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteEvent(String EventId) {
        boolean result = false;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete(databaseTable, columnID + " = ?", new String[]{EventId});
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        if(image!=null) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }else{
            return null;
        }
    }
}
