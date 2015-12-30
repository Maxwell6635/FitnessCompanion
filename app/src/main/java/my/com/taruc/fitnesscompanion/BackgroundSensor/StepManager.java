package my.com.taruc.fitnesscompanion.BackgroundSensor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.RealTimeFitness;
import my.com.taruc.fitnesscompanion.Database.RealTimeFitnessDA;
import my.com.taruc.fitnesscompanion.ServerAPI.ServerRequests;
import my.com.taruc.fitnesscompanion.UserLocalStore;

/**
 * Created by saiboon on 8/11/2015.
 */
public class StepManager{

    public static final String TAG = StepManager.class.getName();
    public static final String BROADCAST_ACTION = "com.example.hexa_jacksonfoo.sensorlistener.MainActivity";
    SharedPreferences sharedPreferences;
    Context context;
    int stepsCount = 0;
    int base = 0;
    int previousStepsCount = 0;
    int tempStepCount;
    Intent intent;
    RealTimeFitnessDA realTimeFitnessDa;
    ServerRequests serverRequests;

    Calendar calendar;
    DateTime currentDateTime;
    DateTime sensorStartDateTime;

    UserLocalStore userLocalStore;

    public StepManager(Context context){
        this.context = context;
        realTimeFitnessDa = new RealTimeFitnessDA(context);
        serverRequests = new ServerRequests(context);
        sensorStartDateTime = getCurrentDateTime();
        previousStepsCount = previousTotalStepCount();
        sharedPreferences = context.getSharedPreferences("StepCount", Context.MODE_PRIVATE);
        userLocalStore = new UserLocalStore(context);
        intent = new Intent(BROADCAST_ACTION);
    }

    public int startSharedPref(){
        //initial shared pref of step count -- get step number if ardy exist
        String SharedPrefStep = sharedPreferences.getString("Step", null);
        String BaseStep = sharedPreferences.getString("Base","0");
        if (SharedPrefStep != null) {
            stepsCount = Integer.parseInt(SharedPrefStep);
            base = Integer.parseInt(BaseStep);
            String SharedPrefDate = sharedPreferences.getString("Date", "2000-1-1");
            String SharedPrefTime = sharedPreferences.getString("Time", "00:00");
            DateTime SharedPrefDateTime = new DateTime(SharedPrefDate + " " + SharedPrefTime);
            if (!sameDateHour(SharedPrefDateTime)){
                resetStepCount(true);
            }
        }
        //start timer
        for( int i=0; i< 24; i++) {
            timer(i, 00, 0);
        }
        DisplayStepCountInfo();
        return stepsCount;
    }

    public void SensorUpdateSharedPref(int SensorStepsCount){ //pass in total step number ( from java file "TheService" )
        currentDateTime = getCurrentDateTime();
        tempStepCount = SensorStepsCount - previousStepsCount; // get steps today
        if(tempStepCount<0){
            Toast.makeText(context, "Step Count Error", Toast.LENGTH_SHORT).show();
            tempStepCount = 0;
        }else if(SensorStepsCount == base){
            Toast.makeText(context,"Same",Toast.LENGTH_SHORT);
        }else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Step", (stepsCount + tempStepCount - base) + "").commit();
            editor.putString("Base", SensorStepsCount + "").commit();
            editor.putString("Time", currentDateTime.getTime().getFullTime()).commit();
            editor.putString("Date", currentDateTime.getDate().getFullDate()).commit();
            DisplayStepCountInfo();
        }
    }

    public void ManualUpdateSharedPref(){ //increment the step count
        stepsCount = stepsCount + 1;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Step", (stepsCount)+"").commit();
        editor.putString("Time", currentDateTime.getTime().getFullTime()).commit();
        editor.putString("Date", currentDateTime.getDate().getFullDate()).commit();
        DisplayStepCountInfo();
    }

    public DateTime getCurrentDateTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String mydate = dateformat.format(calendar.getTime());
        String mytime = hour + ":" + min;
        return new DateTime(mydate + " " + mytime);
    }

    // reset step count every hour and store data into realtimefitness database
    public Runnable runnable = new Runnable() {
        public void run() {
            currentDateTime = getCurrentDateTime();
            previousStepsCount = previousTotalStepCount();
            if(currentDateTime.getTime().getMinutes() == 00 && currentDateTime.getTime().getSeconds() == 0){
                //Toast.makeText(context,"Testing 123",Toast.LENGTH_LONG).show();
                resetStepCount(false);
            }
        }
    };

    //update step number in main menu UI
    public void DisplayStepCountInfo() {
        intent.putExtra("time", new Date().toLocaleString());
        currentDateTime = getCurrentDateTime();
        int totalStepCount = 0;
        try {
            ArrayList<RealTimeFitness> realTimeFitnessArrayList = realTimeFitnessDa.getAllRealTimeFitnessPerDay(currentDateTime);
            for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
                totalStepCount = totalStepCount + realTimeFitnessArrayList.get(i).getStepNumber();
            }
        }catch(Exception ex){

        }
        int mystepinthishour = 0;
        String SharedPrefStep = sharedPreferences.getString("Step", null);
        if (SharedPrefStep != null) {
            mystepinthishour = Integer.parseInt(SharedPrefStep);
        }
        int stepsCountToday = mystepinthishour + totalStepCount;
        intent = new Intent(BROADCAST_ACTION);
        intent.putExtra("counter", String.valueOf(stepsCountToday));
        context.sendBroadcast(intent);
    }

    // Get Step number from specific date (Goal)
    public int GetStepNumber(DateTime startDateTime, DateTime endDateTime) {
        int totalStepCount = 0;
        ArrayList<RealTimeFitness> realTimeFitnessArrayList = realTimeFitnessDa.getAllRealTimeFitnessBetweenDateTime(startDateTime, endDateTime);
        for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
            totalStepCount = totalStepCount + realTimeFitnessArrayList.get(i).getStepNumber();
        }
        int mystepinthishour = 0;
        String SharedPrefStep = sharedPreferences.getString("Step", null);
        if (SharedPrefStep != null) {
            mystepinthishour = Integer.parseInt(SharedPrefStep);
        }
        int stepsCountToday = mystepinthishour + totalStepCount;
        return stepsCountToday;
    }

    //get previous total step count in a day
    public int previousTotalStepCount(){
        int totalStepCount = 0;
        try {
            ArrayList<RealTimeFitness> realTimeFitnessArrayList = realTimeFitnessDa.getAllRealTimeFitnessAfter(sensorStartDateTime);
            for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
                totalStepCount = totalStepCount + realTimeFitnessArrayList.get(i).getStepNumber();
            }
        }catch(Exception ex){
            //Log.i("Error",ex.getMessage());
        }
        return totalStepCount;
    }

    //check whther same date and same hour
    public boolean sameDateHour(DateTime sharedPreferDateTime){
        currentDateTime = getCurrentDateTime();
        if(sharedPreferDateTime.getDate().getFullDate().equals(currentDateTime.getDate().getFullDate())){
            if(sharedPreferDateTime.getTime().getHour() == currentDateTime.getTime().getHour()){
                return true;
            }
        }
        return false;
    }

    public void resetStepCount(Boolean start){
        DateTime lastDateTime = getCurrentDateTime();
        RealTimeFitness realTimeFitness = realTimeFitnessDa.getLastRealTimeFitness();
        DateTime tempLastDateTime = realTimeFitness.getCaptureDateTime();
        if (!sameDateHour(tempLastDateTime)) {
            // insert old step number into record after last record
            try {
                if (start) {

                    lastDateTime.setTime(tempLastDateTime.getTime().addDuration(3600).getFullTime());
                }
            } catch (Exception ex) {
                Log.i(TAG, "Get last date time failed.");
            }
            //insert to database
            int writeInStepCount = Integer.parseInt(sharedPreferences.getString("Step", "0"));
            realTimeFitness = new RealTimeFitness(realTimeFitnessDa.generateNewRealTimeFitnessID(), userLocalStore.returnUserID() + "", lastDateTime, writeInStepCount);
            boolean success = realTimeFitnessDa.addRealTimeFitness(realTimeFitness);
            if (!success) {
                Toast.makeText(context, "Fail to add real time fitness record", Toast.LENGTH_LONG).show();
                Log.i("Fail", "Fail to add real time fitness record");
            } else {
                //[]Fixed. Please Retry
                serverRequests.storeRealTimeFitnessInBackground(realTimeFitness);
                Log.i("Pass", "Pass to add real time fitness record");
                stepsCount = 0;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Step", String.valueOf(stepsCount)).commit();
                editor.putString("Time", currentDateTime.getTime().getFullTime()).commit();
                editor.putString("Date", currentDateTime.getDate().getFullDate()).commit();
                //update previous step count
                previousStepsCount = previousTotalStepCount();
            }
        }
    }

    //active method after hour by hour
    private void timer(int hour, int minutes, int second) {
        Calendar calendar = Calendar.getInstance();
        long currentTimestamp = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, second);
        long diffTimestamp = calendar.getTimeInMillis() - currentTimestamp;
        long myDelay = (diffTimestamp < 0 ? 0 : diffTimestamp);

        new Handler().postDelayed(runnable, myDelay);
    }

}

