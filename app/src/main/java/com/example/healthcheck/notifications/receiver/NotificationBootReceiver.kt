package com.example.healthcheck.notifications.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.healthcheck.notifications.service.NotificationService

class NotificationBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val service = NotificationService(context)
            service.repairNotifications()
            Log.d("Notification", "Receiver восстановил уведомления для regular")
        }

    }

}