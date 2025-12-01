package com.jcmlabs.bandwidthiq.util

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.jcmlabs.bandwidthiq.data.model.SpeedCategory

class NotificationHelper(private val context: Context) {
    private val channelId = "bandwidth_iq_channel"

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Speed Category Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications when internet speed category changes"
                setShowBadge(true)
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun sendCategoryChangeNotification(speed: Double, category: SpeedCategory) {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.stat_sys_download)
            .setContentTitle("Speed Changed: ${category.displayName}")
            .setContentText("${"%.1f".format(speed)} Mbps - ${category.activities.first()}")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Current speed: ${"%.1f".format(speed)} Mbps\n${category.activities.joinToString("\n")}"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setColor(category.color.hashCode())
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
    }
}