package my.com.saiboon.fitnesscompanion.BackgroundSensor;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import my.com.saiboon.fitnesscompanion.Classes.RealTimeFitness;
import my.com.saiboon.fitnesscompanion.Database.FitnessRecordDA;
import my.com.saiboon.fitnesscompanion.Database.RealTimeFitnessDA;
import my.com.saiboon.fitnesscompanion.ServerRequests;

public class TheService extends Service implements SensorEventListener {
    public static final String TAG = TheService.class.getName();
    public static final int SCREEN_OFF_RECEIVER_DELAY = 500;

    public static final String BROADCAST_ACTION = "com.example.hexa_jacksonfoo.sensorlistener.MainActivity";
    private final Handler handler = new Handler();
    Intent intent;

    private SensorManager mSensorManager = null;
    private WakeLock mWakeLock = null;
    SharedPreferences sharedPreferences;
    String stepsCount = "";
    RealTimeFitnessDA realTimeFitnessDa;
    FitnessRecordDA fitnessRecordDa;
    ServerRequests serverRequests;

    private StepManager stepManager;

    private void registerListener() {
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * Un-register this as a sensor event listener.
     */
    private void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive(" + intent + ")");

            if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                return;
            }

            Runnable runnable = new Runnable() {
                public void run() {
                    Log.i(TAG, "Runnable executing.");
                    unregisterListener();
                    registerListener();
                }
            };
            new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);
        }
    };

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(TAG, "onAccuracyChanged().");
    }

    public void onSensorChanged(SensorEvent event) {
        Log.i(TAG, "onSensorChanged().");
        Log.i(TAG, String.valueOf(event.values[0]));
        stepsCount = String.valueOf(event.values[0]);
        int b = (int)Math.round(event.values[0]);
       // SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString("Step", String.valueOf(event.values[0])).commit();
        //DisplayStepCountInfo();
        stepManager.SensorUpdateSharedPref(b);

        new SensorEventLoggerTask().execute(event);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        PowerManager manager =
                (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        //start stepcount
        stepManager = new StepManager(this);
        stepManager.startSharedPref();
    }

    @Override
    public void onStart(Intent intent, int startId) {
       // handler.removeCallbacks(sendUpdatesToUI);
       // handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
           // DisplayStepCountInfo();
            //handler.postDelayed(this, 5000); // 5 seconds
        }
    };

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        unregisterListener();
        mWakeLock.release();
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startForeground(Process.myPid(), new Notification());
        registerListener();
        mWakeLock.acquire();
        return START_STICKY;
    }

    private class SensorEventLoggerTask extends
            AsyncTask<SensorEvent, Void, Void> {
        @Override
        protected Void doInBackground(SensorEvent... events) {
            SensorEvent event = events[0];
            // log the value

            return null;
        }
    }
}