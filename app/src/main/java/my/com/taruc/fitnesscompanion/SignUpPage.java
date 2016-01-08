package my.com.taruc.fitnesscompanion;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.GCM.QuickstartPreferences;
import my.com.taruc.fitnesscompanion.GCM.RegistrationIntentService;
import my.com.taruc.fitnesscompanion.ServerAPI.GetUserCallBack;
import my.com.taruc.fitnesscompanion.ServerAPI.ServerRequests;
import my.com.taruc.fitnesscompanion.Util.ValidateUtil;

public class SignUpPage extends FragmentActivity implements View.OnClickListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String TAG = SignUpPage.class.getName();

    String DOJ;
    int thisYear, birthYear;
    ServerRequests serverRequests;
    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.etName)
    EditText etName;
    @Bind(R.id.etDOB)
    EditText etDOB;
    @Bind(R.id.radioButtonMale)
    RadioButton rbMale;
    @Bind(R.id.radioButtonFemale)
    RadioButton rbFemale;
    @Bind(R.id.etHeight)
    EditText etHeight;
    @Bind(R.id.etWeight)
    EditText etWeight;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.btnRegister)
    Button btnRegister;
    @Bind(R.id.GCM)
    TextView GCM;

    private ValidateUtil mValidateUtil;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("dd-MM-yyyy");
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public String result;
    private SlideDateTimeListener listener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date) {
            Toast.makeText(SignUpPage.this, mFormatter.format(date), Toast.LENGTH_SHORT).show();
            etDOB.setText(mFormatter.format(date));
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year = df.format(date);
            birthYear = Integer.parseInt(year);
            System.out.println(birthYear);
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {
            Toast.makeText(SignUpPage.this, "Canceled", Toast.LENGTH_SHORT).show();

        }
    };

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        ButterKnife.bind(this);
        btnRegister.setOnClickListener(this);
        etDOB.setOnClickListener(this);
        serverRequests = new ServerRequests(this);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                result = intent.getStringExtra("GCM");
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
//                    GCM.setText(result);
                } else {
//                    GCM.setText(getString(R.string.token_error_message));
                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_page, menu);
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

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btnRegister:
            String email = etEmail.getText().toString();
            String name = etName.getText().toString();
            String DOB = etDOB.getText().toString();
            String gender = "";
            if (rbMale.isChecked()) {
                gender = rbMale.getText().toString();
            } else if (rbFemale.isChecked()) {
                gender = rbFemale.getText().toString();
            }
            Double height = Double.parseDouble(etHeight.getText().toString());
            Double weight = Double.parseDouble(etWeight.getText().toString());
            String password = etPassword.getText().toString();
            int age = 0;
            age = thisYear - birthYear;
            int reward = 0;
            Boolean emailTrue = isEmailValid(email);
            if (!emailTrue) {
                showErrorMessage("Email Address Not Correct.Please Check and Try Again");
            } else if (DOB == "") {
                showErrorMessage("Date of Birth Not Correct or Empty.Please Check and Try Again");
            } else if (ValidateUtil.isEmpty(name)) {
                showErrorMessage("Name Cant Leave it Empty.Please Check and Try Again");
            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_profile);
                Integer countID = serverRequests.returnCountID();
                UserProfile userProfile = new UserProfile(countID.toString(), result, email, password, name, new DateTime(DOB), gender, weight, height, reward, new DateTime(DOJ), new DateTime(DOJ), bitmap);
                registerUser(userProfile);
            }
            break;
        case R.id.etDOB:
            Calendar myCalendar = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            DOJ = df.format(myCalendar.getTime());
            System.out.println(DOJ);
            thisYear = myCalendar.get(Calendar.YEAR);
            System.out.println(thisYear);
            new SlideDateTimePicker.Builder(getSupportFragmentManager()).setListener(listener).setInitialDate(new Date())
            //.setMinDate(minDate)
                    //.setMaxDate(maxDate)
                    //.setIs24HourTime(true)
                    .setTheme(SlideDateTimePicker.HOLO_DARK)
                    //.setIndicatorColor(Color.parseColor("#990000"))
                    .build().show();
            break;
        }
    }

    private void registerUser(UserProfile userProfile) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(userProfile, new GetUserCallBack() {
            @Override
            public void done(UserProfile returnUserProfile) {
                startActivity(new Intent(SignUpPage.this, LoginPage.class));
                finish();
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            result = intent.getStringExtra(RegistrationIntentService.EXTRA_KEY_IN);
        }
    };

    private void showErrorMessage(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpPage.this);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


}