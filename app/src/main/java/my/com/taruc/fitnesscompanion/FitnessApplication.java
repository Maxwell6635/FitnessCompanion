package my.com.taruc.fitnesscompanion;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Hexa-Jackson on 1/14/2016.
 */
public class FitnessApplication extends Application {

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
//        refWatcher =  LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        FitnessApplication application = (FitnessApplication) context.getApplicationContext();
        return application.refWatcher;
    }


}
