package my.com.taruc.fitnesscompanion.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
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
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.BackgroundSensor.AccelerometerSensor2;
import my.com.taruc.fitnesscompanion.BackgroundSensor.StepManager;
import my.com.taruc.fitnesscompanion.BackgroundSensor.TheService;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.RealTimeFitness;
import my.com.taruc.fitnesscompanion.Classes.SleepData;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.ConnectionDetector;
import my.com.taruc.fitnesscompanion.Database.RealTimeFitnessDA;
import my.com.taruc.fitnesscompanion.Database.SleepDataDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.ServerAPI.InsertRequest;
import my.com.taruc.fitnesscompanion.ServerAPI.ServerRequests;
import my.com.taruc.fitnesscompanion.ShowAlert;
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
    @Bind(R.id.btn_unlink)
    Button btnUnlink;
    @Bind(R.id.tv_datetimelabel)
    TextView lblDateTime;

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

    int info[] = {};
    private Intent service;
    private SharedPreferences preferences;
    private Receiver receiver;
    private A30BLEService a30bleService;
    private String[] arr;

    private String serviceId2Compare;
    private String pwd2Compare;
    private boolean mUserLearn;
    private boolean mSync = false;
    private static final String PWD = "password";
    private static final String A30_PREFERENCE = "A30sp";
    private static final String SERVICEUUID = "serviceUUID";
    private static final String USERLEARN = "userlearn";

    private UserLocalStore mUserLocalStore;
    private RealTimeFitnessDA mRealTimeFitnessDA;
    private SleepDataDA mSleepDataDA;
    private ServerRequests mServerRequest;
    private InsertRequest mInsertRequest;
    private ConnectionDetector mConnectionDetector;
    private BluetoothAdapter mBluetoothAdapter;
    private ShowAlert alert = new ShowAlert();
    private ProgressDialog mProgressDialog;
    private boolean mSyncAlready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ichoice);
        ButterKnife.bind(this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
        arr = new String[]{"", this.getResources().getString(R.string.set_greetings), this.getResources().getString(R.string.set_profile)
                , this.getResources().getString(R.string.set_targets), this.getResources().getString(R.string.set_distance_unit)
                , this.getResources().getString(R.string.set_celsius_unit)};

        mUserLocalStore = new UserLocalStore(this);
        mRealTimeFitnessDA = new RealTimeFitnessDA(this);
        mSleepDataDA = new SleepDataDA(this);
        mServerRequest = new ServerRequests(this);
        mInsertRequest = new InsertRequest(this);
        mConnectionDetector = new ConnectionDetector(this);
        service = new Intent(this, BleService.class);
        startService(service);
        a30bleService = A30BLEService.getInstance(this);

        setCurrentDateandTime();

        preferences = getSharedPreferences(A30_PREFERENCE, Context.MODE_PRIVATE);
        serviceId2Compare = preferences.getString(SERVICEUUID, null);
        pwd2Compare = preferences.getString(PWD, null);
        mUserLearn = preferences.getBoolean(USERLEARN, false);

        if (!mUserLearn) {
            ShowcaseView showcaseView = new ShowcaseView.Builder(this)
                    .withHoloShowcase()
                    .setTarget(new ViewTarget(R.id.btn_find, this))
                    .setContentTitle("Tutorial")
                    .setContentText("You Need Turn Off Your IChoice After Click, Switch Back When Bluetooth is On")
                    .setShowcaseEventListener(new OnShowcaseEventListener() {
                                                  @Override
                                                  public void onShowcaseViewHide(ShowcaseView showcaseView) {
                                                      lblDateTime.setVisibility(View.VISIBLE);
                                                      tvDateTime.setVisibility(View.VISIBLE);
                                                      preferences.edit().putBoolean(USERLEARN, true).commit();
                                                  }

                                                  @Override
                                                  public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                                                  }

                                                  @Override
                                                  public void onShowcaseViewShow(ShowcaseView showcaseView) {
                                                      lblDateTime.setVisibility(View.INVISIBLE);
                                                      tvDateTime.setVisibility(View.INVISIBLE);
                                                  }
                                              }
                    )
                    .build();
            showcaseView.setDetailTextAlignment(Layout.Alignment.ALIGN_NORMAL);
            showcaseView.setTitleTextAlignment(Layout.Alignment.ALIGN_NORMAL);
            showcaseView.forceTextPosition(ShowcaseView.BELOW_SHOWCASE);
        }

        initview();
        registBroadcast();

        if (pwd2Compare != null) {
            btnFind.setEnabled(false);
        } else {
            btnLink.setEnabled(false);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.spinner_item, android.R.id.text1, arr);
        spChoose.setAdapter(adapter);
        spChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        a30bleService.didSetGreet("HELLO");
                        break;
                    case 2:
                        setProfile();
                        a30bleService.didSetPerInfo(gender, year_birthday, month_birthday, day_birthday, height, weight);
                        break;
                    case 3:
                        AlertDialog.Builder builder = new AlertDialog.Builder(IChoiceActivity.this);
                        builder.setTitle("Set Step Target");
                        final EditText input = new EditText(IChoiceActivity.this);
                        builder.setView(input);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        int maxLengthofEditText = 6;
                        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthofEditText)});
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                input.getText().toString();
                                a30bleService.didSetExerciseTarget(Integer.parseInt(input.getText().toString()));
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                        break;
                    case 4:
                        AlertDialog.Builder adb = new AlertDialog.Builder(IChoiceActivity.this);
                        adb.setTitle("Set Distance Unit");
                        CharSequence items[] = new CharSequence[]{"KM", "MILES"};
                        adb.setSingleChoiceItems(items, 0, null);
                        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                a30bleService.didSetDistanceUnit(selectedPosition);
                            }
                        }).show();
                        break;
                    case 5:
                        AlertDialog.Builder adb2 = new AlertDialog.Builder(IChoiceActivity.this);
                        adb2.setTitle("Set Temperature Unit");
                        CharSequence items2[] = new CharSequence[]{"Celsius", "Degrees Fahrenheit"};
                        adb2.setSingleChoiceItems(items2, 0, null);
                        adb2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                a30bleService.didSetTempertureUnit(selectedPosition);
                            }
                        }).show();
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
        filter.addAction(BleConst.SF_ACTION_DEVICE_HISDATA);
        receiver = new Receiver();
        registerReceiver(receiver, filter);
    }

    private void initview() {
        btnFind.setOnClickListener(this);
        btnLink.setOnClickListener(this);
        btnUnlink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(BleConst.SR_ACTION_SCANDEVICE);
        switch (v.getId()) {
            case R.id.btn_find:
                if (!mConnectionDetector.isConnectingToInternet()) {
                    // Internet Connection is not present
                    alert.showAlertDialog(this, "Fail", "Internet Connection is Not Available", false);
                } else {
                    a30bleService.didFindDeivce();
                    stopService();
                }
                break;
            case R.id.btn_link:
                if (!mConnectionDetector.isConnectingToInternet()) {
                    // Internet Connection is not present
                    alert.showAlertDialog(this, "Fail", "Internet Connection is Not Available", false);
                } else {
                    a30bleService.didLinkDevice(serviceId2Compare, pwd2Compare);
                    stopService();
                }
                break;
            case R.id.btn_unlink:
                showMessageInIChoiceActivity();
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainMenu.class);
        i.putExtra("ichoicestep", txtData.getText().toString());
        startActivity(i);
        finish();
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
                    } else if ((extra + "").contains("失败")) {
                        btnFind.setEnabled(true);
                    } else if (extra.contains("步数")) {
                        String[] splitYear = extra.split(":");
                        System.out.println(splitYear[1]);
                    } else if (extra.contains("Version")) {
                        String[] versionNumber = extra.split(":");
                        tvVersion.setText(versionNumber[1]);
                    } else if (extra.contains("Device ID")) {
                        String[] deviceID = extra.split(":");
                        tvDeviceId.setText(deviceID[1]);
                    } else if (extra.contains("DateTime")) {
                        String[] dateTime = extra.split(":");
                        tvDateTime.setText("20" + dateTime[1] + " " + dateTime[2] + ":" + dateTime[3] + ":" + dateTime[4]);
                    } else if (extra.contains("密码审核成功")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    setCurrentDateandTime();
                                    a30bleService.didSetTime(ymd, week, hour, minute, second);
                                    a30bleService.didGetDeviceBattery();
                                    a30bleService.didGetVersion();
                                    a30bleService.didGetDeviceID();
                                    a30bleService.didGetTime();
                                    a30bleService.didGetHistoryDate();
                                    mSyncAlready = true;
                                } catch (Exception e) {
                                }
                                mProgressDialog.dismiss();
                            }

                        }).start();

                    } else if (extra.contains("null")) {
                        if (tvBattery.getText().toString().equals("-")) {
                            a30bleService.didGetDeviceBattery();
//                        a30bleService.didGetVersion();
//                        a30bleService.didGetDeviceID();
//                        a30bleService.didGetTime();
//                        txtLog.append(extra + "\n");
                        }
                    }
                    break;
                case BleConst.SF_ACTION_DEVICE_RETURNDATA_STEP:
                    txtData.setText(extra);
                    //@saiboon: update distance for exercise
                    //distanceUpdate();
                    break;
                case BleConst.SF_ACTION_SEND_PWD:
                    preferences.edit().putString(PWD, extra).commit();
                    mUserLocalStore.setIChoiceMode(true);
                    btnFind.setEnabled(false);
                    txtLog.append(extra + "\n");
                    pwd2Compare = extra;
                    break;
                case BleConst.SF_ACTION_DEVICE_RETURNDATA_SERVICEID:
                    preferences.edit().putString(SERVICEUUID, extra).commit();
                    txtLog.append(extra + "\n");
                    serviceId2Compare = extra;
                    break;
                case BleConst.SF_ACTION_DEVICE_HISDATA:
                    txtLog.append(extra + "\n");
                    String[] year = extra.split(",");
                    String currentDate, fulldate = "", previousDate = "";
                    int count = 0;
                    int b = 0;
                    int mStepCount = 0;
                    int mSleepCount = 0;
                    int totalSleep = 0;
                    boolean mPreviousIsStep = false;
                    if (mSync == false) {
                        for (int i = count; i < year.length; i++) {
                            if (year[i].contains("年")) {
                                currentDate = year[i];
                                System.out.println(year[i]);
                                for (int j = b + 1; j < year.length; j++) {
                                    if (year[j].contains("分")) {
                                        String mHour = year[j];
                                        Integer hour = Integer.valueOf(mHour.substring(3, mHour.length())) / 60;
                                        Double min = ((Double.valueOf(mHour.substring(3, mHour.length())) / 60) % 1) * 60;
                                        fulldate = currentDate + " " + hour + ":" + min.intValue();
                                    } else if (year[j].contains("步数")) {
                                        if (previousDate == "") {
                                            mPreviousIsStep = true;
                                            previousDate = fulldate;
                                            String mStep = year[j];
                                            mStepCount += Integer.valueOf(mStep.substring(4, mStep.length()));
                                        } else if (fulldate.compareTo(previousDate) == 0) {
                                            mPreviousIsStep = true;
                                            String mStep = year[j];
                                            mStepCount += Integer.valueOf(mStep.substring(4, mStep.length()));
                                        } else {
                                            DateTime myDateTimeObject = new DateTime().iChoiceConversion(fulldate);
                                            RealTimeFitness realTimeFitness =
                                                    new RealTimeFitness(mRealTimeFitnessDA.generateNewRealTimeFitnessID()
                                                            , mUserLocalStore.returnUserID().toString(), myDateTimeObject, mStepCount);
                                            boolean success = mRealTimeFitnessDA.addRealTimeFitness(realTimeFitness);
                                            if (success) {
                                                mServerRequest.storeRealTimeFitnessInBackground(realTimeFitness);
                                            }
                                            mPreviousIsStep = true;
                                            previousDate = fulldate;
                                            String mStep = year[j];
                                            mStepCount = 0 + Integer.valueOf(mStep.substring(4, mStep.length()));
                                        }
                                    } else if (year[j].contains("睡眠数据")) {
                                        if (fulldate.compareTo(previousDate) == 0) {
                                            mPreviousIsStep = false;
                                            String mSleep = year[j];
                                            mSleepCount += Integer.valueOf(mSleep.substring(6, mSleep.length()));
                                        } else if (mPreviousIsStep) {
                                            System.out.println(fulldate + "Fail Movement" + mSleepCount);
                                        } else {
                                            mPreviousIsStep = false;
                                            System.out.println(fulldate + " Movement " + mSleepCount);
                                            totalSleep += mSleepCount;
                                            System.out.println("Total Movement :" + totalSleep);
                                            DateTime myDateTimeObject = new DateTime().iChoiceConversion(fulldate);
                                            SleepData sleepData = new SleepData(mSleepDataDA.generateNewSleepDataID(), mUserLocalStore.returnUserID().toString()
                                                    , mSleepCount, myDateTimeObject, myDateTimeObject);
                                            boolean sleepSuccess = mSleepDataDA.addSleepData(sleepData);
                                            if (sleepSuccess) {
                                                mInsertRequest.storeSleepDataInBackground(sleepData);
                                            }
                                            previousDate = fulldate;
                                            String mSleep = year[j];
                                            mSleepCount = 0 + Integer.valueOf(mSleep.substring(6, mSleep.length()));
                                        }
                                    } else if (year[j].contains("年")) {
                                        count = j;
                                        b = j;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    mSync = true;
                    a30bleService.didDelHistoryData();
                    txtLog.append("HISTORY" + "\n");
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
                    if (!mSyncAlready) {
                        mProgressDialog = ProgressDialog.show(this, "Sync Data", "Syncing....Please Wait", true);
                        a30bleService.didFindDeivce();
                        btnFind.setEnabled(false);
                    }
                } else {
                    if (!mSyncAlready) {
                        mProgressDialog = ProgressDialog.show(this, "Sync Data", "Syncing....Please Wait", true);
                        btnLink.setEnabled(false);
                        a30bleService.didLinkDevice(serviceId2Compare, pwd2Compare);
                    }
                }
            }
        }
    }


    public void setCurrentDateandTime() {
        Calendar calender = Calendar.getInstance();
        int cyear = calender.get(Calendar.YEAR);
        int cmonth = calender.get(Calendar.MONTH) + 1;//this is april so you will receive  3 instead of 4.
        int cday = calender.get(Calendar.DAY_OF_MONTH);
        hour = calender.get(Calendar.HOUR_OF_DAY);
        minute = calender.get(Calendar.MINUTE);
        second = calender.get(Calendar.SECOND);
        ymd = cyear + "-" + cmonth + "-" + cday;
        week = calender.get(Calendar.WEEK_OF_MONTH);
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
            day_birthday = date.getDateNumber();
            height = height_temp.intValue();
            weight = weight_temp.intValue();
        }
    }

    private void showMessageInIChoiceActivity() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(IChoiceActivity.this);
        dialogBuilder.setMessage("Are You Sure Want To Unlink Device ?");
        dialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (pwd2Compare != null) {
                    preferences.edit().putString(PWD, null).commit();
                    preferences.edit().putString(SERVICEUUID, null).commit();
                    btnFind.setEnabled(true);
                    btnLink.setEnabled(false);
                    mUserLocalStore.setIChoiceMode(false);
                }
            }
        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogBuilder.show();
    }

    public void stopService() {
        stopService(new Intent(this, TheService.class));
        stopService(new Intent(this, AccelerometerSensor2.class));
    }

    public void distanceUpdate() {
        try {
            Intent intent = new Intent(StepManager.BROADCAST_ACTION_2);
            sendBroadcast(intent);
        } catch (Exception ex) {
            Log.i("DistanceUpdateErr", ex.getMessage());
        }
    }

}


