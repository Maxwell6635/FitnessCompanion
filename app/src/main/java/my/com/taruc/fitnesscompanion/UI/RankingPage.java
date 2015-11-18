package my.com.taruc.fitnesscompanion.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Adapter.RankingAdapter;
import my.com.taruc.fitnesscompanion.Classes.Ranking;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.ServerRequests;


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

    }
}
