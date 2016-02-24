package my.com.taruc.fitnesscompanion.BackgroundSensor;

/**
 * Created by saiboon on 28/12/2015.
 */
/*
 *  Pedometer - Android App
 *  Copyright (C) 2009 Levente Bagi
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Detects steps and notifies all listeners (that implement StepListener).
 * @author Levente Bagi
 * @todo REFACTOR: SensorListener is deprecated
 */
public class AccelerometerSensor2 extends Service implements SensorEventListener
{
    private final static String TAG = "AccelerometerSensor2";
    //Sensitivity 1.97  2.96  4.44  6.66  10.00  15.00  22.50  33.75  50.62
    private float   mLimit1 = 15.00f;
    private float   mLimit2 = 22.50f;
    private float   mLastValues[] = new float[3*2];
    private float   mScale[] = new float[2];
    private float   mYOffset;

    private float   mLastDirections[] = new float[3*2];
    private float   mLastExtremes[][] = { new float[3*2], new float[3*2] };
    private float   mLastDiff[] = new float[3*2];
    private int     mLastMatch = -1;

    //private ArrayList<StepListener> mStepListeners = new ArrayList<StepListener>();
    public static final int SCREEN_OFF_RECEIVER_DELAY = 500;
    private PowerManager.WakeLock mWakeLock = null;
    private boolean mInitialized;
    private SensorManager mSensorManager = null;
    public static final String BROADCAST_ACTION = "my.com.taruc.fitnesscompanion.ui.MainMenu";
    public static final String BROADCAST_ACTION_2 = "my.com.taruc.fitnesscompanion.ui.ExercisePage";
    Intent intent;
    private StepManager stepManager;
    Context myContext;

    public AccelerometerSensor2() {
        int h = 480; // TODO: remove this constant
        mYOffset = h * 0.5f;
        mScale[0] = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = - (h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        int h = 480; // TODO: remove this constant
        mYOffset = h * 0.5f;
        mScale[0] = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = - (h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mInitialized = false;
        PowerManager manager =
                (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        intent = new Intent(BROADCAST_ACTION);

        //initial shared pref of step count per hour -- get step number if ardy exist
        stepManager = new StepManager(this);
        stepManager.startSharedPref();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        unregisterListener();
        mWakeLock.release();
        stopForeground(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startForeground(android.os.Process.myPid(), new Notification());
        registerListener();
        mWakeLock.acquire();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //Register this as a sensor event listener.
    private void registerListener() {

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        stepManager.DisplayStepCountInfo();
    }

    //Un-register this as a sensor event listener.
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

    public void setSensitivity(float sensitivity) {
        mLimit1 = sensitivity; // 1.97  2.96  4.44  6.66  10.00  15.00  22.50  33.75  50.62
    }

    //public void onSensorChanged(int sensor, float[] values) {
    public void onSensorChanged(SensorEvent
                                        event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            }
            else {
                int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
                if (j == 1) {
                    float vSum = 0;
                    for (int i=0 ; i<3 ; i++) {
                        final float v = mYOffset + event.values[i] * mScale[j];
                        vSum += v;
                    }
                    int k = 0;
                    float v = vSum / 3;

                    float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
                    if (direction == - mLastDirections[k]) {
                        // Direction changed
                        int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
                        mLastExtremes[extType][k] = mLastValues[k];
                        float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

                        if (diff > mLimit1 && diff < mLimit2) {

                            boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k]*2/3);
                            boolean isPreviousLargeEnough = mLastDiff[k] > (diff/3);
                            boolean isNotContra = (mLastMatch != 1 - extType);

                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                Log.i(TAG, "step");
                                stepManager.ManualUpdateSharedPref();
                                mLastMatch = extType;
                            }
                            else {
                                mLastMatch = -1;
                            }
                        }
                        mLastDiff[k] = diff;
                    }
                    mLastDirections[k] = direction;
                    mLastValues[k] = v;
                }
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

}
