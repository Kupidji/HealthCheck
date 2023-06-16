package com.example.healthcheck.model.medicines.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.util.Constants
import com.example.healthcheck.model.medicines.service.MedicinesNotificationService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
                    Log.d("Database", "setRepetitive: сравнивает ${timeInMillis} с ${medicine.timeOfNotify1}")
                    when (timeInMillis) {

                        medicine.timeOfNotify1 -> {
                            medicine.timeOfNotify1 = medicine.timeOfNotify1 + 86400000L
                            Repositories.medicinesRepository.updateMedicine(medicine)
                            Log.d("Database", "setRepetitive: timeOfNotify1 был обновлен до ${medicine.timeOfNotify1}")
                            medicinesNotificationService.setRepetitiveNotification(medicine.timeOfNotify1, message, medicine.channelIDFirstTime)
                        }

                        medicine.timeOfNotify2 -> {
                            medicine.timeOfNotify2 = medicine.timeOfNotify2 + 86400000L
                            Repositories.medicinesRepository.updateMedicine(medicine)
                            Log.d("Database", "setRepetitive: timeOfNotify2 был обновлен до ${medicine.timeOfNotify1}")
                            medicinesNotificationService.setRepetitiveNotification(medicine.timeOfNotify2, message, medicine.channelIDSecondTime)
                        }

                        medicine.timeOfNotify3 -> {
                            medicine.timeOfNotify3 = medicine.timeOfNotify3 + 86400000L
                            Repositories.medicinesRepository.updateMedicine(medicine)
                            Log.d("Database", "setRepetitive: timeOfNotify3 был обновлен до ${medicine.timeOfNotify3}")
                            medicinesNotificationService.setRepetitiveNotification(medicine.timeOfNotify3, message, medicine.channelIDThirdTime)
                        }

                        medicine.timeOfNotify4 -> {
                            medicine.timeOfNotify4 = medicine.timeOfNotify4 + 86400000L
                            Repositories.medicinesRepository.updateMedicine(medicine)
                            Log.d("Database", "setRepetitive: timeOfNotify4 был обновлен до ${medicine.timeOfNotify4}")
                            medicinesNotificationService.setRepetitiveNotification(medicine.timeOfNotify4, message, medicine.channelIDFourthTime)
                        }
                    }
                }
            }
        }
    }

}