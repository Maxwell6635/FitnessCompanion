package my.com.taruc.fitnesscompanion.Reminder.AlarmService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import my.com.taruc.fitnesscompanion.Classes.Reminder;

/**
 * Created by saiboon on 17/12/2015.
 */
public class AlarmServiceController {
    Context context;
    Reminder myReminder;

    public AlarmServiceController(Context context) {
        this.context = context;
    }

    public void startAlarm(Reminder inputReminder){
        myReminder = inputReminder;
        //generate alarm id
        int alarmID = Integer.parseInt(myReminder.getReminderID().replace("RE", ""));
        //generate day
        int myDay = generateDay();
        //generate hour
        int myHour = Integer.parseInt(myReminder.getRemindTime().substring(0, 2));
        //generate minutes
        int myMinutes = Integer.parseInt(myReminder.getRemindTime().substring(2, 4));

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, MyAlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, alarmID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        int currentTime24HourFormat = Integer.parseInt(String.format("%d%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        int reminderTime24HourFormat = Integer.parseInt(String.format("%d%02d",myHour,myMinutes));
        if (myDay!=0) {
            if(calendar.get(Calendar.DAY_OF_WEEK) == myDay){
                if (currentTime24HourFormat >= reminderTime24HourFormat){
                    //delay reminder to next week
                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 7);
                }
            }
            calendar.set(Calendar.DAY_OF_WEEK, myDay);
        }
        else if (currentTime24HourFormat >= reminderTime24HourFormat){
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, myHour);
        calendar.set(Calendar.MINUTE, myMinutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (myReminder.getRemindRepeat().equals("Never")){
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }else if(myDay!=0) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        }else{
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
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
        //Toast.makeText(context, "Cancel-ed Alarm", Toast.LENGTH_LONG).show();
    }

    public int generateDay(){
        if (!myReminder.getRemindDay().equals("")) {
            switch (myReminder.getRemindDay()) {
                case "Sunday":
                    return 1;
                case "Monday":
                    return 2;
                case "Tuesday":
                    return 3;
                case "Wednesday":
                    return 4;
                case "Thursday":
                    return 5;
                case "Friday":
                    return 6;
                case "Saturday":
                    return 7;
            }
        }
        return 0;
    }

}
