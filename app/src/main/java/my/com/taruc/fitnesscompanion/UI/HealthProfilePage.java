package my.com.taruc.fitnesscompanion.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import my.com.taruc.fitnesscompanion.Classes.HealthProfile;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.Database.HealthProfileDA;
import my.com.taruc.fitnesscompanion.Database.UserProfileDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.ServerRequests;
import my.com.taruc.fitnesscompanion.UserLocalStore;


public class HealthProfilePage extends Fragment implements View.OnClickListener {

    EditText editTextWeight, editTextBP, editTextRHR, editTextArm, editTextCalf, editTextChest, editTextThigh, editTextWaist, editTextHIP;
    TextView textViewBMI, textViewBMR, textViewWHR;
    ImageView editHealthProfile, saveHealthProfile, editBodyGirth, saveBodyGirth;
    ViewGroup rootView;
    UserLocalStore userLocalStore;
    UserProfile userProfile;
    UserProfile loadUserProfile;
    HealthProfile loadhealthProfile;
    HealthProfile healthProfile;
    UserProfileDA userProfileDA;
    HealthProfileDA healthProfileDA;
    Double height, weight, ArmGirth, ChestGirth, CalfGirth, ThighGirth, Waist, HIP;
    Integer BP, RHR;
    Double BMI, BMR, Waist_Hip;
    Integer healthID;
    ServerRequests serverRequests;
    String temp;
    boolean success = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_health_profile_page, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextWeight = (EditText) view.findViewById(R.id.editTextWeight);
        editTextBP = (EditText) view.findViewById(R.id.editTextBloodP);
        editTextRHR = (EditText) view.findViewById(R.id.editTextRHR);
        editTextArm = (EditText) view.findViewById(R.id.editTextArm);
        editTextChest = (EditText) view.findViewById(R.id.editTextChest);
        editTextCalf = (EditText) view.findViewById(R.id.editTextCalf);
        editTextThigh = (EditText) view.findViewById(R.id.editTextThigh);
        editTextWaist = (EditText) view.findViewById(R.id.editTextWaist);
        editTextHIP = (EditText) view.findViewById(R.id.editTextHip);
        editHealthProfile = (ImageView) view.findViewById(R.id.editHealthProfile);
        saveHealthProfile = (ImageView) view.findViewById(R.id.saveHealthProfile);
        editBodyGirth = (ImageView) view.findViewById(R.id.editBodyGirth);
        saveBodyGirth = (ImageView) view.findViewById(R.id.saveBodyGirth);
        textViewBMI = (TextView) view.findViewById(R.id.textViewBMI);
        textViewBMR = (TextView) view.findViewById(R.id.textViewBMR);
        textViewWHR = (TextView) view.findViewById(R.id.textViewWHR);
        //set R.id
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(getActivity().getApplicationContext());
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        userProfileDA = new UserProfileDA(getActivity().getApplicationContext());
        healthProfileDA = new HealthProfileDA(getActivity().getApplicationContext());
        serverRequests = new ServerRequests(getActivity().getApplicationContext());
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
                weight = Double.parseDouble(editTextWeight.getText().toString());
                BP = Integer.parseInt(editTextBP.getText().toString());
                RHR = Integer.parseInt(editTextRHR.getText().toString());
                BMI = calculateBMI(weight, height);
                BMR = calculateBMR(weight,height,loadUserProfile.getGender(),loadUserProfile.calAge());
                editTextWeight.setText(weight.toString());
                editTextBP.setText(BP.toString());
                editTextRHR.setText(RHR.toString());
                temp = String.format("%.2f",BMI);
                String temp2 = String.format("%.2f", BMR);
                textViewBMI.setText(temp);
                textViewBMR.setText(temp2);
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                healthProfile = new HealthProfile(loadhealthProfile.getHealthProfileID(), loadhealthProfile.getUserID(), weight, BP, RHR, loadhealthProfile.getArmGirth(), loadhealthProfile.getChestGirth(), loadhealthProfile.getCalfGirth(), loadhealthProfile.getThighGirth(), loadhealthProfile.getWaist(), loadhealthProfile.getHIP(), formattedDate);
                success = healthProfileDA.updateHealthProfile(healthProfile);
                if (success) {
                    serverRequests.updateHealthProfileDataInBackground(healthProfile);
                }
                editTextWeight.setEnabled(false);
                editTextBP.setEnabled(false);
                editTextRHR.setEnabled(false);
                editTextWeight.setFocusable(false);
                editTextBP.setFocusable(false);
                editTextRHR.setFocusable(false);
                break;
            case R.id.saveBodyGirth:
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
                Calendar c2 = Calendar.getInstance();
                System.out.println("Current time => " + c2.getTime());
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate2 = df2.format(c2.getTime());
                healthProfile = new HealthProfile(loadhealthProfile.getHealthProfileID(), loadhealthProfile.getUserID(), loadhealthProfile.getWeight(), loadhealthProfile.getBloodPressure(), loadhealthProfile.getRestingHeartRate(), ArmGirth, ChestGirth, CalfGirth, ThighGirth, Waist, HIP, formattedDate2);
                success = healthProfileDA.updateHealthProfile(healthProfile);
                if (success) {
                    serverRequests.updateHealthProfileDataInBackground(healthProfile);
                }
                editTextArm.setEnabled(false);
                editTextChest.setEnabled(false);
                editTextCalf.setEnabled(false);
                editTextThigh.setEnabled(false);
                editTextWaist.setEnabled(false);
                editTextHIP.setEnabled(false);
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


}
