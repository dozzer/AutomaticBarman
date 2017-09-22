package com.saienko.androidthings.barman;

import android.app.Application;
import com.saienko.androidthings.barman.db.AppDatabase;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 13:38
 */

public class AlcoApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.init(this);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
