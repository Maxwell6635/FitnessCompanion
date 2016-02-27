package my.com.taruc.fitnesscompanion.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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

    ProgressDialog progress;
    Context context;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        context = this;
        ButterKnife.bind(this);
        textViewTitle.setText(R.string.eventTitle);

        //do server request for event
        mRetrieveRequest = new RetrieveRequest(this);
        eventDA = new EventDA(this);
        eventArrayList = eventDA.getAllEvent();
        if (eventArrayList.isEmpty()) {
            progress = ProgressDialog.show(this, "Synchronizing", "Sync with server....Please Wait.", true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            eventArrayList = mRetrieveRequest.fetchAllEventInBackground();
                            progress.dismiss();

                            eventAdapter = new EventAdapter(context, eventArrayList);
                            try {
                                mRecyclerView.swapAdapter(eventAdapter, true);
                                timer.cancel();
                            } catch (Exception ex) {
                                Log.i("EventErr", ex.getMessage());
                            }
                        }
                    });
                }
            }, 500, 10);

        } else {
            eventArrayList = eventDA.getAllEvent();
        }

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        eventAdapter = new EventAdapter(context, eventArrayList);
        mRecyclerView.setAdapter(eventAdapter);
    }

    public void BackAction(View view) {
        this.finish();
    }

}
