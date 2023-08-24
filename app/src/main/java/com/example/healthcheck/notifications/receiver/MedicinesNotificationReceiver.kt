package com.example.healthcheck.notifications.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.ACTION_SET_EXACT
import com.example.data.ACTION_SET_REPETITIVE_EXACT
import com.example.data.CHANNEL_ID
import com.example.data.EXTRA_EXACT_ALARM_TIME
import com.example.data.MESSAGE
import com.example.healthcheck.notifications.service.MedicinesNotificationService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class MedicinesNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_SET_EXACT -> {
                val service = MedicinesNotificationService(context)
                service.showNotification(
                    intent.getStringExtra(MESSAGE)!!,
                    intent.getIntExtra(CHANNEL_ID, 0)
                )
            }

            ACTION_SET_REPETITIVE_EXACT -> {
                val medicinesNotificationService = MedicinesNotificationService(context)
                setRepetitive(
                    medicinesNotificationService,
                    intent.getLongExtra(EXTRA_EXACT_ALARM_TIME, 0L),
                    intent.getStringExtra(MESSAGE)!!,
                    intent.getIntExtra(CHANNEL_ID, 0)
                )
            }
        }
    }

    private fun setRepetitive(medicinesNotificationService: MedicinesNotificationService, timeInMillis : Long, message : String, channelID : Int) {
        medicinesNotificationService.showNotification(message, channelID)
        GlobalScope.launch {
            var list = com.example.data.Repositories.medicinesRepository.getAllMedicineList()
            for (medicine in list) {
                if (medicine.title == message.substring((message.indexOf("-") + 2)..(message.length - 1))) {
                    when (SimpleDateFormat("HH:mm").format(timeInMillis)) {

                        SimpleDateFormat("HH:mm").format(medicine.timeOfNotify1) -> {
                            var timeString = SimpleDateFormat("HHmm").format(medicine.timeOfNotify1)
                            var newTime = Calendar.getInstance()
                            newTime.timeInMillis = System.currentTimeMillis()
                            newTime[Calendar.HOUR_OF_DAY] = timeString.substring(0..1).toInt()
                            newTime[Calendar.MINUTE] = timeString.substring(2..3).toInt()
                            newTime[Calendar.SECOND] = 1
                            medicinesNotificationService.setRepetitiveNotification(newTime.timeInMillis + 86400000L, message, medicine.channelIDFirstTime)
                        }

                        SimpleDateFormat("HH:mm").format(medicine.timeOfNotify2) -> {
                            var timeString = SimpleDateFormat("HHmm").format(medicine.timeOfNotify2)
                            var newTime = Calendar.getInstance()
                            newTime.timeInMillis = System.currentTimeMillis()
                            newTime[Calendar.HOUR_OF_DAY] = timeString.substring(0..1).toInt()
                            newTime[Calendar.MINUTE] = timeString.substring(2..3).toInt()
                            newTime[Calendar.SECOND] = 1
                            medicinesNotificationService.setRepetitiveNotification(newTime.timeInMillis + 86400000L, message, medicine.channelIDSecondTime)
                        }

                        SimpleDateFormat("HH:mm").format(medicine.timeOfNotify3) -> {
                            var timeString = SimpleDateFormat("HHmm").format(medicine.timeOfNotify3)
                            var newTime = Calendar.getInstance()
                            newTime.timeInMillis = System.currentTimeMillis()
                            newTime[Calendar.HOUR_OF_DAY] = timeString.substring(0..1).toInt()
                            newTime[Calendar.MINUTE] = timeString.substring(2..3).toInt()
                            newTime[Calendar.SECOND] = 1
                            medicinesNotificationService.setRepetitiveNotification(newTime.timeInMillis + 86400000L, message, medicine.channelIDThirdTime)
                        }

                        SimpleDateFormat("HH:mm").format(medicine.timeOfNotify4) -> {
                            var timeString = SimpleDateFormat("HHmm").format(medicine.timeOfNotify4)
                            var newTime = Calendar.getInstance()
                            newTime.timeInMillis = System.currentTimeMillis()
                            newTime[Calendar.HOUR_OF_DAY] = timeString.substring(0..1).toInt()
                            newTime[Calendar.MINUTE] = timeString.substring(2..3).toInt()
                            newTime[Calendar.SECOND] = 1
                            medicinesNotificationService.setRepetitiveNotification(newTime.timeInMillis + 86400000L, message, medicine.channelIDFourthTime)
                        }
                    }
                }
            }
        }
    }

}