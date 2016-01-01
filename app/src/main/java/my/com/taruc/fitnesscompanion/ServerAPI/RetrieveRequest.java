package my.com.taruc.fitnesscompanion.ServerAPI;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Classes.DateTime;

/**
 * Created by Hexa-Jackson on 12/27/2015.
 */
public class RetrieveRequest  {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://www.seekt.asia/ServerRequest/";
    private static final String TAG_RESULTS="result";

    public RetrieveRequest(Context context) {

    }

    public ArrayList<ActivityPlan> fetchActivityPlanDataInBackground(){
        ArrayList<ActivityPlan> rankingArrayList = new ArrayList<ActivityPlan>();
        try {
            FetchActivityPlanAsyncTask fetch = new FetchActivityPlanAsyncTask();
            fetch.execute();
            rankingArrayList = fetch.get();
        }  catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rankingArrayList;

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

}
