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
    RealTimeFitnessDA realTimeFitnessDa;
    FitnessRecordDA fitnessRecordDa;
    ActivityPlanDA myActivityPlanDA;

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

    //Running - 6 mph - 10 minute miles - 303
    //url http://walking.about.com/od/measure/a/stepequivalents.htm
    final int BasicRunStepNumber = (10 * 6 * 303);

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
            if(myRealTimeFitnessArr.get(i).getCaptureDateTime().getDate().getDate()!= displayDate.getDate().getDate()){
                if(myRealTimeFitnessArr.get(i).getCaptureDateTime().getTime().getHour()==0){
                    myRealTimeFitnessArr.get(i).getCaptureDateTime().getTime().setHour(24);
                    double x =24;
                    double y = myRealTimeFitnessArr.get(i).getStepNumber();
                    DataPoint v = new DataPoint(x, y);
                    values[i] = v;
                }
            }else{
                double x = myRealTimeFitnessArr.get(i).getCaptureDateTime().getTime().getHour();
                double y = myRealTimeFitnessArr.get(i).getStepNumber();
                DataPoint v = new DataPoint(x, y);
                values[i] = v;
            }
        }
        return values;
    }

    private void displayRealTimeData(DataPointInterface myDataPoint) {
        int tappedTime = (int) (myDataPoint.getX());
        int selectedRecordIndex = -1;
        int j=0;
        do{
            if(tappedTime == myRealTimeFitnessArr.get(j).getCaptureDateTime().getTime().getHour()){
                selectedRecordIndex = j;
            }
            j++;
        }while ( j < myRealTimeFitnessArr.size());
        if (selectedRecordIndex >= 0) {
            //set activity name
            setActivityName(myDataPoint);
            //Start time txt view
            int startTimeIndex = setStartTime(selectedRecordIndex);
            //End time txt view
            int endTimeIndex = setEndTime(selectedRecordIndex);
            //duration txt view
            setDuration(startTimeIndex, endTimeIndex);
            //step num txt view
            int stepNum = setStep(startTimeIndex, endTimeIndex);
            //one calorie for every 20 steps
            //url http://www.livestrong.com/article/320124-how-many-calories-does-the-average-person-use-per-step/
            caloriesTxt.setText(stepNum * 1 / 20 + " calories");
            distanceTxt.setText("-");
            averageHRTxt.setText("-");
        } else {
            clearDetail();
        }
    }

    public void setActivityName(DataPointInterface myDataPoint){
        //Activity txt view
        String myActivity;
        if (myDataPoint.getY() > BasicRunStepNumber) {
            myActivity = "Running";
        } else if (myDataPoint.getY() > 0) {
            myActivity = "Walking";
        } else {
            myActivity = "Sedentary";
        }
        activityTxt.setText(myActivity);
    }

    public int setStartTime(int tappedRecordIndex){
        DateTime startTime = myRealTimeFitnessArr.get(tappedRecordIndex).getCaptureDateTime();
        while(tappedRecordIndex > 0){
            if(sameActivity(myRealTimeFitnessArr.get(tappedRecordIndex - 1).getStepNumber())){
                tappedRecordIndex--;
                startTime = myRealTimeFitnessArr.get(tappedRecordIndex).getCaptureDateTime();
            }
        }
        startTimeTxt.setText(startTime.getTime().getFullTime());
        return tappedRecordIndex;
    }

    public int setEndTime(int tappedRecordIndex){
        DateTime endTime = myRealTimeFitnessArr.get(tappedRecordIndex).getCaptureDateTime();
        while(tappedRecordIndex < myRealTimeFitnessArr.size()-1){
            if(sameActivity(myRealTimeFitnessArr.get(tappedRecordIndex + 1).getStepNumber())){
                tappedRecordIndex++;
                endTime = myRealTimeFitnessArr.get(tappedRecordIndex).getCaptureDateTime();
            }
        }
        endTimeTxt.setText(endTime.getTime().getFullTime());
        return tappedRecordIndex;
    }

    public void setDuration(int startTimeIndex, int endTimeIndex){
        DateTime endTime = myRealTimeFitnessArr.get(endTimeIndex).getCaptureDateTime();
        DateTime startTime = myRealTimeFitnessArr.get(startTimeIndex).getCaptureDateTime();
        durationTxt.setText((endTime.getTime().getHour() - startTime.getTime().getHour()) + " hour(s)");
    }

    public int setStep(int startTimeIndex, int endTimeIndex){
        int stepNum = 0;
        do{
            stepNum += myRealTimeFitnessArr.get(startTimeIndex).getStepNumber();
            startTimeIndex++;
        }while(startTimeIndex != endTimeIndex);
        stepNumTxt.setText(stepNum + " step(s)");
        return stepNum;
    }

    // compare whether previous record or next record having same activity name
    public boolean sameActivity(int stepNumber){
        if (activityTxt.getText().equals("Running")){
            return (stepNumber > BasicRunStepNumber);
        }else if(activityTxt.getText().equals("Walking")){
            return (stepNumber <= BasicRunStepNumber && stepNumber > 0);
        }else{
            return (stepNumber <= 0);
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

    //testing prupose
    public DateTime getCurrentDateTime(int i){
        Calendar calendar = Calendar.getInstance();
        int hour = i;
        String min = "00";
        String second = "00";
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String mydate = dateformat.format(calendar.getTime());
        String mytime = hour + ":" + min + ":" + second;
        return new DateTime(mydate + " " + mytime);
    }

}
