package my.com.taruc.fitnesscompanion;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Hexa-Jackson on 1/14/2016.
 */
public class FitnessApplication extends Application {

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher =  LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        FitnessApplication application = (FitnessApplication) context.getApplicationContext();
        return application.refWatcher;
    }


}
