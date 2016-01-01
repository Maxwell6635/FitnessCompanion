
package my.com.taruc.fitnesscompanion.ServerAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import java.util.List;

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Classes.Goal;
import my.com.taruc.fitnesscompanion.Classes.HealthProfile;
import my.com.taruc.fitnesscompanion.Classes.Ranking;
import my.com.taruc.fitnesscompanion.Classes.RealTimeFitness;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;

import my.com.taruc.fitnesscompanion.Util.DbBitmapUtility;

/**
 * Created by JACKSON on 5/26/2015.
 */
public class ServerRequests {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    //public static final String SERVER_ADDRESS = "http://fitnesscompanion.net16.net/";
//    public static final String SERVER_ADDRESS = "http://fitnesscompanion.freeoda.com/";
    public static final String SERVER_ADDRESS = "http://www.seekt.asia/ServerRequest/";
    ProgressDialog progressDialog;
    private static final String TAG_RESULTS="result";
    String encodedString;
    Bitmap bitmap;
    private Context mContext;

    public ServerRequests(Context context) {
        mContext = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
  
    }

    public void storeUserDataInBackground(UserProfile user, GetUserCallBack userCallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallBack).execute();
    }

    public void fetchUserDataInBackground(UserProfile user, GetUserCallBack callBack) {
        progressDialog.show();
        new FetchUserDataAsyncTask(user, callBack).execute();
    }


    public void storeFBUserDataInBackground(UserProfile user){
        new StoreFBUserDataAsyncTask(user).execute();
    }

    public void storeHealthProfileDataInBackground(HealthProfile healthProfile){
        new StoreHealthProfileDataAsyncTask(healthProfile).execute();
    }

    public void updateHealthProfileDataInBackground(HealthProfile healthProfile){
        new UpdateHealthProfileDataAsyncTask(healthProfile).execute();
    }

    public void storeFitnessRecordInBackground(FitnessRecord fitnessRecord){
        new StoreFitnessRecordDataAsyncTask(fitnessRecord).execute();
    }

    public void storeRealTimeFitnessInBackground(RealTimeFitness realTimeFitnes){
        new StoreRealTimeFitnessDataAsyncTask(realTimeFitnes).execute();
    }

    public void storeGoalDataInBackground(Goal goal){
        new StoreGoalDataAsyncTask(goal).execute();
    }

    public List<HealthProfile> fetchHealthProfileDataInBackground(String userID){
        List<HealthProfile> healthProfileArrayList = new ArrayList<HealthProfile>();
        try {
            FetchHealthProfileAsyncTask fetch = new FetchHealthProfileAsyncTask(userID);
            fetch.execute();
            healthProfileArrayList = fetch.get();
        }  catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return healthProfileArrayList;

    }


    public ArrayList<Ranking> fetchRankingDataInBackground(){
        ArrayList<Ranking> rankingArrayList = new ArrayList<Ranking>();
        try {
            FetchRankingAsyncTask fetch = new FetchRankingAsyncTask();
            fetch.execute();
            rankingArrayList = fetch.get();
        }  catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return rankingArrayList;

    }


    public Integer returnCountID()
    {
        Integer countID = 0;
        Integer returnCount = 0;
        try
        {
            getRowCountBackground task = new getRowCountBackground();
            task.execute();
            countID = Integer.parseInt(task.get()) + 1;
            returnCount = countID;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return returnCount ;
    }


    public class FetchRankingAsyncTask extends AsyncTask<Void, Void, ArrayList<Ranking>> {

        public FetchRankingAsyncTask() {

        }

        @Override
        protected ArrayList<Ranking> doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = null;
            String myJSON;
            ArrayList<Ranking> rankingArrayList = new ArrayList<Ranking>();
            Ranking ranking = null;
            JSONArray jsonArray = null;

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(SERVER_ADDRESS+"FetchRankingRecord.php");
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
                Log.d("Ranking",result);
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String ID = jObject.getString("id");
                        String userID = jObject.getString("user_id");
                        String type = jObject.getString("type");
                        Integer points  = jObject.getInt("points");
                        DateTime createdAt = new DateTime(jObject.getString("created_at"));
                        DateTime updatedAt = new DateTime(jObject.getString("updated_at"));
                        ranking = new Ranking(ID, userID,type,points, createdAt, updatedAt);
                        rankingArrayList.add(ranking);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rankingArrayList;
        }
    }



    public class StoreGoalDataAsyncTask extends AsyncTask<Void,Void,Void>{
        Goal goal;

        public StoreGoalDataAsyncTask(Goal goal){
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
            dataToSend.add(new BasicNameValuePair("createdAt", goal.getCreateAt().getDateTime()));
            dataToSend.add(new BasicNameValuePair("updateAt",goal.getUpdateAt().getDateTime()));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "StoreGoalRecord.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public class StoreRealTimeFitnessDataAsyncTask extends AsyncTask<Void,Void,Void>{
       RealTimeFitness realTimeFitness;

        public StoreRealTimeFitnessDataAsyncTask(RealTimeFitness realTimeFitness){
            this.realTimeFitness = realTimeFitness;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", realTimeFitness.getRealTimeFitnessID()));
            dataToSend.add(new BasicNameValuePair("user_id", realTimeFitness.getUserID()));
            dataToSend.add(new BasicNameValuePair("capture_datetime", realTimeFitness.getCaptureDateTime().getDateTime()));
            dataToSend.add(new BasicNameValuePair("step_number", String.valueOf(realTimeFitness.getStepNumber())));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "StoreRealTimeFitness.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }




    public class StoreFitnessRecordDataAsyncTask extends  AsyncTask<Void,Void,Void>{
        FitnessRecord fitnessRecord;

        public StoreFitnessRecordDataAsyncTask(FitnessRecord fitnessRecord){
            this.fitnessRecord = fitnessRecord;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", fitnessRecord.getFitnessRecordID()));
            dataToSend.add(new BasicNameValuePair("user_id", fitnessRecord.getUserID()));
            dataToSend.add(new BasicNameValuePair("activities_id", fitnessRecord.getActivityPlanID()));
            dataToSend.add(new BasicNameValuePair("record_duration", fitnessRecord.getRecordDuration()+""));
            dataToSend.add(new BasicNameValuePair("record_distance", fitnessRecord.getRecordDistance() + ""));
            dataToSend.add(new BasicNameValuePair("record_calories", fitnessRecord.getRecordCalories()+""));
            dataToSend.add(new BasicNameValuePair("record_step", fitnessRecord.getRecordStep() + ""));
            dataToSend.add(new BasicNameValuePair("average_heart_rate", fitnessRecord.getAverageHeartRate()+""));
            dataToSend.add(new BasicNameValuePair("created_at",fitnessRecord.getCreateAt().getDateTime()));
            dataToSend.add(new BasicNameValuePair("updated_at", fitnessRecord.getUpdateAt().getDateTime()));
            System.out.println(fitnessRecord.getFitnessRecordID());
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "StoreFitnessRecord.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public class StoreHealthProfileDataAsyncTask extends  AsyncTask<Void,Void,Void>{
        HealthProfile healthProfile;

        public StoreHealthProfileDataAsyncTask(HealthProfile healthProfile){
            this.healthProfile = healthProfile;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", healthProfile.getHealthProfileID()));
            dataToSend.add(new BasicNameValuePair("user_id", healthProfile.getUserID()));
            dataToSend.add(new BasicNameValuePair("weight", healthProfile.getWeight()+""));
            dataToSend.add(new BasicNameValuePair("blood_pressure", healthProfile.getBloodPressure() + ""));
            dataToSend.add(new BasicNameValuePair("resting_heart_rate", healthProfile.getRestingHeartRate()+""));
            dataToSend.add(new BasicNameValuePair("arm_girth", healthProfile.getArmGirth() + ""));
            dataToSend.add(new BasicNameValuePair("chest_girth", healthProfile.getChestGirth()+""));
            dataToSend.add(new BasicNameValuePair("calf_girth", healthProfile.getCalfGirth() + ""));
            dataToSend.add(new BasicNameValuePair("thigh_girth", healthProfile.getThighGirth() + ""));
            dataToSend.add(new BasicNameValuePair("waist", healthProfile.getWaist() + ""));
            dataToSend.add(new BasicNameValuePair("hip", healthProfile.getHIP() + ""));
            dataToSend.add(new BasicNameValuePair("created_at", healthProfile.getRecordDateTime().getDateTime()));
            dataToSend.add(new BasicNameValuePair("updated_at", healthProfile.getUpdatedAt().getDateTime()));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "StoreHealthProfile.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
          }
    }

    public class UpdateHealthProfileDataAsyncTask extends  AsyncTask<Void,Void,Void>{
        HealthProfile healthProfile;

        public UpdateHealthProfileDataAsyncTask(HealthProfile healthProfile){
            this.healthProfile = healthProfile;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", healthProfile.getHealthProfileID()));
            dataToSend.add(new BasicNameValuePair("user_id", healthProfile.getUserID()));
            dataToSend.add(new BasicNameValuePair("weight", healthProfile.getWeight()+""));
            dataToSend.add(new BasicNameValuePair("blood_pressure", healthProfile.getBloodPressure() + ""));
            dataToSend.add(new BasicNameValuePair("resting_heart_rate", healthProfile.getRestingHeartRate()+""));
            dataToSend.add(new BasicNameValuePair("arm_girth", healthProfile.getArmGirth() + ""));
            dataToSend.add(new BasicNameValuePair("chest_girth", healthProfile.getChestGirth()+""));
            dataToSend.add(new BasicNameValuePair("calf_girth", healthProfile.getCalfGirth() + ""));
            dataToSend.add(new BasicNameValuePair("thigh_girth", healthProfile.getThighGirth() + ""));
            dataToSend.add(new BasicNameValuePair("waist", healthProfile.getWaist() + ""));
            dataToSend.add(new BasicNameValuePair("hip", healthProfile.getHIP() + ""));
            dataToSend.add(new BasicNameValuePair("created_at", healthProfile.getRecordDateTime().getDateTime()));
            dataToSend.add(new BasicNameValuePair("updated_at", healthProfile.getUpdatedAt().getDateTime()));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateHealthProfile.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }



    public class StoreFBUserDataAsyncTask extends  AsyncTask<Void,Void,Void>{
       UserProfile user;

        public StoreFBUserDataAsyncTask(UserProfile user){
            this.user = user;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("email", user.getEmail()));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
            dataToSend.add(new BasicNameValuePair("name", user.getName()));
            dataToSend.add(new BasicNameValuePair("dob", user.getDOB().getDateTime()));
            dataToSend.add(new BasicNameValuePair("gender", user.getGender()));
            dataToSend.add(new BasicNameValuePair("initial_weight", user.getInitial_Weight() + ""));
            dataToSend.add(new BasicNameValuePair("height", user.getHeight() + ""));
            dataToSend.add(new BasicNameValuePair("reward_point", user.getReward_Point() + ""));
            dataToSend.add(new BasicNameValuePair("created_at", user.getCreated_At().getDateTime()));
            dataToSend.add(new BasicNameValuePair("updated_at", user.getUpdated_At().getDateTime()));
            dataToSend.add(new BasicNameValuePair("image", DbBitmapUtility.encodeImagetoString((user.getBitmap()))));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        UserProfile user;
        GetUserCallBack userCallBack;

        public StoreUserDataAsyncTask(UserProfile user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("email", user.getEmail()));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
            dataToSend.add(new BasicNameValuePair("name", user.getName()));
            dataToSend.add(new BasicNameValuePair("dob", user.getDOB().getDateTime()));
            dataToSend.add(new BasicNameValuePair("gender", user.getGender()));
            dataToSend.add(new BasicNameValuePair("initial_weight", user.getInitial_Weight() + ""));
            dataToSend.add(new BasicNameValuePair("height", user.getHeight() + ""));
            dataToSend.add(new BasicNameValuePair("reward_point", user.getReward_Point() + ""));
            dataToSend.add(new BasicNameValuePair("created_at", user.getCreated_At().getDateTime()));
            dataToSend.add(new BasicNameValuePair("updated_at", user.getUpdated_At().getDateTime()));
            dataToSend.add(new BasicNameValuePair("image", DbBitmapUtility.encodeImagetoString((user.getBitmap()))));
            //System.out.println(user.getDOB());
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallBack.done(null);
            super.onPostExecute(aVoid);
        }
    }


    public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, UserProfile> {

        UserProfile user;
        GetUserCallBack userCallBack;

        public FetchUserDataAsyncTask(UserProfile user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected UserProfile doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("email", user.getEmail()));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
            System.out.println(user.getEmail() + user.getPassword());
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");

            UserProfile returnedUser = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpRespond = client.execute(post);

                HttpEntity entity = httpRespond.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                 if (jObject.length() == 0) {
                    returnedUser = null;
                } else {
                    String id = jObject.getString("id");
                    String name = jObject.getString("name");
                    String dob = jObject.getString("dob");
                    String gender = jObject.getString("gender");
                    Double weight = jObject.getDouble("weight");
                    Double height = jObject.getDouble("height");
                    int reward = jObject.getInt("reward");
                    String DOJ = jObject.getString("doj");
                     DateTime updatedAt;
                     if(jObject.getString("update")!=null){
                         updatedAt = new DateTime(jObject.getString("update"));
                     }else{
                         updatedAt = new DateTime(DOJ);
                         Toast.makeText(mContext, "Updated at is fail to get from server.", Toast.LENGTH_SHORT).show();
                     }
                     String image = jObject.getString("image");
                     byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                     Bitmap bitmap = DbBitmapUtility.getImage(decodedString);
                     returnedUser = new UserProfile(id, user.getEmail(), user.getPassword(), name, new DateTime(dob), gender, weight, height, reward, new DateTime(DOJ), updatedAt, bitmap);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnedUser;
        }

        @Override
        protected void onPostExecute(UserProfile returnedUser) {
            progressDialog.dismiss();
            userCallBack.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }




    public class FetchHealthProfileAsyncTask extends AsyncTask<Void, Void, List<HealthProfile>> {

        String user;

        public FetchHealthProfileAsyncTask(String userID) {
            this.user = userID;
        }

        @Override
        protected List<HealthProfile> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", user));
            System.out.println();
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserHealthProfile.php");

            HealthProfile returnedHealthProfile = null;
            List<HealthProfile> healthProfileList = new ArrayList<HealthProfile>();

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpRespond = client.execute(post);

                HttpEntity entity = httpRespond.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                JSONArray jArray = jObject.getJSONArray("health");

                System.out.println("*****JARRAY*****" + jArray.length());

                if (jObject.length() == 0) {
                    returnedHealthProfile = null;
                } else {
                    for(int i=0; i<jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        returnedHealthProfile = new HealthProfile();
                        returnedHealthProfile.setHealthProfileID(json_data.getString("health_profile_id"));
                        returnedHealthProfile.setUserID(json_data.getString("id"));
                        returnedHealthProfile.setWeight(json_data.getDouble("weight"));
                        returnedHealthProfile.setBloodPressure(json_data.getInt("blood_pressure"));
                        returnedHealthProfile.setRestingHeartRate(json_data.getInt("resting_heartrate"));
                        returnedHealthProfile.setArmGirth(json_data.getDouble("arm_girth"));
                        returnedHealthProfile.setChestGirth(json_data.getDouble("chest_girth"));
                        returnedHealthProfile.setCalfGirth(json_data.getDouble("calf_girth"));
                        returnedHealthProfile.setThighGirth(json_data.getDouble("thigh_girth"));
                        returnedHealthProfile.setWaist(json_data.getDouble("waist"));
                        returnedHealthProfile.setHIP(json_data.getDouble("hip"));
                        returnedHealthProfile.setRecordDateTime(new DateTime(json_data.getString("created_at")));
                        returnedHealthProfile.setUpdatedAt(new DateTime(json_data.getString("updated_at")));
                        healthProfileList.add(returnedHealthProfile);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return healthProfileList ;
        }

    }

    public class getRowCountBackground extends AsyncTask<Void,Void,String>{
        String countID;
        @Override
        protected String doInBackground(Void... params) {
            try{
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(SERVER_ADDRESS+"GetRowCount.php");
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                String result = EntityUtils.toString(httpEntity);
                JSONObject jObject = new JSONObject(result);
                if (jObject.length() == 0) {
                    return null;
                } else {
                    countID = jObject.getString("user_id");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return countID;
        }
    }


}

