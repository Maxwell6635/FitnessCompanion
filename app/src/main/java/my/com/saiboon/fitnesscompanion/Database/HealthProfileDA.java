package my.com.saiboon.fitnesscompanion.Database;

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

import my.com.saiboon.fitnesscompanion.Classes.HealthProfile;
import my.com.saiboon.fitnesscompanion.Classes.Reminder;

/**
 * Created by saiboon on 13/6/2015.
 */
public class HealthProfileDA {
    private Context context;
    FitnessDB fitnessDB;

    public HealthProfileDA(Context context){
        this.context = context;
    }

    public ArrayList<HealthProfile> getAllHealthProfile() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<HealthProfile> datalist = new ArrayList<HealthProfile>();
        HealthProfile myHealthProfile;
        String getquery = "SELECT Health_Profile_ID, User_ID, Weight, Blood_Pressure, Resting_Heart_Rate," +
                "Arm_Girth, Chest_Girth, Calf_Girth, Thigh_Girth,Waist,HIP Record_DateTime FROM Health_Profile";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myHealthProfile = new HealthProfile(c.getString(0),c.getString(1),Integer.parseInt(c.getString(2)),Integer.parseInt(c.getString(3)), Integer.parseInt(c.getString(4)),
                            Double.parseDouble(c.getString(5)), Double.parseDouble(c.getString(6)), Double.parseDouble(c.getString(7)), Double.parseDouble(c.getString(8)),Double.parseDouble(c.getString(9)),Double.parseDouble(c.getString(10)), c.getString(11));
                    datalist.add(myHealthProfile);
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return datalist;
    }

    public HealthProfile getHealthProfile(String HealthProfileID) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        HealthProfile myHealthProfile= new HealthProfile();
        String getquery = "SELECT Health_Profile_ID, User_ID, Weight, Blood_Pressure, Resting_Heart_Rate," +
                "Arm_Girth, Chest_Girth, Calf_Girth, Thigh_Girth,Waist,HIP, Record_DateTime FROM Health_Profile WHERE Health_Profile_ID = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{HealthProfileID});
            if (c.moveToFirst()) {
                do {
                    myHealthProfile = new HealthProfile(c.getString(0),c.getString(1),Integer.parseInt(c.getString(2)),Integer.parseInt(c.getString(3)), Integer.parseInt(c.getString(4)),
                            Double.parseDouble(c.getString(5)), Double.parseDouble(c.getString(6)), Double.parseDouble(c.getString(7)), Double.parseDouble(c.getString(8)),Double.parseDouble(c.getString(9)),Double.parseDouble(c.getString(10)), c.getString(11));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myHealthProfile;
    }

    public HealthProfile getLastHealthProfile() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        HealthProfile myHealthProfile= new HealthProfile();
        String getquery = "SELECT Health_Profile_ID, User_ID, Weight, Blood_Pressure, Resting_Heart_Rate," +
                "Arm_Girth, Chest_Girth, Calf_Girth,Thigh_Girth,Waist,HIP,Record_DateTime FROM Health_Profile ORDER BY Health_Profile_ID DESC";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myHealthProfile = new HealthProfile(c.getString(0),c.getString(1),Integer.parseInt(c.getString(2)),Integer.parseInt(c.getString(3)), Integer.parseInt(c.getString(4)),
                            Double.parseDouble(c.getString(5)), Double.parseDouble(c.getString(6)), Double.parseDouble(c.getString(7)), Double.parseDouble(c.getString(8)),Double.parseDouble(c.getString(9)),Double.parseDouble(c.getString(10)), c.getString(11));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myHealthProfile;
    }

    public boolean addHealthProfile(HealthProfile myHealthProfile) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean success=false;
        try {
            values.put("Health_Profile_ID", myHealthProfile.getHealthProfileID());
            values.put("User_ID", myHealthProfile.getUserID());
            values.put("Weight", myHealthProfile.getWeight());
            values.put("Blood_Pressure", myHealthProfile.getBloodPressure());
            values.put("Resting_Heart_Rate", myHealthProfile.getRestingHeartRate());
            values.put("Arm_Girth", myHealthProfile.getArmGirth());
            values.put("Chest_Girth", myHealthProfile.getChestGirth());
            values.put("Calf_Girth", myHealthProfile.getCalfGirth());
            values.put("Thigh_Girth", myHealthProfile.getThighGirth());
            values.put("Record_DateTime", myHealthProfile.getRecordDateTime());
            values.put("Waist", myHealthProfile.getWaist());
            values.put("HIP",myHealthProfile.getHIP());
            db.insert("Health_Profile", null, values);
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean updateHealthProfile(HealthProfile myHealthProfile) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        String updatequery = "UPDATE Health_Profile SET User_ID = ?, Weight = ?, Blood_Pressure = ?, Resting_Heart_Rate=?," +
                "Arm_Girth=?, Chest_Girth=?, Calf_Girth=? ,Thigh_Girth=?, Waist=?,HIP=?,Record_DateTime=? WHERE Health_Profile_ID = ?";
        boolean success=false;
        try {
            db.execSQL(updatequery, new String[]{myHealthProfile.getUserID() + "", myHealthProfile.getWeight() + "", myHealthProfile.getBloodPressure() + "", myHealthProfile.getRestingHeartRate() + "",
                    myHealthProfile.getArmGirth() + "", myHealthProfile.getChestGirth() + "", myHealthProfile.getCalfGirth() + "", myHealthProfile.getThighGirth() + "",
                    myHealthProfile.getWaist()+"",myHealthProfile.getHIP()+"",myHealthProfile.getRecordDateTime(), myHealthProfile.getHealthProfileID()});
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteHealthProfile(String HealthProfileId) {
        boolean result = false;
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete("Health_Profile", "Health_Profile_ID = ?", new String[]{HealthProfileId});
            result = true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return result;
    }


    public String generateNewHealthProfileID(){
        String  healthProfileID ="";
        HealthProfile lastHealthProfile;
        Calendar c = Calendar.getInstance();
        String myDate = c.get(Calendar.DATE) + "/"+ (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR);
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObj = new Date();
        try {
            dateObj = curFormater.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
        String formattedDate = df.format(dateObj); //current date

        try {
            lastHealthProfile = getLastHealthProfile();
            String[] lastFitnessID = lastHealthProfile.getHealthProfileID().split("HR");
            if ( lastHealthProfile==null|| lastHealthProfile.getHealthProfileID().equals("")){
                healthProfileID= formattedDate+"HR001";
            }
            else if (!lastFitnessID[0].equals(formattedDate)){
                healthProfileID = formattedDate+"HR001" ;
            }
            else{
                String lastFitnessRecordIDNum = lastFitnessID[1];
                int newFitnessRecordIDNum = Integer.parseInt(lastFitnessRecordIDNum) + 1;
                if (newFitnessRecordIDNum>99){
                    healthProfileID= formattedDate + "HR" + newFitnessRecordIDNum ;
                }
                else if(newFitnessRecordIDNum>9){
                    healthProfileID =  formattedDate+ "HR"+  "0"+ newFitnessRecordIDNum;
                }else{
                    healthProfileID = formattedDate +"HR"+ "00" + newFitnessRecordIDNum ;
                }
            }
        }catch (Exception ex){
            healthProfileID = formattedDate + "HR001" ;
        }
        return healthProfileID;
    }



}
