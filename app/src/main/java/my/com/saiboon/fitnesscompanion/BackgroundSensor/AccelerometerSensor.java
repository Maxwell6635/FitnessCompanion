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
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import my.com.saiboon.fitnesscompanion.Database.FitnessRecordDA;
import my.com.saiboon.fitnesscompanion.Classes.RealTimeFitness;
import my.com.saiboon.fitnesscompanion.Database.RealTimeFitnessDA;
import my.com.saiboon.fitnesscompanion.ServerRequests;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hexa-Jackson Foo on 9/25/2015.
 */
public class AccelerometerSensor extends Service implements SensorEventListener {
    public static final String TAG = AccelerometerSensor.class.getName();
    public static final int SCREEN_OFF_RECEIVER_DELAY = 500;

    private SensorManager mSensorManager = null;
    private PowerManager.WakeLock mWakeLock = null;
    private boolean mInitialized;
    private Sensor mAccelerometer;
    int stepsCount = 0;
    private final float NOISE = (float) 2.0;
    double mLastX;
    double mLastY;
    double mLastZ;
    Context myContext;

    SharedPreferences sharedPreferences;

    RealTimeFitnessDA realTimeFitnessDa;
    FitnessRecordDA fitnessRecordDa;
    ServerRequests serverRequests;

    public static final String BROADCAST_ACTION = "com.example.hexa_jacksonfoo.sensorlistener.MainActivity";
    private final Handler handler = new Handler();
    Intent intent;


    /*
     * Register this as a sensor event listener.
     */
    private void registerListener() {
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
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
            myContext = context;

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
        // event object contains values of acceleration, read those
        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];

        final double alpha = 0.8; // constant for our filter below

        double[] gravity = {0,0,0};

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        x = event.values[0] - gravity[0];
        y = event.values[1] - gravity[1];
        z = event.values[2] - gravity[2];

        if (!mInitialized) {
            // sensor is used for the first time, initialize the last read values
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            mInitialized = true;
        } else {
            // sensor is already initialized, and we have previously read values.
            // take difference of past and current values and decide which
            // axis acceleration was detected by comparing values

            double deltaX = Math.abs(mLastX - x);
            double deltaY = Math.abs(mLastY - y);
            double deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE)
                deltaX = (float) 0.0;
            if (deltaY < NOISE)
                deltaY = (float) 0.0;
            if (deltaZ < NOISE)
                deltaZ = (float) 0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;

            if (deltaX > deltaY) {
                // Horizontal shake
                // do something here if you like

            } else if (deltaY > deltaX) {
                // Vertical shake
                // do something here if you like

            } else if ((deltaZ > deltaX) && (deltaZ > deltaY)) {
                // Z shake
                stepsCount = stepsCount + 1;
                if (stepsCount > 0) {
                    //txtCount.setText(String.valueOf(stepsCount));
                    //txtDistance.setText(String.format("%.2f m", (stepsCount * (0.414 * 180)) / 100));
                    Log.i(TAG, "onSensorChanged().");
                    Log.i(TAG, stepsCount + "");

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Step", String.valueOf(stepsCount)).commit();

                    DisplayStepCountInfo();
                }
                // Just for indication purpose, I have added vibrate function
                // whenever our count moves past multiple of 10
                if ((stepsCount % 10) == 0) {
                    //Util.Vibrate(this, 100);
                }
            } else {
                // no shake detected
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mInitialized = false;
        PowerManager manager =
                (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        intent = new Intent(BROADCAST_ACTION);
        sharedPreferences = getSharedPreferences("StepCount", Context.MODE_PRIVATE);

        realTimeFitnessDa = new RealTimeFitnessDA(this);
        fitnessRecordDa = new FitnessRecordDA(this);
        serverRequests = new ServerRequests(this);

        for( int i=0; i< 24; i++) {
            timer(i, 00, 0);
        }
    }


    @Override
    public void onStart(Intent intent, int startId) {
        //handler.removeCallbacks(sendUpdatesToUI);
        //handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
    }

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
        startForeground(android.os.Process.myPid(), new Notification());
        registerListener();
        mWakeLock.acquire();
        return START_STICKY;
    }


    /*private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayStepCountInfo();
            handler.postDelayed(this, 5000); // 5 seconds
        }
    };*/


    private void DisplayStepCountInfo() {
        Log.d(TAG, "entered DisplayInfo");
        intent.putExtra("time", new Date().toLocaleString());
        intent.putExtra("counter", String.valueOf(stepsCount));
        sendBroadcast(intent);
    }

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

    private Runnable runnable = new Runnable() {
        public void run() {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String mydate = format1.format(calendar.getTime());
            String mydatetime = mydate + " " + hour + ":" + min;
            if(min == 0){
                RealTimeFitness realTimeFitness = new RealTimeFitness(realTimeFitnessDa.generateNewRealTimeFitnessID(), mydatetime, stepsCount);
                realTimeFitnessDa.addRealTimeFitness(realTimeFitness);
                serverRequests.storeRealTimeFitnessInBackground(realTimeFitness);
                stepsCount = 0;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Step", String.valueOf(stepsCount)).commit();
            }

        }
    };
}
