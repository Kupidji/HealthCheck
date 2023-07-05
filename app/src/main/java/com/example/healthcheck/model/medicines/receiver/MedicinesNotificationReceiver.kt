package com.example.healthcheck.model.medicines.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.model.medicines.service.MedicinesNotificationService
import com.example.healthcheck.util.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class MedicinesNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Constants.ACTION_SET_EXACT -> {
                val service = MedicinesNotificationService(context)
                service.showNotification(
                    intent.getStringExtra(Constants.MESSAGE)!!,
                    intent.getIntExtra(Constants.CHANNEL_ID, 0)
                )
            }

            Constants.ACTION_SET_REPETITIVE_EXACT -> {
                val medicinesNotificationService = MedicinesNotificationService(context)
                setRepetitive(
                    medicinesNotificationService,
                    intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L),
                    intent.getStringExtra(Constants.MESSAGE)!!,
                    intent.getIntExtra(Constants.CHANNEL_ID, 0)
                )
            }
        }
    }

    private fun setRepetitive(medicinesNotificationService: MedicinesNotificationService, timeInMillis : Long ,message : String, channelID : Int) {
        medicinesNotificationService.showNotification(message, channelID)
        GlobalScope.launch {
            var list = Repositories.medicinesRepository.getAllMedicineList()
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