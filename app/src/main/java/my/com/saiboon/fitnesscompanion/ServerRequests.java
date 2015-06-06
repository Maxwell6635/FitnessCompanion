
package my.com.saiboon.fitnesscompanion;

import android.app.ProgressDialog;
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

import my.com.saiboon.fitnesscompanion.GetUserCallback;

/**
 * Created by JACKSON on 5/26/2015.
 */
public class ServerRequests {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://fitnesscompanion.net16.net/";
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
                    String name = jObject.getString("name");
                    String dob = jObject.getString("dob");
                    String gender = jObject.getString("gender");
                    Double height = jObject.getDouble("height");
                    Double weight = jObject.getDouble("weight");
                    int age = jObject.getInt("age");
                    String DOJ = jObject.getString("doj");
                    int reward = jObject.getInt("reward");
                    returnedUser = new UserProfile(user.email, name, dob, age, gender, height, weight, user.password, DOJ, reward);
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
}

