package my.com.taruc.fitnesscompanion.ServerAPI;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import my.com.taruc.fitnesscompanion.Classes.Achievement;
import my.com.taruc.fitnesscompanion.Classes.Reminder;

/**
 * Created by Hexa-Jackson on 12/27/2015.
 */
public class InsertRequest {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://www.seekt.asia/ServerRequest/";
    private static final String TAG_RESULTS="result";

    private Context context;

    public InsertRequest(Context context) {
        this.context = context;
    }

    public void storeAchievementDataInBackground(Achievement achievement){
        new StoreAchievementDataAsyncTask(achievement).execute();
    }

    public void storeReminderDataInBackground(Reminder reminder){
        new StoreReminderDataAsyncTask(reminder).execute();
    }

    public class StoreReminderDataAsyncTask extends AsyncTask<Void,Void,Void> {
        Reminder reminder;

        public StoreReminderDataAsyncTask(Reminder reminder){
            this.reminder = reminder;
        }
        @Override
        protected Void doInBackground(Void... params) {
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", reminder.getReminderID()));
            dataToSend.add(new BasicNameValuePair("user_id", reminder.getUserID()));
            dataToSend.add(new BasicNameValuePair("repeats", reminder.getRemindRepeat()));
            dataToSend.add(new BasicNameValuePair("time", reminder.getRemindTime()));
            dataToSend.add(new BasicNameValuePair("day", reminder.getRemindDay()));
            dataToSend.add(new BasicNameValuePair("date", String.valueOf(reminder.getRemindDate())));
            dataToSend.add(new BasicNameValuePair("availability",  String.valueOf(reminder.isAvailability())));
            dataToSend.add(new BasicNameValuePair("createdAt",  reminder.getCreatedAt().getDateTimeString()));
            dataToSend.add(new BasicNameValuePair("updateAt",  reminder.getCreatedAt().getDateTimeString()));
            dataToSend.add(new BasicNameValuePair("activity_id", reminder.getActivitesPlanID()));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "StoreReminderRecord.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public class StoreAchievementDataAsyncTask extends AsyncTask<Void,Void,Void> {
        Achievement achievement;

        public StoreAchievementDataAsyncTask(Achievement achievement){
            this.achievement = achievement;
        }
        @Override
        protected Void doInBackground(Void... params) {
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", achievement.getAchievementID()));
            dataToSend.add(new BasicNameValuePair("user_id", achievement.getUserID()));
            dataToSend.add(new BasicNameValuePair("milestoneName", achievement.getMilestoneName()));
            dataToSend.add(new BasicNameValuePair("milestoneResult", String.valueOf(achievement.getMilestoneResult())));
            dataToSend.add(new BasicNameValuePair("createdAt", currentDateTimeString ));
            dataToSend.add(new BasicNameValuePair("updateAt", currentDateTimeString));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "StoreAchievementRecord.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }




}
