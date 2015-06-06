package my.com.saiboon.fitnesscompanion.UI;

import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import my.com.saiboon.fitnesscompanion.R;


public class ExerciseMenu extends ActionBarActivity {

    Chronometer myChronometer;
    Button startExercise;
    TextView heartRate;
    TextView distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_menu);
        startExercise = (Button) findViewById(R.id.buttonStart);

        String[] exerciseName = new String[] {"Walking", "Running", "Cycling", "Hiking ", "Workout","Sport"};
        Spinner spinnerExerciseName = (Spinner) findViewById(R.id.spinnerExerciseName);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, exerciseName);
        spinnerExerciseName.setAdapter(spinnerAdapter);

        myChronometer = (Chronometer) findViewById(R.id.chronometerTimer);
        heartRate = (TextView) findViewById(R.id.textViewHeartRate);
        heartRate.setText("0mbp");
        distance = (TextView) findViewById(R.id.textViewDistance);
        distance.setText("0km");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void StartTimer(View view){
        if (startExercise.getText().equals("Start Exercise")){
            int stoppedMilliseconds = 0;
            String chronoText = myChronometer.getText().toString();
            String array[] = chronoText.split(":");
            if (array.length == 2) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
            } else if (array.length == 3) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000 + Integer.parseInt(array[2]) * 1000;
            }
            myChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
            myChronometer.start();
            startExercise.setText("Stop Exercise");
        }else{
            myChronometer.stop();
            //showElapsedTime();
            startExercise.setText("Start Exercise");
        }
    }

    private void showElapsedTime() {
        long elapsedMillis = SystemClock.elapsedRealtime() - myChronometer.getBase();
        Toast.makeText(this, "Elapsed milliseconds: " + elapsedMillis, Toast.LENGTH_SHORT).show();
    }
}
