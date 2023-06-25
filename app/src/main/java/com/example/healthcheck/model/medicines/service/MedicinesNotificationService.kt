package com.example.healthcheck.model.medicines.service

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.healthcheck.R
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.medicines.entities.Medicines
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
        val fragmentIntent = Intent(context, MainActivity::class.java)
        val fragmentPendingtIntent = PendingIntent.getActivity(
            context,
            channelID,
            fragmentIntent,
            PendingIntent.FLAG_MUTABLE
        )
        val notification = NotificationCompat.Builder(context, Constants.MEDICINES_CHANNEL)
            .setSmallIcon(R.drawable.app_icon_small)
            .setContentTitle(message)
            .setContentText("Напоминание")
            .setContentIntent(fragmentPendingtIntent)
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
                if (medicine.timeOfNotify1 != 0L)
                    setRepetitiveNotification(medicine.timeOfNotify1, medicine.title, medicine.channelIDFirstTime)

                if (medicine.timeOfNotify2 != 0L)
                    setRepetitiveNotification(medicine.timeOfNotify2, medicine.title, medicine.channelIDSecondTime)

                if (medicine.timeOfNotify3 != 0L)
                    setRepetitiveNotification(medicine.timeOfNotify3, medicine.title, medicine.channelIDThirdTime)

                if (medicine.timeOfNotify4 != 0L)
                    setRepetitiveNotification(medicine.timeOfNotify4, medicine.title, medicine.channelIDFourthTime)

                Log.d("Notification", "все уведомления восстановлены")
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
            }
            else {
                setRepetitiveNotification(timeInMillis + 86400000L, message, channelID)
                GlobalScope.launch {
                    var time = timeInMillis + 86400000L
                    val list = Repositories.medicinesRepository.getAllMedicineList()
                    for (medicine in list) {
                        if (medicine.title == message.substring((message.indexOf("-") + 2)..(message.length - 1))) {
                            when (timeInMillis) {

                                medicine.timeOfNotify1 -> {
                                    medicine.timeOfNotify1 = time
                                    Repositories.medicinesRepository.updateMedicine(medicine)
                                    Log.d(
                                        "Notification",
                                        "setRepetitive: timeOfNotify1 было изменено на ${time}"
                                    )
                                }
                            }
                        }
                    }
                }
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
            PendingIntent.FLAG_MUTABLE
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