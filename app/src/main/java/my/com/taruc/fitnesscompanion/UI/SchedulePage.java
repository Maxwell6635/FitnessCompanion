package my.com.taruc.fitnesscompanion.UI;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.Reminder;
import my.com.taruc.fitnesscompanion.Database.ReminderDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.Reminder.AdapterScheduleRecycleView;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.AlarmServiceController;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.MyAlarmService;


public class SchedulePage extends ActionBarActivity {

    ReminderDA myReminderDA;
    AdapterScheduleRecycleView adapter;
    ArrayList<Reminder> myReminderList;
    RecyclerView scheduleRecycleView;
    AlarmServiceController alarmServiceController;

    @Bind(R.id.textViewNoData)
    TextView textViewNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.schedule_page);
        ButterKnife.bind(this);
        alarmServiceController = new AlarmServiceController(this);

        myReminderDA = new ReminderDA(this);
        myReminderList = myReminderDA.getAllReminder();

        scheduleRecycleView = (RecyclerView) findViewById(R.id.scheduleRecycleView);
        scheduleRecycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterScheduleRecycleView(this, this, myReminderList);
        scheduleRecycleView.setAdapter(adapter);

        if (myReminderList.size() == 0) {
            textViewNoData.setText("No Data");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        myReminderList = myReminderDA.getAllReminder();
        adapter.swap(myReminderList);
    }

    public void GoScheduleNew(View view) {
        Intent intent = new Intent(this, ScheduleNewPage.class);
        startActivityForResult(intent, 1);
    }

    //Delete Reminder
    public void onItemLongClick(final int mPosition) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete Reminder")
                .setMessage("Confirm delete this reminder?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final boolean success = myReminderDA.deleteReminder(myReminderList.get(mPosition).getReminderID());
                        if (success) {
                            int alarmID = Integer.parseInt(myReminderList.get(mPosition).getReminderID().replace("RE", ""));
                            alarmServiceController.cancelAlarm(alarmID);
                            //Toast.makeText(SchedulePage.this, "Delete reminder success", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                        } else {
                            Toast.makeText(SchedulePage.this, "Delete reminder fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
    }

    public void toggleButtonStatusChange(int position, boolean checked) {
        if (checked) {
            showUpdateDialog(position, "on", true);
        } else {
            showUpdateDialog(position, "off", false);
        }
    }

    public void showUpdateDialog(final int position, String onOff, final boolean checked) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("On/Off Reminder")
                .setMessage("Confirm " + onOff + " reminder?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Reminder myReminder = myReminderList.get(position);
                        myReminder.setAvailability(checked);
                        final boolean success = myReminderDA.updateReminder(myReminderList.get(position));
                        if (success) {
                            //Toast.makeText(SchedulePage.this, "Update reminder success", Toast.LENGTH_SHORT).show();
                            if(checked){
                                startAlarm(myReminder);
                            }else{
                                int alarmID = Integer.parseInt(myReminder.getReminderID().replace("RE", ""));
                                alarmServiceController.cancelAlarm(alarmID);
                            }
                            finish();
                            startActivity(getIntent());
                        } else {
                            Toast.makeText(SchedulePage.this, "Update reminder fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", null).create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //Update List
                finish();
                startActivity(getIntent());
            }
            if (resultCode == RESULT_CANCELED) {
                //Do nothing?
            }
        }
    }

    public void startAlarm(Reminder myReminder){
        alarmServiceController.startAlarm(myReminder);
    }

}
