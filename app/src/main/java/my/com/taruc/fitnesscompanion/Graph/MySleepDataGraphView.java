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

    @Bind(R.id.textViewSleepDataTitle)
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
    Button previousDay;
    @Bind(R.id.DateDisplay)
    TextView DateDisplay;
    @Bind(R.id.nextDay)
    Button nextDay;

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
    String selectedView = "RealTime History";
    String[] viewName = new String[]{"Activity History", "RealTime History", "Sleep Data"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_data_graph_view);
        ButterKnife.bind(this);
        context = this;

        userLocalStore = new UserLocalStore(this);
        sleepDataDA = new SleepDataDA(this);
        realTimeFitnessDA = new RealTimeFitnessDA(this);
        yesterdayDate = new DateTime().getCurrentDateTime();
        yesterdayDate.getDate().setDateNumber(-1);
        yesterdayDate.setTime("18:00:00");

        //testing data
        //--------------------
        mySleepDataArr = sleepDataDA.getAllSleepData();
        if (mySleepDataArr.size() <= 0) {
            SleepData sampleRecord1 = new SleepData(sleepDataDA.generateNewSleepDataID(), userLocalStore.returnUserID().toString(), 3, new DateTime("2016-01-26 23:05:00"), new DateTime().getCurrentDateTime());
            SleepData sampleRecord2 = new SleepData(sleepDataDA.generateNewSleepDataID(), userLocalStore.returnUserID().toString(), 7, new DateTime("2016-01-26 23:55:00"), new DateTime().getCurrentDateTime());
            SleepData sampleRecord3 = new SleepData(sleepDataDA.generateNewSleepDataID(), userLocalStore.returnUserID().toString(), 2, new DateTime("2016-01-27 01:55:00"), new DateTime().getCurrentDateTime());
            SleepData sampleRecord4 = new SleepData(sleepDataDA.generateNewSleepDataID(), userLocalStore.returnUserID().toString(), 1, new DateTime("2016-01-27 03:18:00"), new DateTime().getCurrentDateTime());
            SleepData sampleRecord5 = new SleepData(sleepDataDA.generateNewSleepDataID(), userLocalStore.returnUserID().toString(), 10, new DateTime("2016-01-27 06:07:00"), new DateTime().getCurrentDateTime());
            sleepDataDA.addSleepData(sampleRecord1);
            sleepDataDA.addSleepData(sampleRecord2);
            sleepDataDA.addSleepData(sampleRecord3);
            sleepDataDA.addSleepData(sampleRecord4);
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
            nextDay.setTextColor(Color.GRAY);
        } else {
            nextDay.setEnabled(true);
            nextDay.setTextColor(Color.WHITE);
        }

        if(!mySleepDataArr.isEmpty()) {
            //get my sleep time
            sleepTime = mySleepDataArr.get(0).getCreated_at();
            boolean continueCount = true;
            myRealTimeArr = realTimeFitnessDA.getAllRealTimeFitnessBeforeLimit(sleepTime);
            for (int i = myRealTimeArr.size() - 1; i >= 0 && continueCount; i--) {
                if (myRealTimeArr.get(i).getStepNumber() > 0) {
                    continueCount = false;
                    sleepTime.setTime(String.format("%02d:00:00", i));
                }
            }
            //get my wake up time
            wakeUpTime = mySleepDataArr.get(mySleepDataArr.size() - 1).getCreated_at();
            continueCount = true;
            myRealTimeArr = realTimeFitnessDA.getAllRealTimeFitnessAfterLimit(sleepTime);
            for (int i = 0; i < myRealTimeArr.size() && continueCount; i++) {
                if (myRealTimeArr.get(i).getStepNumber() > 0) {
                    continueCount = false;
                    wakeUpTime.setTime(String.format("%02d:00:00", i - 1));
                }
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(generateSleepDataPoint());
            series.setColor(Color.parseColor("#FFFFFF"));
            graph.addSeries(series);
            TotalSleepTimeValue.setText(getTotalSleepTime().getDuration());
            AsleepTimeValue.setText(getAsleepTime().getDuration());
            TimesAwakenValue.setText(getTimesAwaken()+"");
            SleepQualityValue.setText(String.format("%.2f %%",calSleepQuality()));
        }else {
            Toast.makeText(this, "No Sleep Record in this day.", Toast.LENGTH_LONG).show();
            TotalSleepTimeValue.setText("--");
            AsleepTimeValue.setText("--");
            TimesAwakenValue.setText("--");
            SleepQualityValue.setText("--");
        }
    }

    private DataPoint[] generateSleepDataPoint() {
        int i = 0;
        DataPoint[] values = new DataPoint[mySleepDataArr.size()*5];
        DataPoint initialDP = new DataPoint(0, 0);
        values[i] = initialDP;
        i++;
        for(int j=0; j < mySleepDataArr.size() && i < values.length; j++){
            DataPoint startDP = new DataPoint(0, 0);
            values[i] = startDP;
            i++;
            DataPoint movementDP = new DataPoint(mySleepDataArr.get(j).getCreated_at().getDateTimeFloat(), mySleepDataArr.get(j).getMovement());
            values[i] = movementDP;
            i++;
            DataPoint endDP = new DataPoint(0, 0);
            values[i] = endDP;
            i++;
        }
        DataPoint deInitialDP = new DataPoint(0, 0);
        values[i] = deInitialDP;
        i++;
        return values;
    }

    public Duration getTotalSleepTime(){
        FitnessFormula myFormula = new FitnessFormula(this);
        Duration sleepDuration = new Duration();
        sleepDuration = myFormula.calculationDuration(sleepTime, wakeUpTime);
        return sleepDuration;
    }

    public int getTimesAwaken(){
        int movement = 0;
        for(int i=0; i< mySleepDataArr.size(); i++){
            movement += mySleepDataArr.get(i).getMovement();
        }
        return movement;
    }

    public Duration getAsleepTime(){
        int moveSecond = getTimesAwaken();
        int totalSleep = getTotalSleepTime().getTotalSeconds();
        int asleepSeconds = totalSleep - moveSecond;
        Duration myDuration = new Duration();
        myDuration.addSeconds(asleepSeconds);
        return myDuration;
    }

    public double calSleepQuality(){
        int totalSleep = getTotalSleepTime().getTotalSeconds();
        if(totalSleep==0){
            totalSleep ++;
        }
        return 100 * getAsleepTime().getTotalSeconds() / totalSleep;
    }

    public void graphUIConfiguration() {
        graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setScrollable(true);
        graph.setScrollContainer(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(4);
        graph.getViewport().setMinX(0);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Times Movement");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.parseColor("#FFFFFF"));
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.parseColor("#FFFFFF"));
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    if (value < 10) {
                        return "0" + super.formatLabel(value, isValueX) + ":00";
                    } else {
                        return super.formatLabel(value, isValueX) + ":00";
                    }
                } else {
                    // show y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });
        graph.getGridLabelRenderer().setGridColor(Color.parseColor("#FFFFFF"));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#FFFFFF"));
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