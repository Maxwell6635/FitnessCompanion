package my.com.taruc.fitnesscompanion;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;


public class SignUpPage extends FragmentActivity implements View.OnClickListener {
    Button btnRegister;
    RadioButton rbMale, rbFemale;
    EditText etEmail, etName, etDOB, etWeight, etHeight, etPassword;
    String DOJ;
    int thisYear, birthYear;
    ServerRequests serverRequests;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("dd-MM-yyyy");
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
        etEmail = (EditText) findViewById(R.id.etEmail);
        etName = (EditText) findViewById(R.id.etName);
        etDOB = (EditText) findViewById(R.id.etDOB);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        rbMale = (RadioButton) findViewById(R.id.radioButtonMale);
        rbFemale = (RadioButton) findViewById(R.id.radioButtonFemale);
        btnRegister.setOnClickListener(this);
        etDOB.setOnClickListener(this);
        serverRequests = new ServerRequests(this);
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
                    showErrorMessage("Email Address Not Correct.Please Check");
                } else if (DOB == "") {
                    showErrorMessage("Date of Birth Not Correct or Empty.Please Check");
                } else {
                    Integer countID =  serverRequests.returnCountID();
                    //UserProfile_X userProfile = new UserProfile_X(countID.toString(),email, name, DOB, age, gender, height, weight, password, DOJ, reward);
                    UserProfile userProfile = new UserProfile(countID.toString(),email, password, name, new DateTime(DOB), gender, weight, height, reward, new DateTime(DOJ), null);
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
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                                //.setMinDate(minDate)
                                //.setMaxDate(maxDate)
                                //.setIs24HourTime(true)
                        .setTheme(SlideDateTimePicker.HOLO_DARK)
                                //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
                break;
        }
    }

    private void registerUser(UserProfile userProfile) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(userProfile, new GetUserCallback() {
            @Override
            public void done(UserProfile returnUserProfile) {
                startActivity(new Intent(SignUpPage.this, LoginPage.class));
                finish();
            }
        });
    }

    private void showErrorMessage(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpPage.this);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }


}