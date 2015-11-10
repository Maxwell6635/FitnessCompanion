package my.com.saiboon.fitnesscompanion.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import my.com.saiboon.fitnesscompanion.Adapter.RankingAdapter;
import my.com.saiboon.fitnesscompanion.Classes.Ranking;
import my.com.saiboon.fitnesscompanion.R;
import my.com.saiboon.fitnesscompanion.ServerRequests;


public class RankingPage extends ActionBarActivity {

    RecyclerView recyclerView;
    RankingAdapter rankingAdapter;
    ArrayList<Ranking> rankingArrayList;
    ServerRequests serverRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_page);
        recyclerView = (RecyclerView) findViewById(R.id.listViewRanking);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        serverRequests = new ServerRequests(getApplicationContext());
        rankingArrayList = new ArrayList<>();

        rankingArrayList = serverRequests.fetchRankingDataInBackground();
        rankingAdapter =  new RankingAdapter(getApplicationContext(),rankingArrayList);
        recyclerView.setAdapter(rankingAdapter);


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
