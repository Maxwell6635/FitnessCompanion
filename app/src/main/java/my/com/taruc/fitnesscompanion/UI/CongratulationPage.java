package my.com.taruc.fitnesscompanion.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.Goal;
import my.com.taruc.fitnesscompanion.Database.GoalDA;
import my.com.taruc.fitnesscompanion.R;

public class CongratulationPage extends ActionBarActivity {

    @Bind(R.id.textViewCongratulation)
    TextView textViewCongratulation;
    @Bind(R.id.textViewCongratulationDetail)
    TextView textViewCongratulationDetail;

    Goal myGoal = new Goal();
    GoalDA goalDA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation_page);
        ButterKnife.bind(this);
        goalDA = new GoalDA(this);

        String goalID = getIntent().getStringExtra("GoalID");
        if(goalID!=null){
            myGoal = goalDA.getGoal(goalID);
            textViewCongratulationDetail.setText("Goal '" + myGoal.getGoalDescription()+ "' is achieved!");
        }
    }

}
