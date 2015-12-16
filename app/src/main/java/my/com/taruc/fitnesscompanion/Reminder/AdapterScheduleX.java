package my.com.taruc.fitnesscompanion.Reminder;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.Reminder;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.UI.SchedulePage;

/**
 * Created by saiboon on 25/7/2015.
 */
public class AdapterScheduleX extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    Reminder tempValues=null;
    int i=0;

    /*************  AdapterScheduleNew Constructor *****************/
    public AdapterScheduleX(Activity a, ArrayList d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {
        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
        public TextView txtReminderActivity;
        public TextView txtRepeat;
        public TextView txtDay;
        public TextView txtTime;
        public Switch myToggleButton;
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.schedule_adapter_recycle_view, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/
            holder = new ViewHolder();
            holder.txtReminderActivity = (TextView) vi.findViewById(R.id.txtReminderActivity);
            holder.txtRepeat = (TextView)vi.findViewById(R.id.txtReminderRepeat);
            holder.txtDay = (TextView)vi.findViewById(R.id.txtReminderDay);
            holder.txtTime = (TextView)vi.findViewById(R.id.txtReminderTime);
            holder.myToggleButton = (Switch) vi.findViewById(R.id.myToggleButton);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0) {
            holder.txtRepeat.setText("No Data");
        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = (Reminder) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.txtReminderActivity.setText("Activity: "+tempValues.getActivitesPlanID() );
            holder.txtRepeat.setText("Repeat: "+tempValues.getRemindRepeat());
            if (tempValues.getRemindDay().equals("")){
                //holder.txtDay.setText("Day: --");
            }else {
                holder.txtDay.setText("Day: " + tempValues.getRemindDay());
            }
            holder.txtTime.setText("Time: " + String.format("%04d", Integer.parseInt(tempValues.getRemindTime())));
            if (tempValues.isAvailability()){
                holder.myToggleButton.setChecked(true);
                holder.txtDay.setText("On");
            }else{
                holder.myToggleButton.setChecked(false);
                holder.txtDay.setText("Off");
            }

            /******** Set Item Click Listner for LayoutInflater for each row *******/
            holder.myToggleButton.setOnCheckedChangeListener(new OnToggleButtonCheckedListener(position));
            vi.setOnLongClickListener(new OnItemLongClickListener(position));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("AdapterScheduleNew", "=====Row button clicked=====");
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

}
