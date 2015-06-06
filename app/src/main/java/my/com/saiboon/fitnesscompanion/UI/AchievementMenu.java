package my.com.saiboon.fitnesscompanion.UI;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import my.com.saiboon.fitnesscompanion.R;


public class AchievementMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_achievement_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void GoMetalPage(View view){
        Intent intent = new Intent(this, MetalPage.class);
        startActivity(intent);
    }

    public void GoHistory(View view){
        Intent intent = new Intent(this, HistoryPage.class);
        startActivity(intent);
    }

    public void GoRanking(View view){
        Intent intent = new Intent(this, RankingPage.class);
        startActivity(intent);
    }
}
