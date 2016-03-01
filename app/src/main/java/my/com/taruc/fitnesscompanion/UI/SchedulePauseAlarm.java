package my.com.taruc.fitnesscompanion.UI;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import my.com.taruc.fitnesscompanion.Classes.Reminder;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.Database.ReminderDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.AlarmSound;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.MyAlarmService;

public class SchedulePauseAlarm extends Activity {

    Reminder reminder;
    ReminderDA reminderDA;
    FitnessRecordDA fitnessRecordDA;
    String activities ="--";

    public static AlarmSound alarmSound = new AlarmSound();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_pause_alarm);

        // search reminder
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("HHmm");
        String mytime = dateformat.format(calendar.getTime());
        reminderDA = new ReminderDA(this);
        fitnessRecordDA = new FitnessRecordDA(this);
        reminder = reminderDA.getReminderByTime(mytime);

        if(reminder.getReminderID()!=null){
            activities = fitnessRecordDA.getActivityPlanName(reminder.getActivitesPlanID());
            alarmSound.play(this, 0);
            AlertDialog alarmDialog = new AlertDialog.Builder(this)
                    .setTitle("Fitness Reminder")
                    .setMessage("You are remind to do " + activities + " now. Keep it up!")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            stopAlarm();
                        }
                    })
                    .create();
            alarmDialog.show();
        }else{
            Log.i("SchedulePauseAlarm","No matched reminder founded in records");
            finish();
        }
    }

    public void stopAlarm(){
        if(reminder.getReminderID()!=null) {
            //MyAlarmService.alarmSound.stop();
            int alarmID = Integer.parseInt(reminder.getReminderID().replace("RE", ""));
            cancelAlarm(alarmID);
        }
        if (alarmSound.isPlay()){
            alarmSound.stop();
        }
        this.finish();
    }

    public void cancelAlarm(int alarmID){
        if (reminder.getRemindRepeat().equals("Never")) {
            reminder.setAvailability(false);
            boolean success = reminderDA.updateReminder(reminder);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, MyAlarmService.class);
            PendingIntent pi = PendingIntent.getService(this, alarmID, intent, 0);
            alarmManager.cancel(pi);
        }
    }

}
