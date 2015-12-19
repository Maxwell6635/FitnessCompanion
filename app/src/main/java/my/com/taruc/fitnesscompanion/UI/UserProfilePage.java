package my.com.taruc.fitnesscompanion.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.ProfilePictureView;

import java.io.File;

import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.Database.UserProfileDA;
import my.com.taruc.fitnesscompanion.PHP.JSONParser;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.ServerRequests;
import my.com.taruc.fitnesscompanion.UserLocalStore;


public class UserProfilePage extends Fragment implements View.OnClickListener{
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_OK = -1;
    UserLocalStore userLocalStore;
    ProfilePictureView profilePictureView;
    EditText editTextName,editTextDOB,editTextGender,editTextAge,editTextHeight;
    ImageView editIcon , saveProfile, profileImage;
    Integer id;
    UserProfile profile;
    UserProfile loadUserProfile;
    UserProfile storeNewUserProfile;
    UserProfileDA userProfileDA;
    ServerRequests serverRequests;
    Button buttonLoadImage;
    int status;
    ProgressDialog progress;


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
        profileImage = (ImageView) view.findViewById(R.id.imageView2);
        editTextName = (EditText) view.findViewById(R.id.editTextName);
        editTextDOB = (EditText) view.findViewById(R.id.editTextDOB);
        editTextGender = (EditText) view.findViewById(R.id.editTextGender);
        editTextHeight = (EditText) view.findViewById(R.id.editTextHeight);
        editTextAge = (EditText) view.findViewById(R.id.editTextAge);
        editIcon = (ImageView) view.findViewById(R.id.editIcon);
        saveProfile = (ImageView) view.findViewById(R.id.saveProfile);
        buttonLoadImage = (Button)view.findViewById(R.id.buttonLoadPicture);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        profile = authenticate();
        id = userLocalStore.returnUserID();
        loadUserProfile = userProfileDA.getUserProfile2();

        editTextName.setText(loadUserProfile.getName());
        editTextDOB.setText(loadUserProfile.getDOB().getDate().getFullDate());
        editTextGender.setText(loadUserProfile.getGender());
        editTextAge.setText(Integer.toString(loadUserProfile.calAge()));
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
        buttonLoadImage.setOnClickListener(this);

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
                progress = ProgressDialog.show(getActivity(), "Save User Profile",
                        "Savings....Please Wait", true);
                String name = editTextName.getText().toString();
                Double height = Double.parseDouble(editTextHeight.getText().toString());
                editTextName.setText(name);
                editTextDOB.setText(editTextDOB.getText().toString());
                editTextGender.setText(editTextGender.getText().toString());
                editTextHeight.setText(height.toString());
                storeNewUserProfile = new UserProfile(loadUserProfile.getUserID(),loadUserProfile.getEmail(), null, name, loadUserProfile.getDOB(),loadUserProfile.getGender(),0.0, height,0,loadUserProfile.getCreated_At(),null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                            boolean success = userProfileDA.updateUserProfile(storeNewUserProfile);
                            if(success){
                                Toast.makeText(getActivity().getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                        }

                        progress.dismiss();
                    }

                }).start();

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
            case R.id.buttonLoadPicture:
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            int rotateImage = getCameraPhotoOrientation(getActivity(), selectedImage, picturePath);

            profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            buttonLoadImage.setVisibility(View.INVISIBLE);

        }
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
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
