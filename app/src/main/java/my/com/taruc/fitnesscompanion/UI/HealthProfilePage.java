package my.com.taruc.fitnesscompanion.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.squareup.leakcanary.RefWatcher;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.HealthProfile;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.ConnectionDetector;
import my.com.taruc.fitnesscompanion.Database.HealthProfileDA;
import my.com.taruc.fitnesscompanion.Database.UserProfileDA;
import my.com.taruc.fitnesscompanion.FitnessApplication;
import my.com.taruc.fitnesscompanion.LoginPage;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.ServerAPI.ServerRequests;
import my.com.taruc.fitnesscompanion.ShowAlert;
import my.com.taruc.fitnesscompanion.UserLocalStore;
import my.com.taruc.fitnesscompanion.Util.AlertDialogUtil;

public class HealthProfilePage extends Fragment implements View.OnClickListener {

    private ViewGroup rootView;
    private UserLocalStore userLocalStore;
    private UserProfile userProfile;
    private UserProfile loadUserProfile;
    private HealthProfile loadhealthProfile;
    private HealthProfile healthProfile;
    private UserProfileDA userProfileDA;
    private HealthProfileDA healthProfileDA;
    private Double height, weight, ArmGirth, ChestGirth, CalfGirth, ThighGirth, Waist, HIP;
    private Integer BP, RHR;
    private Double BMI, BMR, Waist_Hip;
    private ServerRequests serverRequests;
    private String temp, temp2;
    boolean success = false;
    private ProgressDialog progress;
    private AlertDialogUtil alertDialogUtil;
    // Connection detector class
    private ConnectionDetector cd;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    ShowAlert alert = new ShowAlert();

    @Bind(R.id.editHealthProfile)
    ImageView editHealthProfile;
    @Bind(R.id.saveHealthProfile)
    ImageView saveHealthProfile;
    @Bind(R.id.editTextWeight)
    EditText editTextWeight;
    @Bind(R.id.editTextBloodP)
    EditText editTextBP;
    @Bind(R.id.editTextRHR)
    EditText editTextRHR;
    @Bind(R.id.editBodyGirth)
    ImageView editBodyGirth;
    @Bind(R.id.saveBodyGirth)
    ImageView saveBodyGirth;
    @Bind(R.id.editTextArm)
    EditText editTextArm;
    @Bind(R.id.editTextChest)
    EditText editTextChest;
    @Bind(R.id.editTextCalf)
    EditText editTextCalf;
    @Bind(R.id.editTextThigh)
    EditText editTextThigh;
    @Bind(R.id.editTextWaist)
    EditText editTextWaist;
    @Bind(R.id.editTextHip)
    EditText editTextHIP;
    @Bind(R.id.textViewBMR)
    TextView textViewBMR;
    @Bind(R.id.textViewBMI)
    TextView textViewBMI;
    @Bind(R.id.textViewWHR)
    TextView textViewWHR;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_health_profile_page, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, rootView);
        //set R.id
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(getActivity().getApplicationContext());
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        userProfileDA = new UserProfileDA(getActivity().getApplicationContext());
        healthProfileDA = new HealthProfileDA(getActivity().getApplicationContext());
        serverRequests = new ServerRequests(getActivity().getApplicationContext());
        alertDialogUtil = new AlertDialogUtil(getContext());
        //Set Database connection
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadUserProfile = userProfileDA.getUserProfile(userLocalStore.returnUserID().toString());
        loadhealthProfile = healthProfileDA.getLastHealthProfile();
        //Focusable
        editTextWeight.setFocusable(false);
        editTextBP.setFocusable(false);
        editTextRHR.setFocusable(false);
        editTextArm.setFocusable(false);
        editTextChest.setFocusable(false);
        editTextCalf.setFocusable(false);
        editTextThigh.setFocusable(false);
        editTextWaist.setFocusable(false);
        editTextHIP.setFocusable(false);
        //Enabled
        editTextWeight.setEnabled(false);
        editTextBP.setEnabled(false);
        editTextRHR.setEnabled(false);
        editTextArm.setEnabled(false);
        editTextChest.setEnabled(false);
        editTextCalf.setEnabled(false);
        editTextThigh.setEnabled(false);
        editTextWaist.setEnabled(false);
        editTextHIP.setEnabled(false);
        saveHealthProfile.setEnabled(false);
        saveBodyGirth.setEnabled(false);
        editTextWeight.setText(Double.toString(loadhealthProfile.getWeight()));
        editTextBP.setText(Integer.toString(loadhealthProfile.getBloodPressure()));
        editTextRHR.setText(Integer.toString(loadhealthProfile.getRestingHeartRate()));
        editTextArm.setText(Double.toString(loadhealthProfile.getArmGirth()));
        editTextChest.setText(Double.toString(loadhealthProfile.getChestGirth()));
        editTextCalf.setText(Double.toString(loadhealthProfile.getCalfGirth()));
        editTextThigh.setText(Double.toString(loadhealthProfile.getThighGirth()));
        editTextWaist.setText(Double.toString(loadhealthProfile.getWaist()));
        editTextHIP.setText(Double.toString(loadhealthProfile.getHIP()));

        height = loadUserProfile.getHeight();
        HIP = loadhealthProfile.getHIP();
        if (weight == null && height == null) {
            BMI = 0.0;
            textViewBMI.setText(Double.toString(BMI));
        } else {
            BMI = calculateBMI(loadhealthProfile.getWeight(), loadUserProfile.getHeight());
            temp = String.format("%.2f", BMI);
            textViewBMI.setText(temp);
        }
        if (weight == null && height == null) {
            BMR = 0.0;
            textViewBMR.setText(Double.toString(BMR));
        } else {
            BMR = calculateBMR(loadhealthProfile.getWeight(), loadUserProfile.getHeight(), loadUserProfile.getGender(), loadUserProfile.calAge());
            temp = String.format("%.2f", BMR);
            textViewBMR.setText(temp);
        }
        if (HIP == null) {
            Waist_Hip = 0.0;
            textViewWHR.setText(Waist_Hip.toString());
        } else {
            Waist_Hip = calculateWaist_HIP_Ratio(loadhealthProfile.getWaist(), loadhealthProfile.getHIP());
            temp = String.format("%.2f", Waist_Hip);
            textViewWHR.setText(temp);
        }
        editHealthProfile.setOnClickListener(this);
        saveHealthProfile.setOnClickListener(this);
        editBodyGirth.setOnClickListener(this);
        saveBodyGirth.setOnClickListener(this);
        //do whatever
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = FitnessApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editHealthProfile:
                editTextWeight.setEnabled(true);
                editTextBP.setEnabled(true);
                editTextRHR.setEnabled(true);
                editTextWeight.setFocusableInTouchMode(true);
                editTextBP.setFocusableInTouchMode(true);
                editTextRHR.setFocusableInTouchMode(true);
                saveHealthProfile.setEnabled(true);
                editTextWeight.requestFocus();
                editTextWeight.setSelection(editTextWeight.getText().length());
                break;
            case R.id.editBodyGirth:
                editTextArm.setFocusableInTouchMode(true);
                editTextChest.setFocusableInTouchMode(true);
                editTextCalf.setFocusableInTouchMode(true);
                editTextThigh.setFocusableInTouchMode(true);
                editTextWaist.setFocusableInTouchMode(true);
                editTextHIP.setFocusableInTouchMode(true);
                editTextArm.setEnabled(true);
                editTextChest.setEnabled(true);
                editTextCalf.setEnabled(true);
                editTextThigh.setEnabled(true);
                saveBodyGirth.setEnabled(true);
                editTextWaist.setEnabled(true);
                editTextHIP.setEnabled(true);
                editTextArm.requestFocus();
                editTextArm.setSelection(editTextArm.getText().length());
                break;
            case R.id.saveHealthProfile:
                isInternetPresent = cd.haveNetworkConnection();
                if (!isInternetPresent) {
                    // Internet Connection is not present
                    alert.showAlertDialog(getActivity().getApplicationContext(), "Fail", "Internet Connection is NOT Available", false);
                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginPage.class);
                    startActivityForResult(intent, 1);
                } else {
                    if (editTextWeight.getText().toString().isEmpty()) {
                        alertDialogUtil.showErrorMessageActivity("Weight Cant Be Empty!");
                    } else if (editTextBP.getText().toString().isEmpty()) {
                        alertDialogUtil.showErrorMessageActivity("Blood Pressure Cant Be Empty!");
                    } else if (editTextRHR.getText().toString().isEmpty()) {
                        alertDialogUtil.showErrorMessageActivity("Resting Heart Rate Cant Be Empty!");
                    } else {
                        progress = ProgressDialog.show(getActivity(), "Save Health Profile", "Savings....Please Wait", true);
                        weight = Double.parseDouble(editTextWeight.getText().toString());
                        BP = Integer.parseInt(editTextBP.getText().toString());
                        RHR = Integer.parseInt(editTextRHR.getText().toString());
                        BMI = calculateBMI(weight, height);
                        BMR = calculateBMR(weight, height, loadUserProfile.getGender(), loadUserProfile.calAge());
                        editTextWeight.setText(weight.toString());
                        editTextBP.setText(BP.toString());
                        editTextRHR.setText(RHR.toString());
                        temp = String.format("%.2f", BMI);
                        temp2 = String.format("%.2f", BMR);
                        textViewBMI.setText(temp);
                        textViewBMR.setText(temp2);
                        healthProfile = new HealthProfile(healthProfileDA.generateNewHealthProfileID(), loadhealthProfile.getUserID(), weight, BP, RHR, loadhealthProfile.getArmGirth(), loadhealthProfile.getChestGirth(), loadhealthProfile.getCalfGirth(), loadhealthProfile.getThighGirth(), loadhealthProfile.getWaist(), loadhealthProfile.getHIP(), new DateTime().getCurrentDateTime(), new DateTime().getCurrentDateTime());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                    success = healthProfileDA.addHealthProfile(healthProfile);
                                    if (success) {
                                        serverRequests.storeHealthProfileDataInBackground(healthProfile);
                                    }
                                } catch (Exception e) {
                                }

                                progress.dismiss();
                            }

                        }).start();
                    }
                }
                editTextWeight.setEnabled(false);
                editTextBP.setEnabled(false);
                editTextRHR.setEnabled(false);
                editTextWeight.setFocusable(false);
                editTextBP.setFocusable(false);
                editTextRHR.setFocusable(false);
                break;
            case R.id.saveBodyGirth:
                isInternetPresent = cd.haveNetworkConnection();
                if (!isInternetPresent) {
                    // Internet Connection is not present
                    alert.showAlertDialog(getActivity().getApplicationContext(), "Fail", "Internet Connection is NOT Available", false);
                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginPage.class);
                    startActivityForResult(intent, 1);
                } else {
                    if (editTextArm.getText().toString().isEmpty()) {
                        alertDialogUtil.showErrorMessageActivity("Arm Girth Cant Be Empty!");
                    } else if (editTextChest.getText().toString().isEmpty()) {
                        alertDialogUtil.showErrorMessageActivity("Chest Girth Cant Be Empty!");
                    } else if (editTextCalf.getText().toString().isEmpty()) {
                        alertDialogUtil.showErrorMessageActivity("Calf Girth Rate Cant Be Empty!");
                    } else if(editTextThigh.getText().toString().isEmpty()){
                        alertDialogUtil.showErrorMessageActivity("Thigh Girth Cant Be Empty!");
                    } else if (editTextWaist.getText().toString().isEmpty()) {
                        alertDialogUtil.showErrorMessageActivity("Waist Girth Rate Cant Be Empty!");
                    } else if(editTextHIP.getText().toString().isEmpty()){
                        alertDialogUtil.showErrorMessageActivity("HIP Cant Be Empty!");
                    } else {
                        progress = ProgressDialog.show(getActivity(), "Save Health Profile", "Savings....Please Wait", true);
                        ArmGirth = Double.parseDouble(editTextArm.getText().toString());
                        ChestGirth = Double.parseDouble(editTextChest.getText().toString());
                        CalfGirth = Double.parseDouble(editTextCalf.getText().toString());
                        ThighGirth = Double.parseDouble(editTextThigh.getText().toString());
                        Waist = Double.parseDouble(editTextWaist.getText().toString());
                        HIP = Double.parseDouble(editTextHIP.getText().toString());
                        Waist_Hip = calculateWaist_HIP_Ratio(Waist, HIP);
                        temp = String.format("%.2f", Waist_Hip);
                        textViewWHR.setText(temp);
                        editTextArm.setText(ArmGirth.toString());
                        editTextChest.setText(ChestGirth.toString());
                        editTextCalf.setText(CalfGirth.toString());
                        editTextThigh.setText(ThighGirth.toString());
                        editTextWaist.setText(Waist.toString());
                        editTextHIP.setText(HIP.toString());
                        healthProfile = new HealthProfile(healthProfileDA.generateNewHealthProfileID(), loadhealthProfile.getUserID(), loadhealthProfile.getWeight(), loadhealthProfile.getBloodPressure(), loadhealthProfile.getRestingHeartRate(), ArmGirth, ChestGirth, CalfGirth, ThighGirth, Waist, HIP, new DateTime().getCurrentDateTime(), new DateTime().getCurrentDateTime());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(10000);
                                    success = healthProfileDA.addHealthProfile(healthProfile);
                                    if (success) {
                                        serverRequests.storeHealthProfileDataInBackground(healthProfile);
                                    }
                                } catch (Exception e) {
                                }

                                progress.dismiss();
                            }

                        }).start();
                    }
                }
                editTextArm.setEnabled(false);
                editTextChest.setEnabled(false);
                editTextCalf.setEnabled(false);
                editTextThigh.setEnabled(false);
                editTextWaist.setEnabled(false);
                editTextHIP.setEnabled(false);
                editTextArm.setFocusable(false);
                editTextChest.setFocusable(false);
                editTextCalf.setFocusable(false);
                editTextThigh.setFocusable(false);
                editTextWaist.setFocusable(false);
                editTextHIP.setFocusable(false);
                break;
        }
    }

    private double calculateBMI(Double weight, Double height) {
        Double newHeight = 0.0;
        if (height > 100.00) {
            newHeight = height / 100.0;
        } else {
            newHeight = height;
        }
        BMI = (weight / (newHeight * newHeight));
        return BMI;
    }

    private double calculateBMR(Double weight, Double height, String gender, int age) {
        Double BMR = 0.0;
        if (gender == "female") {
            BMR = 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
        } else {
            BMR = 66 + (13.7 * weight) + (5 * height) - (6.8 * age);
        }
        return BMR;
    }

    private double calculateWaist_HIP_Ratio(Double waist, Double HIP) {
        Double Waist_HIP = 0.0;
        if (HIP > 0) {
            Waist_HIP = waist / HIP;
        } else {
            Waist_HIP = 0.0;
        }
        return Waist_HIP;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
