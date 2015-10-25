package my.com.saiboon.fitnesscompanion.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import my.com.saiboon.fitnesscompanion.Adapter.RankingAdapter;
import my.com.saiboon.fitnesscompanion.R;


public class RankingPage extends ActionBarActivity {

    RecyclerView recyclerView;
    RankingAdapter rankingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_page);
        recyclerView = (RecyclerView) findViewById(R.id.listViewRanking);
        recyclerView.setHasFixedSize(true);


        /*ListView listView = (ListView) findViewById(R.id.listViewRanking);
        String[] values = new String[] { "1st   Tan Sai Boon    1500pt",
                "2nd   Tan Sai Boon    1200pt",
                "3rd   Tan Sai Boon    1000pt",
                "4th   Tan Sai Boon    980pt",
                "5th   Tan Sai Boon    700pt",
                "6th   Tan Sai Boon    400pt"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);*/
    }
}
