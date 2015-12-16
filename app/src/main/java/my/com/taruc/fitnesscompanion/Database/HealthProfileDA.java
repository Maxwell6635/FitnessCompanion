package my.com.taruc.fitnesscompanion.Database;

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
import java.util.List;

import my.com.taruc.fitnesscompanion.Classes.HealthProfile;

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
        String getquery = "SELECT id, user_id, weight, blood_pressure, resting_heart_rate," +
                "arm_girth, chest_girth, calf_girth, thigh_girth, waist, hip, created_at FROM Health_Profile";
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
        String getquery = "SELECT id, user_id, weight, blood_pressure, resting_heart_rate," +
                "arm_girth, chest_girth, calf_girth, thigh_girth, waist, hip, created_at FROM Health_Profile WHERE id = ?";
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
        String getquery = "SELECT * \n" +
                "FROM Health_Profile\n" +
                "ORDER BY id ASC";
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
            values.put("id", myHealthProfile.getHealthProfileID());
            values.put("user_id", myHealthProfile.getUserID());
            values.put("weight", myHealthProfile.getWeight());
            values.put("blood_pressure", myHealthProfile.getBloodPressure());
            values.put("resting_heart_rate", myHealthProfile.getRestingHeartRate());
            values.put("arm_girth", myHealthProfile.getArmGirth());
            values.put("chest_girth", myHealthProfile.getChestGirth());
            values.put("calf_girth", myHealthProfile.getCalfGirth());
            values.put("thigh_girth", myHealthProfile.getThighGirth());
            values.put("waist", myHealthProfile.getWaist());
            values.put("hip",myHealthProfile.getHIP());
            values.put("created_at", myHealthProfile.getRecordDateTime());
            db.insert("Health_Profile", null, values);
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }


    public int addListHealthProfile(List<HealthProfile> healthProfileArrayList) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        int count = 0;
        try {
            for(int i=0;i < healthProfileArrayList.size() ; i++) {
                values.put("id", healthProfileArrayList.get(i).getHealthProfileID());
                values.put("user_id", healthProfileArrayList.get(i).getUserID());
                values.put("Weight", healthProfileArrayList.get(i).getWeight());
                values.put("blood_pressure", healthProfileArrayList.get(i).getBloodPressure());
                values.put("resting_heart_rate", healthProfileArrayList.get(i).getRestingHeartRate());
                values.put("arm_girth", healthProfileArrayList.get(i).getArmGirth());
                values.put("chest_girth", healthProfileArrayList.get(i).getChestGirth());
                values.put("calf_girth", healthProfileArrayList.get(i).getCalfGirth());
                values.put("thigh_girth", healthProfileArrayList.get(i).getThighGirth());
                values.put("waist", healthProfileArrayList.get(i).getWaist());
                values.put("hip", healthProfileArrayList.get(i).getHIP());
                values.put("created_at", healthProfileArrayList.get(i).getRecordDateTime());
                db.insert("Health_Profile", null, values);
                count = count+1;
            }
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return count;
    }


    public boolean updateHealthProfile(HealthProfile myHealthProfile) {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        String updatequery = "UPDATE Health_Profile SET user_id = ?, weight = ?, blood_pressure = ?, resting_heart_rate=?," +
                "arm_girth=?, chest_girth=?, calf_girth=? ,thigh_girth=?, waist=?, hip=?, created_at=? WHERE id = ?";
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
            db.delete("Health_Profile", "id = ?", new String[]{HealthProfileId});
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
            if (lastHealthProfile==null|| lastHealthProfile.getHealthProfileID().equals("")){
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
