package com.saienko.androidthings.barman

import android.app.Application
import com.saienko.androidthings.barman.db.DatabaseCreator
import com.tspoon.traceur.Traceur

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 13:38
 */

class AlcoApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        DatabaseCreator.createDb(this)
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            //            // This process is dedicated to LeakCanary for heap analysis.
//            //            // You should not init your app in this process.
//            return
//        }
//        LeakCanary.install(this)
        Traceur.enableLogging()
    }
}
