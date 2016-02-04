package my.com.taruc.fitnesscompanion.UI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.Goal;
import my.com.taruc.fitnesscompanion.R;

public class GoalPageAdd extends Activity {

    @Bind(R.id.spinnerGoal)
    Spinner spinnerGoal;
    @Bind(R.id.txtCurrentStatus)
    TextView txtCurrentStatus;
    @Bind(R.id.inputGoalTarget)
    EditText inputGoalTarget;
    @Bind(R.id.inputGoalDuration)
    EditText inputGoalDuration;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        context = this;
        ButterKnife.bind(this);

        //String[] goalTitle = new String[]{"Reduce Weight", "Step Walk", "Run Duration", "Exercise Duration", "Calories Burn"};
        final Goal myGoal = new Goal();
        Spinner spinnerGoalTitle = (Spinner) findViewById(R.id.spinnerGoal);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myGoal.getGoalTitles());
        spinnerGoalTitle.setAdapter(spinnerAdapter);
        spinnerGoalTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        txtCurrentStatus.setText("Current0: " + myGoal.getCurrentWeight(context));
                        break;
                    case 1:
                        txtCurrentStatus.setText("Current1: " + myGoal.getCurrentStepCount(context));
                        break;
                    case 2 - 4:
                        txtCurrentStatus.setText("Current2: " + myGoal.totalAllFitnessRecord(context, myGoal.getGoalTitles()[position]));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_goal, menu);
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
}
