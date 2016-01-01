package my.com.taruc.fitnesscompanion.BackgroundSensor;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.SensorEventListener;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.com.taruc.fitnesscompanion.HRStripBLE.BluetoothLeService;
import my.com.taruc.fitnesscompanion.UI.ExercisePage;

/**
 * Created by saiboon on 24/12/2015.
 */
public class HeartRateSensor {

    private Context context;

    //========================== HR BLE =====================================================================================
    public String mDeviceName="";
    public String mDeviceAddress="";

    private final static String TAG = ExercisePage.class.getSimpleName();
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
                //finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    public HeartRateSensor(Context context){
        this.context = context;

        SharedPreferences prefs = context.getSharedPreferences("BLEdevice", context.MODE_PRIVATE);
        mDeviceName = prefs.getString("deviceName", "No name defined"); //"No name defined" is the default value.
        if (!mDeviceName.equals("No name defined")) {
            mDeviceAddress = prefs.getString("deviceAddress", "No address defined");
            Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
            context.bindService(gattServiceIntent, mServiceConnection, context.BIND_AUTO_CREATE);
        } else {
            Toast.makeText(context, "No device paired", Toast.LENGTH_SHORT).show();
        }
    }

    public ServiceConnection getmServiceConnection() {
        return mServiceConnection;
    }

    public ArrayList<ArrayList<BluetoothGattCharacteristic>> getmGattCharacteristics() {
        return mGattCharacteristics;
    }

    public BluetoothLeService getmBluetoothLeService() {
        return mBluetoothLeService;
    }

    public void setmBluetoothLeService(BluetoothLeService mBluetoothLeService) {
        this.mBluetoothLeService = mBluetoothLeService;
    }

    public BluetoothGattCharacteristic getmNotifyCharacteristic() {
        return mNotifyCharacteristic;
    }

    public boolean ismConnected() {
        return mConnected;
    }

    public void setmConnected(boolean mConnected) {
        this.mConnected = mConnected;
    }

    public static String getTAG() {
        return TAG;
    }

    public void displayGattServices(List<BluetoothGattService> gattServices) {
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

    public static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
