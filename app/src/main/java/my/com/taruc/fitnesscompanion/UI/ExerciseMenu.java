package my.com.taruc.fitnesscompanion.UI;

import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Classes.HealthProfile;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.Database.HealthProfileDA;
import my.com.taruc.fitnesscompanion.Database.UserProfileDA;
import my.com.taruc.fitnesscompanion.HRStripBLE.BluetoothLeService;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.ServerRequests;
import my.com.taruc.fitnesscompanion.UserLocalStore;


public class ExerciseMenu extends ActionBarActivity implements SensorEventListener {

    Chronometer myChronometer;
    TextView txtHeartRate;
    TextView txtDistance;
    Spinner spinnerExerciseName;
    TextView viewStart;

    FitnessRecordDA myFitnessRecordDA;

    double totalHR = 0.0;
    static int HRno = 0;

    //distance & accelarator
    private boolean mInitialized; // used for initializing sensor only once
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    int stepsCount = 0;
    private final float NOISE = (float) 2.0;
    double mLastX;
    double mLastY;
    double mLastZ;

    UserLocalStore userLocalStore;
    ServerRequests serverRequests;
    FitnessRecord fitnessRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_menu);

        viewStart = (TextView) findViewById(R.id.ViewStart);

        // Initialize Accelerometer sensor
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Initialize HR Strip
        SharedPreferences prefs = getSharedPreferences("BLEdevice", MODE_PRIVATE);
        mDeviceName = prefs.getString("deviceName", "No name defined");//"No name defined" is the default value.
        if (!mDeviceName.equals("No name defined")) {
            mDeviceAddress = prefs.getString("deviceAddress", "No addess defined");
            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        } else {
            Toast.makeText(this, "No device paired", Toast.LENGTH_SHORT).show();
        }

        myFitnessRecordDA = new FitnessRecordDA(this);

        String[] exerciseName = new String[] {"Running", "Cycling", "Hiking ", "Workout","Sport"};
        spinnerExerciseName = (Spinner) findViewById(R.id.spinnerExerciseName);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.exercise_spiiner_item, exerciseName);
        spinnerExerciseName.setAdapter(spinnerAdapter);

        myChronometer = (Chronometer) findViewById(R.id.chronometerTimer);
        txtHeartRate = (TextView) findViewById(R.id.textViewHeartRate);
        txtHeartRate.setText(" -- mbp");
        txtDistance = (TextView) findViewById(R.id.textViewDistance);
        txtDistance.setText("-- m");

        userLocalStore = new UserLocalStore(this);
        serverRequests = new ServerRequests(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothLeService!=null) {
            unbindService(mServiceConnection);
            mBluetoothLeService = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonStart(View view){
        String txt = viewStart.getText().toString();
        if (txt.equals("Start")){
            StartTimer();
            viewStart.setText("Stop");
        }else{
            StopTimer();
            viewStart.setText("Start");
        }
    }

    public void StartTimer(){
        //start count time
        int stoppedMilliseconds = 0;
        String chronoText = myChronometer.getText().toString();
        String array[] = chronoText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000 + Integer.parseInt(array[2]) * 1000;
        }
        myChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
        myChronometer.start();

        //start count step
        startSensor();
        spinnerExerciseName.setEnabled(false);

    }

    public void PauseTimer( ){
        myChronometer.stop();
    }

    public void StopTimer( ){
        myChronometer.stop();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Stop activity")
                .setMessage("Confirm stop fitness activity now?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //reset Timer
                        addFitnessRecord();
                        int stoppedMilliseconds = 0;
                        myChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                        stopSensor();
                        txtDistance.setText("-- m");
                        spinnerExerciseName.setEnabled(true);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        myChronometer.start();
                    }
                }).create();
        dialog.show();

    }

    public void addFitnessRecord(){
        //add in fitness record
        try {
            //get current Datetime
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.sss");
            String formattedTime = df.format(c.getTime());
            String formattedDate = c.get(Calendar.YEAR)+ "-" +(c.get(Calendar.MONTH)+1) + "-" + c.get(Calendar.DATE);

            //get Distance
            String[] distanceString = txtDistance.getText().toString().split("m");
            double distanceAmount = 0.0;
            if (!distanceString[0].trim().equals("--")){
                distanceAmount = Double.parseDouble(distanceString[0].trim());
            }

            //get duration
            int stoppedMilliseconds = 0;
            String chronoText = myChronometer.getText().toString();
            String array[] = chronoText.split(":");
            if (array.length == 2) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
            } else if (array.length == 3) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000 + Integer.parseInt(array[2]) * 1000;
            }
            int timerSecond = stoppedMilliseconds/1000;

            //get calories
            // jogging general MET 7.0
            //url https://en.wikipedia.org/wiki/Metabolic_equivalent
            // Calories = METS x weight (kg) x time (hours) 
            //url http://www.mhhe.com/hper/physed/clw/webreview/web07/tsld007.htm
            HealthProfileDA healthProfileDA = new HealthProfileDA(this);
            HealthProfile healthProfile = healthProfileDA.getLastHealthProfile();
            double calories = 7.0 * healthProfile.getWeight() * (timerSecond/60/60);

            //get HR
            double averageHR = 0.0;
            if (HRno!=0){
                averageHR = totalHR / HRno;
            }

            fitnessRecord = new FitnessRecord(myFitnessRecordDA.generateNewFitnessRecordID(), userLocalStore.returnUserID()+"", spinnerExerciseName.getSelectedItem().toString(), timerSecond, distanceAmount, calories, 0, averageHR, formattedDate + " " + formattedTime);
            //final boolean success = myFitnessRecordDA.addFitnessRecord(new FitnessRecord(myFitnessRecordDA.generateNewFitnessRecordID(), userLocalStore.returnUserID()+"", spinnerExerciseName.getSelectedItem().toString(), timerSecond, distanceAmount, calories, 0, averageHR, formattedDate + " " + formattedTime));
            boolean success = myFitnessRecordDA.addFitnessRecord(fitnessRecord);
            if (success) {
                serverRequests.storeFitnessRecordInBackground(fitnessRecord);
                //Toast.makeText(ExerciseMenu.this, "Insert fitness record success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ExerciseMenu.this, "Insert fitness record fail", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(ExerciseMenu.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //setting button
    public void showSetting(View view){

    }

    //==================================================== Step sensor  ======================================================
    private void startSensor() {
        try {
            mSensorManager.registerListener(this, mAccelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                // Horizontal shake
                // do something here if you like

            } else if (deltaY > deltaX) {
                // Vertical shake
                // do something here if you like

            } else if ((deltaZ > deltaX) && (deltaZ > deltaY)) {
                // Z shake
                stepsCount = stepsCount + 1;
                if (stepsCount > 0) {
                    //step = 0.45 * Height
                    //url http://stackoverflow.com/questions/22292617/how-to-calculate-distance-while-walking-in-android
                    UserProfileDA userProfileDA = new UserProfileDA(this);
                    UserProfile userProfile = userProfileDA.getUserProfile2();
                    txtDistance.setText(String.format("%.2f m", (stepsCount * (0.414 * userProfile.getHeight())) / 100));
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

    //Step sensor
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //========================== HR BLE =====================================================================================
    String mDeviceName="";
    String mDeviceAddress="";

    private final static String TAG = ExerciseMenu.class.getSimpleName();
    private BluetoothLeService mBluetoothLeService;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private boolean mConnected = false;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                Toast.makeText(ExerciseMenu.this,"Heart Rate Sensor connected",Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                Toast.makeText(ExerciseMenu.this,"Heart Rate Sensor disconnected",Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();
                mBluetoothLeService.connect(mDeviceAddress);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private void displayData(String data) {
        if (data != null) {
            txtHeartRate.setText(" " + data + " mbp");
            //record HR
            if (!txtHeartRate.getText().toString().contains("--")) {
                HRno++;
                double currentHR = Double.parseDouble(txtHeartRate.getText().toString().replace("mbp", "").trim());
                totalHR += currentHR;
            }
        }else{
            txtHeartRate.setText(" -- mbp");
        }
    }

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();

            if (uuid.equals("0000180d-0000-1000-8000-00805f9b34fb")) {
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    uuid = gattCharacteristic.getUuid().toString();
                    if (uuid.equals("00002a37-0000-1000-8000-00805f9b34fb")) {
                        updateData(gattCharacteristic);
                    }
                }
            }
        }
    }

    public void updateData(BluetoothGattCharacteristic gattCharacteristic){
        if (mGattCharacteristics != null) {
            final BluetoothGattCharacteristic characteristic = gattCharacteristic;
            final int charaProp = characteristic.getProperties();
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                // If there is an active notification on a characteristic, clear
                // it first so it doesn't update the data field on the user interface.
                if (mNotifyCharacteristic != null) {
                    mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
                    mNotifyCharacteristic = null;
                }
                mBluetoothLeService.readCharacteristic(characteristic);
            }
            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                mNotifyCharacteristic = characteristic;
                mBluetoothLeService.setCharacteristicNotification(characteristic, true);
            }
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

}
