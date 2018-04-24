package com.saienko.androidthings.barman

import android.content.Context
import android.content.pm.PackageManager

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/14/17
 * Time: 18:09
 */

object Utils {
    fun isThingsDevice(context: Context): Boolean {
        val pm = context.packageManager
        return pm.hasSystemFeature(PackageManager.FEATURE_EMBEDDED)
    }
}
