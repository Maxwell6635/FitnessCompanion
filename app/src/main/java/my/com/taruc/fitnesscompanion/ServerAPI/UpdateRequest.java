package my.com.taruc.fitnesscompanion.ServerAPI;

import android.app.ProgressDialog;
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
import my.com.taruc.fitnesscompanion.Classes.Goal;
import my.com.taruc.fitnesscompanion.Classes.Reminder;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.Util.DbBitmapUtility;

/**
 * Created by Hexa-Jackson on 12/26/2015.
 */
public class UpdateRequest {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://www.seekt.asia/ServerRequest/";

    public UpdateRequest(Context context) {
    }

    public void updateAchievementDataInBackground(Achievement achievement){
        new UpdateAchievementDataAsyncTask(achievement).execute();
    }

    public void updateUserProfileDataInBackground(UserProfile userProfile){
        new UpdateUserProfileDataAsyncTask(userProfile).execute();
    }

    public void updateGCMIDInBackground(String userID , String gcmID){
       new UpdateGCMIDDataAsyncTask(userID, gcmID).execute();
    }

    public void updateGoalDataInBackground(Goal goal){
        new UpdateGoalDataAsyncTask(goal).execute();
    }

    public void updateReminderDataInBackground(Reminder reminder){
        new UpdateReminderDataAsyncTask(reminder).execute();
    }

    public class UpdateAchievementDataAsyncTask extends  AsyncTask<Void,Void,Void>{
        Achievement achievement;

        public UpdateAchievementDataAsyncTask(Achievement achievement){
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
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateAchievementRecord.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class UpdateUserProfileDataAsyncTask extends  AsyncTask<Void,Void,Void>{
        UserProfile userProfile;

        public UpdateUserProfileDataAsyncTask(UserProfile userProfile){
            this.userProfile = userProfile;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", userProfile.getUserID()));
            dataToSend.add(new BasicNameValuePair("email", userProfile.getEmail()));
            dataToSend.add(new BasicNameValuePair("gcmID", userProfile.getmGCMID()));
            dataToSend.add(new BasicNameValuePair("password", userProfile.getPassword()));
            dataToSend.add(new BasicNameValuePair("name", userProfile.getName()));
            dataToSend.add(new BasicNameValuePair("dob", userProfile.getDOB().getDate().getFullDateString()));
            dataToSend.add(new BasicNameValuePair("gender", userProfile.getGender()));
            dataToSend.add(new BasicNameValuePair("initial_weight", userProfile.getInitial_Weight() + ""));
            dataToSend.add(new BasicNameValuePair("height", userProfile.getHeight() + ""));
            dataToSend.add(new BasicNameValuePair("reward_point", userProfile.getReward_Point() + ""));
            dataToSend.add(new BasicNameValuePair("created_at", userProfile.getCreated_At().getDateTimeString()));
            dataToSend.add(new BasicNameValuePair("updated_at", userProfile.getUpdated_At().getDateTimeString()));
            dataToSend.add(new BasicNameValuePair("image", DbBitmapUtility.encodeImagetoString(userProfile.getBitmap())));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateUserProfile.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class UpdateGoalDataAsyncTask extends AsyncTask<Void,Void,Void> {
        Goal goal;

        public UpdateGoalDataAsyncTask(Goal goal){
            this.goal = goal;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", goal.getGoalId()));
            dataToSend.add(new BasicNameValuePair("user_id", goal.getUserID()));
            dataToSend.add(new BasicNameValuePair("goal_desc", goal.getGoalDescription()));
            dataToSend.add(new BasicNameValuePair("goal_duration", String.valueOf(goal.getGoalDuration())));
            dataToSend.add(new BasicNameValuePair("goal_target", String.valueOf(goal.getGoalTarget())));
            dataToSend.add(new BasicNameValuePair("goal_done", String.valueOf(goal.isGoalDone())));
            dataToSend.add(new BasicNameValuePair("createdAt", goal.getCreateAt().getDateTimeString()));
            dataToSend.add(new BasicNameValuePair("updateAt",goal.getUpdateAt().getDateTimeString()));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateGoalRecord.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class UpdateReminderDataAsyncTask extends AsyncTask<Void,Void,Void> {
        Reminder reminder;

        public UpdateReminderDataAsyncTask(Reminder reminder){
            this.reminder = reminder;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", reminder.getReminderID()));
            dataToSend.add(new BasicNameValuePair("user_id", reminder.getUserID()));
            if(reminder.isAvailability()){
                dataToSend.add(new BasicNameValuePair("availability", "1"));
            }else{
                dataToSend.add(new BasicNameValuePair("availability", "0"));
            }
            dataToSend.add(new BasicNameValuePair("activity_id", reminder.getActivitesPlanID()));
            dataToSend.add(new BasicNameValuePair("repeat", reminder.getRemindRepeat()));
            dataToSend.add(new BasicNameValuePair("time", reminder.getRemindTime()));
            dataToSend.add(new BasicNameValuePair("day", reminder.getRemindDay()));
            dataToSend.add(new BasicNameValuePair("date", String.valueOf(reminder.getRemindDate())));
            dataToSend.add(new BasicNameValuePair("created_at", reminder.getCreatedAt().getDateTimeString()));
            dataToSend.add(new BasicNameValuePair("updated_at", reminder.getCreatedAt().getDateTimeString()));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateReminderRecord.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class UpdateGCMIDDataAsyncTask extends AsyncTask<Void,Void,Void> {

        String gcmID, userID;

        public UpdateGCMIDDataAsyncTask(String userID , String gcmID){
            this.userID = userID;
            this.gcmID = gcmID;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("userID", userID));
            dataToSend.add(new BasicNameValuePair("gcmID", gcmID));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateGCMID.php");
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


