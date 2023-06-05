package com.example.healthcheck.util

import android.R
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.healthcheck.view.medicinesFragment
import com.example.myday.service.AlarmService

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Constants.ACTION_SET_EXACT -> {
                val service = AlarmService(context)
                service.showNotification(intent.getStringExtra(Constants.MESSAGE)!!, intent.getIntExtra(Constants.CHANNEL_ID, 0))
            }
        }
    }



}