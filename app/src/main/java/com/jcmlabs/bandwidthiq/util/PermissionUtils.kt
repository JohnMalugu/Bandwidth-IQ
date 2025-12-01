package com.jcmlabs.bandwidthiq.util


import android.app.AppOpsManager
import android.content.Context

fun Context.hasUsageStatsPermission(): Boolean {
    val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.checkOpNoThrow(
        "android:get_usage_stats",
        android.os.Process.myUid(),
        packageName
    )
    return mode == AppOpsManager.MODE_ALLOWED
}
