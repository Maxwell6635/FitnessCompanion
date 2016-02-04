package my.com.taruc.fitnesscompanion.UI;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.Reminder;
import my.com.taruc.fitnesscompanion.Database.ActivityPlanDA;
import my.com.taruc.fitnesscompanion.Database.ReminderDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.Reminder.AdapterScheduleNew;
import my.com.taruc.fitnesscompanion.Reminder.AdapterScheduleNewListValue;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.AlarmServiceController;
import my.com.taruc.fitnesscompanion.ServerAPI.InsertRequest;
import my.com.taruc.fitnesscompanion.UserLocalStore;

public class ScheduleNewPage extends ActionBarActivity {

    private ListView list;
    private AdapterScheduleNew adapter;
    public ArrayList<AdapterScheduleNewListValue> CustomListViewValuesArr = new ArrayList<AdapterScheduleNewListValue>();
    AlarmServiceController alarmServiceController;

    private ActivityPlanDA myActivityPlanDA;
    private ArrayList<ActivityPlan> activityPlanArrayList;
    String[] activityList;
    String[] repeatList = new String[]{"Never", "Daily", "Weekly"};
    String[] dayList = new String[]{"Sunday", "Monday", "Tuesday","Wednesday","Thursday","Friday","Saturday"};

    public String activityTitle = "Activity";
    public String activityChoice;
    public String repeatTitle = "Repeat";
    public String repeatChoice;
    public String dayTitle = "Day";
    public String dayChoice;
    public String timeTitle = "Time";
    public String timeChoice;

    private static TimePicker timePicker;
    private RadioGroup myRg;

    //alarm purpose
    private PendingIntent pendingIntent;
    private InsertRequest insertRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_new_page);
        alarmServiceController = new AlarmServiceController(this);
        // get activity plan
        myActivityPlanDA = new ActivityPlanDA(this);
        insertRequest = new InsertRequest(this);
        activityPlanArrayList = myActivityPlanDA.getAllActivityPlan();
        activityList = new String[activityPlanArrayList.size()];
        for(int i=0; i<activityPlanArrayList.size(); i++){
            activityList[i] = activityPlanArrayList.get(i).getActivityName();
        }
        activityChoice = activityList[0];
        repeatChoice = repeatList[0];
        dayChoice = dayList[0];
        //get current time
        DateTime dateTime = new DateTime();
        dateTime.setDateTime(dateTime.getCurrentDateTime().getDateTimeString());
        if (dateTime.getTime().getHour() > 12) {
            timeChoice = String.format("%2d:%2d", dateTime.getTime().getHour() - 12, dateTime.getTime().getMinutes()).replace(" ", "0") + " pm";
        } else if (dateTime.getTime().getHour() == 12) {
            timeChoice = String.format("%2d:%2d", dateTime.getTime().getHour(), dateTime.getTime().getMinutes()).replace(" ", "0") + " pm";
        } else {
            timeChoice = String.format("%2d:%2d", dateTime.getTime().getHour(), dateTime.getTime().getMinutes()).replace(" ", "0") + " am";
        }

        list = (ListView) findViewById(R.id.list);
        updateUI();
    }

    /****** Function to set data in ArrayList *************/
    public void setListData()
    {
        CustomListViewValuesArr.clear();
        AdapterScheduleNewListValue activityList = new AdapterScheduleNewListValue(activityTitle,activityChoice);
        CustomListViewValuesArr.add(activityList);
        AdapterScheduleNewListValue repeatList = new AdapterScheduleNewListValue(repeatTitle,repeatChoice);
        CustomListViewValuesArr.add(repeatList);
        AdapterScheduleNewListValue dayList = new AdapterScheduleNewListValue(dayTitle,dayChoice);
        CustomListViewValuesArr.add(dayList);
        AdapterScheduleNewListValue timeList = new AdapterScheduleNewListValue(timeTitle,timeChoice);
        CustomListViewValuesArr.add(timeList);
    }

    /*****************  This function used by adapter ****************/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onItemClick(int mPosition)
    {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = null;

        if (mPosition==0) {
            //activity list
            dialogView = inflater.inflate(R.layout.schedule_new_dialog, null);
            myRg = (RadioGroup) dialogView.findViewById(R.id.myRg);
            for (int i = 0; i < activityList.length; i++) {
                final RadioButton button1 = new RadioButton(this);
                button1.setText(activityList[i]);
                button1.setPadding(0,20,0,20);
                button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            activityChoice = buttonView.getText().toString();
                        }
                    }
                });
                myRg.addView(button1);
            }
            setCheckedItem(mPosition);
            showSettingDialog(dialogView,false);
        }
        else if (mPosition==1){
            //repeat
            dialogView = inflater.inflate(R.layout.schedule_new_dialog, null);
            myRg = (RadioGroup) dialogView.findViewById(R.id.myRg);
            for (int i = 0; i < repeatList.length; i++) {
                final RadioButton button1 = new RadioButton(this);
                button1.setText(repeatList[i]);
                button1.setPadding(0, 20, 0, 20);
                button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            repeatChoice = buttonView.getText().toString();
                        }
                    }
                });
                myRg.addView(button1);
            }
            setCheckedItem(mPosition);
            showSettingDialog(dialogView,false);
        }
        else if (mPosition==2){
            //day
            if (repeatChoice.equals("Weekly")) {
                dialogView = inflater.inflate(R.layout.schedule_new_dialog, null);
                myRg = (RadioGroup) dialogView.findViewById(R.id.myRg);
                for (int i = 0; i < dayList.length; i++) {
                    final RadioButton button1 = new RadioButton(this);
                    button1.setText(dayList[i]);
                    button1.setPadding(0, 20, 0, 20);
                    button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                dayChoice = buttonView.getText().toString();
                            }
                        }
                    });
                    myRg.addView(button1);
                }
                setCheckedItem(mPosition);
                showSettingDialog(dialogView, false);
            }else
                Toast.makeText(ScheduleNewPage.this, "You choosed " + repeatChoice, Toast.LENGTH_SHORT).show();
        }
        else if (mPosition==3){
            dialogView = inflater.inflate(R.layout.schedule_new_time_picker, null);
            timePicker = (TimePicker) dialogView.findViewById(R.id.timePicker);
            getSetTime();
            setCheckedItem(mPosition);
            showSettingDialog(dialogView,true);
        }
    }

    public void showSettingDialog(View dialogView, final boolean isTimePicker){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Selection")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (isTimePicker) {
                            int currentHour = timePicker.getCurrentHour();
                            if (currentHour > 12) {
                                timeChoice = String.format("%2d:%2d", currentHour - 12, timePicker.getCurrentMinute()).replace(" ", "0") + " pm";
                            } else if (currentHour == 12) {
                                timeChoice = String.format("%2d:%2d", currentHour, timePicker.getCurrentMinute()).replace(" ", "0") + " pm";
                            } else {
                                timeChoice = String.format("%2d:%2d", currentHour, timePicker.getCurrentMinute()).replace(" ", "0") + " am";
                            }
                        }
                        updateUI();
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
    }

    public void updateUI(){
        setListData();
        Resources res = getResources();
        adapter = new AdapterScheduleNew(this, CustomListViewValuesArr, res);
        list.setAdapter(adapter);
    }

    public void setCheckedItem(int position){
        switch (position){
            case 0:
                for (int i=0; i<activityList.length; i++){
                    if (activityList[i].equals(activityChoice.trim())){
                        ((RadioButton)myRg.getChildAt(i)).setChecked(true);
                        break;
                    }
                }
                break;
            case 1:
                for (int i=0; i<repeatList.length; i++){
                    if (repeatList[i].equals(repeatChoice.trim())){
                        ((RadioButton)myRg.getChildAt(i)).setChecked(true);
                        break;
                    }
                }
                break;
            case 2:
                for (int i=0; i<dayList.length; i++){
                    if (dayList[i].equals(dayChoice.trim())){
                        ((RadioButton)myRg.getChildAt(i)).setChecked(true);
                        break;
                    }
                }
                break;
        }
    }

    public void getSetTime(){
        int add12Hour = 0;
        String[] hourAndMinutes = timeChoice.split(":");
        String[] minutesString = hourAndMinutes[1].split(" ");
        if (minutesString[1].equals("pm")){
            add12Hour = 12;
        }
        timePicker.setCurrentHour(Integer.parseInt(hourAndMinutes[0]) + add12Hour);
        timePicker.setCurrentMinute(Integer.parseInt(minutesString[0]));
    }

    public void addNewReminder (View view){
        ReminderDA myReminderDA = new ReminderDA(this);
        //generate time
        String hourAndMinutes;
        if (timeChoice.contains("pm")){
            String[] tempHourAndMinutes = timeChoice.split(":");
            int tempHour = Integer.parseInt(tempHourAndMinutes[0]) + 12;
            String tempHourString = tempHour+"";
            if (tempHour==24){
                tempHourString="12";
            }
            hourAndMinutes = tempHourString + (tempHourAndMinutes[1].replace("pm","").trim());
        }else{
            hourAndMinutes = timeChoice.replace(":", "").replace("am","").trim();
        }

        String myDay="";
        if (repeatChoice.equals("Weekly")){
            myDay = dayChoice;
        }

        UserLocalStore userLocalStore = new UserLocalStore(this);
        Reminder myReminder = new Reminder(myReminderDA.generateNewReminderID(),
                userLocalStore.returnUserID()+"",
                true,
                getActivityPlanID(activityChoice),
                repeatChoice,
                hourAndMinutes,
                myDay, 0, new DateTime().getCurrentDateTime(), new DateTime().getCurrentDateTime());
        Boolean success = myReminderDA.addReminder(myReminder);
        if (success){
            insertRequest.storeReminderDataInBackground(myReminder);
            alarmServiceController.startAlarm(myReminder);
        }else{
            Toast.makeText(this, "Add new reminder fail.", Toast.LENGTH_SHORT).show();
        }
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public String getActivityPlanID(String activityName){
        ActivityPlan activityPlan = myActivityPlanDA.getActivityPlanByName(activityName);
        if(activityPlan!=null){
            return activityPlan.getActivityPlanID();
        }else{
            Toast.makeText(this,"Fail to get activity plan.", Toast.LENGTH_SHORT);
            return "";
        }
    }

    public void BackAction(View view){
        this.finish();
    }
}
