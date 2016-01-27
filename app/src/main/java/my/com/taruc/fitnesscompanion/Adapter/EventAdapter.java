package my.com.taruc.fitnesscompanion.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
        final ItemViewHolder ItemHolder = (ItemViewHolder) holder;
        final Event event = eventArrayList.get(i);
        ItemHolder.banner.setImageBitmap(event.getBanner());
        ItemHolder.banner.setOnClickListener(new View.OnClickListener() {
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
        public ItemViewHolder(View itemView) {
            super(itemView);
            banner = (ImageView)itemView.findViewById(R.id.imageViewBanner);
        }
    }
}
