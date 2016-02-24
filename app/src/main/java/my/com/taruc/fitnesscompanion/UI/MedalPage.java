package my.com.taruc.fitnesscompanion.UI;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.Achievement;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.FitnessFormula;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Classes.RealTimeFitness;
import my.com.taruc.fitnesscompanion.Database.AchievementDA;
import my.com.taruc.fitnesscompanion.Database.ActivityPlanDA;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.Database.RealTimeFitnessDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.UserLocalStore;


public class MedalPage extends ActionBarActivity {

    @Bind(R.id.textViewWalk)
    TextView textViewWalk;
    @Bind(R.id.imageViewWalk1km)
    ImageView imageViewWalk1km;
    @Bind(R.id.imageViewWalk10km)
    ImageView imageViewWalk10km;
    @Bind(R.id.imageViewWalk100km)
    ImageView imageViewWalk100km;
    @Bind(R.id.txtViewWalk1km)
    TextView txtViewWalk1km;
    @Bind(R.id.txtViewWalk10km)
    TextView txtViewWalk10km;
    @Bind(R.id.txtViewWalk100km)
    TextView txtViewWalk100km;
    @Bind(R.id.TableMetalWalk)
    TableLayout TableMetalWalk;
    @Bind(R.id.textViewRun)
    TextView textViewRun;
    @Bind(R.id.imageViewRun1km)
    ImageView imageViewRun1km;
    @Bind(R.id.imageViewRun10km)
    ImageView imageViewRun10km;
    @Bind(R.id.imageViewRun100km)
    ImageView imageViewRun100km;
    @Bind(R.id.txtViewRun1km)
    TextView txtViewRun1km;
    @Bind(R.id.txtViewRun10km)
    TextView txtViewRun10km;
    @Bind(R.id.txtViewRun100km)
    TextView txtViewRun100km;
    @Bind(R.id.TableMetalRun)
    TableLayout TableMetalRun;
    @Bind(R.id.textViewCycle)
    TextView textViewCycle;
    @Bind(R.id.imageViewRide1km)
    ImageView imageViewRide1km;
    @Bind(R.id.imageViewRide10km)
    ImageView imageViewRide10km;
    @Bind(R.id.imageViewRide100km)
    ImageView imageViewRide100km;
    @Bind(R.id.txtViewRide1km)
    TextView txtViewRide1km;
    @Bind(R.id.txtViewRide10km)
    TextView txtViewRide10km;
    @Bind(R.id.txtViewRide100km)
    TextView txtViewRide100km;
    @Bind(R.id.TableMetalRide)
    TableLayout TableMetalRide;
    @Bind(R.id.textViewHike)
    TextView textViewHike;
    @Bind(R.id.imageViewHike1hr)
    ImageView imageViewHike1hr;
    @Bind(R.id.imageViewHike10hr)
    ImageView imageViewHike10hr;
    @Bind(R.id.imageViewHike100hr)
    ImageView imageViewHike100hr;
    @Bind(R.id.txtViewHike1km)
    TextView txtViewHike1km;
    @Bind(R.id.txtViewHike10km)
    TextView txtViewHike10km;
    @Bind(R.id.txtViewHike100km)
    TextView txtViewHike100km;
    @Bind(R.id.TableMetalHike)
    TableLayout TableMetalHike;
    @Bind(R.id.ScrollView01)
    ScrollView ScrollView01;
    @Bind(R.id.textViewTitle)
    TextView textViewTitle;

    RealTimeFitnessDA realTimeFitnessDA;
    FitnessRecordDA fitnessRecordDA;
    ActivityPlanDA activityPlanDA;
    AchievementDA achievementDA;

    double totalCycleDistance = 0;
    int totalHikeSec = 0;
    double totalWalkDistance = 0;
    double totalRunDistance = 0;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medal_page);
        ButterKnife.bind(this);
        textViewTitle.setText(R.string.medal);

        realTimeFitnessDA = new RealTimeFitnessDA(this);
        fitnessRecordDA = new FitnessRecordDA(this);
        activityPlanDA = new ActivityPlanDA(this);
        achievementDA = new AchievementDA(this);

        progress = ProgressDialog.show(this, "Loading Medal", "Connecting....Please Wait.", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    updateUI();
                }catch (Exception ex){
                    Log.i("Expected exception","Change medal ui");
                }
                progress.dismiss();
            }
        }).start();
    }

    public void BackAction(View view) {
        this.finish();
    }

    public void updateUI(){
        //calculation
        RealTimeFormula();
        FitnessRecordFormula();

        //update walk
        if (totalWalkDistance > 100) {
            loadBitmap(imageViewWalk100km);
            loadBitmap(imageViewWalk10km);
            loadBitmap(imageViewWalk1km);
        }else if (totalWalkDistance > 10) {
            loadBitmap(imageViewWalk10km);
            loadBitmap(imageViewWalk1km);
        }else if (totalWalkDistance > 1) {
            loadBitmap(imageViewWalk1km);
        }

        //update run
        if (totalRunDistance > 100) {
            loadBitmap(imageViewRun100km);
            loadBitmap(imageViewRun10km);
            loadBitmap(imageViewRun1km);
        }else if(totalRunDistance > 10){
            loadBitmap(imageViewRun10km);
            loadBitmap(imageViewRun1km);
        }else if (totalRunDistance > 1){
            loadBitmap(imageViewRun1km);
        }

        //update cycle
        if (totalCycleDistance > 100) {
            loadBitmap(imageViewRide100km);
            loadBitmap(imageViewRide10km);
            loadBitmap(imageViewRide1km);
        }else if (totalCycleDistance > 10) {
            loadBitmap(imageViewRide10km);
            loadBitmap(imageViewRide1km);
        }else if (totalCycleDistance > 1) {
            loadBitmap(imageViewRide1km);
        }

        //update hike
        int totalHikeHour = totalHikeSec / 3600;
        if (totalHikeHour > 100) {
            loadBitmap(imageViewHike100hr);
            loadBitmap(imageViewHike10hr);
            loadBitmap(imageViewHike1hr);
        }else if (totalHikeHour > 10) {
            loadBitmap(imageViewHike10hr);
            loadBitmap(imageViewHike1hr);
        }else  if (totalHikeHour > 1) {
            loadBitmap(imageViewHike1hr);
        }
    }

    public void loadBitmap(ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(R.drawable.medal_reached);
    }

    public void RealTimeFormula() {
        ArrayList<Double> doubleArrayList = realTimeFitnessDA.getTotalDistancesFromRealTimeFitness();
        if(doubleArrayList.size()!=2){
            Log.i("Medal Err", "Real Time DA get zero double array list");
        }else{
            totalWalkDistance = doubleArrayList.get(0);
            totalRunDistance = doubleArrayList.get(1);
        }
    }

    private void FitnessRecordFormula(){
        ArrayList<Double> doubleArrayList = fitnessRecordDA.getTotalValuesFromRealTimeFitness();
        if(doubleArrayList.size()!=2){
            Log.i("Medal Err","Fitness Record DA get zero double array list");
        }else{
            totalCycleDistance = doubleArrayList.get(0);
            totalHikeSec = doubleArrayList.get(1).intValue();
        }
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            //Bitmap Icon = BitmapFactory.decodeResource(getResources(), R.drawable.medal_reached);
            return decodeSampledBitmapFromResource(getResources(), data, 100, 100);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                             int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }

        public int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }
    }
}
