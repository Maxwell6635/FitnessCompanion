package my.com.taruc.fitnesscompanion.ServerAPI;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.Achievement;
import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.Event;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Util.DbBitmapUtility;

/**
 * Created by Hexa-Jackson on 12/27/2015.
 */
public class RetrieveRequest  {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://www.seekt.asia/ServerRequest/";
    private static final String TAG_RESULTS="result";

    public RetrieveRequest(Context context) {

    }

    public ArrayList<Event> fetchAllEventInBackground(){
        ArrayList<Event> mEventArrayList = new ArrayList<Event>();
        try {
            FetchAllEventAsyncTask fetch = new FetchAllEventAsyncTask();
            fetch.execute();
            mEventArrayList = fetch.get();
        }  catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mEventArrayList;
    }

    public FitnessRecord fetchFitnessRecord(String id, String userId){
        FitnessRecord fitnessRecord = null;
        try {
            FetchFitnessRecordAsyncTask fetch = new FetchFitnessRecordAsyncTask(id,userId);
            fetch.execute();
            fitnessRecord = fetch.get();
        }  catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return fitnessRecord;
    }

    public ArrayList<String> fetchAllEmailInBackground(){
        ArrayList<String> mEmailArrayList = new ArrayList<String>();
        try {
            FetchEmailAsyncTask fetch = new FetchEmailAsyncTask();
            fetch.execute();
            mEmailArrayList = fetch.get();
        }  catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return mEmailArrayList;
    }

    public ArrayList<ActivityPlan> fetchActivityPlanDataInBackground(){
        ArrayList<ActivityPlan> activityPlanArrayList = new ArrayList<ActivityPlan>();
        try {
            FetchActivityPlanAsyncTask fetch = new FetchActivityPlanAsyncTask();
            fetch.execute();
            activityPlanArrayList = fetch.get();
        }  catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return activityPlanArrayList;
    }

    public ArrayList<Achievement> fetchAchievementInBackground(String userID){
        ArrayList<Achievement> achievementArrayList = new ArrayList<Achievement>();
        try {
            FetchAchievementAsyncTask fetch = new FetchAchievementAsyncTask(userID);
            fetch.execute();
            achievementArrayList = fetch.get();
        }  catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return achievementArrayList;
    }


    public class FetchAllEventAsyncTask extends AsyncTask<Void, Void, ArrayList<Event>> {

        public FetchAllEventAsyncTask() {

        }

        @Override
        protected ArrayList<Event> doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = null;
            String myJSON;
            ArrayList<Event> eventArrayList = new ArrayList<Event>();
            Event event = null;
            JSONArray jsonArray = null;

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(SERVER_ADDRESS + "FetchEventRecord.php");
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

                inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.d("Event", result);
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String eventID = jObject.getString("id");
                        String banner = jObject.getString("banner");
                        String url = jObject.getString("url");
                        String createdAt =  jObject.getString("created_at");
                        String updatedAt = jObject.getString("updated_at");
                        Bitmap returnBitmap = DbBitmapUtility.getImageFromJSon(banner);
                        event = new Event(eventID, returnBitmap, url, new DateTime(createdAt), new DateTime(updatedAt));
                        eventArrayList.add(event);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return eventArrayList;
        }
    }


    public class FetchFitnessRecordAsyncTask extends AsyncTask<Void, Void, FitnessRecord> {

        String id, userId;

        public FetchFitnessRecordAsyncTask(String id, String userId) {
            this.id = id;
            this.userId = userId;
        }

        @Override
        protected FitnessRecord doInBackground(Void... params) {
            String result = null;
            FitnessRecord fitnessRecord = null;

            try {
                ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                dataToSend.add(new BasicNameValuePair("id", id));
                dataToSend.add(new BasicNameValuePair("userID", userId));
                HttpParams httpRequestParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchFitnessRecord.php");

                try {
                    post.setEntity(new UrlEncodedFormEntity(dataToSend));
                    HttpResponse httpRespond = client.execute(post);

                    HttpEntity entity = httpRespond.getEntity();
                    result = EntityUtils.toString(entity);

                    JSONObject jObject = new JSONObject(result);

                    if (jObject.length() != 0) {
                        String fitnessRecordID = jObject.getString("id");
                        String userID = jObject.getString("userID");
                        int record_duration = jObject.getInt("record_duration");
                        double record_distance = jObject.getDouble("record_distance");
                        double record_calories = jObject.getDouble("record_calories");
                        int record_step =  jObject.getInt("record_step");
                        double average_heart_rate = jObject.getInt("average_heart_rate");
                        String createdAt =  jObject.getString("created_at");
                        String updatedAt = jObject.getString("updated_at");
                        String activity_id = jObject.getString("activity_id");
                        fitnessRecord= new FitnessRecord(fitnessRecordID, userID, activity_id, record_duration, record_distance
                                , record_calories, record_step, average_heart_rate, new DateTime(createdAt), new DateTime(updatedAt));
                        return fitnessRecord;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return fitnessRecord;
        }
    }




    public class FetchActivityPlanAsyncTask extends AsyncTask<Void, Void, ArrayList<ActivityPlan>> {

        public FetchActivityPlanAsyncTask() {

        }

        @Override
        protected ArrayList<ActivityPlan> doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = null;
            String myJSON;
            ArrayList<ActivityPlan> activityPlanArrayList = new ArrayList<ActivityPlan>();
            ActivityPlan activityPlan = null;
            JSONArray jsonArray = null;

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(SERVER_ADDRESS + "FetchActivityPlanRecord.php");
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

                inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.d("Activity Plan", result);
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String activityPlanID = jObject.getString("id");
                        String userID = jObject.getString("user_id");
                        String type = jObject.getString("type");
                        String activityName = jObject.getString("name");
                        String description = jObject.getString("description");
                        double estimateCalories =  jObject.getDouble("estimate_calories");
                        int duration = jObject.getInt("duration");
                        String createdAt =  jObject.getString("created_at");
                        String updatedAt = jObject.getString("updated_at");
                        int trainer_id = jObject.getInt("trainer_id");
                        activityPlan = new ActivityPlan(activityPlanID, userID, type, activityName, description, estimateCalories, duration, new DateTime(createdAt), new DateTime(updatedAt), trainer_id);
                        activityPlanArrayList.add(activityPlan);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return activityPlanArrayList;
        }
    }



    public class FetchAchievementAsyncTask extends AsyncTask<Void, Void, ArrayList<Achievement>> {
        String userID;

        public FetchAchievementAsyncTask(String userID) {
            this.userID = userID;
        }

        @Override
        protected ArrayList<Achievement> doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = null;
            String myJSON;
            ArrayList<Achievement> achievementArrayList = new ArrayList<Achievement>();
            Achievement achievement = null;
            JSONArray jsonArray = null;

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                dataToSend.add(new BasicNameValuePair("user_id", userID));
                HttpGet httpGet = new HttpGet(SERVER_ADDRESS + "FetchAchievementRecord.php"+"?"+ URLEncodedUtils.format(dataToSend, "utf-8"));
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

                inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.d("Activity Plan", result);
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String achievementID = jObject.getString("id");
                        String userID = jObject.getString("user_id");
                        String milestoneName = jObject.getString("milestones_name");
                        boolean milestoneResult = jObject.getBoolean("milestones_result");
                        String createdAt =  jObject.getString("created_at");
                        String updatedAt = jObject.getString("updated_at");
                        achievement = new Achievement(achievementID, userID, milestoneName, milestoneResult, new DateTime(createdAt), new DateTime(updatedAt));
                        achievementArrayList.add(achievement);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return achievementArrayList;
        }
    }


    public class FetchEmailAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

        public FetchEmailAsyncTask() {
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = null;
            String myJSON;
            JSONArray jsonArray = null;
            ArrayList<String> emailArrayList = new ArrayList<>();

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(SERVER_ADDRESS + "FetchAllEmail.php");
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

                inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.d("Email", result);
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String email = jObject.getString("email");
                        emailArrayList.add(email);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return emailArrayList;
        }
    }


}
