package my.com.taruc.fitnesscompanion.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.FitnessFormula;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Classes.RealTimeFitness;
import my.com.taruc.fitnesscompanion.Database.ActivityPlanDA;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.Database.RealTimeFitnessDA;
import my.com.taruc.fitnesscompanion.R;


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

    RealTimeFitnessDA realTimeFitnessDA;
    FitnessRecordDA fitnessRecordDA;
    ActivityPlanDA activityPlanDA;
    @Bind(R.id.textViewTitle)
    TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medal_page);
        ButterKnife.bind(this);
        textViewTitle.setText("Medal");

        realTimeFitnessDA = new RealTimeFitnessDA(this);
        fitnessRecordDA = new FitnessRecordDA(this);
        activityPlanDA = new ActivityPlanDA(this);

        //update walk
        if (getTotalWalkDistance() > 1) {
            imageViewWalk1km.setImageResource(R.drawable.medal_reached);
            if (getTotalWalkDistance() > 10) {
                imageViewWalk10km.setImageResource(R.drawable.medal_reached);
                if (getTotalWalkDistance() > 100) {
                    imageViewWalk100km.setImageResource(R.drawable.medal_reached);
                } else {
                    imageViewWalk100km.setImageResource(R.drawable.medal_unreached);
                }
            } else {
                imageViewWalk10km.setImageResource(R.drawable.medal_unreached);
            }
        } else {
            imageViewWalk1km.setImageResource(R.drawable.medal_unreached);
        }
        //update run
        if (getTotalRunDistance() > 1) {
            imageViewRun1km.setImageResource(R.drawable.medal_reached);
            if (getTotalRunDistance() > 10) {
                imageViewRun10km.setImageResource(R.drawable.medal_reached);
                if (getTotalRunDistance() > 100) {
                    imageViewRun100km.setImageResource(R.drawable.medal_reached);
                } else {
                    imageViewRun100km.setImageResource(R.drawable.medal_unreached);
                }
            } else {
                imageViewRun10km.setImageResource(R.drawable.medal_unreached);
            }
        } else {
            imageViewRun1km.setImageResource(R.drawable.medal_unreached);
        }
        //update cycle
        if (getCycleDistance() > 1) {
            imageViewRide1km.setImageResource(R.drawable.medal_reached);
            if (getCycleDistance() > 10) {
                imageViewRide10km.setImageResource(R.drawable.medal_reached);
                if (getCycleDistance() > 100) {
                    imageViewRide100km.setImageResource(R.drawable.medal_reached);
                } else {
                    imageViewRide100km.setImageResource(R.drawable.medal_unreached);
                }
            } else {
                imageViewRide10km.setImageResource(R.drawable.medal_unreached);
            }
        } else {
            imageViewRide1km.setImageResource(R.drawable.medal_unreached);
        }
        //update hike
        if (getHikeHour() > 1) {
            imageViewHike1hr.setImageResource(R.drawable.medal_reached);
            if (getHikeHour() > 10) {
                imageViewHike10hr.setImageResource(R.drawable.medal_reached);
                if (getHikeHour() > 100) {
                    imageViewHike100hr.setImageResource(R.drawable.medal_reached);
                } else {
                    imageViewHike100hr.setImageResource(R.drawable.medal_unreached);
                }
            } else {
                imageViewHike10hr.setImageResource(R.drawable.medal_unreached);
            }
        } else {
            imageViewHike1hr.setImageResource(R.drawable.medal_unreached);
        }

    }


    public void BackAction(View view) {
        this.finish();
    }

    public double getTotalWalkDistance() {
        int totalWalkStep = 0;
        FitnessFormula fitnessFormula = new FitnessFormula(this);
        RealTimeFitness realTimeFitness = new RealTimeFitness();
        ArrayList<RealTimeFitness> realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitness();
        for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
            realTimeFitness = realTimeFitnessArrayList.get(i);
            if (realTimeFitness.isWalking()) {
                totalWalkStep += realTimeFitness.getStepNumber();
            }
        }
        return (fitnessFormula.getDistance(totalWalkStep) / 1000.0);
    }

    public double getTotalRunDistance() {
        int totalRunStep = 0;
        FitnessFormula fitnessFormula = new FitnessFormula(this);
        RealTimeFitness realTimeFitness = new RealTimeFitness();
        ArrayList<RealTimeFitness> realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitness();
        for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
            realTimeFitness = realTimeFitnessArrayList.get(i);
            if (realTimeFitness.isRunning()) {
                totalRunStep += realTimeFitness.getStepNumber();
            }
        }
        return (fitnessFormula.getDistance(totalRunStep) / 1000.0);
    }

    public double getCycleDistance() {
        double distance = 0;
        FitnessRecord fitnessRecord = new FitnessRecord();
        ArrayList<FitnessRecord> fitnessRecordArrayList = fitnessRecordDA.getAllFitnessRecord();
        for (int i = 0; i < fitnessRecordArrayList.size(); i++) {
            fitnessRecord = fitnessRecordArrayList.get(i);
            String ActivityName = activityPlanDA.getActivityPlan(fitnessRecord.getActivityPlanID()).getActivityName();
            if (ActivityName.equalsIgnoreCase("Cycling") || ActivityName.equalsIgnoreCase("Cycle")) {
                distance += fitnessRecord.getRecordDistance();
            }
        }
        return distance / 1000.0;
    }

    public int getHikeHour() {
        int seconds = 0;
        FitnessRecord fitnessRecord = new FitnessRecord();
        ArrayList<FitnessRecord> fitnessRecordArrayList = fitnessRecordDA.getAllFitnessRecord();
        for (int i = 0; i < fitnessRecordArrayList.size(); i++) {
            fitnessRecord = fitnessRecordArrayList.get(i);
            String ActivityName = activityPlanDA.getActivityPlan(fitnessRecord.getActivityPlanID()).getActivityName();
            if (ActivityName.equalsIgnoreCase("Hiking") || ActivityName.equalsIgnoreCase("Hike")) {
                seconds += fitnessRecord.getRecordDuration();
            }
        }
        return (int) seconds / 3600;
    }

}
