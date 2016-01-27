package my.com.taruc.fitnesscompanion.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Adapter.EventAdapter;
import my.com.taruc.fitnesscompanion.Classes.Event;
import my.com.taruc.fitnesscompanion.Database.EventDA;
import my.com.taruc.fitnesscompanion.R;

public class EventPage extends ActionBarActivity {

    @Bind(R.id.textViewTitle)
    TextView textViewTitle;
    @Bind(R.id.imageViewBackButton)
    ImageView imageViewBackButton;
    @Bind(R.id.RecyclerViewEvent)
    RecyclerView RecyclerViewEvent;

    EventAdapter eventAdapter;
    EventDA eventDA;
    ArrayList<Event> eventArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        ButterKnife.bind(this);

        //do server request for event  here ?

        eventDA = new EventDA(this);
        eventArrayList = eventDA.getAllEvent();

        RecyclerViewEvent.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(this, eventArrayList);
        RecyclerViewEvent.setAdapter(eventAdapter);
    }

    public void BackAction(View view) {
        this.finish();
    }

}
