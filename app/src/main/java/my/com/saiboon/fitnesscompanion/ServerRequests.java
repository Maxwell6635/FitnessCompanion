
package my.com.saiboon.fitnesscompanion;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
import org.json.JSONObject;

import java.util.ArrayList;

import my.com.saiboon.fitnesscompanion.Classes.FitnessRecord;
import my.com.saiboon.fitnesscompanion.Classes.HealthProfile;
import my.com.saiboon.fitnesscompanion.Classes.Ranking;
import my.com.saiboon.fitnesscompanion.Classes.RealTimeFitness;
import my.com.saiboon.fitnesscompanion.GetUserCallback;

/**
 * Created by JACKSON on 5/26/2015.
 */
public class ServerRequests {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    //public static final String SERVER_ADDRESS = "http://fitnesscompanion.net16.net/";
    public static final String SERVER_ADDRESS = "http://fitnesscompanion.freeoda.com/";
    ProgressDialog progressDialog;

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(UserProfile user, GetUserCallback userCallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallBack).execute();
    }

    public void fetchUserDataInBackground(UserProfile user, GetUserCallback callBack) {
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
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            ArrayList<Ranking> rankingArrayList = new ArrayList<Ranking>();

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchRankingData.php");

            Ranking ranking = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpRespond = client.execute(post);

                HttpEntity entity = httpRespond.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);


                if (jObject.length() == 0) {
                   ranking = null;
                } else {
                    Integer rank = jObject.getInt("ranking_no");
                    String name = jObject.getString("name");
                    Integer points  = jObject.getInt("points");
                    ranking = new Ranking(rank,name,points);
                    rankingArrayList.add(ranking);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rankingArrayList;
        }

    }

    public class StoreRealTimeFitnessDataAsyncTask extends  AsyncTask<Void,Void,Void>{
       RealTimeFitness realTimeFitness;

        public StoreRealTimeFitnessDataAsyncTask(RealTimeFitness realTimeFitness){
            this.realTimeFitness = realTimeFitness;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("realtimefitnessID", realTimeFitness.getRealTimeFitnessID()));
            dataToSend.add(new BasicNameValuePair("stepNumber", String.valueOf(realTimeFitness.getStepNumber())));
            dataToSend.add(new BasicNameValuePair("Time", realTimeFitness.getCaptureDateTime().toString()));
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
            dataToSend.add(new BasicNameValuePair("fitnessRecordID", fitnessRecord.getFitnessRecordID()));
            dataToSend.add(new BasicNameValuePair("userID", fitnessRecord.getUserID()));
            dataToSend.add(new BasicNameValuePair("fitnessActivity", fitnessRecord.getFitnessActivity()));
            dataToSend.add(new BasicNameValuePair("recordDuration", fitnessRecord.getRecordDuration()+""));
            dataToSend.add(new BasicNameValuePair("recordDistance", fitnessRecord.getRecordDistance() + ""));
            dataToSend.add(new BasicNameValuePair("recordCalories", fitnessRecord.getRecordCalories()+""));
            dataToSend.add(new BasicNameValuePair("recordStep", fitnessRecord.getRecordStep() + ""));
            dataToSend.add(new BasicNameValuePair("HR", fitnessRecord.getAverageHeartRate()+""));
            dataToSend.add(new BasicNameValuePair("Time",fitnessRecord.getFitnessRecordDateTime()));
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
            dataToSend.add(new BasicNameValuePair("healthprofileid", healthProfile.getHealthProfileID()));
            dataToSend.add(new BasicNameValuePair("userID", healthProfile.getUserID()));
            dataToSend.add(new BasicNameValuePair("weight", healthProfile.getWeight()+""));
            dataToSend.add(new BasicNameValuePair("BP", healthProfile.getBloodPressure() + ""));
            dataToSend.add(new BasicNameValuePair("RHR", healthProfile.getRestingHeartRate()+""));
            dataToSend.add(new BasicNameValuePair("ArmG", healthProfile.getArmGirth() + ""));
            dataToSend.add(new BasicNameValuePair("ChestG", healthProfile.getChestGirth()+""));
            dataToSend.add(new BasicNameValuePair("CalfG", healthProfile.getCalfGirth() + ""));
            dataToSend.add(new BasicNameValuePair("ThighG", healthProfile.getThighGirth() + ""));
            dataToSend.add(new BasicNameValuePair("Waist", healthProfile.getWaist() + ""));
            dataToSend.add(new BasicNameValuePair("HIP", healthProfile.getHIP() + ""));
            dataToSend.add(new BasicNameValuePair("Time", healthProfile.getRecordDateTime()));
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
            dataToSend.add(new BasicNameValuePair("healthprofileid", healthProfile.getHealthProfileID()));
            dataToSend.add(new BasicNameValuePair("userID", healthProfile.getUserID()));
            dataToSend.add(new BasicNameValuePair("weight", healthProfile.getWeight()+""));
            dataToSend.add(new BasicNameValuePair("BP", healthProfile.getBloodPressure() + ""));
            dataToSend.add(new BasicNameValuePair("RHR", healthProfile.getRestingHeartRate()+""));
            dataToSend.add(new BasicNameValuePair("ArmG", healthProfile.getArmGirth() + ""));
            dataToSend.add(new BasicNameValuePair("ChestG", healthProfile.getChestGirth()+""));
            dataToSend.add(new BasicNameValuePair("CalfG", healthProfile.getCalfGirth() + ""));
            dataToSend.add(new BasicNameValuePair("ThighG", healthProfile.getThighGirth() + ""));
            dataToSend.add(new BasicNameValuePair("Waist", healthProfile.getWaist() + ""));
            dataToSend.add(new BasicNameValuePair("HIP", healthProfile.getHIP() + ""));
            dataToSend.add(new BasicNameValuePair("Time", healthProfile.getRecordDateTime()));
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
            dataToSend.add(new BasicNameValuePair("name", user.getName()));
            dataToSend.add(new BasicNameValuePair("dob", user.getDOB()));
            dataToSend.add(new BasicNameValuePair("age", user.getAge() + ""));
            dataToSend.add(new BasicNameValuePair("gender", user.getGender()));
            dataToSend.add(new BasicNameValuePair("height", user.getHeight() + ""));
            dataToSend.add(new BasicNameValuePair("weight", user.getWeight() + ""));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
            dataToSend.add(new BasicNameValuePair("doj", user.DOJ));
            dataToSend.add(new BasicNameValuePair("reward", user.reward + ""));
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
        GetUserCallback userCallBack;

        public StoreUserDataAsyncTask(UserProfile user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("email", user.getEmail()));
            dataToSend.add(new BasicNameValuePair("name", user.getName()));
            dataToSend.add(new BasicNameValuePair("dob", user.getDOB()));
            dataToSend.add(new BasicNameValuePair("age", user.getAge() + ""));
            dataToSend.add(new BasicNameValuePair("gender", user.getGender()));
            dataToSend.add(new BasicNameValuePair("height", user.getHeight() + ""));
            dataToSend.add(new BasicNameValuePair("weight", user.getWeight() + ""));
            dataToSend.add(new BasicNameValuePair("password", user.getPassword()));
            dataToSend.add(new BasicNameValuePair("doj", user.DOJ));
            dataToSend.add(new BasicNameValuePair("reward", user.reward + ""));
            System.out.println(user.age);
            System.out.println(user.getDOB());
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
        GetUserCallback userCallBack;

        public FetchUserDataAsyncTask(UserProfile user, GetUserCallback userCallBack) {
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
                    Double height = jObject.getDouble("height");
                    Double weight = jObject.getDouble("weight");
                    int age = jObject.getInt("age");
                    String DOJ = jObject.getString("doj");
                    int reward = jObject.getInt("reward");
                    returnedUser = new UserProfile(id,user.email, name, dob, age, gender, height, weight, user.password, DOJ, reward);
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

