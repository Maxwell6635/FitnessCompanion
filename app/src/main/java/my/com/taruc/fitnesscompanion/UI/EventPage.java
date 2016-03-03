package my.com.taruc.fitnesscompanion.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import my.com.taruc.fitnesscompanion.Classes.TaskCanceler;
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
    private ArrayList<Event> eventArrayList = new ArrayList<>();
    private ArrayList<Event> eventArrayListFromServer;

    private ConnectionDetector mConnectionDetector;
    private ShowAlert alert = new ShowAlert();

    private Context context;
    private ProgressDialog mProgressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        context = this;
        ButterKnife.bind(this);
        textViewTitle.setText(R.string.eventTitle);

        mConnectionDetector = new ConnectionDetector(this);
        mRetrieveRequest = new RetrieveRequest(this);
        eventDA = new EventDA(this);

        eventArrayList = eventDA.getAllEvent();

        if(!mConnectionDetector.isConnectingToInternet()){
            alert.showAlertDialog(this, "Internet Error", "No Internet", false);
        } else {
            mProgressDialog = ProgressDialog.show(this, "Synchronizing", "Sync with server....Please Wait.", true);
            new Thread(new Runnable() {
                @Override
                public void run() {

                    eventArrayListFromServer = mRetrieveRequest.fetchAllEventInBackground();
                    if (eventArrayList.size() != eventArrayListFromServer.size() && !eventArrayListFromServer.isEmpty()) {
                        try{
                            Thread.sleep(1000);
                        }catch (Exception ex){
                            Log.i("Event","Thread Exception");
                        }
                        eventDA.deleteAll();
                        eventDA.addEventArrayList(eventArrayListFromServer);
                        eventArrayList = eventDA.getAllEvent();

                        eventAdapter = new EventAdapter(context, eventArrayList);
                        mRecyclerView.swapAdapter(eventAdapter, true);
                    }
                    mProgressDialog.dismiss();
                }

            }).start();
        }

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        eventAdapter = new EventAdapter(context, eventArrayList);
        mRecyclerView.setAdapter(eventAdapter);
    }

    public void BackAction(View view) {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
