package my.com.taruc.fitnesscompanion.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.squareup.leakcanary.RefWatcher;

import my.com.taruc.fitnesscompanion.FitnessApplication;
import my.com.taruc.fitnesscompanion.Graph.MyRealTimeGraphView;
import my.com.taruc.fitnesscompanion.R;


public class AchievementMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_menu);
    }

    public void GoMetalPage(View view){
        Intent intent = new Intent(this, MetalPage.class);
        startActivity(intent);
    }

    public void GoHistory(View view){
        Intent intent = new Intent(this, MyRealTimeGraphView.class);
        startActivity(intent);
    }

    public void GoRanking(View view){
        Intent intent = new Intent(this, RankingPage.class);
        startActivity(intent);
    }

    public void GoEvent(View view){
        Intent intent = new Intent(this, EventPage.class);
        startActivity(intent);
    }

    public void BackAction(View view) {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = FitnessApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

}
