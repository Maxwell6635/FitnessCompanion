package my.com.taruc.fitnesscompanion.ServerAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
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

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.Goal;

/**
 * Created by Hexa-Jackson on 12/26/2015.
 */
public class UpdateRequest {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://www.seekt.asia/ServerRequest/";
    ProgressDialog progressDialog;

    public UpdateRequest(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void updateHealthProfileDataInBackground(Goal goal){
        new UpdateGoalDataAsyncTask(goal).execute();
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
            dataToSend.add(new BasicNameValuePair("createdAt", goal.getCreateAt()));
            dataToSend.add(new BasicNameValuePair("updateAt",goal.getUpdateAt()));
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

}


