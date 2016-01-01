package my.com.taruc.fitnesscompanion.Reminder.AlarmService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.UI.MainMenu;


public class Utils {

	public static NotificationManager mManager;

	@SuppressWarnings("static-access")
	public static void generateNotification(Context context){
		
		mManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		Intent intent1 = new Intent(context,MainMenu.class);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//		Notification notification = new Notification(R.drawable.ic_launcher,"This is a test message!", System.currentTimeMillis());
		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = builder.setContentIntent(pendingNotificationIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Health")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true).setContentTitle("FitnessCompanion")
                .setContentText("You Haven't Log Your Heart Rate and BP Today!").build();
//		notification.setLatestEventInfo(context, "AlarmManagerDemo", "You Havent Log Your Heart Rate Today!", pendingNotificationIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		mManager.notify(0, notification);
	}
}
