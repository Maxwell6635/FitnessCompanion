
package my.com.taruc.fitnesscompanion.UI;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.choicemmed.a30.A30BLEService;
import com.choicemmed.a30.BleConst;
import com.choicemmed.a30.BleService;
import com.choicemmed.a30.CmdQueue;

import my.com.taruc.fitnesscompanion.R;

public class IChoiceActivity extends Activity implements View.OnClickListener {


    // 默认值
    private int gender = 1;
    private int year_birthday = 1991;
    private int month_birthday = 10;
    private int day_birthday = 28;
    private int height = 175;
    private int weight = 60;
    private String ymd = "2015-12-27";
    private int week = 3;
    private int hour = 16;
    private int minute = 45;
    private int second = 23;
    private int target = 65432;
    private int unit = 1;
    private int t = 1;
    private int hour_Tzone = 8;
    private int minute_Tzone = 52;
    private int timeFormat = 12;




    int info[] = {};
    private Intent service;
    private TextView txt_data, txt_log;
    private EditText ed_pwd;
    private Button btn_find, btn_connected, btn_clear, btn_clearList;

    private SharedPreferences preferences;
    private Receiver receiver;
    private Spinner sp;
    private A30BLEService a30bleService;
    private String[] arr = {"获取Id", "电池电量", "软件版本号", "获取历史数据", "设置问候语",
            "设置设备用户信息", "设置时间制式", "设置时间", "设置显示顺序", "设置锻炼目标", "设置距离单位",
            "设置温度单位", "清除运动数据", "设置时间时区", "获取设备时间"};

    private String serviceId2Compare;
    private String pwd2Compare;

    private static final String PWD = "password";
    private static final String A30_PREFERENCE = "A30sp";
    private static final String SERVICEUUID = "serviceUUID";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ichoice);


        service = new Intent(this, BleService.class);
        startService(service);
        a30bleService = A30BLEService.getInstance(this);

        a30bleService.didSetTimeFormat(timeFormat);
        a30bleService.didSetTime(ymd, week, hour, minute, second);

        preferences = getSharedPreferences(A30_PREFERENCE, Context.MODE_PRIVATE);
        serviceId2Compare = preferences.getString(SERVICEUUID, null);
        pwd2Compare = preferences.getString(PWD, null);

        initview();

        registBroadcast();

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arr);

        sp.setAdapter(adapter);

        // 选中那个 发那个广播给service
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String str = ed_pwd.getText().toString().trim();
                switch (position) {
                    case 0:
                        a30bleService.didGetDeviceID();
                        break;
                    case 1:
                        a30bleService.didGetDeviceBattery();
                        break;
                    case 2:
                        a30bleService.didGetVerson();
                        break;
                    case 3:
                        a30bleService.didGetHistoryDate();
                        break;
                    case 4:
                        a30bleService.didSetGreet(str);
                        break;
                    case 5:
                        a30bleService.didSetPerInfo(gender, year_birthday,
                                month_birthday, day_birthday, height, weight);
                        break;
                    case 6:
                        a30bleService.didSetTimeFormat(timeFormat);
                        break;
                    case 7:
                        a30bleService.didSetTime(ymd, week, hour, minute, second);
                        a30bleService.didGetTime();
                        break;
                    case 8:
                        break;
                    case 9:
                        a30bleService.didSetExerciseTarget(target);
                        break;
                    case 10:
                        a30bleService.didSetDistanceUnit(unit);
                        break;
                    case 11:
                        a30bleService.didSetTempertureUnit(t);
                        break;
                    case 12:
                        a30bleService.didDelHistoryData();
                        break;
                    case 13:
                        a30bleService.didSetTimeZone(hour_Tzone, minute_Tzone);
                        break;
                    case 14:
                        a30bleService.didGetTime();
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BleConst.SF_ACTION_DEVICE_CONNECTED_STATE_CONNECTED);
        filter.addAction(BleConst.SF_ACTION_DEVICE_CONNECTED_STATE_DISCONNECTED);
        filter.addAction(BleConst.SF_ACTION_DEVICE_CONNECTED_FAILED);
        filter.addAction(BleConst.SF_ACTION_DEVICE_CONNECTED_SUCCESS);
        filter.addAction(BleConst.SF_ACTION_DEVICE_RETURNDATA_DEVICEID);
        filter.addAction(BleConst.SF_ACTION_DEVICE_RETURNDATA_SERVICEID);
        filter.addAction(BleConst.SF_ACTION_DEVICE_RETURNDATA);
        filter.addAction(BleConst.SF_ACTION_DEVICE_RETURNDATA_STEP);
        filter.addAction(BleConst.SF_ACTION_OPEN_BLUETOOTH);
        filter.addAction(BleConst.SF_ACTION_SEND_PWD);
        receiver = new Receiver();
        registerReceiver(receiver, filter);
    }

    private void initview() {
        txt_data = (TextView) this.findViewById(R.id.txt_data);
        txt_log = (TextView) this.findViewById(R.id.txt_log);
        sp = (Spinner) this.findViewById(R.id.sp_choose);
        ed_pwd = (EditText) this.findViewById(R.id.ed_pwd);
        btn_clear = (Button) this.findViewById(R.id.btn_clear);
        btn_clearList = (Button) this.findViewById(R.id.btn_clearList);
        btn_find = (Button) this.findViewById(R.id.btn_find);
        btn_connected = (Button) this.findViewById(R.id.btn_link);

        btn_clear.setOnClickListener(this);
        btn_clearList.setOnClickListener(this);
        btn_find.setOnClickListener(this);
        btn_connected.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(BleConst.SR_ACTION_SCANDEVICE);
        switch (v.getId()) {
            case R.id.btn_find:
                a30bleService.didFindDeivce();
                v.setEnabled(false);
                break;
            case R.id.btn_link:
                a30bleService.didLinkDevice(serviceId2Compare, pwd2Compare);
                break;
            case R.id.btn_clear:
                txt_log.setText("");
                break;
            case R.id.btn_clearList:
                CmdQueue.getInstance(this).clearList();
                break;
            default:
                break;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }

        stopService(service);
    }

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String extra = intent.getStringExtra("DATA");
            switch (intent.getAction()) {
                case BleConst.SF_ACTION_DEVICE_RETURNDATA:
                    txt_log.append(extra + "\n");
                    if ((extra + "").contains("失败")) {
                        btn_find.setEnabled(true);
                    }

                    break;
                case BleConst.SF_ACTION_DEVICE_RETURNDATA_STEP:
                    txt_data.setText(extra);
                    break;
                case BleConst.SF_ACTION_SEND_PWD:
                    preferences.edit().putString(PWD, extra).commit();
                    txt_log.append(extra + "\n");
                    pwd2Compare = extra;
                    break;
                case BleConst.SF_ACTION_DEVICE_RETURNDATA_SERVICEID:
                    preferences.edit().putString(SERVICEUUID, extra).commit();
                    txt_log.append(extra + "\n");
                    serviceId2Compare = extra;
                    break;
                case BleConst.SF_ACTION_OPEN_BLUETOOTH:// 打开蓝牙操作
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    break;

                default:

                    break;
            }
        }
    }


    private int REQUEST_ENABLE_BT = 0x101; // 蓝牙开关返回值

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (Activity.RESULT_OK == resultCode) {
                Intent intent = new Intent(BleConst.SR_ACTION_SCANDEVICE);
                a30bleService.didFindDeivce();
                btn_find.setEnabled(false);
            }
        }

    }

}


