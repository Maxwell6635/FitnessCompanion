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

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.Util.DbBitmapUtility;

/**
 * Created by saiboon on 9/6/2015.
 */
public class UserProfileDA {
    private Context context;
    FitnessDB fitnessDB;

    private String DatabaseTable = "User";
    private String columnID ="id";
    private String columnEmail = "email";
    private String columnPassword = "password";
    private String columnUserName = "name";
    private String columnDOB = "dob";
    private String columnGender = "gender";
    private String columnInitialWeight = "initial_weight";
    private String columnHeight = "height";
    private String columnRewardPoint = "reward_point";
    private String columnCreateAt = "created_at";
    private String columnImage = "image";
    private String columnString = columnID + ", " + columnEmail + ", " + columnPassword + ", " + columnUserName + ", " +
        columnDOB + ", " + columnGender + ", " + columnInitialWeight + ", " + columnHeight + ", " + columnRewardPoint +
            ", " + columnCreateAt + ", " + columnImage;

    DbBitmapUtility dbBitmapUtility;

    public UserProfileDA(Context context){
        this.context = context;
        dbBitmapUtility = new DbBitmapUtility();
    }

    public ArrayList<UserProfile> getAllUserProfile() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<UserProfile> datalist = new ArrayList<UserProfile>();
        UserProfile myUserProfile;
        String getquery = "SELECT " + columnString + " FROM " + DatabaseTable;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    byte[] image = c.getBlob(10);
                    myUserProfile = new UserProfile(c.getString(0), c.getString(1), c.getString(2), c.getString(3), new DateTime(c.getString(4)), c.getString(5),
                            Double.parseDouble(c.getString(6)), Double.parseDouble(c.getString(7)), Integer.parseInt(c.getString(8)), new DateTime(c.getString(9)), getImage(image));
                    datalist.add(myUserProfile);
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public UserProfile getUserProfile(String UserProfileID) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        UserProfile myUserProfile= new UserProfile();
        String getquery = "SELECT " + columnString + " FROM " + DatabaseTable + " WHERE " + columnID + " = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{UserProfileID});
            if (c.moveToFirst()) {
                do {
                    byte[] image = c.getBlob(10);
                    myUserProfile = new UserProfile(c.getString(0), c.getString(1), c.getString(2), c.getString(3), new DateTime(c.getString(4)), c.getString(5),
                            Double.parseDouble(c.getString(6)), Double.parseDouble(c.getString(7)), Integer.parseInt(c.getString(8)), new DateTime(c.getString(9)), getImage(image));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myUserProfile;
    }

    //get first record
    public UserProfile getUserProfile2() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        UserProfile myUserProfile= new UserProfile();
        String getquery = "SELECT " + columnString + " FROM  " + DatabaseTable;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    byte[] image = c.getBlob(10);
                    myUserProfile = new UserProfile(c.getString(0), c.getString(1), c.getString(2), c.getString(3), new DateTime(c.getString(4)), c.getString(5),
                            Double.parseDouble(c.getString(6)), Double.parseDouble(c.getString(7)), Integer.parseInt(c.getString(8)), new DateTime(c.getString(9)), getImage(image));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myUserProfile;
    }

    public boolean addUserProfile(UserProfile myUserProfile) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success=false;
        try {
            values.put(columnID, myUserProfile.getUserID());
            values.put(columnEmail, myUserProfile.getEmail());
            values.put(columnPassword, myUserProfile.getPassword());
            values.put(columnUserName, myUserProfile.getName());
            values.put(columnDOB, myUserProfile.getDOB().getDate().getFullDate());
            values.put(columnGender, myUserProfile.getGender());
            values.put(columnInitialWeight, myUserProfile.getInitial_Weight());
            values.put(columnHeight, myUserProfile.getHeight());
            values.put(columnRewardPoint, myUserProfile.getReward_Point());
            values.put(columnCreateAt, myUserProfile.getCreated_At().getDateTime());
            values.put(columnImage,  getBytes(myUserProfile.getBitmap()));
            db.insert(DatabaseTable, null, values);
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean updateUserProfile(UserProfile myUserProfile) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        String updatequery = "UPDATE " + DatabaseTable + " SET " +
                columnEmail + " = ?, " +
                columnPassword + " = ?, " +
                columnUserName + " = ?, " +
                columnDOB + " = ?, " +
                columnGender + " = ?, " +
                columnInitialWeight + " = ?, " +
                columnHeight + " = ?, " +
                columnRewardPoint + " = ?, " +
                columnCreateAt + " = ?, " +
                columnImage + " = ?  WHERE " + columnID + " = ?";
        ContentValues values = new ContentValues();
        boolean success=false;
        try {
            Toast.makeText(context,"DB = "+myUserProfile.getUserID(),Toast.LENGTH_SHORT).show();
            db.execSQL(updatequery, new String[]{myUserProfile.getEmail(), myUserProfile.getPassword(), myUserProfile.getName(),
                    myUserProfile.getDOB().getDateTime(), myUserProfile.getGender(), myUserProfile.getInitial_Weight()+"", myUserProfile.getHeight()+"",
                    myUserProfile.getReward_Point()+"", myUserProfile.getCreated_At().getDateTime(),getBytes(myUserProfile.getBitmap())+"", myUserProfile.getUserID()});
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteUserProfile(String UserProfileId) {
        boolean result = false;
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete(DatabaseTable, columnID + " = ?", new String[] {UserProfileId});
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
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
