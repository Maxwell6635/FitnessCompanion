package my.com.saiboon.fitnesscompanion.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import my.com.saiboon.fitnesscompanion.R;


public class AchievementMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_menu);
    }

    public void GoMetalPage(View view){
        Toast.makeText(this,"Phase 2",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MetalPage.class);
        startActivity(intent);
    }

    public void GoHistory(View view){
        Intent intent = new Intent(this, HistoryPage.class);
        startActivity(intent);
    }

    public void GoRanking(View view){
        Toast.makeText(this, "Phase 2", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, RankingPage.class);
        startActivity(intent);
    }

    public void GoEvent(View view){
        Toast.makeText(this, "Phase 2", Toast.LENGTH_LONG).show();
    }
}
