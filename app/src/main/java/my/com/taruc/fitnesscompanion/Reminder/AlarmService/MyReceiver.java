package my.com.taruc.fitnesscompanion.Reminder.AlarmService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import my.com.taruc.fitnesscompanion.Classes.DateTime;

public class MyReceiver extends BroadcastReceiver {

    DateTime dateTime = new DateTime();
    private static String mCurrentTime = "15:00:00";
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("My Reciever", dateTime.getCurrentDateTime().getTime().getFullTimeString());
        try {
            System.out.println(dateTime.getCurrentDateTime().getTime().getFullTimeString());
            if (dateTime.getCurrentDateTime().getTime().getFullTimeString().equals(mCurrentTime)) {
                Utils.generateNotification(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
