package my.com.saiboon.fitnesscompanion.Graph;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import my.com.saiboon.fitnesscompanion.R;
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

import my.com.saiboon.fitnesscompanion.Classes.DateTime;
import my.com.saiboon.fitnesscompanion.Classes.FitnessRecord;
import my.com.saiboon.fitnesscompanion.Classes.RealTimeFitness;
import my.com.saiboon.fitnesscompanion.Database.FitnessRecordDA;
import my.com.saiboon.fitnesscompanion.Database.RealTimeFitnessDA;

public class MyGraphView extends Activity {

    GraphView graph;
    ArrayList<RealTimeFitness> myRealTimeFitnessArr = new ArrayList();
    ArrayList<FitnessRecord> myFitnessRecordArr = new ArrayList();
    RealTimeFitnessDA realTimeFitnessDa;
    FitnessRecordDA fitnessRecordDa;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_view);

        datedisplay = (TextView) findViewById(R.id.DateDisplay);
        activityTxt = (TextView) findViewById(R.id.ActivityDisplay);
        startTimeTxt = (TextView) findViewById(R.id.StartTimeDisplay);
        endTimeTxt = (TextView) findViewById(R.id.EndTimeDisplay);
        durationTxt = (TextView) findViewById(R.id.DurationDisplay);
        stepNumTxt = (TextView) findViewById(R.id.StepNumDisplay);
        caloriesTxt = (TextView) findViewById(R.id.CaloriesDisplay);
        distanceTxt = (TextView) findViewById(R.id.DistanceDisplay);
        averageHRTxt = (TextView) findViewById(R.id.AveHRDisplay);

        //Initial Fitness Data
        realTimeFitnessDa = new RealTimeFitnessDA(this);
        fitnessRecordDa = new FitnessRecordDA(this);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString =  dateformat.format(calendar.getTime());
        todayDate = new DateTime(dateString);
        displayDate = new DateTime(todayDate.getDateTime());

        graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setScrollable(true);
        graph.setScrollContainer(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(4);
        graph.getViewport().setMinX(0);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Step");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
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
        createGraphView();
    }

    private void createGraphView(){
        myRealTimeFitnessArr = realTimeFitnessDa.getAllRealTimeFitnessPerDay(displayDate);
        myFitnessRecordArr = fitnessRecordDa.getAllFitnessRecord();
        graph.removeAllSeries();

        datedisplay.setText(displayDate.getDate().getFullDate());

        //initial graph start to end
        LineGraphSeries<DataPoint> seriesStart = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 0)
        });
        graph.addSeries(seriesStart);
        LineGraphSeries<DataPoint> seriesEnd = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(24, 0)
        });
        graph.addSeries(seriesEnd);

        //add fitness data block to chart
        ArrayList<LineGraphSeries<DataPoint>> lineGraphSeriesArr =  generateFitnessRecordData();
        for (int i=0; i<lineGraphSeriesArr.size(); i++){
            graph.addSeries(lineGraphSeriesArr.get(i));
        }

        //add real time data to graph
        if(!myRealTimeFitnessArr.isEmpty()) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(generateRealTimeDataPoint());
            graph.addSeries(series);
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    //Toast.makeText(MyGraphView.this, "Series1: On Data Point clicked: " + dataPoint.getX(), Toast.LENGTH_SHORT).show();
                    displayRealTimeData(dataPoint);
                }
            });
        }
    }

    private DataPoint[] generateRealTimeDataPoint() {
        DataPoint[] values  = new DataPoint[myRealTimeFitnessArr.size()];
        for (int i = 0; i < myRealTimeFitnessArr.size(); i++) {
            double x = myRealTimeFitnessArr.get(i).getCaptureDateTime().getTime().getHour();
            double y = myRealTimeFitnessArr.get(i).getStepNumber();
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    private ArrayList<LineGraphSeries<DataPoint>> generateFitnessRecordData(){
        ArrayList<LineGraphSeries<DataPoint>> values = new ArrayList<LineGraphSeries<DataPoint>>();
        DateTime datetime ;
        DateTime.Time StartTime;
        DateTime.Time EndTime;
        double startPoint =0.0;
        double endPoint =0.0;
        for (int i=0; i<myFitnessRecordArr.size(); i++) {
            datetime = new DateTime(myFitnessRecordArr.get(i).getFitnessRecordDateTime());
            StartTime = datetime.getTime();
            EndTime = StartTime.addDuration(myFitnessRecordArr.get(i).getRecordDuration());
            startPoint = StartTime.getHour()+ (StartTime.getMinutes()/60.0);
            endPoint = EndTime.getHour()+ (EndTime.getMinutes()/60.0);
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
        for (int i = 0; i <= maxNum; i++){
            LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
                    new DataPoint(startPoint, i),
                    new DataPoint(endPoint, i),
            });
            lineGraphSeries.setBackgroundColor(Color.parseColor("#E0E0E0"));
            lineGraphSeries.setDrawBackground(true);
            lineGraphSeries.setColor(Color.parseColor("#E0E0E0"));
            final int arrNum = i;
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

    private int getMaxStepNum(){
        int max=0;
        for (int i=0; i<myRealTimeFitnessArr.size(); i++){
            if (max<myRealTimeFitnessArr.get(i).getStepNumber()){
                max = myRealTimeFitnessArr.get(i).getStepNumber();
            }
        }
        return max;
    }

    private void displayRealTimeData (DataPointInterface myDataPoint){
        int tappedTime = (int) (myDataPoint.getX());
        RealTimeFitnessDA realTimeFitnessDA = new RealTimeFitnessDA(this);
        System.out.print(myRealTimeFitnessArr.get(1).getCaptureDateTime());
        RealTimeFitness tappedRecord = realTimeFitnessDA.getRealTimeFitnessByDateTime(displayDate.getDate().getFullDate() + " " + tappedTime + ":0:0");
        if(tappedRecord!=null) {
            //Activity txt view
            String myActivity;
            if (myDataPoint.getY() > 80) {
                myActivity = "Running";
            } else if (myDataPoint.getY() > 0) {
                myActivity = "Walking";
            } else {
                myActivity = "Sedantory";
            }
            activityTxt.setText(myActivity);
            //Start time txt view
            DateTime startTime = tappedRecord.getCaptureDateTime();
            boolean sameStartTime = true;
            int tapStartTime = tappedTime;
            int tempCountStart = tapStartTime;
            while (tempCountStart >= 0 && sameStartTime) {
                RealTimeFitness PreviousRecord = realTimeFitnessDA.getRealTimeFitnessByDateTime(displayDate.getDate().getFullDate() + " " + tempCountStart + ":0:0");
                if (tappedRecord.getStepNumber() == PreviousRecord.getStepNumber()) {
                    PreviousRecord = realTimeFitnessDA.getRealTimeFitnessByDateTime(displayDate.getDate().getFullDate() + " " + (tempCountStart-1) + ":0:0");
                    startTime = PreviousRecord.getCaptureDateTime();
                } else {
                    sameStartTime = false;
                }
                tempCountStart--;
            }
            startTimeTxt.setText(startTime.getTime().getFullTime());
            //End time txt view
            int tapEndTime = tappedTime;
            DateTime endTime = tappedRecord.getCaptureDateTime();
            boolean sameEndTime = true;
            int tempCountEnd = tapEndTime;
            while (tempCountEnd < 24 && sameEndTime) {
                RealTimeFitness NextRecord = realTimeFitnessDA.getRealTimeFitnessByDateTime(displayDate.getDate().getFullDate() + " " + tempCountEnd + ":0:0");
                if (tappedRecord.getStepNumber() == NextRecord.getStepNumber()) {
                    endTime = NextRecord.getCaptureDateTime();
                } else {
                    sameEndTime = false;
                }
                tempCountEnd++;
            }
            endTimeTxt.setText(endTime.getTime().getFullTime());
            //duration txt view
            durationTxt.setText((endTime.getTime().getHour() - startTime.getTime().getHour()) + " hour(s)");
            //step num txt view
            int stepNum = 0;
            for (int i = startTime.getTime().getHour(); i < endTime.getTime().getHour(); i++) {
                stepNum += realTimeFitnessDA.getRealTimeFitnessByDateTime(displayDate.getDate().getFullDate() + " " + (i+1) + ":0:0").getStepNumber();
            }
            stepNumTxt.setText(stepNum + " step(s)");
            caloriesTxt.setText("-");
            distanceTxt.setText("-");
            averageHRTxt.setText("-");
        }else{
            clearDetail();
        }
    }

    private void displayFitnessRecordData (FitnessRecord fitnessRecord){
        activityTxt.setText(fitnessRecord.getFitnessActivity());
        //get Start Time
        DateTime StartDateTime = new DateTime(fitnessRecord.getFitnessRecordDateTime());
        startTimeTxt.setText(StartDateTime.getTime().getFullTime());
        //get End Time
        DateTime.Time EndTime = StartDateTime.getTime().addDuration(fitnessRecord.getRecordDuration());
        endTimeTxt.setText(EndTime.getFullTime());
        int duration = fitnessRecord.getRecordDuration();
        durationTxt.setText(duration/3600 + " hour(s) " + duration/60 + " minute(s) " + duration%60 + " second(s)");
        stepNumTxt.setText(fitnessRecord.getRecordStep()+"");
        caloriesTxt.setText(fitnessRecord.getRecordCalories()+"");
        distanceTxt.setText(fitnessRecord.getRecordDistance() + "");
        averageHRTxt.setText(fitnessRecord.getAverageHeartRate()+"");
    }

    private void clearDetail(){
        activityTxt.setText("-");
        startTimeTxt.setText("-");
        endTimeTxt.setText("-");
        durationTxt.setText("-");
        stepNumTxt.setText("-");
        caloriesTxt.setText("-");
        distanceTxt.setText("-");
        averageHRTxt.setText("-");
    }

    public void PreviousDayClick(View view){
        displayDate.getDate().setDate(displayDate.getDate().getDate()-1);
        createGraphView();
    }

    public void NextDayClick(View view){
        if(!displayDate.getDate().getFullDate().equals(todayDate.getDate().getFullDate())){
            displayDate.getDate().setDate(displayDate.getDate().getDate()+1);
            createGraphView();
        }
    }
}
