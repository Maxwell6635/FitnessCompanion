package com.choicemmed.a30;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.choicemmed.a30.command.DelHistoryDataCmd;
import com.choicemmed.a30.command.DeviceBatteryCmd;
import com.choicemmed.a30.command.DeviceHistoryCmd;
import com.choicemmed.a30.command.DeviceIDCmd;
import com.choicemmed.a30.command.DeviceTimeCmd;
import com.choicemmed.a30.command.DeviceVersionCmd;
import com.choicemmed.a30.command.ICommand;
import com.choicemmed.a30.command.SetDistanceUnitCmd;
import com.choicemmed.a30.command.SetExeriseTargetCmd;
import com.choicemmed.a30.command.SetGreetCmd;
import com.choicemmed.a30.command.SetPerInfoCmd;
import com.choicemmed.a30.command.SetPwdCmd;
import com.choicemmed.a30.command.SetTempUnitCmd;
import com.choicemmed.a30.command.SetTimeCmd;
import com.choicemmed.a30.command.SetTimeFormatCmd;
import com.choicemmed.a30.command.SetTimeZoneCmd;

import java.util.UUID;

@SuppressLint({"NewApi", "HandlerLeak", "DefaultLocale"})
public class BleService extends Service {

    public static final String TAG = "BleService";

    // private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattService blservice;

    private Receiver receiver;

    /**
     * 搜索设备的uuid
     **/
    private String deviceUUID;
    /**
     * 设备链接状态
     **/
    private boolean deviceConneted = false;
    /**
     * 设备前缀
     **/
    private final String DEVICEPREIX_FF = "ba11f08c5f140b0d1030ff";// ba11f08c5f140b0d1030
    private final String DEVICEPREIX_00 = "ba11f08c5f140b0d103000";// ba11f08c5f140b0d1030

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    private static final String Characteristic_UUID_CD01 = "0000cd01-0000-1000-8000-00805f9b34fb";
    private static final String Characteristic_UUID_CD02 = "0000cd02-0000-1000-8000-00805f9b34fb";
    private static final String Characteristic_UUID_CD03 = "0000cd03-0000-1000-8000-00805f9b34fb";
    private static final String Characteristic_UUID_CD04 = "0000cd04-0000-1000-8000-00805f9b34fb";
    private static final String Characteristic_UUID_CD20 = "0000cd20-0000-1000-8000-00805f9b34fb";

    protected static final int FIND_DEVICEFAILED = 0x0001;
    protected static final int H_FIND_DEVICE_SUCCESS = 0x0002;
    protected static final int H_STATE_CONNETED = 0x0003;
    protected static final int H_STATE_DISCONNECTED = 0x0004;
    protected static final int H_RETURN_SERVICEID = 0x0005;
    protected static final int H_RETURN_DATA = 0x0006;
    protected static final int H_RETURN_DATA_STEP = 0x0007;

    // private List cmdList = new LinkedList<String>();

    private String serviceCCID = null;

    private String pwd = null;
    /**
     * 命令队列
     **/
    private CmdQueue queue;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initAndRegistReceiver();
        Log.i(TAG, "服务开启");
    }

    /**
     * service广播接收器
     *
     * @author lazy_xia
     */
    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data;
            String cmd;
            Log.i(TAG, "onreceive 接到了广播 。。。。");
            switch (intent.getAction()) {
                case BleConst.SR_ACTION_SCANDEVICE:
                    mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                    mBluetoothAdapter = mBluetoothManager.getAdapter();
                    if (mBluetoothAdapter != null) {
                        Log.i(TAG, "支持蓝牙4.0 的上位机设备");
                        if (mBluetoothAdapter.isEnabled()) {
                            Log.i(TAG, "蓝牙打开状态  ");
                            String str = intent.getStringExtra("DATA");// ba11f08c-5f14-0b0d-1080-007cbe000320
                            if (str != null) {
                                String[] split = str.split("&");
                                scanDevice(split[0]);
                                pwd = split[1];// 得到配对的密码 直接进行密码验证
                            } else {
                                scanDevice(null);
                            }

                        } else {
                            // 发送 界面开启蓝牙操作的 广播
                            Log.i(TAG, "发送    ACTION_OPEN_BLUETOOTH");
                            updateBroadCast(BleConst.SF_ACTION_OPEN_BLUETOOTH, null);// TODO
                        }
                    }
                    break;

                case BleConst.SR_ACTION_SEND_PWD:
                    data = intent.getStringExtra("DATA");
                    SetPwdCmd pwdCmd = new SetPwdCmd(data);
                    queue.addCmdTOP(pwdCmd);
                    queue.executeNextCmd(blservice, mBluetoothGatt);
                    updateBroadCast(BleConst.SF_ACTION_SEND_PWD, data);
                    break;
                case BleConst.SR_ACTION_DEVICEID:
                    add2Execute(new DeviceIDCmd());
                    break;
                case BleConst.SR_ACTION_BATTERYPOWER:
                    add2Execute(new DeviceBatteryCmd());
                    break;
                case BleConst.SR_ACTION_VERSION:
                    add2Execute(new DeviceVersionCmd());
                    break;
                case BleConst.SR_ACTION_HISTORYDATA:
                    add2Execute(new DeviceHistoryCmd());
                    break;
                case BleConst.SR_ACTION_SET_PERINFO:
                    data = intent.getStringExtra("DATA");
                    add2Execute(new SetPerInfoCmd(data));
                    break;
                case BleConst.SR_ACTION_SET_GREET:
                    data = intent.getStringExtra("DATA");
                    add2Execute(new SetGreetCmd(data));
                    break;
                case BleConst.SR_ACTION_STATE_DISCONNECTED:
                    break;
                case BleConst.SR_ACTION_STOPSCANDEVICE:
                    break;
                case BleConst.SR_ACTION_SET_TIMEZONE:
                    data = intent.getStringExtra("DATA");
                    add2Execute(new SetTimeZoneCmd(data));
                    break;

                case BleConst.SR_ACTION_SET_TIME:
                    data = intent.getStringExtra("DATA");
                    add2Execute(new SetTimeCmd(data));
                    break;
                case BleConst.SR_ACTION_SET_TIMEFORMAT:
                    data = intent.getStringExtra("DATA");
                    add2Execute(new SetTimeFormatCmd(data));
                    break;
                case BleConst.SR_ACTION_SET_EXERCISE_TARGET:
                    data = intent.getStringExtra("DATA");
                    add2Execute(new SetExeriseTargetCmd(data));
                    break;
                case BleConst.SR_ACTION_SET_DISTANCE_UNIT:
                    data = intent.getStringExtra("DATA");
                    add2Execute(new SetDistanceUnitCmd(data));
                    break;
                case BleConst.SR_ACTION_SET_TEMPERATURE_UNIT:
                    data = intent.getStringExtra("DATA");
                    add2Execute(new SetTempUnitCmd(data));
                    break;
                case BleConst.SR_ACTION_DEL_HISTORYDATA:
                    data = intent.getStringExtra("DATA");
                    add2Execute(new DelHistoryDataCmd());
                    break;
                case BleConst.SR_ACTION_TIME:
                    add2Execute(new DeviceTimeCmd());
                    break;

                default:
                    break;
            }
        }

        void add2Execute(ICommand bCmd) {
            if (queue != null) {
                queue.addCmd(bCmd);
                queue.executeNextCmd(blservice, mBluetoothGatt);
            }
        }

    }

    private Handler blueHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case H_FIND_DEVICE_SUCCESS:
                    BluetoothDevice device = (BluetoothDevice) msg.obj;
                    // 建立gatt
                    mBluetoothGatt = device.connectGatt(BleService.this, false, gattCallback);
                    break;
                case H_STATE_CONNETED: // 设备链接成功
                    updateBroadCast(BleConst.SF_ACTION_DEVICE_CONNECTED_STATE_CONNECTED, null);
                    mBluetoothGatt.discoverServices();// 找设备服务
                    break;
                case H_STATE_DISCONNECTED: // 设备链接失败
                    updateBroadCast(
                            BleConst.SF_ACTION_DEVICE_CONNECTED_STATE_DISCONNECTED,
                            null);
                    // 状态链接失败 需要重新扫描设备 建立gatt
                    if (mBluetoothGatt != null) {
                        mBluetoothGatt.disconnect();
                        mBluetoothGatt.close();

                    }
                    // 重新建立 gatt链接
                    if (mBluetoothAdapter != null) {
                        mBluetoothAdapter.stopLeScan(leScanCallback);
                        scanDevice(deviceUUID); // 这里找到的是 之前的设备
                    }

                    break;
                case H_RETURN_DATA:
                    updateBroadCast(BleConst.SF_ACTION_DEVICE_RETURNDATA, msg.obj + "");
                    break;
                case H_RETURN_DATA_STEP:
                    updateBroadCast(BleConst.SF_ACTION_DEVICE_RETURNDATA_STEP, msg.obj + "");
                    break;
                case H_RETURN_SERVICEID:
                    updateBroadCast(BleConst.SF_ACTION_DEVICE_RETURNDATA_SERVICEID, msg.obj + "");
                    break;
                default:
                    break;
            }
        }
    };

    private LeScanCallback leScanCallback = new LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) { //
            final String struuid = bytes2HexString(scanRecord).replace("-", "").toLowerCase();
            new Thread(new Runnable() {
                String uuid4compare = DEVICEPREIX_FF.toLowerCase();

                @Override
                public void run() {
                    Message msg = Message.obtain();
                    if (deviceUUID != null) {
                        mBluetoothAdapter.stopLeScan(leScanCallback);
                        Log.i(TAG, "之前设备的deviceUUID" + deviceUUID);
                        uuid4compare = deviceUUID;
                    }

                    if (struuid.contains(uuid4compare)) {
                        deviceConneted = true;
                        mBluetoothAdapter.stopLeScan(leScanCallback);
                        msg.what = H_FIND_DEVICE_SUCCESS;
                        msg.obj = device;
                        Log.i(TAG, "找到了设备" + device.getAddress());
                    }
                    blueHandler.sendMessage(msg);
                }
            }).start();
        }
    };

    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            // 判断设备链接状态改变 发送消息 对应处理广播事件

            Message msg = Message.obtain();
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    msg.what = H_STATE_CONNETED;
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    msg.what = H_STATE_DISCONNECTED;
                    break;

                default:
                    break;
            }

            blueHandler.sendMessage(msg);
            Log.i(TAG, "onConnectionStateChange()  发送了" + msg.what);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Message msg = Message.obtain();
            switch (status) {
                case BluetoothGatt.GATT_SUCCESS:
                    Log.i(TAG, "写cd01 GATT_SUCCESS");
                    for (BluetoothGattService gattService : gatt.getServices()) {
                        String serviceUUID = gattService.getUuid().toString();

                        String serviceUUID4Compare = serviceUUID.toLowerCase()
                                .toLowerCase();
                        if (serviceUUID4Compare.replace("-", "").contains(DEVICEPREIX_00)) {
                            serviceCCID = serviceUUID;
                            // 找到UUID 存数据库 设备的uuid
                            msg.what = H_RETURN_SERVICEID;
                            msg.obj = serviceUUID.replace("-", "");
                            // TODO 这个地方要写一次吗？
                            setCharacteristicNotification(mBluetoothGatt,
                                    gattService.getCharacteristic(UUID
                                            .fromString(Characteristic_UUID_CD01)),
                                    true);
                            Log.i(TAG, "写cd01");
                            break;
                        }
                    }
                case BluetoothGatt.GATT_FAILURE:
                    // TODO msg.what = H_GATT_FAILED; 重新在找
                    deviceConneted = false;
                    // msg.what = H_STATE_DISCONNECTED;
                    break;

                default:
                    break;
            }
            blueHandler.sendMessage(msg);
            Log.i(TAG, "onServicesDiscovered() 发送了" + msg.what);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            Message msg = Message.obtain();
            byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                StringBuilder builder = new StringBuilder();
                for (byte b : data) {
                    builder.append(String.format("%02X", b));
                }
                String dataString = builder.toString();// 这个是16进制的 设备返回的值

                queue.analysResp(characteristic, blservice, gatt, dataString);
                Log.i(TAG, "onCharacteristicChanged() 设备返回" + dataString);
            }

            blueHandler.sendMessage(msg);

        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor descriptor, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                blservice = gatt.getService(UUID.fromString(serviceCCID));// 找到之前匹配
                // 的ccid
                if (descriptor.getCharacteristic().getUuid()
                        .equals(UUID.fromString(Characteristic_UUID_CD01))) {
                    BluetoothGattCharacteristic characteristic = blservice
                            .getCharacteristic(UUID
                                    .fromString(Characteristic_UUID_CD02));
                    setCharacteristicNotification(gatt, characteristic, true);
                    Log.i(TAG, "onDescriptorWrite()" + status + "  CD01");
                } else if (descriptor.getCharacteristic().getUuid()
                        .equals(UUID.fromString(Characteristic_UUID_CD02))) {
                    BluetoothGattCharacteristic characteristic = blservice
                            .getCharacteristic(UUID
                                    .fromString(Characteristic_UUID_CD03));
                    setCharacteristicNotification(gatt, characteristic, true);
                    Log.i(TAG, "onDescriptorWrite()" + status + "  CD02");
                } else if (descriptor.getCharacteristic().getUuid()
                        .equals(UUID.fromString(Characteristic_UUID_CD03))) {
                    BluetoothGattCharacteristic characteristic = blservice
                            .getCharacteristic(UUID
                                    .fromString(Characteristic_UUID_CD04));
                    setCharacteristicNotification(gatt, characteristic, true);
                    Log.i(TAG, "onDescriptorWrite()" + status + "  CD03");
                } else if (descriptor.getCharacteristic().getUuid()
                        .equals(UUID.fromString(Characteristic_UUID_CD04))) {

                    queue = CmdQueue.getInstance(BleService.this);
                    if (pwd != null) {
                        queue.addCmd(new SetPwdCmd(pwd));// TODO aa5504b1
                    } else {
                        queue.initializationCmdList();
                    }
                    queue.executeNextCmd(blservice, mBluetoothGatt);
                    Log.i(TAG, "onDescriptorWrite()" + status + "  CD04");
                    // cmdList.add("AA5502B0B2");
                    // executeCmd(blservice, mBluetoothGatt);
                }
            } else {

            }

            Log.i(TAG, "onDescriptorWrite()" + status);

        }

    };

    /**
     * @param action 动作
     * @param data   数据
     */
    private void updateBroadCast(String action, String data) {
        Intent intent = new Intent(action);
        if (data != null) {
            intent.putExtra("DATA", data);
        }
        this.sendBroadcast(intent);

    }

    /**
     * @param gatt
     * @param characteristic
     * @param enabled
     */
    public void setCharacteristicNotification(BluetoothGatt gatt,
                                              BluetoothGattCharacteristic characteristic, boolean enabled) {
        gatt.setCharacteristicNotification(characteristic, enabled);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID
                .fromString(CLIENT_CHARACTERISTIC_CONFIG));
        if (descriptor != null) {
            System.out.println("write descriptor");
            descriptor
                    .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);
        }
        Log.i(TAG, "设置特征值：" + characteristic.getUuid() + "通知");
    }

    /**
     * 准备开始搜寻蓝牙设备--下位机
     *
     * @param uuID 若为null 则找寻所有可以匹配的下位机
     */
    @SuppressWarnings("deprecation")
    private synchronized void scanDevice(String uuID) {
        // 找设备的 id 如果没有则是null
        deviceUUID = uuID;
        Log.i("2-5", "扫描的设备：" + deviceUUID);
        if (mBluetoothAdapter != null) {
            if (mBluetoothGatt != null) {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
            }
            mBluetoothAdapter.stopLeScan(leScanCallback);
        }
        //睡1s
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mBluetoothAdapter.startLeScan(leScanCallback);

        // 没有链接过设备 扫描是有时间限制 30s
        if (uuID == null) {
            blueHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!deviceConneted) {
                        blueHandler.sendEmptyMessage(FIND_DEVICEFAILED);// TODO
                        mBluetoothAdapter.stopLeScan(leScanCallback);
                    }
                }
            }, 40000);
        }

    }

    @SuppressWarnings("unused")
    private String bytes2String(byte[] data) {
        if (data != null && data.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (byte b : data) {
                builder.append(String.format("%02X", b));
            }
            String dataString = builder.toString();// 这个是16进制的 设备返回的值
        }
        return null;
    }

    // 十六进制转byte
    public static String bytes2HexString(byte[] a) {

        int len = a.length;
        byte[] b = new byte[len];
        for (int k = 0; k < len; k++) {
            b[k] = a[a.length - 1 - k];
        }

        String ret = "";
        for (int i = 0; i < len; i++) {
            // String hex = Integer .toHexString(b[ i ]);
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
            // ret += " ";
        }

        return ret;
    }

    public byte[] getHexBytes(String cmd) {
        int len = cmd.length() / 2;
        char[] chars = cmd.toCharArray();
        String[] hexStr = new String[len];
        byte[] bytes = new byte[len];
        for (int i = 0, j = 0; j < len; i += 2, j++) {
            hexStr[j] = "" + chars[i] + chars[i + 1];
            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
        }
        return bytes;
    }

    private byte[] getBytesIncludeVerifySum(String cmd) {
        byte[] value = getHexBytes(cmd);
        byte verifySum = 0;
        for (int i = 2; i < value.length; i++) {
            verifySum += value[i];
        }
        byte[] values = new byte[value.length + 1];
        for (int i = 0; i < value.length; i++) {
            values[i] = value[i];
        }
        values[value.length] = verifySum;
        return values;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (receiver != null) {
            unregisterReceiver(receiver);
            Log.i(TAG, "取消广播注册。。");
        }
        Log.i(TAG, "service 销毁。。");
    }

    /**
     * 实例化并注册 bleService需监听的广播
     */
    private void initAndRegistReceiver() {
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BleConst.SR_ACTION_SCANDEVICE);
        filter.addAction(BleConst.SR_ACTION_SEND_PWD);
        filter.addAction(BleConst.SR_ACTION_STATE_DISCONNECTED);
        filter.addAction(BleConst.SR_ACTION_STOPSCANDEVICE);
        filter.addAction(BleConst.SR_ACTION_SET_BASEINFO);
        filter.addAction(BleConst.SR_ACTION_SET_TIME);
        filter.addAction(BleConst.SR_ACTION_SET_TIMEFORMAT);
        filter.addAction(BleConst.SR_ACTION_SET_TIMEZONE);
        filter.addAction(BleConst.SR_ACTION_SET_GREET);
        filter.addAction(BleConst.SR_ACTION_DEVICEID);
        filter.addAction(BleConst.SR_ACTION_BATTERYPOWER);
        filter.addAction(BleConst.SR_ACTION_VERSION);
        filter.addAction(BleConst.SR_ACTION_TIME);
        filter.addAction(BleConst.SR_ACTION_HISTORYDATA);
        filter.addAction(BleConst.SR_ACTION_SET_EXERCISE_TARGET);
        filter.addAction(BleConst.SR_ACTION_SET_DISTANCE_UNIT);
        filter.addAction(BleConst.SR_ACTION_SET_TEMPERATURE_UNIT);
        filter.addAction(BleConst.SR_ACTION_DEL_HISTORYDATA);
        filter.addAction(BleConst.SR_ACTION_SET_PERINFO);
        registerReceiver(receiver, filter);
    }
}
