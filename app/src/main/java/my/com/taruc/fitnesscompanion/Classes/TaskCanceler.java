package my.com.taruc.fitnesscompanion.Classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import my.com.taruc.fitnesscompanion.ShowAlert;

/**
 * Created by saiboon on 3/3/2016.
 */
public class TaskCanceler implements Runnable{
    private Thread thread;
    private Context context;
    private ShowAlert showAlert = new ShowAlert();
    private ProgressDialog progress;

    public TaskCanceler(Thread thread, ProgressDialog progress, Context context) {
        this.thread = thread;
        this.context = context;
        this.progress = progress;
    }

    @Override
    public void run() {
        if (thread.getState() == Thread.State.RUNNABLE ) {
            thread.stop();
            progress.dismiss();
            //Toast.makeText(context, "Sync Error. Please check your network connection.", Toast.LENGTH_LONG).show();
            showAlert.showAlertDialog(context, "Network Error", "Please check your network connection.", true);
        }
    }
}
