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

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.HealthProfile;

/**
 * Created by saiboon on 13/6/2015.
 */
public class HealthProfileDA {
    private Context context;
    FitnessDB fitnessDB;

    private String databaseName = "Health_Profile";
    private String columnID = "id";
    private String columnUserID = "user_id";
    private String columnWeight = "weight";
    private String columnBlood = "blood_pressure";
    private String columnHeartRate = "resting_heart_rate";
    private String columnArm = "arm_girth";
    private String columnChest = "chest_girth";
    private String columnCalf = "calf_girth";
    private String columnThigh = "thigh_girth";
    private String columnWaist = "waist";
    private String columnHIP = "hip";
    private String columnCreatedAt = "created_at";
    private String columnUpdatedAt = "updated_at";
    private String allColumn = columnID +"," + columnUserID+","+columnWeight+","+columnBlood+","+
            columnHeartRate+","+columnArm+","+columnChest+","+columnCalf+","+columnThigh+","+
            columnWaist+","+columnHIP+","+columnCreatedAt+","+columnUpdatedAt;

    public HealthProfileDA(Context context){
        this.context = context;
    }

    public List<HealthProfile> getAllHealthProfile() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        List<HealthProfile> datalist = new ArrayList<HealthProfile>();
        HealthProfile myHealthProfile;
        String getquery = "SELECT "+allColumn+" FROM "+databaseName;
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                do {
                    myHealthProfile = new HealthProfile(c.getString(0),c.getString(1),Integer.parseInt(c.getString(2)),Integer.parseInt(c.getString(3)), Integer.parseInt(c.getString(4)),
                            Double.parseDouble(c.getString(5)), Double.parseDouble(c.getString(6)), Double.parseDouble(c.getString(7)), Double.parseDouble(c.getString(8)),
                            Double.parseDouble(c.getString(9)),Double.parseDouble(c.getString(10)), new DateTime(c.getString(11)), new DateTime(c.getString(12)));
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
        String getquery = "SELECT "+allColumn+" FROM "+databaseName+" WHERE "+columnID+" = ?";
        try {
            Cursor c = db.rawQuery(getquery, new String[]{HealthProfileID});
            if (c.moveToFirst()) {
                do {
                    myHealthProfile = new HealthProfile(c.getString(0),c.getString(1),Integer.parseInt(c.getString(2)),Integer.parseInt(c.getString(3)), Integer.parseInt(c.getString(4)),
                            Double.parseDouble(c.getString(5)), Double.parseDouble(c.getString(6)), Double.parseDouble(c.getString(7)), Double.parseDouble(c.getString(8)),
                            Double.parseDouble(c.getString(9)),Double.parseDouble(c.getString(10)), new DateTime(c.getString(11)), new DateTime(c.getString(12)));
                } while (c.moveToNext());
                c.close();
            }}catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return myHealthProfile;
    }

    /*public HealthProfile getLastHealthProfile() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        HealthProfile myHealthProfile= new HealthProfile();
        String getquery = "SELECT * " +
                "FROM Health_Profile " +
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
    }*/

    public HealthProfile getLastHealthProfile() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        HealthProfile myHealthProfile= new HealthProfile();
        String getquery = "SELECT * " +
                "FROM " + databaseName +
                " ORDER BY "+columnID+" DESC";
        try {
            Cursor c = db.rawQuery(getquery, null);
            if (c.moveToFirst()) {
                    myHealthProfile = new HealthProfile(c.getString(0),c.getString(1),Integer.parseInt(c.getString(2)),Integer.parseInt(c.getString(3)), Integer.parseInt(c.getString(4)),
                            Double.parseDouble(c.getString(5)), Double.parseDouble(c.getString(6)), Double.parseDouble(c.getString(7)), Double.parseDouble(c.getString(8)),
                            Double.parseDouble(c.getString(9)),Double.parseDouble(c.getString(10)), new DateTime(c.getString(11)), new DateTime(c.getString(12)));
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
            values.put(columnID, myHealthProfile.getHealthProfileID());
            values.put(columnUserID, myHealthProfile.getUserID());
            values.put(columnWeight, myHealthProfile.getWeight());
            values.put(columnBlood, myHealthProfile.getBloodPressure());
            values.put(columnHeartRate, myHealthProfile.getRestingHeartRate());
            values.put(columnArm, myHealthProfile.getArmGirth());
            values.put(columnChest, myHealthProfile.getChestGirth());
            values.put(columnCalf, myHealthProfile.getCalfGirth());
            values.put(columnThigh, myHealthProfile.getThighGirth());
            values.put(columnWaist, myHealthProfile.getWaist());
            values.put(columnHIP,myHealthProfile.getHIP());
            values.put(columnCreatedAt, myHealthProfile.getRecordDateTime().getDateTimeString());
            if(myHealthProfile.getUpdatedAt()!=null){
                values.put(columnUpdatedAt, myHealthProfile.getUpdatedAt().getDateTimeString());
            }
            db.insert(databaseName, null, values);
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
                values.put(columnID, healthProfileArrayList.get(i).getHealthProfileID());
                values.put(columnUserID, healthProfileArrayList.get(i).getUserID());
                values.put(columnWeight, healthProfileArrayList.get(i).getWeight());
                values.put(columnBlood, healthProfileArrayList.get(i).getBloodPressure());
                values.put(columnHeartRate, healthProfileArrayList.get(i).getRestingHeartRate());
                values.put(columnArm, healthProfileArrayList.get(i).getArmGirth());
                values.put(columnChest, healthProfileArrayList.get(i).getChestGirth());
                values.put(columnCalf, healthProfileArrayList.get(i).getCalfGirth());
                values.put(columnThigh, healthProfileArrayList.get(i).getThighGirth());
                values.put(columnWaist, healthProfileArrayList.get(i).getWaist());
                values.put(columnHIP, healthProfileArrayList.get(i).getHIP());
                values.put(columnCreatedAt, healthProfileArrayList.get(i).getRecordDateTime().getDateTimeString());
                values.put(columnUpdatedAt, healthProfileArrayList.get(i).getUpdatedAt().getDateTimeString());
                db.insert(databaseName, null, values);
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
        String updatequery = "UPDATE "+ databaseName+" SET "+columnUserID+" = ?, "+columnWeight+" = ?, "+columnBlood+" = ?, "+columnHeartRate+"=?," +
                columnArm+"=?, "+columnChest+"=?, "+columnCalf+"=? ,"+columnThigh+"=?, "+columnWaist+"=?, "+columnHIP+"=?, "+columnCreatedAt+"=?, "+ columnUpdatedAt +"=? WHERE "+columnID+" = ?";
        boolean success=false;
        try {
            db.execSQL(updatequery, new String[]{myHealthProfile.getUserID() + "", myHealthProfile.getWeight() + "", myHealthProfile.getBloodPressure() + "", myHealthProfile.getRestingHeartRate() + "",
                    myHealthProfile.getArmGirth() + "", myHealthProfile.getChestGirth() + "", myHealthProfile.getCalfGirth() + "", myHealthProfile.getThighGirth() + "",
                    myHealthProfile.getWaist()+"",myHealthProfile.getHIP()+"",myHealthProfile.getRecordDateTime().getDateTimeString(), myHealthProfile.getRecordDateTime().getDateTimeString(), myHealthProfile.getHealthProfileID()});
            success=true;
        }catch(SQLException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public boolean deleteHealthProfile(String HealthProfileId) {
        boolean result = false;
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        try {
            db.delete(databaseName, columnID+" = ?", new String[]{HealthProfileId});
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
