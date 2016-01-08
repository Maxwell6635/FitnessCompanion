package my.com.taruc.fitnesscompanion.ServerAPI;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.Goal;
import my.com.taruc.fitnesscompanion.Classes.Reminder;

/**
 * Created by Hexa-Jackson on 1/3/2016.
 */
public class DeleteRequest {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://www.seekt.asia/ServerRequest/";
    private static final String TAG_RESULTS="result";

    public DeleteRequest(Context context) {

    }

    public void deleteReminderDataInBackground(Reminder reminder){
        new DeleteReminderDataAsyncTask(reminder).execute();
    }

    public void deleteGoalDataInBackground(Goal goal){
        new DeleteGoalDataAsyncTask(goal).execute();
    }

    public class DeleteReminderDataAsyncTask extends AsyncTask<Void,Void,Integer> {
        Reminder reminder;
        int return_result;
        public DeleteReminderDataAsyncTask(Reminder reminder){
            this.reminder = reminder;
        }
        @Override
        protected Integer doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", reminder.getReminderID()));
            dataToSend.add(new BasicNameValuePair("user_id", reminder.getUserID()));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "DeleteReminderRecord.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpRespond = client.execute(post);

                HttpEntity entity = httpRespond.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);
                if (jObject.length() == 0) {
                    return_result = 0;
                } else {
                    return_result = jObject.getInt("result");
                    return return_result;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public class DeleteGoalDataAsyncTask extends AsyncTask<Void,Void,Integer> {
        Goal goal;
        int return_result;
        public DeleteGoalDataAsyncTask(Goal goal){
            this.goal = goal;
        }
        @Override
        protected Integer doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", goal.getGoalId()));
            dataToSend.add(new BasicNameValuePair("user_id", goal.getUserID()));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "DeleteGoalRecord.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpRespond = client.execute(post);

                HttpEntity entity = httpRespond.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);
                if (jObject.length() == 0) {
                    return_result = 0;
                } else {
                    return_result = jObject.getInt("result");
                    return return_result;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }




}
