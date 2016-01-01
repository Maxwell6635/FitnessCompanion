package my.com.taruc.fitnesscompanion.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import my.com.taruc.fitnesscompanion.Classes.Ranking;
import my.com.taruc.fitnesscompanion.R;

/**
 * Created by Hexa-Jackson Foo on 10/25/2015.
 */
public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.MyViewHolder> {
    LayoutInflater inflater;
    List<Ranking> data = Collections.emptyList();
    Context context;
    Integer ranking_no = 0;

    public RankingAdapter(Context context, List<Ranking> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_ranking, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Ranking current = data.get(position);
        if(current.getUserID() == ""){

        }
        ranking_no = position + 1;
        holder.ranking.setText("No. " + ranking_no);
        holder.name.setText(current.getUserID());
        holder.points.setText(current.getPoints().toString() + " Points");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ranking, name, points;

        public MyViewHolder(View itemView) {
            super(itemView);
            ranking = (TextView) itemView.findViewById(R.id.tv_ranking);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            points = (TextView) itemView.findViewById(R.id.tv_point);
        }

    }
}
