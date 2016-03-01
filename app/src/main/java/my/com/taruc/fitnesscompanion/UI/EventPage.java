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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Adapter.EventAdapter;
import my.com.taruc.fitnesscompanion.Classes.Event;
import my.com.taruc.fitnesscompanion.ConnectionDetector;
import my.com.taruc.fitnesscompanion.Database.EventDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.ServerAPI.RetrieveRequest;
import my.com.taruc.fitnesscompanion.ShowAlert;

public class EventPage extends ActionBarActivity {

    @Bind(R.id.textViewTitle)
    TextView textViewTitle;
    @Bind(R.id.imageViewBackButton)
    ImageView imageViewBackButton;
    @Bind(R.id.RecyclerViewEvent)
    RecyclerView mRecyclerView;

    private EventAdapter eventAdapter;
    private EventDA eventDA;
    private RetrieveRequest mRetrieveRequest;
    private ArrayList<Event> eventArrayList;
    private ArrayList<Event> eventArrayListFromServer;

    private ConnectionDetector mConnectionDetector;
    private ShowAlert alert = new ShowAlert();
    private ProgressDialog mProgressDialog;

    private Context context;
    private Timer timer = new Timer();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        context = this;
        ButterKnife.bind(this);
        textViewTitle.setText(R.string.eventTitle);

        //do server request for event
        mConnectionDetector = new ConnectionDetector(this);
        mRetrieveRequest = new RetrieveRequest(this);
        eventDA = new EventDA(this);
        eventArrayList = eventDA.getAllEvent();
        eventArrayListFromServer = mRetrieveRequest.fetchAllEventInBackground();

        if(!mConnectionDetector.isConnectingToInternet()){
            alert.showAlertDialog(this, "Internet Error", "No Internet", false);
        } else {
            if (eventArrayList.isEmpty()) {
                mProgressDialog = ProgressDialog.show(this, "Synchronizing", "Sync with server....Please Wait.", true);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                eventDA.addEventArrayList(eventArrayListFromServer);
                                mProgressDialog.dismiss();
                                eventAdapter = new EventAdapter(context, eventArrayListFromServer);
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

            } else if (eventArrayList.size() != eventArrayListFromServer.size()) {
                eventDA.deleteAll();
                eventDA.addEventArrayList(eventArrayListFromServer);
            }
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
