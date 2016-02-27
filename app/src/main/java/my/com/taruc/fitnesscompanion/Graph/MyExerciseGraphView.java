package my.com.taruc.fitnesscompanion.Graph;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
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

    ArrayList<FitnessRecord> myFitnessRecordArr = new ArrayList();
    FitnessRecordDA fitnessRecordDa;
    ActivityPlanDA myActivityPlanDA;

    String selectedView = "Activity History";
    String[] viewName = new String[]{"Activity History", "RealTime History", "Sleep Data"};

    DateTime todayDate;
    DateTime displayDate;
    Context context;

    @Bind(R.id.textViewTitle)
    TextView textViewTitle;
    @Bind(R.id.imageViewBackButton)
    ImageView imageViewBackButton;
    @Bind(R.id.previousDay)
    ImageView previousDay;
    @Bind(R.id.DateDisplay)
    TextView datedisplay;
    @Bind(R.id.nextDay)
    ImageView nextDay;
    @Bind(R.id.graph)
    GraphView graph;
    @Bind(R.id.ActivityTxtView)
    TextView ActivityTxtView;
    @Bind(R.id.separator1)
    TextView separator1;
    @Bind(R.id.ActivityDisplay)
    TextView ActivityDisplay;
    @Bind(R.id.StartTimeTxtView)
    TextView StartTimeTxtView;
    @Bind(R.id.separator2)
    TextView separator2;
    @Bind(R.id.StartTimeDisplay)
    TextView StartTimeDisplay;
    @Bind(R.id.EndTimeTxtView)
    TextView EndTimeTxtView;
    @Bind(R.id.separator3)
    TextView separator3;
    @Bind(R.id.EndTimeDisplay)
    TextView EndTimeDisplay;
    @Bind(R.id.DurationTxtView)
    TextView DurationTxtView;
    @Bind(R.id.separator4)
    TextView separator4;
    @Bind(R.id.DurationDisplay)
    TextView DurationDisplay;
    @Bind(R.id.StepTxtView)
    TextView StepTxtView;
    @Bind(R.id.separator5)
    TextView separator5;
    @Bind(R.id.StepNumDisplay)
    TextView StepNumDisplay;
    @Bind(R.id.CaloriesTxtView)
    TextView CaloriesTxtView;
    @Bind(R.id.separator6)
    TextView separator6;
    @Bind(R.id.CaloriesDisplay)
    TextView CaloriesDisplay;
    @Bind(R.id.DistanceTxtView)
    TextView DistanceTxtView;
    @Bind(R.id.separator7)
    TextView separator7;
    @Bind(R.id.DistanceDisplay)
    TextView DistanceDisplay;
    @Bind(R.id.AveHRTxtView)
    TextView AveHRTxtView;
    @Bind(R.id.separator8)
    TextView separator8;
    @Bind(R.id.AveHRDisplay)
    TextView AveHRDisplay;
    @Bind(R.id.TableDetail)
    TableLayout TableDetail;
    @Bind(R.id.textViewChangeView)
    TextView textViewChangeView;
    @Bind(R.id.ScrollView01)
    ScrollView ScrollView01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_graph_view);
        ButterKnife.bind(this);
        context = this;

        textViewTitle.setText(R.string.exerciseHistory);

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
            //Toast.makeText(this, "No Fitness Records exist in database.", Toast.LENGTH_LONG).show();
            visibleDetails(View.INVISIBLE);
        } else {
            graph.addSeries(generateFitnessRecordSeries());
            visibleDetails(View.VISIBLE);
        }
    }

    private void clearDetail() {
        ActivityDisplay.setText("-");
        StartTimeDisplay.setText("-");
        EndTimeDisplay.setText("-");
        DurationDisplay.setText("-");
        StepNumDisplay.setText("-");
        CaloriesDisplay.setText("-");
        DistanceDisplay.setText("-");
        AveHRDisplay.setText("-");
    }

    private void visibleDetails(int visibility) {
        TableDetail.setVisibility(visibility);
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
        final BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<DataPoint>(generateDataPoint());
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
        // styling
        barGraphSeries.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 10);
            }
        });
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
            ActivityDisplay.setText(fitnessRecordDa.getActivityPlanName(fitnessRecord.getActivityPlanID()));
            //get Start Time
            DateTime StartDateTime = new DateTime(fitnessRecord.getCreateAt().getDateTimeString());
            StartTimeDisplay.setText(StartDateTime.getTime().getFullTimeString());
            //get End Time
            StartDateTime.getTime().addSecond(fitnessRecord.getRecordDuration());
            EndTimeDisplay.setText(StartDateTime.getTime().getFullTimeString());
            int duration = fitnessRecord.getRecordDuration();
            DurationDisplay.setText(String.format("%02d hr %02d min %02d sec", (duration / 3600), ((duration / 60) - (duration / 3600 * 60)), (duration % 60)));
            CaloriesDisplay.setText(fitnessRecord.getRecordCalories() + " joules");
            DistanceDisplay.setText(fitnessRecord.getRecordDistance() + " meters");
            AveHRDisplay.setText(fitnessRecord.getAverageHeartRate() + " bpm");

            changeAllDetailColor(dataPoint);
        }
    }

    public void changeAllDetailColor(DataPointInterface data){
        int color = Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 10);
        ActivityDisplay.setTextColor(color);
        StartTimeDisplay.setTextColor(color);
        EndTimeDisplay.setTextColor(color);
        DurationDisplay.setTextColor(color);
        CaloriesDisplay.setTextColor(color);
        DistanceDisplay.setTextColor(color);
        AveHRDisplay.setTextColor(color);
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
