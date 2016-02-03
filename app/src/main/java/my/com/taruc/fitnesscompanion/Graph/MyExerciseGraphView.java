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
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Database.ActivityPlanDA;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.R;

public class MyExerciseGraphView extends Activity {

    GraphView graph;
    ArrayList<FitnessRecord> myFitnessRecordArr = new ArrayList();
    FitnessRecordDA fitnessRecordDa;
    ActivityPlanDA myActivityPlanDA;

    String selectedView = "Activity History";
    String[] viewName = new String[]{"Activity History", "RealTime History", "Sleep Data"};

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
    @Bind(R.id.textViewSleepDataTitle)
    TextView textViewHistoryTitle;

    Context context;
    @Bind(R.id.previousDay)
    ImageView previousDay;
    @Bind(R.id.nextDay)
    ImageView nextDay;
    @Bind(R.id.textViewChangeView)
    TextView textViewChangeView;

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

        textViewHistoryTitle.setText("Exercise History");

        //Initial Fitness Data
        //realTimeFitnessDa = new RealTimeFitnessDA(this);
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
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxX(4);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setMinY(0);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Calories");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.parseColor("#FFFFFF"));
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setGridColor(Color.parseColor("#FFFFFF"));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#FFFFFF"));
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#FFFFFF"));
        createGraphView();
    }

    public void BackAction(View view) {
        this.finish();
    }

    private void createGraphView() {
        //myRealTimeFitnessArr = realTimeFitnessDa.getAllRealTimeFitnessPerDay(displayDate);
        myFitnessRecordArr = fitnessRecordDa.getAllFitnessRecordPerDay(displayDate);
        graph.removeAllSeries();
        datedisplay.setText(displayDate.getDate().getFullDateString());
        if (datedisplay.getText().equals(todayDate.getDate().getFullDateString())) {
            nextDay.setEnabled(false);
            nextDay.setVisibility(View.INVISIBLE);
        } else {
            nextDay.setEnabled(true);
            nextDay.setVisibility(View.VISIBLE);
        }

        //add fitness data block to chart
        if (myFitnessRecordArr.size() <= 0) {
            Toast.makeText(this, "No Fitness Records exist in database.", Toast.LENGTH_LONG).show();
        } else {
            graph.addSeries(generateFitnessRecordSeries());
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
            displayDate.getDate().addDateNumber(+1);
            createGraphView();
            clearDetail();
        }
    }

    private BarGraphSeries<DataPoint> generateFitnessRecordSeries() {
        BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<DataPoint>(generateDataPoint());
        barGraphSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                displayFitnessRecordData(dataPoint);
            }
        });
        barGraphSeries.setSpacing(50);
        // draw values on top
        barGraphSeries.setDrawValuesOnTop(true);
        barGraphSeries.setValuesOnTopColor(Color.WHITE);
        return barGraphSeries;
    }

    private DataPoint[] generateDataPoint() {
        DataPoint[] values = new DataPoint[myFitnessRecordArr.size()];
        double y = 0.1;
        for (int i = 0; i < myFitnessRecordArr.size(); i++) {
            if (myFitnessRecordArr.get(i).getRecordCalories() > 0) {
                y = myFitnessRecordArr.get(i).getRecordCalories();
            }
            DataPoint dataPoint = new DataPoint(i, y);
            values[i] = dataPoint;
        }
        return values;
    }

    private void displayFitnessRecordData(DataPointInterface dataPoint) {
        int tappedLocation = (int) (dataPoint.getX());
        FitnessRecord fitnessRecord = myFitnessRecordArr.get(tappedLocation);
        if (fitnessRecord != null) {
            activityTxt.setText(fitnessRecordDa.getActivityPlanName(fitnessRecord.getActivityPlanID()));
            //get Start Time
            DateTime StartDateTime = new DateTime(fitnessRecord.getCreateAt().getDateTimeString());
            startTimeTxt.setText(StartDateTime.getTime().getFullTimeString());
            //get End Time
            StartDateTime.getTime().addSecond(fitnessRecord.getRecordDuration());
            endTimeTxt.setText(StartDateTime.getTime().getFullTimeString());
            int duration = fitnessRecord.getRecordDuration();
            durationTxt.setText(String.format("%02d hr %02d min %02d sec",(duration / 3600) , ((duration / 60) - (duration / 3600 * 60)) , (duration % 60)));
            stepNumTxt.setText("-");
            caloriesTxt.setText(fitnessRecord.getRecordCalories() + " joules");
            distanceTxt.setText(fitnessRecord.getRecordDistance() + " meters");
            averageHRTxt.setText(fitnessRecord.getAverageHeartRate() + " bpm");
        }
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
