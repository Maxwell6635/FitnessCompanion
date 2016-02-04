package my.com.taruc.fitnesscompanion.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Adapter.ActivityPlanAdapter;
import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Database.ActivityPlanDA;
import my.com.taruc.fitnesscompanion.R;

public class ActivityPlanPage extends Activity {

    @Bind(R.id.textViewTitle)
    TextView textViewTitle;
    @Bind(R.id.imageViewBackButton)
    ImageView imageViewBackButton;
    @Bind(R.id.RecycleViewCommonActivityPlan)
    RecyclerView RecycleViewCommonActivityPlan;

    ActivityPlanAdapter activityPlanAdapter;
    ActivityPlanDA myActivityPlanDA;
    ArrayList<ActivityPlan> activityPlanArrayList = new ArrayList<>();
    ArrayList<String> activityTypeArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_plan_page);
        ButterKnife.bind(this);

        //testing purpose
        //--------------------------
        /*myActivityPlanDA = new ActivityPlanDA(this);
        ActivityPlan sample = new ActivityPlan("P0006","247","recommend","Drive","Testing drive",1.0, 500, new DateTime().getCurrentDateTime(), new DateTime().getCurrentDateTime(), 20);
        myActivityPlanDA.addActivityPlan(sample);*/
        //--------------------------

        // get activity plan
        myActivityPlanDA = new ActivityPlanDA(this);
        activityPlanArrayList = myActivityPlanDA.getAllActivityPlan();
        activityTypeArrayList = myActivityPlanDA.getAllActivityPlanType();

        RecycleViewCommonActivityPlan.setLayoutManager(new LinearLayoutManager(this));
        activityPlanAdapter = new ActivityPlanAdapter(this, this, activityPlanArrayList, activityTypeArrayList);
        RecycleViewCommonActivityPlan.setAdapter(activityPlanAdapter);
    }

    public void BackAction(View view) {
        this.finish();
    }

}
