package my.com.taruc.fitnesscompanion.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.ConnectionDetector;
import my.com.taruc.fitnesscompanion.Database.UserProfileDA;
import my.com.taruc.fitnesscompanion.FitnessApplication;
import my.com.taruc.fitnesscompanion.LoginPage;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.ServerAPI.UpdateRequest;
import my.com.taruc.fitnesscompanion.ShowAlert;
import my.com.taruc.fitnesscompanion.UserLocalStore;
import my.com.taruc.fitnesscompanion.Util.AlertDialogUtil;

public class UserProfilePage extends Fragment implements View.OnClickListener {
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_OK = -1;
    private UserLocalStore userLocalStore;
    private Integer id;
    private UserProfile profile;
    private UserProfile loadUserProfile;
    private UserProfile storeNewUserProfile;
    private UserProfileDA userProfileDA;
    private UpdateRequest mUpdateRequest;

    private int status;
    private ProgressDialog progress;
    private Bitmap bitmap;
    private SlideDateTimeListener listener;

    @Bind(R.id.imageView2)
    ImageView profileImage;
    @Bind(R.id.buttonLoadPicture)
    Button buttonLoadPicture;
    @Bind(R.id.editIcon)
    ImageView editIcon;
    @Bind(R.id.saveProfile)
    ImageView saveProfile;
    @Bind(R.id.textViewName)
    TextView textViewName;
    @Bind(R.id.editTextName)
    EditText editTextName;
    @Bind(R.id.textViewDOB)
    TextView textViewDOB;
    @Bind(R.id.editTextDOB)
    EditText editTextDOB;
    @Bind(R.id.textViewGender)
    TextView textViewGender;
    @Bind(R.id.editTextGender)
    EditText editTextGender;
    @Bind(R.id.textViewHeight)
    TextView textViewHeight;
    @Bind(R.id.editTextHeight)
    EditText editTextHeight;
    @Bind(R.id.textViewAge)
    TextView textViewAge;
    @Bind(R.id.editTextAge)
    EditText editTextAge;

    // Connection detector class
    private ConnectionDetector cd;
    private AlertDialogUtil alertDialogUtil;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    ShowAlert alert = new ShowAlert();
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyy-MM-dd");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_user_profile_page, container, false);
        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(getActivity().getApplicationContext());
        userProfileDA = new UserProfileDA(getActivity().getApplicationContext());
        mUpdateRequest = new UpdateRequest(getActivity().getBaseContext());
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        alertDialogUtil = new AlertDialogUtil(getContext());
        listener = new SlideDateTimeListener() {
            @Override
            public void onDateTimeSet(Date date) {
                editTextDOB.setText(mFormatter.format(date));
            }

            // Optional cancel listener
            @Override
            public void onDateTimeCancel() {
//            Toast.makeText(SignUpPage.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        // initialise your views
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        profile = authenticate();
        id = userLocalStore.returnUserID();
        loadUserProfile = userProfileDA.getUserProfile2();
        profileImage.setImageBitmap(loadUserProfile.getBitmap());
        editTextName.setText(loadUserProfile.getName());
        editTextDOB.setText(loadUserProfile.getDOB().getDate().getFullDateString());
        editTextGender.setText(loadUserProfile.getGender());
        editTextAge.setText(Integer.toString(loadUserProfile.calAge()));
        editTextHeight.setText(Double.toString(loadUserProfile.getHeight()));

        //Focusable
        editTextName.setFocusable(false);
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
        editTextGender.setOnClickListener(this);
        editTextDOB.setOnClickListener(this);
        saveProfile.setOnClickListener(this);
        buttonLoadPicture.setOnClickListener(this);
        buttonLoadPicture.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = FitnessApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editIcon:
                editTextName.setEnabled(true);
                editTextDOB.setEnabled(true);
                editTextGender.setEnabled(true);
                editTextHeight.setEnabled(true);
                saveProfile.setEnabled(true);
                editTextName.setFocusableInTouchMode(true);
                editTextHeight.setFocusableInTouchMode(true);
                editTextName.requestFocus();
                editTextName.setSelection(editTextName.getText().length());
                buttonLoadPicture.setVisibility(View.VISIBLE);
                break;
            case R.id.saveProfile:
                isInternetPresent = cd.haveNetworkConnection();
                if (!isInternetPresent) {
                    // Internet Connection is not present
                    alert.showAlertDialog(getActivity().getApplicationContext(), "Fail", "Internet Connection is NOT Available", false);
                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginPage.class);
                    startActivityForResult(intent, 1);
                } else {
                    if (editTextName.getText().toString().isEmpty()) {
                        alertDialogUtil.showErrorMessageActivity("Name Cant Be Empty!");
                    } else if (editTextHeight.getText().toString().isEmpty()) {
                        alertDialogUtil.showErrorMessageActivity("Height Cant Be Empty!");
                    } else if (editTextDOB.getText().toString().isEmpty()) {
                        alertDialogUtil.showErrorMessageActivity("DOB Cant Be Empty!");
                    } else if (editTextGender.getText().toString().isEmpty()) {
                        alertDialogUtil.showErrorMessageActivity("Gender Cant Be Empty!");
                    } else {
                        progress = ProgressDialog.show(getActivity(), "Save User Profile", "Savings....Please Wait", true);
                        String name = editTextName.getText().toString();
                        Double height = Double.parseDouble(editTextHeight.getText().toString());
                        String DOB = editTextDOB.getText().toString();
                        String gender = editTextGender.getText().toString();
                        editTextName.setText(name);
                        editTextDOB.setText(editTextDOB.getText().toString());
                        editTextGender.setText(editTextGender.getText().toString());
                        editTextHeight.setText(height.toString());
                        bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
                        storeNewUserProfile = new UserProfile(loadUserProfile.getUserID(), loadUserProfile.getmGCMID(), loadUserProfile.getEmail(), loadUserProfile.getPassword(), name, new DateTime(DOB), gender, loadUserProfile.getInitial_Weight(), height, loadUserProfile.getReward_Point(), loadUserProfile.getCreated_At(), new DateTime().getCurrentDateTime(), bitmap);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                    boolean success = userProfileDA.updateUserProfile(storeNewUserProfile);
                                    if (success) {
                                        mUpdateRequest.updateUserProfileDataInBackground(storeNewUserProfile);
                                        editTextAge.setText(Integer.toString(storeNewUserProfile.calAge()));
                                    }
                                } catch (Exception e) {

                                }
                                progress.dismiss();
                            }

                        }).start();
                    }
                    editTextName.setEnabled(false);
                    editTextDOB.setEnabled(false);
                    editTextGender.setEnabled(false);
                    editTextHeight.setEnabled(false);
                    saveProfile.setEnabled(false);
                    editTextName.setFocusable(false);
                    editTextDOB.setFocusable(false);
                    editTextHeight.setFocusable(false);
                    editTextGender.setFocusable(false);
                    buttonLoadPicture.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.buttonLoadPicture:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;
            case R.id.editTextDOB:
                new SlideDateTimePicker.Builder(this.getFragmentManager()).setListener(listener).setInitialDate(new Date())
                        //.setMinDate(minDate)
                        .setMaxDate(new Date())
                                //.setIs24HourTime(true)
                        .setTheme(SlideDateTimePicker.HOLO_DARK)
                                //.setIndicatorColor(Color.parseColor("#990000"))
                        .build().show();
                break;
            case R.id.editTextGender:
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                AlertDialog.Builder adb2 = new AlertDialog.Builder(getActivity());
                adb2.setTitle("Choose Gender");
                CharSequence items2[] = new CharSequence[] {"Male", "Female"};
                adb2.setSingleChoiceItems(items2, 0, null);
                adb2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        String gender;
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        if(selectedPosition==0){
                            gender = "Male";
                        }else {
                            gender = "Female";
                        }
                        editTextGender.setText(gender);
                    }
                }).show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, bounds);

            BitmapFactory.Options opts = new BitmapFactory.Options();
            Bitmap bm = BitmapFactory.decodeFile(picturePath, opts);

            int rotateImage = getCameraPhotoOrientation(getActivity(), selectedImage, picturePath);

            Matrix matrix = new Matrix();
            matrix.postRotate(rotateImage, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);

            profileImage.setImageBitmap(getResizedBitmap(rotatedBitmap, 400));
            buttonLoadPicture.setVisibility(View.INVISIBLE);
        }
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
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
                default:
                    rotate = 0;
            }

            Log.d("RotateImage", "Exif orientation: " + orientation);
            Log.d("RotateImage", "Rotate value: " + rotate);
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
            } else {

            }
        } else {
            userProfile = userLocalStore.getFacebookLoggedInUser();
        }
        return userProfile;
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
