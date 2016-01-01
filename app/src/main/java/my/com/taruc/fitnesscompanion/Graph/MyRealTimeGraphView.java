package my.com.taruc.fitnesscompanion.Graph;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import at.markushi.ui.CircleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Classes.RealTimeFitness;
import my.com.taruc.fitnesscompanion.Database.ActivityPlanDA;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.Database.RealTimeFitnessDA;
import my.com.taruc.fitnesscompanion.R;

public class MyRealTimeGraphView extends Activity {

    GraphView graph;
    ArrayList<RealTimeFitness> myRealTimeFitnessArr = new ArrayList();
    ArrayList<FitnessRecord> myFitnessRecordArr = new ArrayList();
    RealTimeFitnessDA realTimeFitnessDa;
    FitnessRecordDA fitnessRecordDa;
    ActivityPlanDA myActivityPlanDA;
    RealTimeFitness realTimeFitness;

    TextView datedisplay;
    TextView activityTxt;
    TextView startTimeTxt;
    TextView endTimeTxt;
    TextView durationTxt;
    TextView stepNumTxt;
    TextView caloriesTxt;
    TextView distanceTxt;
    TextView averageHRTxt;

    DateTime todayDate;
    DateTime displayDate;
    @Bind(R.id.changeViewButton)
    CircleButton changeViewButton;
    @Bind(R.id.textViewHistoryTitle)
    TextView textViewHistoryTitle;
    @Bind(R.id.previousDay)
    Button previousDay;
    @Bind(R.id.nextDay)
    Button nextDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_view);
        ButterKnife.bind(this);

        datedisplay = (TextView) findViewById(R.id.DateDisplay);
        activityTxt = (TextView) findViewById(R.id.ActivityDisplay);
        startTimeTxt = (TextView) findViewById(R.id.StartTimeDisplay);
        endTimeTxt = (TextView) findViewById(R.id.EndTimeDisplay);
        durationTxt = (TextView) findViewById(R.id.DurationDisplay);
        stepNumTxt = (TextView) findViewById(R.id.StepNumDisplay);
        caloriesTxt = (TextView) findViewById(R.id.CaloriesDisplay);
        distanceTxt = (TextView) findViewById(R.id.DistanceDisplay);
        averageHRTxt = (TextView) findViewById(R.id.AveHRDisplay);


        textViewHistoryTitle.setText("Real Time History");

        //Initial Fitness Data
        realTimeFitnessDa = new RealTimeFitnessDA(this);
        fitnessRecordDa = new FitnessRecordDA(this);
        myActivityPlanDA = new ActivityPlanDA(this);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateformat.format(calendar.getTime());
        todayDate = new DateTime(dateString);
        displayDate = new DateTime(todayDate.getDateTime());

        graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setScrollable(true);
        graph.setScrollContainer(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(4);
        graph.getViewport().setMinX(0);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Step");
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
        createGraphView();
    }

    public void BackAction(View view) {
        this.finish();
    }

    public void changeView(View view) {
        finish();
        Intent intent = new Intent(this, MyExerciseGraphView.class);
        startActivity(intent);
    }

    private void createGraphView() {
        myRealTimeFitnessArr = realTimeFitnessDa.getAllRealTimeFitnessPerDay(displayDate);
        myFitnessRecordArr = fitnessRecordDa.getAllFitnessRecordPerDay(displayDate);

        graph.removeAllSeries();

        datedisplay.setText(displayDate.getDate().getFullDate());
        if(datedisplay.getText().equals(todayDate.getDate().getFullDate())){
            nextDay.setEnabled(false);
        }else{
            nextDay.setEnabled(true);
        }

        //initial graph start to end
        LineGraphSeries<DataPoint> seriesStart = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 0)
        });
        graph.addSeries(seriesStart);
        LineGraphSeries<DataPoint> seriesEnd = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(24, 0)
        });
        graph.addSeries(seriesEnd);

        //add fitness data block to chart
        /*if(myFitnessRecordArr.size()<=0){
            Toast.makeText(this, "No Fitness Records exist in database.",Toast.LENGTH_LONG).show();
        }else {
            ArrayList<LineGraphSeries<DataPoint>> lineGraphSeriesArr = generateFitnessRecordData();
            for (int i = 0; i < lineGraphSeriesArr.size(); i++) {
                graph.addSeries(lineGraphSeriesArr.get(i));
            }
        }*/

        //add real time data to graph
        if (!myRealTimeFitnessArr.isEmpty()) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(generateRealTimeDataPoint());
            series.setColor(Color.parseColor("#FFFFFF"));
            graph.addSeries(series);
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    //Toast.makeText(MyRealTimeGraphView.this, "Series1: On Data Point clicked: " + dataPoint.getX(), Toast.LENGTH_SHORT).show();
                    displayRealTimeData(dataPoint);
                }
            });
        } else {
            Toast.makeText(this, "No Real Time Fitness Records in this day.", Toast.LENGTH_LONG).show();
        }
    }

    private DataPoint[] generateRealTimeDataPoint() {
        DataPoint[] values = new DataPoint[myRealTimeFitnessArr.size()];
        for (int i = 0; i < myRealTimeFitnessArr.size(); i++) {
            double x = myRealTimeFitnessArr.get(i).getCaptureDateTime().getTime().getHour();
            double y = myRealTimeFitnessArr.get(i).getStepNumber();
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    private void displayRealTimeData(DataPointInterface myDataPoint) {
        int tappedTime = (int) (myDataPoint.getX());
        realTimeFitnessDa = new RealTimeFitnessDA(this);
        RealTimeFitness tappedRecord = realTimeFitnessDa.getRealTimeFitnessByDateTime(displayDate.getDate().getFullDate() + " " + tappedTime + ":0:0");
        if (tappedRecord != null) {
            //Activity txt view
            String myActivity;
            if (myDataPoint.getY() > (10 * 6 * 303)) {
                //Running - 6 mph - 10 minute miles - 303
                //url http://walking.about.com/od/measure/a/stepequivalents.htm
                myActivity = "Running";
            } else if (myDataPoint.getY() > 0) {
                myActivity = "Walking";
            } else {
                myActivity = "Sedentary";
            }
            activityTxt.setText(myActivity);
            //Start time txt view
            DateTime startTime = tappedRecord.getCaptureDateTime();
            boolean sameStartTime = true;
            int tapStartTime = tappedTime;
            int tempCountStart = tapStartTime;
            RealTimeFitness PreviousRecord;
            while (tempCountStart > 0 && sameStartTime) {
                PreviousRecord = realTimeFitnessDa.getRealTimeFitnessByDateTime(displayDate.getDate().getFullDate() + " " + tempCountStart + ":0:0");
                if (PreviousRecord != null) { //null record at specific time
                    if (tappedRecord.getStepNumber() == PreviousRecord.getStepNumber()) {
                        PreviousRecord = realTimeFitnessDa.getRealTimeFitnessByDateTime(displayDate.getDate().getFullDate() + " " + (tempCountStart - 1) + ":0:0");
                        if (PreviousRecord != null) { //null record at specific time
                            startTime = PreviousRecord.getCaptureDateTime();
                        } else {
                            startTime.getTime().setHour(startTime.getTime().getHour() - 1);
                        }
                    } else {
                        sameStartTime = false;
                    }
                } else {
                    startTime.getTime().setHour(startTime.getTime().getHour() - 1);
                }
                tempCountStart--;
            }
            startTimeTxt.setText(startTime.getTime().getFullTime());
            //End time txt view
            int tapEndTime = tappedTime;
            DateTime endTime = tappedRecord.getCaptureDateTime();
            boolean sameEndTime = true;
            int tempCountEnd = tapEndTime;
            while (tempCountEnd < myRealTimeFitnessArr.size() && sameEndTime) {
                RealTimeFitness NextRecord = realTimeFitnessDa.getRealTimeFitnessByDateTime(displayDate.getDate().getFullDate() + " " + tempCountEnd + ":0:0");
                if (NextRecord != null) {
                    if (tappedRecord.getStepNumber() == NextRecord.getStepNumber()) {
                        endTime = NextRecord.getCaptureDateTime();
                    } else {
                        sameEndTime = false;
                    }
                }
                tempCountEnd++;
            }
            endTimeTxt.setText(endTime.getTime().getFullTime());
            //duration txt view
            durationTxt.setText((endTime.getTime().getHour() - startTime.getTime().getHour()) + " hour(s)");
            //step num txt view
            int stepNum = 0;
            for (int i = startTime.getTime().getHour(); i < endTime.getTime().getHour(); i++) {
                realTimeFitness = realTimeFitnessDa.getRealTimeFitnessByDateTime(displayDate.getDate().getFullDate() + " " + (i + 1) + ":0:0");
                if (realTimeFitness != null) {
                    stepNum += realTimeFitnessDa.getRealTimeFitnessByDateTime(displayDate.getDate().getFullDate() + " " + (i + 1) + ":0:0").getStepNumber();
                }
            }
            stepNumTxt.setText(stepNum + " step(s)");
            //one calorie for every 20 steps
            //url http://www.livestrong.com/article/320124-how-many-calories-does-the-average-person-use-per-step/
            caloriesTxt.setText(stepNum * 1 / 20 + " calories");
            distanceTxt.setText("-");
            averageHRTxt.setText("-");
        } else {
            clearDetail();
        }
    }


    private void clearDetail() {
        activityTxt.setText("-");
        startTimeTxt.setText("-");
        endTimeTxt.setText("-");
        durationTxt.setText("-");
        stepNumTxt.setText("-");
        caloriesTxt.setText("-");
        distanceTxt.setText("-");
        averageHRTxt.setText("-");
    }

    public void PreviousDayClick(View view) {
        displayDate.getDate().setDate(displayDate.getDate().getDate() - 1);
        createGraphView();
        clearDetail();
    }

    public void NextDayClick(View view) {
        if (!displayDate.getDate().getFullDate().equals(todayDate.getDate().getFullDate())) {
            displayDate.getDate().setDate(displayDate.getDate().getDate() + 1);
            createGraphView();
            clearDetail();
        }
    }

    public String getActivityPlanName(String activityPlanID) {
        ActivityPlan activityPlan = myActivityPlanDA.getActivityPlan(activityPlanID);
        if (activityPlan != null) {
            return activityPlan.getActivityName();
        } else {
            Toast.makeText(this, "Fail to get activity plan.", Toast.LENGTH_SHORT);
            return "";
        }
    }

    /*

    private ArrayList<LineGraphSeries<DataPoint>> generateFitnessRecordData(){
        ArrayList<LineGraphSeries<DataPoint>> values = new ArrayList<LineGraphSeries<DataPoint>>();
        DateTime datetime ;
        DateTime.Time StartTime;
        DateTime.Time EndTime;
        double startPoint =0.0;
        double endPoint =0.0;
        for (int i=0; i<myFitnessRecordArr.size(); i++) {
            datetime = new DateTime(myFitnessRecordArr.get(i).getCreateAt());
            StartTime = datetime.getTime();
            EndTime = StartTime.addDuration(myFitnessRecordArr.get(i).getRecordDuration());
            startPoint = StartTime.getHour() + (StartTime.getMinutes() / 60.0);
            endPoint = EndTime.getHour() + (EndTime.getMinutes() / 60.0);
            LineGraphSeries<DataPoint>[] FitnessRecordSeriesArr = generateFitnessRecordSeries(startPoint, endPoint, getMaxStepNum(), myFitnessRecordArr.get(i));
            for (int j = 0; j < FitnessRecordSeriesArr.length; j++) {
                values.add(FitnessRecordSeriesArr[j]);
            }
        }
        return values;
    }

    private LineGraphSeries<DataPoint>[] generateFitnessRecordSeries(double startPoint, double endPoint, int maxNum, FitnessRecord fitnessRecord){
        LineGraphSeries<DataPoint>[] values = new LineGraphSeries[maxNum+1];
        final FitnessRecord myFitnessRecord = fitnessRecord;
        for (int i = maxNum; i >= 0; i--){
            LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
                    new DataPoint(startPoint, i),
                    new DataPoint(endPoint, i),
            });
            lineGraphSeries.setBackgroundColor(Color.parseColor("#E0E0E0"));
            lineGraphSeries.setDrawBackground(true);
            lineGraphSeries.setColor(Color.parseColor("#E0E0E0"));
            lineGraphSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    displayFitnessRecordData(myFitnessRecord);
                }
            });
            values[i] = lineGraphSeries;
        }
        return values;
    }

    private void displayFitnessRecordData (FitnessRecord fitnessRecord){
        if(fitnessRecord!=null) {
            activityTxt.setText(getActivityPlanName(fitnessRecord.getActivityPlanID()));
            //get Start Time
            DateTime StartDateTime = new DateTime(fitnessRecord.getCreateAt());
            startTimeTxt.setText(StartDateTime.getTime().getFullTime());
            //get End Time
            DateTime.Time EndTime = StartDateTime.getTime().addDuration(fitnessRecord.getRecordDuration());
            endTimeTxt.setText(EndTime.getFullTime());
            int duration = fitnessRecord.getRecordDuration();
            durationTxt.setText(duration / 3600 + ":" + ((duration / 60)-(duration / 3600*60)) + ":" + duration % 60 + "");
            //stepNumTxt.setText(fitnessRecord.getRecordStep() + "");
            stepNumTxt.setText("-");
            caloriesTxt.setText(fitnessRecord.getRecordCalories() + " joules");
            distanceTxt.setText(fitnessRecord.getRecordDistance() + " meters");
            averageHRTxt.setText(fitnessRecord.getAverageHeartRate() + " bpm");
        }
    }

    private int getMaxStepNum(){
        int max=2;
        for (int i=0; i<myRealTimeFitnessArr.size(); i++){
            if (max<myRealTimeFitnessArr.get(i).getStepNumber()){
                max = myRealTimeFitnessArr.get(i).getStepNumber();
            }
        }
        return max;
    }
     */
}
