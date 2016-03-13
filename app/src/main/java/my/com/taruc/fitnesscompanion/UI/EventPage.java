package my.com.taruc.fitnesscompanion.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
    private ArrayList<Event> eventArrayList = new ArrayList<>();

    private ConnectionDetector mConnectionDetector;
    private ShowAlert alert = new ShowAlert();

    private Context context;
    private ProgressDialog mProgressDialog;
    private Timer timer = new Timer();
    private ArrayList<Event> eventArrayListFromServer;
    private RetrieveRequest mRetrieveRequest;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        context = this;
        ButterKnife.bind(this);
        textViewTitle.setText(R.string.eventTitle);

        mConnectionDetector = new ConnectionDetector(this);
        eventDA = new EventDA(this);
        mRetrieveRequest = new RetrieveRequest(this);

        eventArrayList = eventDA.getAllEvent();

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        eventAdapter = new EventAdapter(context, eventArrayList);
        mRecyclerView.setAdapter(eventAdapter);

        boolean firstTime = true;
        if(getIntent().getExtras()!=null) {
            firstTime = getIntent().getBooleanExtra("FirstTime",true);
        }

        if(!mConnectionDetector.isConnectingToInternet()){
            alert.showAlertDialog(this, "Internet Error", "No Internet", false);
        } else {
            if(firstTime) {
                mProgressDialog = ProgressDialog.show(context, "Synchronizing", "Sync with server....Please Wait.", true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        eventArrayListFromServer = mRetrieveRequest.fetchAllEventInBackground();
                        if (eventArrayList.size() != eventArrayListFromServer.size() && !eventArrayListFromServer.isEmpty()) {
                            eventDA.deleteAll();
                            eventDA.addEventArrayList(eventArrayListFromServer);
                            finish();
                            Intent intent = new Intent(context, EventPage.class);
                            intent.putExtra("FirstTime", false);
                            startActivity(intent);
                        }
                        mProgressDialog.dismiss();
                    }
                }).start();
            }
        }
    }

    public void BackAction(View view) {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
