package my.com.taruc.fitnesscompanion.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Adapter.RankingAdapter;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.Ranking;
import my.com.taruc.fitnesscompanion.Database.RankingDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.ServerAPI.ServerRequests;
import my.com.taruc.fitnesscompanion.UserLocalStore;


public class RankingPage extends ActionBarActivity {

    //get user id
    UserLocalStore userLocalStore;

    RankingDA rankingDA;
    RecyclerView recyclerView;
    RankingAdapter rankingAdapter;
    ArrayList<Ranking> AllRankingArrayList;
    ArrayList<String> rankingTypeList;
    String selectedType;
    ServerRequests serverRequests;
    @Bind(R.id.TextViewOverallTitle)
    TextView TextViewRankingType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_page);
        ButterKnife.bind(this);
        recyclerView = (RecyclerView) findViewById(R.id.listViewRanking);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        userLocalStore = new UserLocalStore(this);

        //serverRequests = new ServerRequests(getApplicationContext());
        rankingDA = new RankingDA(this);
        //rankingDA.deleteAllRanking();
        AllRankingArrayList = rankingDA.getAllRanking();

        //testing purpose
        if (AllRankingArrayList.isEmpty()) {
            DateTime dateTime = new DateTime().getCurrentDateTime();
            rankingDA.addRanking(new Ranking("R001", userLocalStore.returnUserID().toString(), "Running", 300,"", dateTime, dateTime));
            rankingDA.addRanking(new Ranking("R002", userLocalStore.returnUserID().toString(), "Running", 200,"", dateTime, dateTime));
            rankingDA.addRanking(new Ranking("R003", userLocalStore.returnUserID().toString(), "Running", 100,"", dateTime, dateTime));
            rankingDA.addRanking(new Ranking("R004", userLocalStore.returnUserID().toString(), "Running", 90,"", dateTime, dateTime));
            rankingDA.addRanking(new Ranking("R005", userLocalStore.returnUserID().toString(), "Walking", 190,"", dateTime, dateTime));
            rankingDA.addRanking(new Ranking("R006", userLocalStore.returnUserID().toString(), "Walking", 60,"", dateTime, dateTime));
            rankingDA.addRanking(new Ranking("R007", userLocalStore.returnUserID().toString(), "Sleeping", 40,"", dateTime, dateTime));
            rankingDA.addRanking(new Ranking("R008", userLocalStore.returnUserID().toString(), "Sleeping", 10,"", dateTime, dateTime));
        }

        if(!AllRankingArrayList.isEmpty()) {
            rankingTypeList = rankingDA.getAllRankingType();
            selectedType = rankingTypeList.get(0);
            updateUI(selectedType);
        }else{
            Toast.makeText(this, "There is no ranking record.", Toast.LENGTH_SHORT).show();
        }
        //rankingArrayList = serverRequests.fetchRankingDataInBackground();
    }

    public void updateUI(String type) {
        TextViewRankingType.setText(type);
        ArrayList<Ranking> myRankingList = rankingDA.getAllRankingByType(type);
        rankingAdapter = new RankingAdapter(getApplicationContext(), myRankingList);
        recyclerView.setAdapter(rankingAdapter);
    }

    public void changeType(View view) {
        if(!AllRankingArrayList.isEmpty()) {
            //build dialog
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.schedule_new_dialog, null); //reuse schedule new dialog
            RadioGroup myRg = (RadioGroup) dialogView.findViewById(R.id.myRg);
            for (int i = 0; i < rankingTypeList.size(); i++) {
                final RadioButton button1 = new RadioButton(this);
                button1.setText(rankingTypeList.get(i));
                button1.setPadding(0, 20, 0, 20);
                button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            selectedType = buttonView.getText().toString();
                        }
                    }
                });
                myRg.addView(button1);
            }
            //set checked item
            for (int j = 0; j < rankingTypeList.size(); j++) {
                if (rankingTypeList.get(j).equals(selectedType)) {
                    ((RadioButton) myRg.getChildAt(j)).setChecked(true);
                    break;
                }
            }
            //show dialog
            showRankingTypeDialog(dialogView);
        }else{
            Toast.makeText(this, "There is no ranking record.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showRankingTypeDialog(View dialogView) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Selection")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        updateUI(selectedType);
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
    }

    public void BackAction(View view) {
        this.finish();
    }
}
