package my.com.taruc.fitnesscompanion.Graph;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.Duration;
import my.com.taruc.fitnesscompanion.Classes.FitnessFormula;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Classes.RealTimeFitness;
import my.com.taruc.fitnesscompanion.Classes.SleepData;
import my.com.taruc.fitnesscompanion.Database.RealTimeFitnessDA;
import my.com.taruc.fitnesscompanion.Database.SleepDataDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.UserLocalStore;

public class MySleepDataGraphView extends Activity {

    @Bind(R.id.textViewTitle)
    TextView textViewSleepDataTitle;
    @Bind(R.id.imageViewBackButton)
    ImageView imageViewBackButton;
    @Bind(R.id.graph)
    GraphView graph;
    @Bind(R.id.SleepQualityCaption)
    TextView SleepQualityCaption;
    @Bind(R.id.SleepQualityValue)
    TextView SleepQualityValue;
    @Bind(R.id.TotalSleepTimeCaption)
    TextView TotalSleepTimeCaption;
    @Bind(R.id.separator1)
    TextView separator1;
    @Bind(R.id.TotalSleepTimeValue)
    TextView TotalSleepTimeValue;
    @Bind(R.id.AsleepTimeCaption)
    TextView AsleepTimeCaption;
    @Bind(R.id.separator2)
    TextView separator2;
    @Bind(R.id.AsleepTimeValue)
    TextView AsleepTimeValue;
    @Bind(R.id.TimesAwakenCaption)
    TextView TimesAwakenCaption;
    @Bind(R.id.separator3)
    TextView separator3;
    @Bind(R.id.TimesAwakenValue)
    TextView TimesAwakenValue;
    @Bind(R.id.TableDetail)
    TableLayout TableDetail;
    @Bind(R.id.ScrollView01)
    ScrollView ScrollView01;
    @Bind(R.id.previousDay)
    ImageView previousDay;
    @Bind(R.id.DateDisplay)
    TextView DateDisplay;
    @Bind(R.id.nextDay)
    ImageView nextDay;

    UserLocalStore userLocalStore;
    DateTime displayDate;
    DateTime yesterdayDate;
    ArrayList<SleepData> mySleepDataArr = new ArrayList();
    ArrayList<RealTimeFitness> myRealTimeArr = new ArrayList<>();
    SleepDataDA sleepDataDA;
    RealTimeFitnessDA realTimeFitnessDA;

    DateTime wakeUpTime;
    DateTime sleepTime;

    Context context;
    String selectedView = "Sleep Data";
    String[] viewName = new String[]{"Activity History", "RealTime History", "Sleep Data"};
    String oldate, newdate;
    FitnessFormula fitnessFormula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_data_graph_view);
        ButterKnife.bind(this);
        context = this;
        textViewSleepDataTitle.setText("Sleep Data");

        userLocalStore = new UserLocalStore(this);
        sleepDataDA = new SleepDataDA(this);
        realTimeFitnessDA = new RealTimeFitnessDA(this);
        fitnessFormula = new FitnessFormula(this);
        yesterdayDate = new DateTime().getCurrentDateTime();
        yesterdayDate.getDate().setDateNumber(-1);
        yesterdayDate.setTime("18:00:00");

        //testing data
        //--------------------
        //sleepDataDA.deleteAllSleepData();
        mySleepDataArr = sleepDataDA.getAllSleepData();
        if (mySleepDataArr.size() <= 0) {
            SleepData sampleRecord1 = new SleepData(sleepDataDA.generateNewSleepDataID(), userLocalStore.returnUserID().toString(), 3, new DateTime("2016-01-29 23:05:00"), new DateTime().getCurrentDateTime());
            sleepDataDA.addSleepData(sampleRecord1);
            SleepData sampleRecord2 = new SleepData(sleepDataDA.generateNewSleepDataID(), userLocalStore.returnUserID().toString(), 7, new DateTime("2016-01-29 23:55:00"), new DateTime().getCurrentDateTime());
            sleepDataDA.addSleepData(sampleRecord2);
            SleepData sampleRecord3 = new SleepData(sleepDataDA.generateNewSleepDataID(), userLocalStore.returnUserID().toString(), 2, new DateTime("2016-01-30 01:55:00"), new DateTime().getCurrentDateTime());
            sleepDataDA.addSleepData(sampleRecord3);
            SleepData sampleRecord4 = new SleepData(sleepDataDA.generateNewSleepDataID(), userLocalStore.returnUserID().toString(), 1, new DateTime("2016-01-30 03:18:00"), new DateTime().getCurrentDateTime());
            sleepDataDA.addSleepData(sampleRecord4);
            SleepData sampleRecord5 = new SleepData(sleepDataDA.generateNewSleepDataID(), userLocalStore.returnUserID().toString(), 10, new DateTime("2016-01-30 06:07:00"), new DateTime().getCurrentDateTime());
            sleepDataDA.addSleepData(sampleRecord5);
        }
        //----------------------

        displayDate = new DateTime(yesterdayDate.getDateTimeString());

        graphUIConfiguration();
        createGraphView();
    }

    public void BackAction(View view) {
        this.finish();
    }

    public void PreviousDayClick(View view) {
        displayDate.getDate().addDateNumber(-1);
        createGraphView();
    }

    public void NextDayClick(View view) {
        if (!displayDate.getDate().getFullDateString().equals(yesterdayDate.getDate().getFullDateString())) {
            displayDate.getDate().addDateNumber(1);
            createGraphView();
        }
    }

    public void createGraphView() {

        mySleepDataArr = sleepDataDA.getAllSleepDataPerDay(displayDate);

        //create graph
        graph.removeAllSeries();

        DateDisplay.setText(displayDate.getDate().getFullDateString());
        if (DateDisplay.getText().equals(yesterdayDate.getDate().getFullDateString())) {
            nextDay.setEnabled(false);
            nextDay.setVisibility(View.INVISIBLE);
        } else {
            nextDay.setEnabled(true);
            nextDay.setVisibility(View.VISIBLE);
        }

        if(!mySleepDataArr.isEmpty()) {
            //get my sleep time
            sleepTime = mySleepDataArr.get(0).getCreated_at();
            oldate = fitnessFormula.convertDateToStringWithoutSymbol(sleepTime.getDateTimeString());
            boolean continueCount = true;
            myRealTimeArr = realTimeFitnessDA.getAllRealTimeFitnessBeforeLimit(sleepTime);
            for (int i = myRealTimeArr.size() - 1; i >= 0 && continueCount; i--) {
                if (myRealTimeArr.get(i).getStepNumber() > 0) {
                    continueCount = false;
                    sleepTime.setTime(String.format("%02d:00:00", myRealTimeArr.get(i).getCaptureDateTime().getTime().getHour()));
                }
            }
            //get my wake up time
            wakeUpTime = mySleepDataArr.get(mySleepDataArr.size()-1).getCreated_at();
            newdate =  fitnessFormula.convertDateToStringWithoutSymbol(wakeUpTime.getDateTimeString());
            continueCount = true;
            myRealTimeArr = realTimeFitnessDA.getAllRealTimeFitnessAfterLimit(wakeUpTime);
            for (int i = 0; i < myRealTimeArr.size() && continueCount; i++) {
                if (myRealTimeArr.get(i).getStepNumber() > 0) {
                    continueCount = false;
                    wakeUpTime.setTime(String.format("%02d:00:00", myRealTimeArr.get(i).getCaptureDateTime().getTime().getHour() - 1));
                }
            }

            //Toast.makeText(this, "Sleep Time: "+sleepTime.getDateTimeString()
            //        + "\nWake Up: " + wakeUpTime.getDateTimeString(), Toast.LENGTH_LONG).show();

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(generateSleepDataPoint());
            series.setColor(Color.parseColor("#FFFFFF"));
            graph.addSeries(series);
            TotalSleepTimeValue.setText(getTotalSleepTime().getDuration());
            AsleepTimeValue.setText(getAsleepTime().getDuration());
            TimesAwakenValue.setText(getTimesAwaken()+"");
            SleepQualityValue.setText(String.format("%.2f %%",calSleepQuality()));
        }else {
            //Toast.makeText(this, "No Sleep Record in this day.", Toast.LENGTH_LONG).show();
            TotalSleepTimeValue.setText("--");
            AsleepTimeValue.setText("--");
            TimesAwakenValue.setText("--");
            SleepQualityValue.setText("--");
        }
    }

    private DataPoint[] generateSleepDataPoint() {
        double differentDayHourAddition = 0;
        int i = 0;
        DataPoint[] values = new DataPoint[mySleepDataArr.size()*3+2];
        DataPoint initialDP = new DataPoint(0, 0);
        values[i] = initialDP;
        i++;
        for(int j=0; j < mySleepDataArr.size() && i < values.length; j++){
            if(mySleepDataArr.get(j).getCreated_at().getDate().getDateNumber()!=sleepTime.getDate().getDateNumber()){
                differentDayHourAddition = 24.0;
            }
            double sleepRecordTime = mySleepDataArr.get(j).getCreated_at().getDateTimeFloat() + differentDayHourAddition;
            double x = sleepRecordTime - sleepTime.getDateTimeFloat();
            DataPoint startDP = new DataPoint(x-0.01, 0);
            values[i] = startDP;
            i++;
            DataPoint movementDP = new DataPoint(x, mySleepDataArr.get(j).getMovement());
            values[i] = movementDP;
            i++;
            DataPoint endDP = new DataPoint(x+0.01, 0);
            values[i] = endDP;
            i++;
        }
        DataPoint deInitialDP = new DataPoint(wakeUpTime.getDateTimeFloat() + differentDayHourAddition, 0);
        values[i] = deInitialDP;
        i++;
        return values;
    }

    public long getTotalSleepTimeInHour(){
        FitnessFormula myFormula = new FitnessFormula(this);
        Duration sleepDuration = new Duration();
//        sleepDuration = myFormula.calculationDuration(sleepTime, wakeUpTime);
        long duration = myFormula.diffTwoDateDaysBy14(newdate, oldate);
        return duration;
    }

    public Duration getTotalSleepTime(){
        FitnessFormula myFormula = new FitnessFormula(this);
        Duration sleepDuration = new Duration();
        sleepDuration = myFormula.calculationDuration(sleepTime, wakeUpTime);
        return sleepDuration;
    }

    public Double getTimesAwaken(){
        Double movement = 0.0;
        for(int i=0; i< mySleepDataArr.size(); i++){
            movement += mySleepDataArr.get(i).getMovement();
        }
        return movement;
    }

    public Duration getAsleepTime(){
        Double moveSecond = getTimesAwaken();
        Long totalSleepDuration = getTotalSleepTimeInHour() * 60 * 60;
        int asleepSeconds = totalSleepDuration.intValue() - moveSecond.intValue();;
        Duration myDuration = new Duration();
        myDuration.addSeconds(asleepSeconds);
        return myDuration;
    }

    public double calSleepQuality(){
        Long totalSleep = getTotalSleepTimeInHour();
        if(totalSleep==0){
            totalSleep ++;
        }
        Double quality = ((getTimesAwaken()/60.0)/ (totalSleep*60));
        Double percentage_quality =  100.0 - (100.0 * quality);
        return percentage_quality;
    }

    public void graphUIConfiguration() {
        graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setScrollable(true);
        graph.setScrollContainer(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Times Movement");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.parseColor("#FFFFFF"));
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.parseColor("#FFFFFF"));
        graph.getGridLabelRenderer().setGridColor(Color.parseColor("#FFFFFF"));
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#FFFFFF"));
        graph.getGridLabelRenderer().setLabelVerticalWidth(5);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#FFFFFF"));
    }

    public RealTimeFitness IsRecordExist(int hour, ArrayList<RealTimeFitness> realTimeFitnessArrayList){
        for(int i=0; i<realTimeFitnessArrayList.size(); i++){
            if(realTimeFitnessArrayList.get(i).getCaptureDateTime().getTime().getHour()==hour){
                return realTimeFitnessArrayList.get(i);
            }
        }
        return null;
    }

    public void changeView(View view) {
        //build dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.schedule_new_dialog, null); //reuse schedule new dialog
        RadioGroup myRg = (RadioGroup) dialogView.findViewById(R.id.myRg);
        for (int i = 0; i < viewName.length; i++) {
            final RadioButton button1 = new RadioButton(this);
            button1.setText(viewName[i]);
            button1.setPadding(0, 20, 0, 20);
            button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedView = buttonView.getText().toString();
                    }
                }
            });
            myRg.addView(button1);
        }
        //set checked item
        for (int j = 0; j < viewName.length; j++) {
            if (viewName[j].equals(selectedView)) {
                ((RadioButton) myRg.getChildAt(j)).setChecked(true);
                break;
            }
        }
        //show dialog
        showViewDialog(dialogView);
    }

    public void showViewDialog(View dialogView) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Selection")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                        Intent intent;
                        if (selectedView.equalsIgnoreCase(viewName[0])) {
                            intent = new Intent(context, MyExerciseGraphView.class);
                        } else if (selectedView.equalsIgnoreCase(viewName[1])) {
                            intent = new Intent(context, MyRealTimeGraphView.class);
                        } else {
                            intent = new Intent(context, MySleepDataGraphView.class);
                        }
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
    }

}
