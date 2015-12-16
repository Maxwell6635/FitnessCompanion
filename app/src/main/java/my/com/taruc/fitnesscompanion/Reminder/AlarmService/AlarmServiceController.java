package my.com.taruc.fitnesscompanion.Reminder.AlarmService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

import my.com.taruc.fitnesscompanion.Classes.Reminder;

/**
 * Created by saiboon on 17/12/2015.
 */
public class AlarmServiceController {
    Context context;

    public AlarmServiceController(Context context) {
        this.context = context;
    }
    public void startAlarm(Reminder myReminder){
        //generate alarm id
        int alarmID = Integer.parseInt(myReminder.getReminderID().replace("RE", ""));
        //generate day ID
        int myDay = 0;
        if (!myReminder.getRemindDay().equals("")) {
            switch (myReminder.getRemindDay()) {
                case "Sunday":
                    myDay = 1;
                    break;
                case "Monday":
                    myDay = 2;
                    break;
                case "Tuesday":
                    myDay = 3;
                    break;
                case "Wednesday":
                    myDay = 4;
                    break;
                case "Thursday":
                    myDay = 5;
                    break;
                case "Friday":
                    myDay = 6;
                    break;
                case "Saturday":
                    myDay = 7;
                    break;
            }
        }
        //generate hour
        int myHour = Integer.parseInt(myReminder.getRemindTime().substring(0, 2));
        //generate minutes
        int myMinutes = Integer.parseInt(myReminder.getRemindTime().substring(2, 4));

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, MyAlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, alarmID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        if (myDay!=0) {
            calendar.set(Calendar.DAY_OF_WEEK, myDay);
        }
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, myHour);
        calendar.set(Calendar.MINUTE, myMinutes);
        calendar.set(Calendar.SECOND, 0);

        if (myReminder.getRemindRepeat().equals("Never")){
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(context, "Alarm started", Toast.LENGTH_LONG).show();
        }else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Toast.makeText(context,"Repeat Alarm started",Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(this, "Start-ed Alarm", Toast.LENGTH_LONG).show();
    }

    public void cancelAlarm(int alarmID){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService( context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyAlarmService.class);
        PendingIntent pi = PendingIntent.getService( context, alarmID, intent, 0);
        alarmManager.cancel(pi);

        if (MyAlarmService.alarmSound.isPlay()){
            MyAlarmService.alarmSound.stop();
        }
        // Tell the user about what we did.
        Toast.makeText(context, "Cancel-ed Alarm", Toast.LENGTH_LONG).show();
    }
}
