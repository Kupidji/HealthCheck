package com.example.healthcheck.model.global_notifications.service

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.healthcheck.R
import com.example.healthcheck.model.global_notifications.receiver.NotificationReceiver
import com.example.healthcheck.util.Constants
import com.example.healthcheck.view.MainActivity
import java.text.SimpleDateFormat
import java.util.Calendar

class NotificationService (val context: Context) {
    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    fun repairNotifications() {
        var time = Calendar.getInstance()
        time.set(Calendar.HOUR_OF_DAY, 18)
        time.set(Calendar.MINUTE, 0)
        val settings = context.applicationContext.getSharedPreferences(Constants.TIME_OF_NOTIFICATION, Context.MODE_PRIVATE)
        var notifTime = settings.getLong(Constants.TIME_OF_NOTIFICATION, time.timeInMillis)
        setRepetitiveAlarm(notifTime, Constants.REGULAR_MESSAGE, Constants.REGULAR_CHANNEL_ID)
    }

    fun setRepetitiveAlarm(timeInMillis: Long, message : String, channelID: Int) {
        if (timeInMillis != 0L) {
            val currentTime = Calendar.getInstance()
            if (currentTime.timeInMillis <= timeInMillis) {
                setRepetitiveNotification(timeInMillis, message, channelID)
                Log.d("Notification", "setRepetitiveAlarm: RegularNotification было создано в ${SimpleDateFormat("dd.MM HH:mm").format(timeInMillis)}")
            }
            else {
                var newTime = timeInMillis + 86400000L
                setRepetitiveNotification(newTime, message, channelID)
                val settingsNotification = context?.applicationContext?.getSharedPreferences(Constants.TIME_OF_NOTIFICATION, Context.MODE_PRIVATE)
                val editorNotification = settingsNotification?.edit()
                editorNotification?.putLong(Constants.TIME_OF_NOTIFICATION, newTime)?.apply()
                Log.d("Notification", "setRepetitiveAlarm: RegularNotification было создано в ${SimpleDateFormat("dd.MM HH:mm").format(newTime)}")
            }
        }
    }

    fun setRepetitiveNotification(timeInMillis: Long, message : String, channelID : Int) {
        if (timeInMillis != 0L) {
            setAlarm(
                timeInMillis,
                getPendingIntent(
                    channelID,
                    getIntent().apply {
                        action = Constants.ACTION_SET_REPETITIVE_EXACT_REGULAR
                        putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                        putExtra(Constants.MESSAGE, message)
                        putExtra(Constants.CHANNEL_ID, channelID)
                    }
                )
            )
        }
    }

    fun showNotification(message: String, channelID : Int) {
        val fragmentIntent = Intent(context, MainActivity::class.java)
        val fragmentPendingtIntent = PendingIntent.getActivity(
            context,
            channelID,
            fragmentIntent,
            PendingIntent.FLAG_MUTABLE
        )
        val notification = NotificationCompat.Builder(context, Constants.REGULAR_CHANNEL)
            .setSmallIcon(R.drawable.app_icon_small)
            .setContentTitle(message)
            .setContentText("Не забудьте внести данные за день")
            .setContentIntent(fragmentPendingtIntent)
            .setAutoCancel(true)
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(channelID, notification)
        Log.d("Notification", "showNotification: Уведомление ${message} было показано")
    }

    private fun getPendingIntent(channelID: Int, intent: Intent) =
        PendingIntent.getBroadcast(
            context,
            channelID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent) {
        alarmManager?.let {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        }
    }

    private fun getIntent() = Intent(context, NotificationReceiver::class.java)

}