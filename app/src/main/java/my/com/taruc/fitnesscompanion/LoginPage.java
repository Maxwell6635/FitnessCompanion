package my.com.taruc.fitnesscompanion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.Database.HealthProfileDA;
import my.com.taruc.fitnesscompanion.Database.UserProfileDA;
import my.com.taruc.fitnesscompanion.ServerAPI.GetUserCallBack;
import my.com.taruc.fitnesscompanion.ServerAPI.ServerRequests;
import my.com.taruc.fitnesscompanion.UI.MainMenu;

public class LoginPage extends ActionBarActivity implements View.OnClickListener {

    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.btnSignUp)
    Button btnSignUp;
    @Bind(R.id.login_button)
    LoginButton loginButton;
    @Bind(R.id.textViewForgetPassword)
    TextView tvForgetPassword;

    UserLocalStore userLocalStore;
    CallbackManager callbackManager;
    AccessToken accessToken;

    private String id, email, name, gender, birthday, DOJ;
    private Profile profile;
    private int age = 0;
    private UserProfileDA userProfileDA;

    // Connection detector class
    private ConnectionDetector cd;
    // flag for Internet connection status
    private Boolean isInternetPresent = false;
    // Alert Dialog Manager
    ShowAlert alert = new ShowAlert();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login_page);
        ButterKnife.bind(this);
        userProfileDA = new UserProfileDA(this.getApplicationContext());
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        loginButton.setEnabled(false);
        loginButton.setReadPermissions("public_profile email user_birthday user_friends ");
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, mCallBack);
        cd = new ConnectionDetector(getApplicationContext());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_page, menu);
        return true;
    }

    @Override
    //FACEBOOK
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        userLocalStore.setUserLoggedIn(true);
        userLocalStore.setFirstTime(true);
        Intent returnIntent = new Intent();
        setResult(1, returnIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btnLogin:
            isInternetPresent = cd.isConnectingToInternet();
            if (!isInternetPresent) {
                // Internet Connection is not present
                alert.showAlertDialog(this, "Fail", "Internet Connection is Not Available", false);
            } else {
                if (etEmail.getText().toString().isEmpty()){
                    alert.showAlertDialog(this, "Fail", "Email Address Input Field Can't Be Blank", false);
                } else if (etPassword.getText().toString().isEmpty()) {
                    alert.showAlertDialog(this, "Fail", "Password Input Field Can't Be Blank", false);
                } else {
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    UserProfile userProfile = new UserProfile(email, password);
                    authenticate(userProfile);
                }
            }
            break;
        case R.id.btnSignUp:
            startActivity(new Intent(this, SignUpPage.class));
            break;
        case R.id.textViewForgetPassword:
            startActivity(new Intent(this, ForgetPassword.class));
            break;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("No", null).show();
    }

    private void authenticate(UserProfile user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.fetchUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(UserProfile returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginPage.this);
        dialogBuilder.setMessage("Incorrect User Details, Please Try Again");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();

    }

    //Normal Login
    private void logUserIn(UserProfile returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        userLocalStore.setNormalUser(true);
        UserProfile checkProfile = userProfileDA.getUserProfile(returnedUser.getUserID());
        if (checkProfile.getName() == null) {
            userLocalStore.setFirstTime(true);
            userLocalStore.setUserID(Integer.parseInt(returnedUser.getUserID()));
        } else {
            userLocalStore.setFirstTime(false);
            userLocalStore.setUserID(Integer.parseInt(returnedUser.getUserID()));
        }
        Intent intent = new Intent(LoginPage.this, MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            accessToken = loginResult.getAccessToken();
            profile = Profile.getCurrentProfile();
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    // Application code
                    try {
                        id = object.getString("id");
                        name = object.getString("name");
                        email = object.getString("email");
                        gender = object.getString("gender");
                        birthday = object.getString("birthday");
                        //do something with the data here
                        Calendar myCalendar = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        DOJ = df.format(myCalendar.getTime());
                        int year = myCalendar.get(Calendar.YEAR);
                        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
                        SimpleDateFormat format2 = new SimpleDateFormat("yyyy");
                        try {
                            Date date = format1.parse(birthday);
                            System.out.println(date);
                            String year2 = format2.format(date);
                            int birthyear = Integer.parseInt(year2);
                            //System.out.println(birthyear);
                            age = year - birthyear;
                            System.out.println(age);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        userLocalStore.storeFacebookUserData(id, email, name, gender, birthday, DOJ, age, 0.0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Log.v("LoginActivity", "cancel");
        }

        @Override
        public void onError(FacebookException e) {
            Log.v("LoginActivity", e.getCause().toString());
        }
    };


}



