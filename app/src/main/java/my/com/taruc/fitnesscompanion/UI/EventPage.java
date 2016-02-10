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
import my.com.taruc.fitnesscompanion.ServerAPI.RetrieveRequest;

public class EventPage extends ActionBarActivity {

    @Bind(R.id.textViewTitle)
    TextView textViewTitle;
    @Bind(R.id.imageViewBackButton)
    ImageView imageViewBackButton;
    @Bind(R.id.RecyclerViewEvent)
    RecyclerView mRecyclerView;

    EventAdapter eventAdapter;
    EventDA eventDA;
    private RetrieveRequest mRetrieveRequest;
    ArrayList<Event> eventArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        ButterKnife.bind(this);
        textViewTitle.setText("Event");

        //do server request for event  here ?
        mRetrieveRequest = new RetrieveRequest(this);
        eventDA = new EventDA(this);
        eventArrayList = eventDA.getAllEvent();
        if (eventArrayList.isEmpty()) {
            eventArrayList = mRetrieveRequest.fetchAllEventInBackground();

        } else {
            eventArrayList = eventDA.getAllEvent();
        }

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(this, eventArrayList);
        mRecyclerView.setAdapter(eventAdapter);
    }

    public void BackAction(View view) {
        this.finish();
    }

}
