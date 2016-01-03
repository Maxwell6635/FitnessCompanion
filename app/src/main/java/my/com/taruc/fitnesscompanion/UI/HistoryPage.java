package my.com.taruc.fitnesscompanion.UI;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Classes.RealTimeFitness;
import my.com.taruc.fitnesscompanion.Database.FitnessDB;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.Database.RealTimeFitnessDA;
import my.com.taruc.fitnesscompanion.R;


public class HistoryPage extends ActionBarActivity {

    FitnessDB myFitnessDB;
    FitnessRecordDA myFitnessRecordDA;
    boolean isAll = false;

    DateTime displayDate = new DateTime();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);

        displayDate = displayDate.getCurrentDateTime();

        //create DB
        myFitnessDB = new FitnessDB(this);
        SQLiteDatabase sqLiteDatabase = myFitnessDB.getWritableDatabase();
        myFitnessDB.onCreate(sqLiteDatabase);
        myFitnessRecordDA = new FitnessRecordDA(this);

        try {
            //ArrayList<FitnessRecord> myFitnessRecordList = myFitnessRecordDA.getAllFitnessRecord();
            //FitnessRecord myRecord = new FitnessRecord();

            RealTimeFitnessDA realTimeFitnessDA = new RealTimeFitnessDA(this);
            //ArrayList<RealTimeFitness> realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitness();
            //DateTime displayDate = new DateTime();
            ArrayList<RealTimeFitness> realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitnessPerDay(displayDate);
            RealTimeFitness myRecord = new RealTimeFitness();
            if (realTimeFitnessArrayList != null) {
                ListView listView = (ListView) findViewById(R.id.listViewHistory);
                String[] values = new String[realTimeFitnessArrayList.size()];
                for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
                    myRecord = realTimeFitnessArrayList.get(i);
                    values[i] = "RecordID: " + myRecord.getRealTimeFitnessID() + " \n" +
                            "Step: " + myRecord.getStepNumber() + " \n" +
                            "Capture: " + myRecord.getCaptureDateTime().getDateTime();
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
        }catch(Exception ex){
            Toast.makeText(this,"1..."+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    public void iclick(View view) {
        if (isAll == false) {
            RealTimeFitnessDA realTimeFitnessDA = new RealTimeFitnessDA(this);
            ArrayList<RealTimeFitness> realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitness();
            RealTimeFitness myRecord = new RealTimeFitness();
            if (realTimeFitnessArrayList != null) {
                ListView listView = (ListView) findViewById(R.id.listViewHistory);
                String[] values = new String[realTimeFitnessArrayList.size()];
                for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
                    myRecord = realTimeFitnessArrayList.get(i);
                    values[i] = "RecordID: " + myRecord.getRealTimeFitnessID() + " \n" +
                            "Step: " + myRecord.getStepNumber() + " \n" +
                            "Capture: " + myRecord.getCaptureDateTime().getDateTime();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
                listView.setAdapter(adapter);
                isAll = true;
            }
        } else {
            RealTimeFitnessDA realTimeFitnessDA = new RealTimeFitnessDA(this);
            ArrayList<RealTimeFitness> realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitnessPerDay(displayDate);
            RealTimeFitness myRecord = new RealTimeFitness();
            if (realTimeFitnessArrayList != null) {
                ListView listView = (ListView) findViewById(R.id.listViewHistory);
                String[] values = new String[realTimeFitnessArrayList.size()];
                for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
                    myRecord = realTimeFitnessArrayList.get(i);
                    values[i] = "RecordID: " + myRecord.getRealTimeFitnessID() + " \n" +
                            "Step: " + myRecord.getStepNumber() + " \n" +
                            "Capture: " + myRecord.getCaptureDateTime().getDateTime();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
                listView.setAdapter(adapter);
                isAll = true;
            }
        }
    }

    public void Previous(View view){
        RealTimeFitnessDA realTimeFitnessDA = new RealTimeFitnessDA(this);
        displayDate.getDate().setDate(displayDate.getDate().getDate()-1);
        ArrayList<RealTimeFitness> realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitnessPerDay(displayDate);
        RealTimeFitness myRecord = new RealTimeFitness();
        if (realTimeFitnessArrayList != null) {
            ListView listView = (ListView) findViewById(R.id.listViewHistory);
            String[] values = new String[realTimeFitnessArrayList.size()];
            for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
                myRecord = realTimeFitnessArrayList.get(i);
                values[i] = "RecordID: " + myRecord.getRealTimeFitnessID() + " \n" +
                        "Step: " + myRecord.getStepNumber() + " \n" +
                        "Capture: " + myRecord.getCaptureDateTime().getDateTime();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
            listView.setAdapter(adapter);
            isAll = true;
        }
    }

    public void Next(View view){
        RealTimeFitnessDA realTimeFitnessDA = new RealTimeFitnessDA(this);
        displayDate.getDate().setDate(displayDate.getDate().getDate()+1);
        ArrayList<RealTimeFitness> realTimeFitnessArrayList = realTimeFitnessDA.getAllRealTimeFitnessPerDay(displayDate);
        RealTimeFitness myRecord = new RealTimeFitness();
        if (realTimeFitnessArrayList != null) {
            ListView listView = (ListView) findViewById(R.id.listViewHistory);
            String[] values = new String[realTimeFitnessArrayList.size()];
            for (int i = 0; i < realTimeFitnessArrayList.size(); i++) {
                myRecord = realTimeFitnessArrayList.get(i);
                values[i] = "RecordID: " + myRecord.getRealTimeFitnessID() + " \n" +
                        "Step: " + myRecord.getStepNumber() + " \n" +
                        "Capture: " + myRecord.getCaptureDateTime().getDateTime();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
            listView.setAdapter(adapter);
            isAll = true;
        }
    }

}
