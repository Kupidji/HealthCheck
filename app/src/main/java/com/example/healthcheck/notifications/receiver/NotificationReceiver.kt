package com.example.healthcheck.notifications.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.healthcheck.notifications.service.NotificationService
import com.example.healthcheck.util.Constants

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {

            Constants.ACTION_SET_REPETITIVE_EXACT_REGULAR -> {
                val service = NotificationService(context)
                setRepetativeAlarm(
                    service,
                    intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L),
                    intent.getStringExtra(Constants.MESSAGE)!!,
                    intent.getIntExtra(Constants.CHANNEL_ID, 0),
                    context
                )
            }

        }
    }

    private fun setRepetativeAlarm(notificationService: NotificationService, timeInMillis : Long, message : String, channelID : Int, context : Context) {
        notificationService.showNotification(message, channelID)
        val settings = context.applicationContext.getSharedPreferences(Constants.TIME_OF_NOTIFICATION, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.putLong(Constants.TIME_OF_NOTIFICATION, timeInMillis + 86400000L)
        notificationService.setRepetitiveAlarm(settings.getLong(Constants.TIME_OF_NOTIFICATION, timeInMillis + 86400000L), message, channelID)
    }

}