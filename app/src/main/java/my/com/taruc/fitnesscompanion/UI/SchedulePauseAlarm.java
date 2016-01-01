package my.com.taruc.fitnesscompanion.UI;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Classes.Reminder;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.Database.ReminderDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.MyAlarmService;

public class SchedulePauseAlarm extends Activity {

    Reminder reminder;
    ReminderDA reminderDA;
    FitnessRecordDA fitnessRecordDA;

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

        AlertDialog alarmDialog = new AlertDialog.Builder(this)
                .setTitle("Fitness Reminder")
                .setMessage("You are remind to do " + fitnessRecordDA.getActivityPlanName(reminder.getActivitesPlanID()) + " now. Keep it up!")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        stopAlarm();
                    }
                })
                .create();
        alarmDialog.show();
    }

    public void stopAlarm(){
        //MyAlarmService.alarmSound.stop();
        int alarmID = Integer.parseInt(reminder.getReminderID().replace("RE", ""));
        cancelAlarm(alarmID);
        this.finish();
    }

    public void cancelAlarm(int alarmID){
        if(reminder.getRemindRepeat().equals("Never")) {
            reminder.setAvailability(false);
            boolean success = reminderDA.updateReminder(reminder);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, MyAlarmService.class);
            PendingIntent pi = PendingIntent.getService(this, alarmID, intent, 0);
            alarmManager.cancel(pi);
        }

        if (MyAlarmService.alarmSound.isPlay()){
            MyAlarmService.alarmSound.stop();
        }
        // Tell the user about what we did.
        //Toast.makeText(this, "Cancel-ed Alarm", Toast.LENGTH_LONG).show();
    }

}
