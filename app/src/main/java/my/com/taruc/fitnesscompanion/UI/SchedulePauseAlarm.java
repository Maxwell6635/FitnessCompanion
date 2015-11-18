package my.com.taruc.fitnesscompanion.UI;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import my.com.taruc.fitnesscompanion.Classes.Reminder;
import my.com.taruc.fitnesscompanion.Database.ReminderDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.MyAlarmService;

public class SchedulePauseAlarm extends Activity {

    Reminder reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_pause_alarm);

        // search reminder
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("HHmm");
        String mytime = dateformat.format(calendar.getTime());
        ReminderDA reminderDA = new ReminderDA(this);
        reminder = reminderDA.getReminderByTime(mytime);

        TextView desc = (TextView) findViewById(R.id.textViewAlarmDesc);
        desc.setText(mytime);
    }

    public void stopAlarm(View view){
        //MyAlarmService.alarmSound.stop();
        int alarmID = Integer.parseInt(reminder.getReminderID().replace("RE", ""));
        cancelAlarm(alarmID);
        this.finish();
    }

    public void cancelAlarm(int alarmID){
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarmService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
        alarmManager.cancel(pi);

        if (MyAlarmService.alarmSound.isPlay()){
            MyAlarmService.alarmSound.stop();
        }
        // Tell the user about what we did.
        Toast.makeText(this, "Cancel-ed Alarm", Toast.LENGTH_LONG).show();
    }

}
