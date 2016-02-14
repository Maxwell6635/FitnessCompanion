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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
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

    Context context;
    String selectedView = "RealTime History";
    String[] viewName = new String[]{"Activity History", "RealTime History", "Sleep Data"};

    //Running - 6 mph - 10 minute miles - 303
    //url http://walking.about.com/od/measure/a/stepequivalents.htm
    final int BasicRunStepNumber = (10 * 6 * 303);

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
    @Bind(R.id.textViewTitle)
    TextView textViewHistoryTitle;
    @Bind(R.id.previousDay)
    ImageView previousDay;
    @Bind(R.id.nextDay)
    ImageView nextDay;
    @Bind(R.id.textViewChangeView)
    TextView textViewChangeView;
    @Bind(R.id.TableDetail)
    TableLayout TableDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_graph_view);
        ButterKnife.bind(this);
        context = this;

        datedisplay = (TextView) findViewById(R.id.DateDisplay);
        activityTxt = (TextView) findViewById(R.id.ActivityDisplay);
        startTimeTxt = (TextView) findViewById(R.id.StartTimeDisplay);
        endTimeTxt = (TextView) findViewById(R.id.EndTimeDisplay);
        durationTxt = (TextView) findViewById(R.id.DurationDisplay);
        stepNumTxt = (TextView) findViewById(R.id.StepNumDisplay);
        caloriesTxt = (TextView) findViewById(R.id.CaloriesDisplay);
        distanceTxt = (TextView) findViewById(R.id.DistanceDisplay);
        averageHRTxt = (TextView) findViewById(R.id.AveHRDisplay);

        textViewHistoryTitle.setText(R.string.realTimeHistory);

        //Initial Fitness Data
        realTimeFitnessDa = new RealTimeFitnessDA(this);
        fitnessRecordDa = new FitnessRecordDA(this);
        myActivityPlanDA = new ActivityPlanDA(this);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateformat.format(calendar.getTime());
        todayDate = new DateTime(dateString);
        displayDate = new DateTime(todayDate.getDateTimeString());

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

    private void createGraphView() {
        myRealTimeFitnessArr = realTimeFitnessDa.getAllRealTimeFitnessPerDay(displayDate);
        graph.removeAllSeries();

        datedisplay.setText(displayDate.getDate().getFullDateString());
        if (datedisplay.getText().equals(todayDate.getDate().getFullDateString())) {
            nextDay.setEnabled(false);
            nextDay.setVisibility(View.INVISIBLE);
        } else {
            nextDay.setEnabled(true);
            nextDay.setVisibility(View.VISIBLE);
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
            visibleDetails(View.VISIBLE);
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
            visibleDetails(View.INVISIBLE);
            //Toast.makeText(this, "No Real Time Fitness Records in this day.", Toast.LENGTH_LONG).show();
        }
    }

    private void visibleDetails(int visibility) {
        TableDetail.setVisibility(visibility);
    }

    private DataPoint[] generateRealTimeDataPoint() {
        DataPoint[] values = new DataPoint[myRealTimeFitnessArr.size()];
        for (int i = 0; i < myRealTimeFitnessArr.size(); i++) {
            if (myRealTimeFitnessArr.get(i).getCaptureDateTime().getDate().getDateNumber() != displayDate.getDate().getDateNumber()) {
                if (myRealTimeFitnessArr.get(i).getCaptureDateTime().getTime().getHour() == 0) {
                    //if 00:00 then change to 24:00
                    myRealTimeFitnessArr.get(i).getCaptureDateTime().getTime().setHour24(24);
                    double x = 24;
                    double y = myRealTimeFitnessArr.get(i).getStepNumber();
                    DataPoint v = new DataPoint(x, y);
                    values[i] = v;
                }
            } else {
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
        int j = 0;
        do {
            if (tappedTime == myRealTimeFitnessArr.get(j).getCaptureDateTime().getTime().getHour()) {
                selectedRecordIndex = j;
            }
            j++;
        } while (j < myRealTimeFitnessArr.size() && selectedRecordIndex < 0);
        if (selectedRecordIndex >= 0) {
            //set activity name
            setActivityName(myDataPoint);
            //End time txt view
            int endTimeIndex = setEndTime(selectedRecordIndex);
            //Start time txt view
            int startTimeIndex = setStartTime(selectedRecordIndex);
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

    public void setActivityName(DataPointInterface myDataPoint) {
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

    public int setStartTime(int tappedRecordIndex) {
        boolean noSameActivityAlready = true;
        DateTime startTime = new DateTime(myRealTimeFitnessArr.get(tappedRecordIndex).getCaptureDateTime().getDateTimeString());
        int startTimeIndex = tappedRecordIndex;
        while (tappedRecordIndex > 0 && noSameActivityAlready) {
            if (sameActivity(myRealTimeFitnessArr.get(tappedRecordIndex - 1).getStepNumber())) {
                startTime = new DateTime(myRealTimeFitnessArr.get(tappedRecordIndex - 1).getCaptureDateTime().getDateTimeString());
                startTimeIndex = tappedRecordIndex - 1;
            } else {
                noSameActivityAlready = false;
            }
            tappedRecordIndex--;
        }
        //step of tapped point is start tracking from 1hour before.
        //Avoid time display yesterday time.
        if (startTime.getTime().getHour() > 0) {
            startTime.getTime().addHour(-1);
        }
        startTimeTxt.setText(startTime.getTime().getFullTimeString());
        return startTimeIndex;
    }

    public int setEndTime(int tappedRecordIndex) {
        boolean noSameActivityAlready = true;
        DateTime endTime = myRealTimeFitnessArr.get(tappedRecordIndex).getCaptureDateTime();
        int endTimeIndex = tappedRecordIndex;
        while (tappedRecordIndex < myRealTimeFitnessArr.size() - 1 && noSameActivityAlready) {
            if (sameActivity(myRealTimeFitnessArr.get(tappedRecordIndex + 1).getStepNumber())) {
                endTime = myRealTimeFitnessArr.get(tappedRecordIndex + 1).getCaptureDateTime();
                endTimeIndex = tappedRecordIndex + 1;
            } else {
                noSameActivityAlready = false;
            }
            tappedRecordIndex++;
        }
        endTimeTxt.setText(endTime.getTime().getFullTimeString());
        return endTimeIndex;
    }

    public void setDuration(int startTimeIndex, int endTimeIndex) {
        DateTime endTime = myRealTimeFitnessArr.get(endTimeIndex).getCaptureDateTime();
        DateTime startTime = myRealTimeFitnessArr.get(startTimeIndex).getCaptureDateTime();
        durationTxt.setText((endTime.getTime().getHour() - startTime.getTime().getHour() + 1) + " hour(s)");
    }

    public int setStep(int startTimeIndex, int endTimeIndex) {
        int stepNum = 0;
        while (startTimeIndex <= endTimeIndex) {
            stepNum += myRealTimeFitnessArr.get(startTimeIndex).getStepNumber();
            startTimeIndex++;
        }
        ;
        stepNumTxt.setText(stepNum + " step(s)");
        return stepNum;
    }

    // compare whether previous record or next record having same activity name
    public boolean sameActivity(int stepNumber) {
        if (activityTxt.getText().equals("Running")) {
            return (stepNumber > BasicRunStepNumber);
        } else if (activityTxt.getText().equals("Walking")) {
            return (stepNumber <= BasicRunStepNumber && stepNumber > 0);
        } else {
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
        displayDate.getDate().addDateNumber(-1);
        createGraphView();
        clearDetail();
    }

    public void NextDayClick(View view) {
        if (!displayDate.getDate().getFullDateString().equals(todayDate.getDate().getFullDateString())) {
            displayDate.getDate().addDateNumber(1);
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
    public DateTime getCurrentDateTime(int i) {
        Calendar calendar = Calendar.getInstance();
        int hour = i;
        String min = "00";
        String second = "00";
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String mydate = dateformat.format(calendar.getTime());
        String mytime = hour + ":" + min + ":" + second;
        return new DateTime(mydate + " " + mytime);
    }

    /*public void changeView(View view) {
        finish();
        Intent intent = new Intent(this, MyExerciseGraphView.class);
        startActivity(intent);
    }*/

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
