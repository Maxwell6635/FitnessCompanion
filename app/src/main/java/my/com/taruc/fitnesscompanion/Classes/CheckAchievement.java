package my.com.taruc.fitnesscompanion.Classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Database.GoalDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.AlarmSound;
import my.com.taruc.fitnesscompanion.UI.ActivityPlanPage;
import my.com.taruc.fitnesscompanion.UI.CongratulationPage;
import my.com.taruc.fitnesscompanion.UI.ExercisePage;
import my.com.taruc.fitnesscompanion.UI.GoalPage;
import my.com.taruc.fitnesscompanion.UI.MainMenu;
import my.com.taruc.fitnesscompanion.UI.MedalPage;

/**
 * Created by saiboon on 3/2/2016.
 */
public class CheckAchievement {

    Context context;
    GoalDA goalDA;
    ArrayList<Goal> goalArrayList = new ArrayList<>();
    Goal goal;
    AlarmSound alarmSound = new AlarmSound();
    private Activity activity;

    public CheckAchievement(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        goalDA = new GoalDA(context);
    }

    public void checkGoal(){
        boolean success = false;
        goalArrayList = goalDA.getAllNotDoneGoal();
        for(int i =0; i< goalArrayList.size(); i++){
            goal = goalArrayList.get(i);
            if (goal.getGoalDescription().equals(goal.getReduceWeightTitle())) {
                //check Weight
                success = goal.getCurrentWeight(context) <= goal.getGoalTarget();
            } else if (goal.getGoalDescription().equals(goal.getStepWalkTitle())) {
                //check step count
                success = goal.getCurrentStepCount(context) >= goal.getGoalTarget();
            } else {
                int currentValue = goal.totalAllFitnessRecord(context, goal.getGoalDescription());
                if (goal.getGoalDescription().equals(goal.getRunDuration()) ||
                        goal.getGoalDescription().equals(goal.getExerciseDuration())) {
                    //check "Run Duration", "Exercise Duration"
                    success = (currentValue/60.0) >= goal.getGoalTarget();
                } else if (goal.getGoalDescription().equals(goal.getCaloriesBurn())) {
                    //check "Calories Burn"
                    success = currentValue >= goal.getGoalTarget();
                }
            }
            if(success){
                goal.setGoalDone(true);
                goalDA.updateGoal(goal);
                /*alarmSound.play(context, 1);
                Toast.makeText(context, "Congratulation", Toast.LENGTH_SHORT).show();
                alarmSound.stop();*/
                notification();
            }
        }
    }

    public void notification(){
        AlarmSound alarmSound = new AlarmSound();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo_drawer)
                .setContentTitle("Congratulation!")
                .setAutoCancel(true)
                .setSound(alarmSound.getNotificationSound())
                .setVibrate(new long[]{10})
                .setContentText("Goal " + goal.getGoalDescription() + " achieved!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, GoalPage.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainMenu.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent( 0, PendingIntent.FLAG_UPDATE_CURRENT );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

    }
}
