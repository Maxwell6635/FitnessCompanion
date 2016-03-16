package my.com.taruc.fitnesscompanion.Reminder.AlarmService;

/**
 * Created by saiboon on 19/7/2015.
 */
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import my.com.taruc.fitnesscompanion.UI.ReminderPauseAlarm;

public class MyAlarmService extends Service {

    public static AlarmSound alarmSound = new AlarmSound();

    @Override
    public void onCreate() {
// TODO Auto-generated method stub
        //Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
// TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public void onDestroy() {
// TODO Auto-generated method stub
        super.onDestroy();
        Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
// TODO Auto-generated method stub
        super.onStart(intent, startId);
        //alarmSound.play(this,0);
        //Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();

        Intent schedulePauseAlarm = new Intent(this, ReminderPauseAlarm.class);
        schedulePauseAlarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(schedulePauseAlarm);
    }

    @Override
    public boolean onUnbind(Intent intent) {
// TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }


}
