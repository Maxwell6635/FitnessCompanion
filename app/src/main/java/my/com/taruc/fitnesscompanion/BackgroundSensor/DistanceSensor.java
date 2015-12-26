package my.com.taruc.fitnesscompanion.BackgroundSensor;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.Database.UserProfileDA;

/**
 * Created by saiboon on 24/12/2015.
 */
public class DistanceSensor implements SensorEventListener {
    //distance & accelarator
    private boolean mInitialized; // used for initializing sensor only once
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    int stepsCount = 0;
    private final float NOISE = (float) 2.0;
    double mLastX;
    double mLastY;
    double mLastZ;
    Context context;

    public static final String BROADCAST_ACTION = "com.taruc.fitnesscompanion.ui.ExercisePage";
    Intent intent;

    public DistanceSensor(Context context){
        this.context = context;

        mInitialized = false;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        intent = new Intent(BROADCAST_ACTION);
    }

    //==================================================== Step sensor  ======================================================
    public void startSensor() {
        try {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Step sensor
    public void stopSensor(){
        mSensorManager.unregisterListener(this);
    }

    //Step sensor
    @Override
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

            } else if (deltaY > deltaX) {

            } else if ((deltaZ > deltaX) && (deltaZ > deltaY)) {
                // Z shake
                stepsCount = stepsCount + 1;
                /*if (stepsCount > 0) {
                    //step = 0.45 * Height
                    //url http://stackoverflow.com/questions/22292617/how-to-calculate-distance-while-walking-in-android
                    UserProfileDA userProfileDA = new UserProfileDA(this);
                    UserProfile userProfile = userProfileDA.getUserProfile2();
                    txtDistance.setText(String.format("%.2f m", (stepsCount * (0.414 * userProfile.getHeight())) / 100));
                }*/
                intent = new Intent(BROADCAST_ACTION);
                intent.putExtra("stepCounter", String.valueOf(stepsCount));
                context.sendBroadcast(intent);
            }
        }
    }

    //Step sensor
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
