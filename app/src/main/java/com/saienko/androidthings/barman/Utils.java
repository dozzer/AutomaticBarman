package com.saienko.androidthings.barman;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/14/17
 * Time: 18:09
 */

public class Utils {
    public static boolean isThingsDevice(Context context) {
        final PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_EMBEDDED);
    }
}
