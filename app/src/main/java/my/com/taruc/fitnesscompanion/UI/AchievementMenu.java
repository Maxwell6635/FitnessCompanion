package my.com.taruc.fitnesscompanion.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Graph.MyExerciseGraphView;
import my.com.taruc.fitnesscompanion.R;


public class AchievementMenu extends ActionBarActivity {

    @Bind(R.id.textViewTitle)
    TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_menu);
        ButterKnife.bind(this);

        textViewTitle.setText("Achievement");
    }

    public void GoMedalPage(View view) {
        Intent intent = new Intent(this, MedalPage.class);
        startActivity(intent);
    }

    public void GoHistory(View view) {
        Intent intent = new Intent(this, MyExerciseGraphView.class);
        startActivity(intent);
    }

    public void GoRanking(View view) {
        Intent intent = new Intent(this, RankingPage.class);
        startActivity(intent);
    }

    public void GoEvent(View view) {
        Intent intent = new Intent(this, EventPage.class);
        startActivity(intent);
    }

    public void BackAction(View view) {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = FitnessApplication.getRefWatcher(this);
//        refWatcher.watch(this);
    }

}
