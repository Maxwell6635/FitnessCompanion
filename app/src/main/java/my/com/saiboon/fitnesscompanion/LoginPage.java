package my.com.saiboon.fitnesscompanion;

import android.app.AlertDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import my.com.saiboon.fitnesscompanion.UI.MainMenu;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.w3c.dom.Text;


public class LoginPage extends ActionBarActivity implements View.OnClickListener {

    Button btnLogin, btnSignUp;
    LoginButton loginButton;
    EditText etEmail, etPassword;
    TextView tvForgetPassword;
    UserLocalStore userLocalStore;
    CallbackManager callbackManager;
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                authenticateFacebook(profile);
            }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        tvForgetPassword = (TextView) findViewById(R.id.textViewForgetPassword);
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, mCallBack);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_page, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                System.out.println(email + password);
                UserProfile userProfile = new UserProfile(email, password);
                authenticate(userProfile);
                break;
            case R.id.btnSignUp:
                startActivity(new Intent(this, SignUpPage.class));
                break;
            case R.id.textViewForgetPassword:
                //startActivity(new Intent(this, ForgetPassword.class));
                break;
        }
    }

    private void authenticate(UserProfile user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.fetchUserDataInBackground(user, new GetUserCallback() {
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

    private void authenticateFacebook(Profile profile) {
        if (profile == null) {
            showErrorMessage();
        } else {
            userLocalStore.storeFacebookUserData(profile.getId(), profile.getName());
            userLocalStore.setUserLoggedIn(true);
            startActivity(new Intent(this, MainMenu.class));
        }
    }


    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginPage.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();

    }

    private void logUserIn(UserProfile returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(this, MainMenu.class));
    }
}
