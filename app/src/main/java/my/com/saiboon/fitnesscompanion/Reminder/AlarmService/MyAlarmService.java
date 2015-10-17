package my.com.saiboon.fitnesscompanion.Reminder.AlarmService;

/**
 * Created by saiboon on 19/7/2015.
 */
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import my.com.saiboon.fitnesscompanion.UI.ScheduleNewPage;

public class MyAlarmService extends Service {

    @Override
    public void onCreate() {
// TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();
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
        ScheduleNewPage.alarmSound.play(this);
        Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onUnbind(Intent intent) {
// TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }


}
