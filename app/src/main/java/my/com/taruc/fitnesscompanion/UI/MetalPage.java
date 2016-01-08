package my.com.taruc.fitnesscompanion.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import my.com.taruc.fitnesscompanion.R;


public class MetalPage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metal_page);
    }


    public void BackAction(View view) {
        this.finish();
    }

}
