package my.com.taruc.fitnesscompanion.Reminder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.Reminder;
import my.com.taruc.fitnesscompanion.Database.ReminderDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.UI.SchedulePage;

/**
 * Created by saiboon on 16/12/2015.
 */
public class AdapterScheduleRecycleView extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private LayoutInflater inflater;
    private Context context;
    private String adapterString;

    private Activity activity;
    private ArrayList<Reminder> reminderArrayList;

    public AdapterScheduleRecycleView(Context context, Activity activity, ArrayList<Reminder> reminderArrayList){
        inflater = LayoutInflater.from(context);
        this.context = context;

        this.activity = activity;
        this.reminderArrayList = reminderArrayList;
    }

    public void setData(String dietPlanArrayList){
        this.adapterString = dietPlanArrayList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.schedule_adapter_recycle_view,parent,false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        final ItemViewHolder ItemHolder = (ItemViewHolder) holder;

        ItemHolder.txtReminderActivity.setText("Activity: " + reminderArrayList.get(i).getActivitesPlanID());
        ItemHolder.txtRepeat.setText("Repeat: " + reminderArrayList.get(i).getRemindRepeat());
        ItemHolder.txtTime.setText("Time: " + String.format( "%04d", Integer.parseInt( reminderArrayList.get(i).getRemindTime() ) ) );

        if (!reminderArrayList.get(i).getRemindDay().equals("")){
            ItemHolder.txtDay.setText("Day: " + reminderArrayList.get(i).getRemindDay());
        }else{
            ItemHolder.txtDay.setText("Day: --");
        }

        if (reminderArrayList.get(i).isAvailability()){
            ItemHolder.myToggleButton.setChecked(true);
        }else{
            ItemHolder.myToggleButton.setChecked(false);
        }

        ItemHolder.myToggleButton.setOnCheckedChangeListener(new OnToggleButtonCheckedListener(i));

        /******** Set Item Click Listner for LayoutInflater for each row *******/
        ItemHolder.itemView.setOnLongClickListener(new OnItemLongClickListener(i));
    }

    /********* Called when Item click in ListView ************/
    private class OnItemLongClickListener implements View.OnLongClickListener{
        private int mPosition;
        OnItemLongClickListener(int position){
            mPosition = position;
        }
        @Override
        public boolean onLongClick(View arg0) {
            SchedulePage sct = (SchedulePage)activity;
            sct.onItemLongClick(mPosition);
            return false;
        }
    }

    /********* Switch listener ************/
    private class OnToggleButtonCheckedListener implements CompoundButton.OnCheckedChangeListener {
        private int mPosition;
        OnToggleButtonCheckedListener(int position){
            mPosition = position;
        }
        SchedulePage sct = (SchedulePage)activity;
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                //Toast.makeText(activity,"Changed to true",Toast.LENGTH_SHORT).show();
                sct.toggleButtonStatusChange(mPosition, true);
            } else {
                //Toast.makeText(activity,"Changed to false",Toast.LENGTH_SHORT).show();
                sct.toggleButtonStatusChange(mPosition, false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return reminderArrayList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView txtReminderActivity, txtDay, txtRepeat, txtTime;
        ToggleButton myToggleButton;

        public ItemViewHolder(View itemView) {
            super(itemView);

            txtReminderActivity = (TextView) itemView.findViewById(R.id.txtReminderActivity);
            txtRepeat = (TextView) itemView.findViewById(R.id.txtReminderRepeat);
            txtDay = (TextView) itemView.findViewById(R.id.txtReminderDay);
            txtTime = (TextView) itemView.findViewById(R.id.txtReminderTime);
            myToggleButton = (ToggleButton) itemView.findViewById(R.id.myToggleButton);

        }
    }
}
