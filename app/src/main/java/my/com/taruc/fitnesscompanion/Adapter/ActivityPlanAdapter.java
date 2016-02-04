package my.com.taruc.fitnesscompanion.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Database.ActivityPlanDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.UI.ActivityPlanPage;
import my.com.taruc.fitnesscompanion.UI.ExercisePage;
import my.com.taruc.fitnesscompanion.UI.MedalPage;
import my.com.taruc.fitnesscompanion.UI.RankingPage;

/**
 * Created by saiboon on 31/1/2016.
 */
public class ActivityPlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private LayoutInflater inflater;
    private Context context;
    ArrayList<ActivityPlan> activityPlanArrayList = new ArrayList<>();
    ArrayList<Integer> headerPosition = new ArrayList<>();
    ArrayList<String> TypeValue = new ArrayList<>();
    ActivityPlanDA activityPlanDA;
    int tempPositionIndex = 0;
    private Activity activity;

    boolean noHeader = true;
    int index = 0;
    final int TYPE_HEADER = 0;
    final int TYPE_ITEM = 1;

    public ActivityPlanAdapter(Context context, Activity activity, ArrayList<ActivityPlan> activityPlanArrayList, ArrayList<String> TypeValue){
        inflater = LayoutInflater.from(context);
        this.activity = activity;
        this.context = context;
        activityPlanDA = new ActivityPlanDA(context);
        this.activityPlanArrayList = activityPlanArrayList;
        this.TypeValue = TypeValue;

        //retrieve header position index
        int j = 0;
        for(int i=0; i<activityPlanArrayList.size()+TypeValue.size(); i++){
            if (i == 0) {
                //header
                headerPosition.add(tempPositionIndex);
                tempPositionIndex++;
            } else if (j>0) {
                if ((!activityPlanArrayList.get(j).getType().equals(activityPlanArrayList.get(j - 1).getType())) && noHeader) {
                    //header
                    headerPosition.add(tempPositionIndex);
                    tempPositionIndex++;
                    noHeader = false;
                }else{
                    tempPositionIndex++;
                    j++;
                    noHeader = true;
                }
            } else{
                tempPositionIndex++;
                j++;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        if(i==TYPE_HEADER) {
            View view = inflater.inflate(R.layout.adapter_activity_plan_header, parent, false);
            HeaderViewHolder holder = new HeaderViewHolder(view);
            return holder;
        }else{
            View view = inflater.inflate(R.layout.adapter_activity_plan_item, parent, false);
            ItemViewHolder holder = new ItemViewHolder(view);
            holder.itemView.setOnClickListener(new PlanOnClickListener(i,index));
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder HeaderHolder = (HeaderViewHolder) holder;
            HeaderHolder.subTitle.setText(activityPlanArrayList.get(index).getType());
        } else {
            ItemViewHolder ItemHolder = (ItemViewHolder) holder;
            if (activityPlanArrayList.get(index).getType().equalsIgnoreCase("common")) {
                ItemHolder.smallIcon.setImageResource(R.drawable.icon_common);
            } else {
                ItemHolder.smallIcon.setImageResource(R.drawable.icon_recommend);
            }
            ItemHolder.detail.setText(activityPlanArrayList.get(index).getActivityName() + "\n"
                    + "Description: " + activityPlanArrayList.get(index).getDescription() + "\n"
                    + "Suggested Duration: " + activityPlanArrayList.get(index).getDuration() + "min\n"
                    + "Calories burn/min: " + activityPlanArrayList.get(index).getEstimateCalories() + "\n");
            index++;
        }
    }

    @Override
    public int getItemCount() {
        return activityPlanArrayList.size() + TypeValue.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(isHeader(position)){
            return TYPE_HEADER;
        }else{
            return TYPE_ITEM;
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder{
        TextView subTitle;
        public HeaderViewHolder(View headerView){
            super(headerView);
            subTitle = (TextView)headerView.findViewById(R.id.textViewTitleCaption);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView smallIcon;
        TextView detail;
        public ItemViewHolder(View itemView) {
            super(itemView);
            smallIcon = (ImageView)itemView.findViewById(R.id.smallIcon);
            detail = (TextView)itemView.findViewById(R.id.activityPlanDetail);
        }
    }

    private class PlanOnClickListener implements View.OnClickListener{
        private int position;
        private int clickIndex;
        PlanOnClickListener(int i, int index){
            position = i;
            clickIndex = index;
        }
        @Override
        public void onClick(View v) {
            if(!isHeader(position)){
                ActivityPlanPage activityPlanPage = (ActivityPlanPage)activity;
                Bundle localBundle = new Bundle();
                localBundle.putString("ActivityPlanID", activityPlanArrayList.get(clickIndex).getActivityPlanID());
                Intent localIntent = new Intent(activityPlanPage, ExercisePage.class);
                localIntent.putExtras(localBundle);
                activityPlanPage.startActivity(localIntent);
            }
        }
    }

    public boolean isHeader(int position){
        if(headerPosition.contains(position)) {
            return true;
        }else{
            return false;
        }
    }
}
