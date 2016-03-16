package my.com.taruc.fitnesscompanion.UI;

import android.app.AlertDialog;
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
import my.com.taruc.fitnesscompanion.Reminder.AdapterReminderRecycleView;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.AlarmServiceController;
import my.com.taruc.fitnesscompanion.ServerAPI.DeleteRequest;


public class ReminderPage extends ActionBarActivity {

    ReminderDA myReminderDA;
    AdapterReminderRecycleView adapter;
    ArrayList<Reminder> myReminderList;
    RecyclerView scheduleRecycleView;
    DeleteRequest deleteRequest;
    AlarmServiceController alarmServiceController;

    @Bind(R.id.textViewNoData)
    TextView textViewNoData;
    @Bind(R.id.textViewTitle)
    TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule_page);
        ButterKnife.bind(this);
        textViewTitle.setText(R.string.schedule);
        alarmServiceController = new AlarmServiceController(this);

        myReminderDA = new ReminderDA(this);
        deleteRequest = new DeleteRequest(this);
        myReminderList = myReminderDA.getAllReminder();

        scheduleRecycleView = (RecyclerView) findViewById(R.id.scheduleRecycleView);
        scheduleRecycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterReminderRecycleView(this, this, myReminderList);
        scheduleRecycleView.setAdapter(adapter);

        if (myReminderList.size() == 0) {
            textViewNoData.setText("No Data");
        }
    }

    public void BackAction(View view) {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myReminderList = myReminderDA.getAllReminder();
        adapter.swap(myReminderList);
    }

    public void GoScheduleNew(View view) {
        Intent intent = new Intent(this, ReminderNewPage.class);
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
                            deleteRequest.deleteReminderDataInBackground(myReminderList.get(mPosition));
                            int alarmID = Integer.parseInt(myReminderList.get(mPosition).getReminderID().replace("RE", ""));
                            alarmServiceController.cancelAlarm(alarmID);
                            //Toast.makeText(ReminderPage.this, "Delete reminder success", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                        } else {
                            Toast.makeText(ReminderPage.this, "Delete reminder fail", Toast.LENGTH_SHORT).show();
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
                            //Toast.makeText(ReminderPage.this, "Update reminder success", Toast.LENGTH_SHORT).show();
                            if (checked) {
                                startAlarm(myReminder);
                            } else {
                                int alarmID = Integer.parseInt(myReminder.getReminderID().replace("RE", ""));
                                alarmServiceController.cancelAlarm(alarmID);
                            }
                            finish();
                            startActivity(getIntent());
                        } else {
                            Toast.makeText(ReminderPage.this, "Update reminder fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                        startActivity(getIntent());
                    }
                }).create();
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

    public void startAlarm(Reminder myReminder) {
        alarmServiceController.startAlarm(myReminder);
    }

}
