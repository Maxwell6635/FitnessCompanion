package my.com.taruc.fitnesscompanion.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.Event;
import my.com.taruc.fitnesscompanion.R;

/**
 * Created by saiboon on 25/1/2016.
 */
public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Event> eventArrayList;

    public EventAdapter(Context context, ArrayList<Event> eventArrayList){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.eventArrayList = eventArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.adapter_event,parent,false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        final Event event = eventArrayList.get(i);
        itemViewHolder.banner.setImageBitmap(event.getBanner());
        if(event.getTitle().length()>25){
            itemViewHolder.tv_title.setText(event.getTitle().substring(0,25) + "...");
        }else{
            itemViewHolder.tv_title.setText(event.getTitle());
        }
        itemViewHolder.tv_location.setText(event.getLocation());
        itemViewHolder.tv_date.setText(event.getEventDate());
        itemViewHolder.btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getUrl()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView banner;
        TextView tv_title, tv_location, tv_date;
        Button btn_join;

        public ItemViewHolder(View itemView) {
            super(itemView);
            banner = (ImageView)itemView.findViewById(R.id.imageViewBanner);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            btn_join = (Button) itemView.findViewById(R.id.btn_join);
        }
    }
}
