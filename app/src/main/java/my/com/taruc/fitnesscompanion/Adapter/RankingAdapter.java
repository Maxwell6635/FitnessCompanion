package my.com.taruc.fitnesscompanion.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import my.com.taruc.fitnesscompanion.Classes.Event;
import my.com.taruc.fitnesscompanion.Classes.Ranking;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.UI.ExercisePage;
import my.com.taruc.fitnesscompanion.UI.HistoryPage;
import my.com.taruc.fitnesscompanion.UI.RankingPage;
import my.com.taruc.fitnesscompanion.UI.SchedulePage;

/**
 * Created by Hexa-Jackson Foo on 10/25/2015.
 */
public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.MyViewHolder> {
    LayoutInflater inflater;
    List<Ranking> data = Collections.emptyList();
    Context context;
    Integer ranking_no = 0;
    private Activity activity;

    public RankingAdapter(Context context, List<Ranking> data, Activity activity) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_ranking, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Ranking current = data.get(position);
        if(current.getUserID() != "") {
            ranking_no = position + 1;
            holder.ranking.setText("No. " + ranking_no);
            holder.name.setText(current.getUserID());
            holder.points.setText(current.getPoints().toString() + " scores");
            if(current.getType().equalsIgnoreCase("Running")) {
                holder.challenge.setVisibility(View.VISIBLE);
                holder.challenge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        challenge(current);
                    }
                });
            }else{
                holder.challenge.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void challenge(Ranking current){
        RankingPage rankingPage = (RankingPage)activity;
        Bundle localBundle = new Bundle();
        localBundle.putString("FitnessRecordID", current.getFitnessRecordID());
        localBundle.putString("UserID", current.getUserID());
        Intent localIntent = new Intent(rankingPage, ExercisePage.class);
        localIntent.putExtras(localBundle);
        rankingPage.startActivity(localIntent);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ranking, name, points;
        Button challenge;

        public MyViewHolder(View itemView) {
            super(itemView);
            ranking = (TextView) itemView.findViewById(R.id.tv_ranking);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            points = (TextView) itemView.findViewById(R.id.tv_point);
            challenge = (Button) itemView.findViewById(R.id.challenge);
        }

    }
}
