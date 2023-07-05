package com.example.healthcheck.model.medicines.service

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.healthcheck.R
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.medicines.receiver.MedicinesNotificationReceiver
import com.example.healthcheck.util.Constants
import com.example.healthcheck.view.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class MedicinesNotificationService(private val context: Context) {
    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    fun showNotification(message: String, channelID : Int) {
        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.navigation_graph)
            .setDestination(R.id.medicinesFragment)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(context, Constants.MEDICINES_CHANNEL)
            .setSmallIcon(R.drawable.app_icon_small)
            .setContentTitle(message)
            .setContentText("Напоминание")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(channelID, notification)
        Log.d("Notification", "showNotification: Уведомление ${message} было показано")
    }

    fun repairNotifications() {

        GlobalScope.launch {
            var list = Repositories.medicinesRepository.getAllMedicineList()
            for (medicine in list) {
                var timeString1 = SimpleDateFormat("HHmm").format(medicine.timeOfNotify1)
                var newTime1 = Calendar.getInstance()
                newTime1.timeInMillis = System.currentTimeMillis()
                newTime1[Calendar.HOUR_OF_DAY] = timeString1.substring(0..1).toInt()
                newTime1[Calendar.MINUTE] = timeString1.substring(2..3).toInt()
                newTime1[Calendar.SECOND] = 1
                if (medicine.timeOfNotify1 != 0L)
                    setRepetitiveNotification(newTime1.timeInMillis, "Первый приём - ${medicine.title}", medicine.channelIDFirstTime)

                var timeString2 = SimpleDateFormat("HHmm").format(medicine.timeOfNotify2)
                var newTime2 = Calendar.getInstance()
                newTime2.timeInMillis = System.currentTimeMillis()
                newTime2[Calendar.HOUR_OF_DAY] = timeString2.substring(0..1).toInt()
                newTime2[Calendar.MINUTE] = timeString2.substring(2..3).toInt()
                newTime2[Calendar.SECOND] = 1
                if (medicine.timeOfNotify2 != 0L)
                    setRepetitiveNotification(newTime2.timeInMillis, "Второй приём - ${medicine.title}", medicine.channelIDSecondTime)

                var timeString3 = SimpleDateFormat("HHmm").format(medicine.timeOfNotify3)
                var newTime3 = Calendar.getInstance()
                newTime3.timeInMillis = System.currentTimeMillis()
                newTime3[Calendar.HOUR_OF_DAY] = timeString3.substring(0..1).toInt()
                newTime3[Calendar.MINUTE] = timeString3.substring(2..3).toInt()
                newTime3[Calendar.SECOND] = 1
                if (medicine.timeOfNotify3 != 0L)
                    setRepetitiveNotification(newTime3.timeInMillis, "Третий приём - ${medicine.title}", medicine.channelIDThirdTime)

                var timeString4 = SimpleDateFormat("HHmm").format(medicine.timeOfNotify4)
                var newTime4 = Calendar.getInstance()
                newTime4.timeInMillis = System.currentTimeMillis()
                newTime4[Calendar.HOUR_OF_DAY] = timeString4.substring(0..1).toInt()
                newTime4[Calendar.MINUTE] = timeString4.substring(2..3).toInt()
                newTime4[Calendar.SECOND] = 1
                if (medicine.timeOfNotify4 != 0L)
                    setRepetitiveNotification(newTime4.timeInMillis, "Четвёртый приём - ${medicine.title}", medicine.channelIDFourthTime)
            }
        }
    }

    fun setExactAlarm(timeInMillis: Long, message : String, channelID : Int) {
        var currentTime = Calendar.getInstance()
        if (currentTime.timeInMillis <= timeInMillis && timeInMillis != 0L) {
            setAlarm(
                timeInMillis,
                getPendingIntent(
                    channelID,
                    getIntent().apply {
                        action = Constants.ACTION_SET_EXACT
                        putExtra(Constants.MESSAGE, message)
                        putExtra(Constants.CHANNEL_ID, channelID)
                        putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                    }
                )
            )
        }
    }

    fun cancelNotification(timeInMillis: Long, message : String, channelID : Int) {
        if (timeInMillis != 0L) {
            cancelExactAlarm(
                getPendingIntent(
                    channelID,
                    getIntent().apply {
                        action = Constants.ACTION_SET_EXACT
                        putExtra(Constants.MESSAGE, message)
                        putExtra(Constants.CHANNEL_ID, channelID)
                        putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                    }
                )
            )
        }
        Log.d("Notification", "cancelNotification: notification ${message} было отменено")
    }

    fun setRepetitiveAlarm(timeInMillis: Long, message : String, channelID : Int) {
        if (timeInMillis != 0L) {
            val currentTime = Calendar.getInstance()
            if (currentTime.timeInMillis <= timeInMillis) {
                setRepetitiveNotification(timeInMillis, message, channelID)
            } else {
                setRepetitiveNotification(timeInMillis + 86400000L, message, channelID)
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
                        action = Constants.ACTION_SET_REPETITIVE_EXACT
                        Log.d("Notification", "setRepetitiveNotification: в интетн передал ${timeInMillis}")
                        putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                        putExtra(Constants.MESSAGE, message)
                        putExtra(Constants.CHANNEL_ID, channelID)
                    }
                )
            )
            Log.d(
                "Notification",
                "setRepetitiveNotification: Repetitive notification ${message} было создано после " + "${SimpleDateFormat("dd.MM, HH:mm").format(timeInMillis)} (${timeInMillis})"
            )
        }
    }

    fun cancelRepetitiveNotification(timeInMillis: Long, message : String, channelID : Int) {
        if (timeInMillis != 0L) {
            cancelExactAlarm(
                getCancelPendingIntent(
                    channelID,
                    getIntent().apply {
                        action = Constants.ACTION_SET_REPETITIVE_EXACT
                        putExtra(Constants.MESSAGE, message)
                        putExtra(Constants.CHANNEL_ID, channelID)
                        putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                    }
                )
            )
        }
        Log.d("Notification", "cancelNotification: Repetitive notification ${message} at ${SimpleDateFormat("dd.MM").format(timeInMillis)} было отменено")
    }

    private fun getPendingIntent(channelID: Int, intent: Intent) =
        PendingIntent.getBroadcast(
            context,
            channelID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

    private fun getCancelPendingIntent(channelID: Int, intent: Intent) =
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

    private fun cancelExactAlarm(pendingIntent: PendingIntent) {
        alarmManager?.cancel(pendingIntent)
    }

    private fun getIntent() = Intent(context, MedicinesNotificationReceiver::class.java)

}