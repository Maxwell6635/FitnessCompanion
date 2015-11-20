package my.com.taruc.fitnesscompanion.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.ProfilePictureView;

import my.com.taruc.fitnesscompanion.Database.UserProfileDA;
import my.com.taruc.fitnesscompanion.PHP.JSONParser;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.ServerRequests;
import my.com.taruc.fitnesscompanion.UserLocalStore;
import my.com.taruc.fitnesscompanion.UserProfile;


public class UserProfilePage extends Fragment implements View.OnClickListener{
    UserLocalStore userLocalStore;
    ProfilePictureView profilePictureView;
    EditText editTextName,editTextDOB,editTextGender,editTextAge,editTextHeight;
    ImageView editIcon , saveProfile;
    Integer id;
    UserProfile profile;
    UserProfile loadUserProfile;
    UserProfile storeNewUserProfile;
    UserProfileDA userProfileDA;
    ServerRequests serverRequests;
    int status;
    JSONParser jParser = new JSONParser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_user_profile_page, container, false);
        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(getActivity().getApplicationContext());
        serverRequests = new ServerRequests(getActivity().getApplicationContext());
        userProfileDA = new UserProfileDA(getActivity().getApplicationContext());
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // initialise your views
        profilePictureView = (ProfilePictureView) view.findViewById(R.id.imageView2);
        editTextName = (EditText) view.findViewById(R.id.editTextName);
        editTextDOB = (EditText) view.findViewById(R.id.editTextDOB);
        editTextGender = (EditText) view.findViewById(R.id.editTextGender);
        editTextHeight = (EditText) view.findViewById(R.id.editTextHeight);
        editTextAge = (EditText) view.findViewById(R.id.editTextAge);
        editIcon = (ImageView) view.findViewById(R.id.editIcon);
        saveProfile = (ImageView) view.findViewById(R.id.saveProfile);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        profile = authenticate();
        id = userLocalStore.returnUserID();
        loadUserProfile = userProfileDA.getUserProfile2();
        Toast.makeText(this.getActivity(),id+"",Toast.LENGTH_SHORT).show();
        profilePictureView.setProfileId(profile.getId());
        editTextName.setText(loadUserProfile.getName());
        editTextDOB.setText(loadUserProfile.getDOB());
        editTextGender.setText(loadUserProfile.getGender());
        editTextAge.setText(Integer.toString(loadUserProfile.getAge()));
        editTextHeight.setText(Double.toString(loadUserProfile.getHeight()));
        //Focusable
        editTextName.setFocusable(false);
        editTextDOB.setFocusable(false);
        editTextGender.setFocusable(false);
        editTextHeight.setFocusable(false);
        editTextAge.setFocusable(false);
        //Enabled
        editTextName.setEnabled(false);
        editTextDOB.setEnabled(false);
        editTextGender.setEnabled(false);
        editTextHeight.setEnabled(false);
        editTextAge.setEnabled(false);
        saveProfile.setEnabled(false);

        editIcon.setOnClickListener(this);
        saveProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editIcon:
                editTextName.setEnabled(true);
                editTextDOB.setEnabled(true);
                editTextGender.setEnabled(true);
                editTextHeight.setEnabled(true);
                saveProfile.setEnabled(true);
                editTextName.setFocusableInTouchMode(true);
                editTextDOB.setFocusableInTouchMode(true);
                editTextGender.setFocusableInTouchMode(true);
                editTextHeight.setFocusableInTouchMode(true);
                editTextName.requestFocus();
                editTextName.setSelection(editTextName.getText().length());
                break;
            case R.id.saveProfile:
                String name = editTextName.getText().toString();
                Double height = Double.parseDouble(editTextHeight.getText().toString());
                editTextName.setText(name);
                editTextDOB.setText(editTextDOB.getText().toString());
                editTextGender.setText(editTextGender.getText().toString());
                editTextHeight.setText(height.toString());
                storeNewUserProfile = new UserProfile(loadUserProfile.getId(),loadUserProfile.getEmail(),name,loadUserProfile.getDOB(),loadUserProfile.getAge(),loadUserProfile.getGender(),height,0.0,"",loadUserProfile.getDOJ(),0);
                boolean success = userProfileDA.updateUserProfile(storeNewUserProfile);
                if(success){
                        Toast.makeText(getActivity().getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                }
                editTextName.setEnabled(false);
                editTextDOB.setEnabled(false);
                editTextGender.setEnabled(false);
                editTextHeight.setEnabled(false);
                editTextAge.setEnabled(false);
                saveProfile.setEnabled(false);
                editTextName.setFocusable(false);
                editTextDOB.setFocusable(false);
                editTextHeight.setFocusable(false);
                editTextGender.setFocusable(false);
                editTextAge.setFocusable(false);
                break;
        }
    }

    private UserProfile authenticate() {
        UserProfile userProfile = new UserProfile();
        if (userLocalStore.getFacebookLoggedInUser() == null) {
            if (userLocalStore.getLoggedInUser() != null) {
                 userProfile = userLocalStore.getLoggedInUser();
            }else{

            }
        }else{
            userProfile = userLocalStore.getFacebookLoggedInUser();
        }
        return userProfile;
    }

}
