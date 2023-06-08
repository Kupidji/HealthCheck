package com.example.healthcheck.service

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.healthcheck.R
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.medicines.entities.Medicines
import com.example.healthcheck.receiver.MedicinesNotificationReceiver
import com.example.healthcheck.util.Constants
import com.example.healthcheck.view.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar


class MedicinesNotificationService(private val context: Context) {
    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    fun repairNotifications() {
        Log.d("Notification", "trying reboot notifications")

        GlobalScope.launch {
            var list = Repositories.medicinesRepository.getAllMedicineList()
            for (medicine in list) {
                if (medicine.timeOfNotify1 != 0L)
                    setRepetitiveAlarm(medicine.timeOfNotify1, medicine.title, medicine.channelIDFirstTime)

                if (medicine.timeOfNotify2 != 0L)
                    setRepetitiveAlarm(medicine.timeOfNotify2, medicine.title, medicine.channelIDSecondTime)

                if (medicine.timeOfNotify3 != 0L)
                    setRepetitiveAlarm(medicine.timeOfNotify3, medicine.title, medicine.channelIDThirdTime)

                if (medicine.timeOfNotify4 != 0L)
                    setRepetitiveAlarm(medicine.timeOfNotify4, medicine.title, medicine.channelIDFourthTime)

            }
        }



    }

    fun showNotification(message: String, channelID : Int) {
        val fragmentIntent = Intent(context, MainActivity::class.java)
        val fragmentPendingtIntent = PendingIntent.getActivity(
            context,
            channelID,
            fragmentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, Constants.MEDICINES_CHANNEL)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle("Пора выпить")
            .setContentText(message)
            .setContentIntent(fragmentPendingtIntent)
            .setAutoCancel(true)
//            .addAction(
//                 todo добавить фунционал для отметки об подтверждении
//            )
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(channelID, notification)
    }

    fun setExactAlarm(timeInMillis: Long, message : String, channelID : Int) {
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

    fun setRepetitiveAlarm(timeInMillis: Long, message : String, channelID : Int) {
        setAlarm(
            timeInMillis,
            getPendingIntent(
                channelID,
                getIntent().apply {
                    action = Constants.ACTION_SET_REPETITIVE_EXACT
                    putExtra(Constants.MESSAGE, message)
                    putExtra(Constants.CHANNEL_ID, channelID)
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                }
            )
        )
        Log.d("Notification", "setRepetitiveAlarm: Notification was created")
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
    }

    private fun getPendingIntent(channelID: Int, intent: Intent) =
        PendingIntent.getBroadcast(
            context,
            channelID,
            intent,
            PendingIntent.FLAG_IMMUTABLE
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