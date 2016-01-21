package my.com.taruc.fitnesscompanion.UI;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.RealTimeFitness;
import my.com.taruc.fitnesscompanion.Database.FitnessDB;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.Database.RealTimeFitnessDA;
import my.com.taruc.fitnesscompanion.R;


public class HistoryPage extends ActionBarActivity {

    FitnessDB myFitnessDB;
    FitnessRecordDA myFitnessRecordDA;
    boolean isAll = false;
    boolean isAfter = false;

    DateTime displayDate = new DateTime();
    @Bind(R.id.listViewHistory)
    ListView listViewHistory;
    @Bind(R.id.button2)
    Button button2;
    @Bind(R.id.button4)
    Button button4;
    @Bind(R.id.button5)
    Button button5;
    @Bind(R.id.button3)
    Button button3;
    @Bind(R.id.textView2)
    TextView textView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);
        ButterKnife.bind(this);

        displayDate = displayDate.getCurrentDateTime();

        //create DB
        myFitnessDB = new FitnessDB(this);
        SQLiteDatabase sqLiteDatabase = myFitnessDB.getWritableDatabase();
        myFitnessDB.onCreate(sqLiteDatabase);
        myFitnessRecordDA = new FitnessRecordDA(this);

        button2.setBackgroundColor(Color.GREEN);
        button3.setBackgroundColor(Color.LTGRAY);

        try {
            //ArrayList<FitnessRecord> myFitnessRecordList = myFitnessRecordDA.getAllFitnessRecord();
            //FitnessRecord myRecord = new FitnessRecord();

            RealTimeFitnessDA realTimeFitnessDA = new RealTimeFitnessDA(this);
            ArrayList<RealTimeFitness> realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitness();
            //DateTime displayDate = new DateTime();
            //ArrayList<RealTimeFitness> realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitnessPerDay(displayDate);
            RealTimeFitness myRecord = new RealTimeFitness();
            if (realTimeFitnessArrayList != null) {
                ListView listView = (ListView) findViewById(R.id.listViewHistory);
                String[] values = new String[realTimeFitnessArrayList.size()];
                for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
                    myRecord = realTimeFitnessArrayList.get(i);
                    values[i] = "RecordID: " + myRecord.getRealTimeFitnessID() + " \n" +
                            "Step: " + myRecord.getStepNumber() + " \n" +
                            "Capture: " + myRecord.getCaptureDateTime().getDateTimeString();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
                listView.setAdapter(adapter);
            }

            /*if (myFitnessRecordList != null) {
                ListView listView = (ListView) findViewById(R.id.listViewHistory);
                String[] values = new String[myFitnessRecordList.size()];
                for (int i = 0; i < myFitnessRecordList.size(); i++) {
                    myRecord = myFitnessRecordList.get(i);
                    values[i] = "RecordID: " + myRecord.getFitnessRecordID() + " \n" +
                            "Activity: " + myRecord.getFitnessActivity() + " \n" +
                            "Duration: " + myRecord.getRecordDuration() + " second(s) \n" +
                            "Distance: " + myRecord.getRecordDistance() + " m \n" +
                            "HR: " + myRecord.getAverageHeartRate() + " mpb \n" +
                            "Calories: " + myRecord.getRecordCalories() + " \n" +
                            "DateTime: " + myRecord.getFitnessRecordDateTime();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
                listView.setAdapter(adapter);
            }*/
        } catch (Exception ex) {
            Toast.makeText(this, "1..." + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void iclick(View view) {
        RealTimeFitnessDA realTimeFitnessDA = new RealTimeFitnessDA(this);
        RealTimeFitness myRecord = new RealTimeFitness();
        ListView listView = (ListView) findViewById(R.id.listViewHistory);
        ArrayList<RealTimeFitness> realTimeFitnessArrayList;

        if (isAll == false) {
            realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitness();
            isAll = true;
            button2.setText("Get display date only");
            textView2.setText(displayDate.getDateTimeString());
        } else {
            realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitnessPerDay(displayDate);
            isAll = false;
            button2.setText("Get All Record");
            textView2.setText("null");
        }

        if (realTimeFitnessArrayList != null) {
            String[] values = new String[realTimeFitnessArrayList.size()];
            for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
                myRecord = realTimeFitnessArrayList.get(i);
                values[i] = "RecordID: " + myRecord.getRealTimeFitnessID() + " \n" +
                        "Step: " + myRecord.getStepNumber() + " \n" +
                        "Capture: " + myRecord.getCaptureDateTime().getDateTimeString();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
            listView.setAdapter(adapter);
        }else {
            listView.setAdapter(null);
        }
        button2.setBackgroundColor(Color.GREEN);
        isAfter = false;
        button3.setBackgroundColor(Color.LTGRAY);
    }

    public void Previous(View view) {
        RealTimeFitnessDA realTimeFitnessDA = new RealTimeFitnessDA(this);
        ArrayList<RealTimeFitness> realTimeFitnessArrayList;
        if(isAll) {
//            displayDate.getDate().addDateNumber(-1);
            realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitnessPerDay(displayDate);
        }else{
            //get after
            displayDate.getTime().addHour(-1);
            realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitnessAfter(displayDate);
        }
        RealTimeFitness myRecord = new RealTimeFitness();
        if(!textView2.getText().equals("null")) {
            if (realTimeFitnessArrayList != null) {
                ListView listView = (ListView) findViewById(R.id.listViewHistory);
                String[] values = new String[realTimeFitnessArrayList.size()];
                for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
                    myRecord = realTimeFitnessArrayList.get(i);
                    values[i] = "RecordID: " + myRecord.getRealTimeFitnessID() + " \n" +
                            "Step: " + myRecord.getStepNumber() + " \n" +
                            "Capture: " + myRecord.getCaptureDateTime().getDateTimeString();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
                listView.setAdapter(adapter);
         //       isAll = true;
            }
        }
        textView2.setText(displayDate.getDateTimeString());
    }

    public void Next(View view) {
        RealTimeFitnessDA realTimeFitnessDA = new RealTimeFitnessDA(this);
        ArrayList<RealTimeFitness> realTimeFitnessArrayList;
        if(isAll) {
//            displayDate.getDate().addDateNumber(1);
            realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitnessPerDay(displayDate);
        }else{
            //get after
            displayDate.getTime().addHour(1);
            realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitnessAfter(displayDate);
        }
        if(!textView2.getText().equals("null")) {
            RealTimeFitness myRecord = new RealTimeFitness();
            if (realTimeFitnessArrayList != null) {
                ListView listView = (ListView) findViewById(R.id.listViewHistory);
                String[] values = new String[realTimeFitnessArrayList.size()];
                for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
                    myRecord = realTimeFitnessArrayList.get(i);
                    values[i] = "RecordID: " + myRecord.getRealTimeFitnessID() + " \n" +
                            "Step: " + myRecord.getStepNumber() + " \n" +
                            "Capture: " + myRecord.getCaptureDateTime().getDateTimeString();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
                listView.setAdapter(adapter);
                //isAfter = true;
            }
        }
        textView2.setText(displayDate.getDateTimeString());
    }

    public void getAfter(View view){
        RealTimeFitnessDA realTimeFitnessDA = new RealTimeFitnessDA(this);
        ArrayList<RealTimeFitness> realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitnessAfter(displayDate);
        RealTimeFitness myRecord = new RealTimeFitness();
        ListView listView = (ListView) findViewById(R.id.listViewHistory);
        if (realTimeFitnessArrayList != null) {
            String[] values = new String[realTimeFitnessArrayList.size()];
            for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
                myRecord = realTimeFitnessArrayList.get(i);
                values[i] = "RecordID: " + myRecord.getRealTimeFitnessID() + " \n" +
                        "Step: " + myRecord.getStepNumber() + " \n" +
                        "Capture: " + myRecord.getCaptureDateTime().getDateTimeString();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
            listView.setAdapter(adapter);
        }else{
            listView.setAdapter(null);
        }
        isAfter = true;
        button2.setBackgroundColor(Color.LTGRAY);
        button3.setBackgroundColor(Color.GREEN);
        textView2.setText(displayDate.getDateTimeString());
    }

}
