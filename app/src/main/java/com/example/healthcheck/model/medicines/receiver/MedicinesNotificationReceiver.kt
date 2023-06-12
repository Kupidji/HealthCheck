package com.example.healthcheck.model.medicines.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.healthcheck.model.Repositories
import com.example.healthcheck.util.Constants
import com.example.healthcheck.model.medicines.service.MedicinesNotificationService
import java.util.Calendar

class MedicinesNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Constants.ACTION_SET_EXACT -> {
                val service = MedicinesNotificationService(context)
                service.showNotification(intent.getStringExtra(Constants.MESSAGE)!!, intent.getIntExtra(
                    Constants.CHANNEL_ID, 0))
            }
            Constants.ACTION_SET_REPETITIVE_EXACT -> {
                setRepetitiveAlarm(
                    MedicinesNotificationService(context), intent.getStringExtra(Constants.MESSAGE)!!, intent.getIntExtra(
                    Constants.CHANNEL_ID, 0))
            }

        }
    }

    private fun setRepetitiveAlarm(medicinesNotificationService: MedicinesNotificationService, message : String, channelID : Int) {
        val cal = Calendar.getInstance().apply {
            this.add(Calendar.HOUR_OF_DAY, 24)
            medicinesNotificationService.showNotification(message, channelID)
        }
        medicinesNotificationService.setRepetitiveAlarm(cal.timeInMillis, message, channelID)
    }

}