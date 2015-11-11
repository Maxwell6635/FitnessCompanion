package my.com.saiboon.fitnesscompanion.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.saiboon.fitnesscompanion.Classes.Reminder;
import my.com.saiboon.fitnesscompanion.Database.ReminderDA;
import my.com.saiboon.fitnesscompanion.R;
import my.com.saiboon.fitnesscompanion.Reminder.CustomAdapterSchedule;


public class SchedulePage extends ActionBarActivity {

    ReminderDA myReminderDA;
    CustomAdapterSchedule adapter;
    ArrayList<Reminder> myReminderList;
    ListView listViewSchedule;
    public SchedulePage CustomListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_page);

        myReminderDA = new ReminderDA(this);
        myReminderList = myReminderDA.getAllReminder();

        if (myReminderList!=null) {
            listViewSchedule = (ListView) findViewById(R.id.listViewSchedule);
            CustomListView = this;
            Resources res = getResources();
            adapter = new CustomAdapterSchedule( CustomListView, myReminderList, res );
            listViewSchedule.setAdapter(adapter);
        }
    }

    public void GoScheduleNew(View view){
        Intent intent = new Intent(this, ScheduleNewPage.class);
        //startActivity(intent);
        startActivityForResult(intent, 1);
    }

    public void onItemLongClick(final int mPosition) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete Reminder")
                .setMessage("Confirm delete this reminder?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final boolean success = myReminderDA.deleteReminder(myReminderList.get(mPosition).getReminderID());
                        if (success) {
                            Toast.makeText(SchedulePage.this, "Delete reminder success", Toast.LENGTH_SHORT).show();
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

    public void toggleButtonStatusChange(int position, boolean checked){
        /*Reminder myReminder = myReminderList.get(position);*/
        if (checked){
            //myReminder.setAvailability(true);
            showUpdateDialog(position,"on",true);
        }else {
            //myReminder.setAvailability(false);
            showUpdateDialog(position,"off",false);
        }
        /*boolean success = myReminderDA.updateReminder(myReminder);
        if (!success){
            Toast.makeText(this,"Update reminder fail",Toast.LENGTH_SHORT).show();
        }*/
    }

    public void showUpdateDialog(final int position, String onOff, final boolean checked){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("On/Off Reminder")
                .setMessage("Confirm "+ onOff + " reminder?" + position)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Reminder myReminder = myReminderList.get(position);
                        myReminder.setAvailability(checked);
                        final boolean success = myReminderDA.updateReminder(myReminderList.get(position));
                        if (success) {
                            //Toast.makeText(SchedulePage.this, "Update reminder success", Toast.LENGTH_SHORT).show();
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
            if(resultCode == RESULT_OK){
                //Update List
                finish();
                startActivity(getIntent());
            }
            if (resultCode == RESULT_CANCELED) {
                //Do nothing?
            }
        }
    }
}
