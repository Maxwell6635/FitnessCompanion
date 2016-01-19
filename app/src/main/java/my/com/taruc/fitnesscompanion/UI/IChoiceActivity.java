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
import android.widget.Spinner;
import android.widget.TextView;

import com.choicemmed.a30.A30BLEService;
import com.choicemmed.a30.BleConst;
import com.choicemmed.a30.BleService;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.Database.RealTimeFitnessDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.UserLocalStore;

public class IChoiceActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.txt_data)
    TextView txtData;
    @Bind(R.id.btn_find)
    Button btnFind;
    @Bind(R.id.btn_link)
    Button btnLink;
    @Bind(R.id.sp_choose)
    Spinner spChoose;
    @Bind(R.id.tv_Log)
    TextView txtLog;
    @Bind(R.id.tv_battery)
    TextView tvBattery;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.tv_deviceid)
    TextView tvDeviceId;
    @Bind(R.id.tv_datetime)
    TextView tvDateTime;

    // 默认值
    private int gender;
    private int year_birthday;
    private int month_birthday;
    private int day_birthday;
    private int height;
    private int weight;
    private String ymd;
    private int week;
    private int hour;
    private int minute;
    private int second;
    private int target = 10000;
    private int unit = 1;
    private int t = 1;
    private int hour_Tzone = 8;
    private int minute_Tzone = 59;
    private int timeFormat = 24;

    int info[] = {};
    private Intent service;
    private SharedPreferences preferences;
    private Receiver receiver;
    private A30BLEService a30bleService;
    private String[] arr = {"", "获取历史数据", "设置问候语", "设置设备用户信息", "设置时间制式", "设置时间", "设置锻炼目标", "设置距离单位", "设置温度单位", "清除运动数据", "设置时间时区"};

    private String serviceId2Compare;
    private String pwd2Compare;
    private static final String PWD = "password";
    private static final String A30_PREFERENCE = "A30sp";
    private static final String SERVICEUUID = "serviceUUID";

    private UserLocalStore mUserLocalStore;
    private RealTimeFitnessDA realTimeFitnessDA;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ichoice);
        ButterKnife.bind(this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
        mUserLocalStore = new UserLocalStore(this);
        service = new Intent(this, BleService.class);
        startService(service);
        a30bleService = A30BLEService.getInstance(this);

        setCurrentDateandTime();
        a30bleService.didSetTimeFormat(timeFormat);
        a30bleService.didSetTime(ymd, week, hour, minute, second);

        preferences = getSharedPreferences(A30_PREFERENCE, Context.MODE_PRIVATE);
        serviceId2Compare = preferences.getString(SERVICEUUID, null);
        pwd2Compare = preferences.getString(PWD, null);

        initview();
        registBroadcast();

        if (pwd2Compare != null) {
            btnFind.setEnabled(false);
        } else {
            btnLink.setEnabled(false);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, arr);
        spChoose.setAdapter(adapter);
        spChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                case 0:
                    break;
                case 1:
                    a30bleService.didGetHistoryDate();
                    break;
                case 2:
                    a30bleService.didSetGreet("HELLO");
                    break;
                case 3:
                    setProfile();
                    a30bleService.didSetPerInfo(gender, year_birthday, month_birthday, day_birthday, height, weight);
                    break;
                case 4:
                    a30bleService.didSetTimeFormat(timeFormat);
                    a30bleService.didGetTime();
                    break;
                case 5:
                    setCurrentDateandTime();
                    a30bleService.didSetTime(ymd, week, hour, minute, second);
                    a30bleService.didGetTime();
                    break;
                case 6:
                    a30bleService.didSetExerciseTarget(target);
                    break;
                case 7:
                    a30bleService.didSetDistanceUnit(unit);
                    break;
                case 8:
                    a30bleService.didSetTempertureUnit(t);
                    break;
                case 9:
                    a30bleService.didDelHistoryData();
                    break;
                case 10:
                    a30bleService.didSetTimeZone(hour_Tzone, minute_Tzone);
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
        btnFind.setOnClickListener(this);
        btnLink.setOnClickListener(this);
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
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
    }

    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String extra = intent.getStringExtra("DATA");
            switch (intent.getAction()) {
            case BleConst.SF_ACTION_DEVICE_RETURNDATA:
                if (extra.contains("Level")) {
                    String[] battery = extra.split(":");
                    tvBattery.setText(battery[1]);
                }else if ((extra + "").contains("失败")) {
                    btnFind.setEnabled(true);
                }else if (extra.contains("步数")) {
                    String[] step = extra.split(":");
                    txtLog.append(step[0]);
                }else if(extra.contains("Version")){
                    String[] versionNumber = extra.split(":");
                    tvVersion.setText(versionNumber[1]);
                }else if(extra.contains("Device ID")){
                    String[] deviceID = extra.split(":");
                    tvDeviceId.setText(deviceID[1]);
                }else if(extra.contains("DateTIme")){
                    String[] dateTime = extra.split(":");
                    tvDateTime.setText(dateTime[1]);
                }else if(extra.contains("密码审核成功")){
                    a30bleService.didGetDeviceBattery();
                    a30bleService.didGetVerson();
                    a30bleService.didGetDeviceID();
                    a30bleService.didGetTime();
                }else {
                    txtLog.append(extra + "\n");
                }
                break;
            case BleConst.SF_ACTION_DEVICE_RETURNDATA_STEP:
                txtData.setText(extra);
                break;
            case BleConst.SF_ACTION_SEND_PWD:
                preferences.edit().putString(PWD, extra).commit();
                txtLog.append(extra + "\n");
                pwd2Compare = extra;
                break;
            case BleConst.SF_ACTION_DEVICE_RETURNDATA_SERVICEID:
                preferences.edit().putString(SERVICEUUID, extra).commit();
                txtLog.append(extra + "\n");
                serviceId2Compare = extra;
                break;
            case BleConst.SF_ACTION_DEVICE_HISDATA:
                if (extra != null) {
                    String[] splitYear = extra.split(":");
                }
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
                if (pwd2Compare == null) {
                    Intent intent = new Intent(BleConst.SR_ACTION_SCANDEVICE);
                    a30bleService.didFindDeivce();
                    btnFind.setEnabled(false);
                } else {
                    a30bleService.didLinkDevice(serviceId2Compare, pwd2Compare);
                }
            }
        }
    }

    public void setCurrentDateandTime() {
        Calendar calender = Calendar.getInstance();
        int cyear = calender.get(Calendar.YEAR);//calender year starts from 1900 so you must add 1900 to the value recevie.i.e., 1990+112 = 2012
        int cmonth = calender.get(Calendar.MONTH) + 1;//this is april so you will receive  3 instead of 4.
        int cday = calender.get(Calendar.DAY_OF_MONTH);
        hour = calender.get(Calendar.HOUR_OF_DAY);
        minute = calender.get(Calendar.MINUTE);
        second = calender.get(Calendar.SECOND);
        ymd = cyear + "-" + cmonth + "-" + cday;
        week = calender.get(Calendar.WEEK_OF_YEAR);
    }

    public void setProfile() {
        UserProfile userProfile = mUserLocalStore.getLoggedInUser();
        if (userProfile != null) {
            Double height_temp = userProfile.getHeight();
            Double weight_temp = userProfile.getInitial_Weight();
            if (userProfile.getGender().equalsIgnoreCase("Male")) {
                gender = 2;
            } else {
                gender = 3;
            }
            DateTime.Date date = userProfile.getDOB().getDate();
            year_birthday = date.getYear();
            month_birthday = date.getMonth();
            day_birthday = date.getNoOfDayInCurrentMonth();
            height = height_temp.intValue();
            weight = weight_temp.intValue();
      }
    }
}


