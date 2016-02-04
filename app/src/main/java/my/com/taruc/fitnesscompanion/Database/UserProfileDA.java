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

    private String databaseTableName = "User";
    private String columnID = "id";
    private String columnGCMID = "gcm_id";
    private String columnEmail = "email";
    private String columnPassword = "password";
    private String columnUserName = "name";
    private String columnDOB = "dob";
    private String columnGender = "gender";
    private String columnInitialWeight = "initial_weight";
    private String columnHeight = "height";
    private String columnRewardPoint = "reward_point";
    private String columnCreateAt = "created_at";
    private String columnUpdatedAt = "updated_at";
    private String
            columnImage = "image";
    private String columnString = columnID + ", " + columnGCMID + ", " + columnEmail + ", " + columnPassword + ", " + columnUserName + ", " +
            columnDOB + ", " + columnGender + ", " + columnInitialWeight + ", " + columnHeight + ", " + columnRewardPoint +
            ", " + columnCreateAt + ", " + columnUpdatedAt + ", " + columnImage;

    DbBitmapUtility dbBitmapUtility;

    public UserProfileDA(Context context) {
        this.context = context;
        dbBitmapUtility = new DbBitmapUtility();
    }

    public ArrayList<UserProfile> getAllUserProfile() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<UserProfile> datalist = new ArrayList<UserProfile>();
        UserProfile myUserProfile;
        String getquery = "SELECT " + columnString + " FROM " + databaseTableName;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    byte[] image = c.getBlob(12);
                    myUserProfile = new UserProfile(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), new DateTime(c.getString(5)), c.getString(6),
                            Double.parseDouble(c.getString(7)), Double.parseDouble(c.getString(8)), Integer.parseInt(c.getString(9)), new DateTime(c.getString(10)), new DateTime(c.getString(11)), getImage(image));
                    datalist.add(myUserProfile);
                } while (c.moveToNext());
                c.close();
            }
        } catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public UserProfile getUserProfile(String UserProfileID) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        UserProfile myUserProfile = new UserProfile();
        String getquery = "SELECT " + columnString + " FROM " + databaseTableName + " WHERE " + columnID + " = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{UserProfileID});
            if (c.moveToFirst()) {
                do {
                    byte[] image = c.getBlob(12);
                    myUserProfile = new UserProfile(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), new DateTime(c.getString(5)), c.getString(6),
                            Double.parseDouble(c.getString(7)), Double.parseDouble(c.getString(8)), Integer.parseInt(c.getString(9)), new DateTime(c.getString(10)), new DateTime(c.getString(11)), getImage(image));
                } while (c.moveToNext());
                c.close();
            }
        } catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myUserProfile;
    }

    //get first record
    public UserProfile getUserProfile2() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        UserProfile myUserProfile = new UserProfile();
        String getquery = "SELECT " + columnString + " FROM  " + databaseTableName;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    byte[] image = c.getBlob(12);
                    myUserProfile = new UserProfile(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), new DateTime(c.getString(5)), c.getString(6),
                            Double.parseDouble(c.getString(7)), Double.parseDouble(c.getString(8)), Integer.parseInt(c.getString(9)), new DateTime(c.getString(10)), new DateTime(c.getString(11)), getImage(image));
                } while (c.moveToNext());
                c.close();
            }
        } catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myUserProfile;
    }

    public UserProfile getLastUserProfile() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        UserProfile myUserProfile = new UserProfile();
        String getquery = "SELECT " + columnString + " FROM  " + databaseTableName + " ORDER BY " + columnUpdatedAt + " DESC";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    byte[] image = c.getBlob(12);
                    myUserProfile = new UserProfile(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), new DateTime(c.getString(5)), c.getString(6),
                            Double.parseDouble(c.getString(7)), Double.parseDouble(c.getString(8)), Integer.parseInt(c.getString(9)), new DateTime(c.getString(10)), new DateTime(c.getString(11)), getImage(image));
                } while (c.moveToNext());
                c.close();
            }
        } catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myUserProfile;
    }

    public boolean addUserProfile(UserProfile myUserProfile) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success = false;
        try {
            values.put(columnID, myUserProfile.getUserID());
            values.put(columnGCMID, myUserProfile.getmGCMID());
            values.put(columnEmail, myUserProfile.getEmail());
            values.put(columnPassword, myUserProfile.getPassword());
            values.put(columnUserName, myUserProfile.getName());
            values.put(columnDOB, myUserProfile.getDOB().getDate().getFullDateString());
            values.put(columnGender, myUserProfile.getGender());
            values.put(columnInitialWeight, myUserProfile.getInitial_Weight());
            values.put(columnHeight, myUserProfile.getHeight());
            values.put(columnRewardPoint, myUserProfile.getReward_Point());
            values.put(columnCreateAt, myUserProfile.getCreated_At().getDateTimeString());
            if (myUserProfile.getUpdated_At() != null) {
                values.put(columnUpdatedAt, myUserProfile.getUpdated_At().getDateTimeString());
            }
            values.put(columnImage, getBytes(myUserProfile.getBitmap()));
            db.insert(databaseTableName, null, values);
            success = true;
        } catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean updateUserProfile(UserProfile myUserProfile) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(columnGCMID, myUserProfile.getmGCMID());
        values.put(columnEmail, myUserProfile.getEmail());
        values.put(columnPassword, myUserProfile.getPassword());
        values.put(columnUserName, myUserProfile.getName());
        values.put(columnDOB, myUserProfile.getDOB().getDate().getFullDateString());
        values.put(columnGender, myUserProfile.getGender());
        values.put(columnInitialWeight, myUserProfile.getInitial_Weight());
        values.put(columnHeight, myUserProfile.getHeight());
        values.put(columnRewardPoint, myUserProfile.getReward_Point());
        values.put(columnCreateAt, myUserProfile.getCreated_At().getDateTimeString());
        if (myUserProfile.getUpdated_At() != null) {
            values.put(columnUpdatedAt, myUserProfile.getUpdated_At().getDateTimeString());
        }
        values.put(columnImage, getBytes(myUserProfile.getBitmap()));
        boolean success = false;
        try {
//            Toast.makeText(context,"DB = "+myUserProfile.getUserID(),Toast.LENGTH_SHORT).show();
            db.update(databaseTableName, values, "id=" + myUserProfile.getUserID(), null);
            success = true;
        } catch (SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteUserProfile(String UserProfileId) {
        boolean result = false;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete(databaseTableName, columnID + " = ?", new String[]{UserProfileId});
            result = true;
        } catch (SQLException e) {
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
        if (image != null) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        } else {
            return null;
        }
    }

}
